package mo.subclassInheritsStatic;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import mo.elementManagers.Message;

@SupportedSourceVersion(SourceVersion.RELEASE_8) //indicates the latest release this works on
@SupportedAnnotationTypes("*") //allows me to process all classes. I process all classes because
//I start at any given class and check it's superclasses for the annotated variable.
public class SubclassInheritsStaticProcessor extends AbstractProcessor {
	
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		//Filter out the class types
		Set<TypeElement> classes = ElementFilter.typesIn(roundEnv.getRootElements());
		//Allow Message class to print errors by giving it the messager
		Message.messager = processingEnv.getMessager();
		
		for (TypeElement clazz: classes) { //for each class, process the "SubInherStatic" annotation
			//try {
				new SubInherStaticClassElementManager(clazz, processingEnv).process();
			//} catch(NullPointerException e) {
				//When classes using the annotation get deleted a NullPointerException may occur.
				//I can't replicate it every time, and once I got a ConcurrentModification Exception
				//instead. (which may have since been fixed)
				//As far as I can tell it is harmless, and using the currently coded-out try
				//block would be acceptable. That being said, I don't think absorbing the
				//exception is the best idea, especially since I don't know what causes it in
				//my code.
			//}
		} return false; //should return false.
		//returning true implies that '*' (all annotations)
		//have been claimed/taken care of
	}
}

