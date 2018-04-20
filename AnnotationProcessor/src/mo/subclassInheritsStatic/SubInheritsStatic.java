package mo.subclassInheritsStatic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To be used in front of static member. This annotation will force subclasses to implement the tagged static field.
 * */
@Inherited()
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubInheritsStatic {
	//no values to associate when calling annotation
}
