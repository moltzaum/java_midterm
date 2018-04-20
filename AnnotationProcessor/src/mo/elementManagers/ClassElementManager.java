package mo.elementManagers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

/**
 * An object to encapsulate basic element operations.
 * */
public class ClassElementManager {
	
	final public TypeElement clazz;
	final public List<VariableElement> classFields;
	final public List<ExecutableElement> classMethods;
	public ProcessingEnvironment processingEnv = null;
	
	/**
	 * @throws IllegalArgumentException if the given element is not a type
	 * @throws NullPointerException if the processingEnv is null.
	 * */
	public ClassElementManager(TypeElement clazz, ProcessingEnvironment pe) {
		if (!(clazz.getKind().isClass() || clazz.getKind().isInterface())) {
			throw new IllegalArgumentException("Element must be of a class kind.");
		}
		if (pe == null) {
			throw new NullPointerException();
		}
		
		this.clazz = clazz;
		classFields = ElementFilter.fieldsIn(clazz.getEnclosedElements());
		classMethods = ElementFilter.methodsIn(clazz.getEnclosedElements());
		this.processingEnv = pe;
	}
	
	/**
	 * Determines if two elements have the same name.
	 * */
	public static boolean elementsMatchNames(Element e1, Element e2) {
		return e1.getSimpleName().toString().equals(e2.getSimpleName().toString());
	}
	
	/**
	 * Compares the signatures of fields to determine if they match. This is equivalent to calling
	 * "ClassElementManager.fieldSignature(field1).equals(ClassElementManager.fieldSignature(field2))"
	 * Note that the annotations must be in the same order to match.
	 * */
	public static boolean fieldsMatch(VariableElement field1, VariableElement field2) {
		return fieldSignature(field1).equals(fieldSignature(field2));
	}
	
	//unused
	/**
	 * Determines if the modifers for two given elements match.
	 * */
	public static boolean modifiersMatch(Element e1, Element e2) {
		Iterator<Modifier> mod1 = e1.getModifiers().iterator();
		Iterator<Modifier> mod2 = e2.getModifiers().iterator();
		while(mod1.hasNext()) {
			if (!mod2.hasNext()) return false;
			if (mod1.next() != mod2.next()) return false;
		} return true;
	}
	
	/**
	 * Returns the "signature" (or header) of an field. This includes modifiers, type,
	 * and name. <p> In the future this might be changed to "elementSignature(Element e)"
	 * so that I can check other elements. (for now, I am only interested in fields) For
	 * classes this method might include "extends ClassName", and methods would include
	 * parameters.
	 * */
	public static String fieldSignature(VariableElement field) {
		StringBuilder sb = new StringBuilder();
		for (Modifier mod: field.getModifiers()) {
			sb.append(mod + " ");
		} sb.append(field.asType() + " " + field.getSimpleName());
		return sb.toString();
	}
	
	/**
	 * Returns a list of ClassElementManager objects that this object extends
	 * or implements. The first object in the list is the extended class, and
	 * the others are interfaces. I belive the Object class is included, although
	 * it probably shouldn't be. If "this" is the Object class, the returned
	 * list will be empty.
	 * */
	public List<ClassElementManager> getSuper() {
		TypeMirror typeMirror = clazz.asType();
		List<? extends TypeMirror> list = processingEnv.getTypeUtils().directSupertypes(typeMirror);
		
		List<ClassElementManager> supers = new LinkedList<>();
		for (TypeMirror type: list) {
			supers.add(new ClassElementManager((TypeElement) processingEnv.getTypeUtils().asElement(type), processingEnv));
		} return supers;
	}
	
	/**
	 * Returns the element that was input into the constructor, or this.clazz.
	 * */
	public TypeElement getClazz() { //pratically getter for clazz
		return clazz;
	}
	
	public List<? extends Element> getClassFields() {
		return classFields;
	}
	
	public List<ExecutableElement> getClassMethods() {
		return classMethods;
	}
}
