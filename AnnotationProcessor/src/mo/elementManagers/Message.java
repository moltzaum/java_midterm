package mo.elementManagers;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

/**
 * A class to make it easier to change a message as needed.
 * Prints to a messager. Does not include functionality to
 * print to an annotation mirror or an annotation value.
 * */
public class Message {
	
	private Kind kind;
	private String msg;
	private Element e;
	public static Messager messager;
	
	public Message(Kind kind, String msg) {
		this.kind = kind;
		this.msg = msg;
	}
	
	public Message(Kind kind, String msg, Element e) {
		this.kind = kind;
		this.msg = msg;
		this.e = e;
	}
	
	public void appendToMsg(String str) {
		msg += str;
	}
	
	//Getters
	public Kind getKind() {return kind;}
	public String getMessage() {return msg;}
	public Element getElement() {return e;}
	
	//Setters
	public void setKind(Kind kind) {this.kind = kind;}
	public void setMessage(String msg) {this.msg = msg;}
	public void setElement(Element e) {this.e = e;}
	
	public void print(Element e) {
		messager.printMessage(kind, msg, e);
	}
	
	public void print() {
		if (e != null) {
			messager.printMessage(kind, msg, e);
		} else messager.printMessage(kind, msg);
	}
}
