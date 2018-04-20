package mo.objects;


import static mo.main.Main.currentMenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

import mo.entertainment.Entertainment;
import mo.main.MENU;

/**
 * This class is a list of Entertainment objects. It might as well extend ArrayList<Entertainment>
 * (The current implementation feels DRY). This class isn't completely DRY as it has a name variable
 * and selection variable. <p>
 * 
 * Originally, I was going to allow the user to apply Ratings to an object, but I didn't want them
 * directly attached to Entertainment objects. In this implementation I mapped ratings to objects
 * via this class. I didn't extend ArrayList so that list-changing methods couldn't be called without
 * this class knowing. <p>
 * 
 * Future implementation?: <p>
 * List < ObjectForList > <p>
 * ObjectForList: has Entertainment, has rating <p>
 * */
public class UserList implements Serializable {
	
	private static final long serialVersionUID = 530383651660037607L;
		
	private ArrayList<Entertainment> list = new ArrayList<>();
	private String name;
	private Entertainment selection = null;
	
	UserList() {
		//do nothing
	}
	
	public UserList(String name) {
		this.name = name;
	}
	
	public Iterator<Entertainment> iterator() {
		return list.iterator();
	}
	
	public boolean add(Entertainment e) {
		return list.add(e);
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
	public int size() {
		return list.size();
	}
	
	public Entertainment get(int i) {
		return list.get(i);
	}
	
	public Entertainment remove(int i) {
		selection = null;
		return list.remove(i);
	}
	
	public boolean remove(Entertainment e) {
		selection = null;
		return list.remove(e);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void sort(Comparator<? super Entertainment> c) {
		list.sort(c);
	}
	
	public boolean hasSelection() {
		return (selection != null);
	}
	
	public Entertainment select(String name) {
		for (int i = 0; i< list.size(); i++) {
			if (list.get(i).getTitle().equals(name)) {
				currentMenu = MENU.ITEM_SELECTION;
				selection = get(i);
				return selection;
			}
		} throw new IllegalArgumentException(String.format("Entertainment with title \"%s\" not found", name));
	}
	
	public Entertainment select(int i) {
		selection = list.get(i);
		return selection;
	}
	
	public Entertainment getSelection() {
		return selection;
	}
	
	public String print(Set<String> excludedTypes, boolean b) {
		StringBuilder sb = new StringBuilder();
		for (Entertainment e: list) {
			
			excludeType: {
			
				for (String type: excludedTypes) {
					if (e.getClass().getSimpleName().toLowerCase().equals(type)) {
						break excludeType;
					}
				}
				
				//if break excludeType is called, this gets skiped over
				sb.append(e.toString(b) + "\n");
			}
		} return sb.toString();
	}
}
