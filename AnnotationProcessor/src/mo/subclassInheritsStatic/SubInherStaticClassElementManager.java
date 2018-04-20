package mo.subclassInheritsStatic;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

import mo.elementManagers.AnnotationClassElementManager;
import mo.elementManagers.ClassElementManager;
import mo.elementManagers.Message;
import mo.misc.HelperClass;

public class SubInherStaticClassElementManager extends AnnotationClassElementManager {
	
	//re-defining annotatedElements to be a VariableElement to suit the needs of this class
	public List<VariableElement> annotatedElements = null;
	
	/**
	 * Creates a SubInherStaticClassElementManager based off of another manager.
	 * */
	public SubInherStaticClassElementManager(ClassElementManager manager) {
		this(manager.clazz, manager.processingEnv);
	}
	
	/**
	 * @throws NullPointerException if the static variable processingEnv is null.
	 * */
	public SubInherStaticClassElementManager(TypeElement clazz, ProcessingEnvironment pe) {
		super(clazz, pe, SubInheritsStatic.class);
		this.errorMsgForNonAnnotateable.setMessage("Annotated field must be non-private and static.");
	}
	
	/**
	 * Check to see if a given is annotateable. In the case of this
	 * class, an element is annotateable if it is a field, is static,
	 * and is non-private.
	 * */
	public boolean isAnnotateable(Element field) { //param is Element so it overrides method
		if (!field.getKind().isField()) {
			return false;
		}
		boolean isStatic = false;
		for (Modifier mod: field.getModifiers()) {
			if (mod == Modifier.STATIC) {
				isStatic = true;
			} else if (mod == Modifier.PRIVATE) {
				return false;
			}
		} return isStatic;
	}
	
	/**
	 * Corresponding method name for "field" is "getField"
	 * */
	private String correspondingMethodName(VariableElement field) {
		return "get" + HelperClass.capitalizeStr(field.getSimpleName().toString());
	}
	
	/**
	 * Returns true if the given 
	 * */
	private boolean hasMethodForAnnotatedField(VariableElement field) {
		if (!field.getKind().isField()) {
			throw new IllegalArgumentException("Element must be of type field.");
		}
		for (ExecutableElement method: classMethods) { //for each method
			String methodName = correspondingMethodName(field);
			if (method.getSimpleName().toString().equals(methodName)) { //match name
				if (!field.asType().equals(method.getReturnType())) { //check return type
					processingEnv.getMessager().printMessage(
						Kind.ERROR,
						methodName + " must return " + field.asType(),
						method
					);
				}
				//even if the method return type is incorrect, return true; the method still exists				
				return true;
			}
		} return false;
	}
	
	/**
	 * Forces this class to implement annotated variables found on
	 * the superclass.
	 * */
	private void imposeFields(List<VariableElement> imposedFields) {
		
		for (VariableElement imposedField: imposedFields) {
			
			//set the default message
			Message msg = new Message(Kind.ERROR, "Class must contain ", clazz);
			
			matchFields: {
				for (VariableElement classField: classFields) {
					if (fieldsMatch(classField, imposedField, true)) {
						break matchFields; //then we good
					} else if (elementsMatchNames(classField, imposedField)) { //change message
						msg.setMessage("Illegal Modifiers. Modifiers must match superclass variable.\n"
							+ "Correct modifiers are:\n");
						msg.setElement(classField);
					}
				}
				//if break matchFields was called, this gets skipped over
				msg.appendToMsg(fieldSignature(imposedField, true));
				msg.print();
			}
		}
	}
	
	public List<VariableElement> getAnnotatedElements() {
		
		if (annotatedElements != null) return annotatedElements;
		
		List<VariableElement> annotatedElements = new ArrayList<>();
		
		for (VariableElement field: classFields) {
			Annotation anno = field.getAnnotation(this.anno);
			if (anno != null) {
				if (!isAnnotateable(field)) {
					errorMsgForNonAnnotateable.print(field);
				} else annotatedElements.add(field);
			}
		} this.annotatedElements = annotatedElements;
		return annotatedElements;
	}
	
	/**
	 * If an annotated variable needs to implement a method, print an error if the
	 * method is not implemented. A method needs to be implemented from the highest
	 * class with the annotated variable.
	 * */
	public void implementMethod(List<ClassElementManager> superObjects, List<VariableElement> annotatedElements) {
		
		for (VariableElement annotatedField: annotatedElements) {
			
			matchFields: {
			
				for (ClassElementManager sup: superObjects) {	

					SubInherStaticClassElementManager superclass = new SubInherStaticClassElementManager(sup);
					List<VariableElement> superAnnotatedElements = superclass.getAnnotatedElements();
					
					for (VariableElement superAnnotatedField: superAnnotatedElements) {
						if (fieldsMatch(annotatedField, superAnnotatedField, true)) {
							break matchFields;
						}
					}
				}
				
				//if break matchFields is called, this gets skipped over
				if (!this.hasMethodForAnnotatedField(annotatedField)) {
					processingEnv.getMessager().printMessage(
						Kind.ERROR,
						"Class must contain " + correspondingMethodName(annotatedField) +
						" method for " + annotatedField,
						annotatedField
					);
				}
			}
		}
	}
	
	//Questions asked by this class:
	//what does super impose on this class?
	//does this class need to change it's var?
	//does this class need to supply a method for it's var?
	//is this class a top-annotated class? (does the superclass carry the same annotated variable?)
	public void process() {
		
		//get a list of super objects, including interfaces
		List<ClassElementManager> superObjects = this.getSuper();
		
		List<VariableElement> annotatedElements = this.getAnnotatedElements();
		
		//This will return immediately if annotatedElements is an empty list.
		implementMethod(superObjects, annotatedElements);
		
		for (ClassElementManager sup: superObjects) {

			SubInherStaticClassElementManager superclass = new SubInherStaticClassElementManager(sup);
			List<VariableElement> superAnnotatedElements = superclass.getAnnotatedElements();
			
			//Note: imposeFields() will return immediately if given an empty list.
			imposeFields(superAnnotatedElements); //make super fields affect this class
			
			superclass.process(); //process the next type (this is recursive)
		}
	}
}


