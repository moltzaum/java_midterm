This contains notes about how the SubInheritsStatic annotation should be used,
and how might change it in the future.

The @SubInheritsStatic annotation forces subclasses of a class containing a tagged
variable to have the same variable, including annotations and annotation values.

The annotation @SubInheritsStatic only affects variables.

The annotation cannot be used on private variables and must be
used on static variables.

The annotation processor may throw an NullPointerException if classes
are deleted. This error can be ignored for now, and I plan to 'fix' it
in the future.

The original class (or interface) to implement the annotation must
provide a method to get the tagged variable, which includes returning
that variable's type. Implementation of this method is up to whoever
implements the annotation. The method's purpose is to get the tagged
variable of one of it's subclasses.

The following is my implementation of the method:

private int getVar() {
	try {
		return (int) this.getClass().getField("var").get(null); //any given class will have "var"
	} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
		e.printStackTrace();
		System.exit(1);
	} return -1; //this is dead code but eclipse doesn't recognize it
}

Note to those implementing the method:
If getVar() is static, X implements the annotation, and Y extends X,
then Y.getVar() should not be called.

In the future, I may make messages display more specific information, and I may
make messages able to generate code for the user. I may restrict how the method
is implemented and I might change how I determine that the method belongs to a variable.
