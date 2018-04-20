package mo.elementManagers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

/**
 * An abstract class which implements annotation-specific methods.
 * */
public abstract class AnnotationClassElementManager extends ClassElementManager {
	
	protected Class<? extends Annotation> anno;
	public List<? extends Element> annotatedElements = null;
	public Message errorMsgForNonAnnotateable = new Message(Kind.ERROR, "Field isn't annotateable.");
	
	/**
	 * @throws IllegalArgumentException if the given element is not of kind ElementKind.CLASS
	 * @throws NullPointerException if the processingEnv is null.
	 * */
	protected AnnotationClassElementManager(TypeElement clazz, ProcessingEnvironment pe, Class<? extends Annotation> anno) {
		super(clazz, pe);
		this.anno = anno;
	}

	/**
	 * Check to see if a given element is annotateable according to this
	 * AnnotationClassElementManager. Whether an annotation is annotatable
	 * or not may depend on the element's ElementKind and modifiers. In the
	 * default implementation, all elements are considered annotateable.
	 * */
	public boolean isAnnotateable(Element e) {
		return true;
	}
	
	/**
	 * This method is an extension of fieldsMatch(field1, field2) from ClassElementManager.
	 * If the boolean value is true, the annotations on each field are compared as well.
	 * */
	public static boolean fieldsMatch(VariableElement field1, VariableElement field2, boolean b) {
		return fieldSignature(field1, b).equals(fieldSignature(field2, b));
	}
	
	/**
	 * This method is an extension of fieldSignature(field1, field2) from ClassElementManager.
	 * If the boolean value is true, the annotations on each field are returned as well.
	 * @see {@link ClassElementManager#fieldSignature}
	 * */
	public static String fieldSignature(VariableElement field, boolean b) {
		String signature = fieldSignature(field);
		if (b) {
			StringBuilder sb = new StringBuilder();
			for (AnnotationMirror annotation: field.getAnnotationMirrors()) {
				String anno = annotation.toString();
				sb.append('@' + anno.substring(anno.lastIndexOf('.') + 1) + '\n');
			} return sb.toString() + signature;
		} return signature;
	}
	
	/**
	 * Gets all the elements that are annotated. No element in the returned list
	 * is able to return false for isAnnotateable(element). To change the message
	 * for this method, change "errorMsgForNonAnnotateable". This returns an empty
	 * list if there are no annotated elements.
	 * */
	public List<? extends Element> getAnnotatedElements() {
		if (annotatedElements != null) {
			return annotatedElements;
		}
		List<Element> annotatedElements = new ArrayList<>();
		for (Element e: clazz.getEnclosedElements()) {
			Annotation anno = e.getAnnotation(this.anno);
			if (anno != null) {
				if (!isAnnotateable(e)) {
					errorMsgForNonAnnotateable.print(e);
				} else annotatedElements.add(e);
			}
		} this.annotatedElements = annotatedElements;
		return annotatedElements;
	}
	
	/**
	 * Run a process on an object involving looking for and handling annotations.
	 * */
	public abstract void process();
	
	//not used - I might as well test the size of the result of getAnnotatedElements
	/**
	 * Checks to see if any part of the element class (this.clazz) has
	 * the annotation specifed by this class.
	 * */
	public final boolean containsAnnotation() {	//could do: return !getAnnotatedElements.isEmpty();
		for (Element field: clazz.getEnclosedElements()) {
			if (field.getAnnotation(anno) != null) return true;
		} return false;
	}
}
