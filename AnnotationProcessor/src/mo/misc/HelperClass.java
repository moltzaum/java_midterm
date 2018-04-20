package mo.misc;

/**
 * A class designated for methods that don't fit in other classes.
 * The only method is capitalizeStr() :(
 * */
public class HelperClass {
	
	public static String capitalizeStr(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
}
