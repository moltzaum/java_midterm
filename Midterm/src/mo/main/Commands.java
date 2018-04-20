package mo.main;

import mo.objects.Command;
import mo.objects.Time;
import mo.objects.Command.*;
import mo.objects.RatingSystem.OfficialRating;
import mo.objects.UserList;
import static mo.main.Main.*;

import java.util.Iterator;
import java.util.Scanner;

import mo.entertainment.EntertainmentComparator.CompareType;
import mo.entertainment.Entertainment;
import mo.entertainment.Movie;
import mo.entertainment.TVShow;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * A collection of final static commands to be used by menu objects. If this was an enum
 * I could iteratre through the commands.
 * */
public class Commands {
	
	public static final Command load = new Command("load",
		(SingleArg) (directory) -> {
			
			try {
				
				//file is only one line
				FileReader reader = new FileReader(directory);
				
				if (!directory.endsWith(".lst")) {
					System.out.println("Can only open file with .lst extension.");
					reader.close();
					return;
				}
				
				if (list != null) {
					System.out.println("A list is already loaded. Override?");
					if (!Main.haveUserChooseOption("yes", "no")) {
						System.out.println("Load canceled.");
						reader.close();
						return;
					}
				}
				
				Scanner file = new Scanner(reader);
				String line = file.nextLine();
				System.out.println(line);
				String[] args = Main.getArgsFromLine(line);
				reader.close();
				file.close();
				
				UserList list = new UserList(args[0]);
				
				for (int i = 1; i < args.length; i++) {
					
					String[] objectVariables = args[i].split(" ");
					
					Entertainment obj = Entertainment.createSubclassFrom(objectVariables[0]);
					obj.setTitle(objectVariables[1].replace("_", " "));
					obj.setOfficialRating(objectVariables[2]);
					if (obj instanceof Movie) {
						((Movie) obj).setRunTime(new Time(objectVariables[3]));
					} else if (obj instanceof TVShow) {
						((TVShow) obj).setNumbOfSeasons(Integer.parseInt(objectVariables[3]));
					}
					list.add(obj);
				}
				
				Main.list = list;
				currentMenu = MENU.LIST;
				System.out.println("List loaded.");
				
			} catch(FileNotFoundException e) {
				System.out.println("File doesn't exist.");
			} catch(IOException e) { //idk what happened
				System.out.println("Could not read file. (IDK why)");
			} catch(Exception e) { //my fault
				System.out.println("Could not read file. (My fault)");
				throw e;
			}
		}
	);
	
	public static final Command new_ = new Command("new",
		(SingleArg) (name) -> {
			if (list != null) {
				System.out.println("Are you sure you want to create a new list? The old one will be overriden.");
				if (!Main.haveUserChooseOption("yes", "no")) {
					System.out.println("Cancelling.");
					return;
				}
			}
			list = new UserList(name);
			currentMenu = MENU.LIST;
			System.out.println("List created.");
		}
	);
	
	public static final Command list_ = new Command("list",
		(EmptyArgs) () -> {
			if (list == null) {
				System.out.println("Cannot go to list, list isn't isn't created yet.");
			} else currentMenu = MENU.LIST;
		}
	);
	
	public static final Command exit = new Command("exit",
		(EmptyArgs) () -> {
			if (list == null) {
				exitFlag = true;
				return;
			}
			System.out.println("Are you sure you want to exit? Unsaved changes will be lost.");
			if (Main.haveUserChooseOption("yes", "cancel")) {
				exitFlag = true;
			}
		}
	);
	
	public static final Command print = new Command("print", 0, 1,
		(Args) (args) -> {
			
			boolean isDetailed = false;
			
			if (args.length == 1 && args[0].equals("detailed")) {
				isDetailed = true;
			}
			
			if (currentMenu == MENU.LIST) {
				if (list.isEmpty()) {
					System.out.println("List is empty.");
				} else {
					System.out.println(list.print(Main.excludedTypes, isDetailed));
				}
			} else if (currentMenu == MENU.ITEM_SELECTION) {
				if (!list.hasSelection()) {
					System.out.println("No item is selected.");
				} else {
					System.out.println(list.getSelection().toString(isDetailed));
				}
			}
		}
	);
	
	public static final Command select = new Command("select",
		(SingleArg) (name) -> {
			if (list == null) {
				System.out.println("Cannot select anything yet, list isn't created.");
			} else {
				list.select(name);
				currentMenu = MENU.ITEM_SELECTION;
			}
		}
	);
	
	public static final Command save = new Command("save",
		(EmptyArgs) () -> {
			
		    File file = new File(list.getName() + ".lst");
		    
		    try {

			    if (file.exists() == false) {
			    	System.out.println("Creating new file.");
			    	file.createNewFile();
		    	} else {
		    		System.out.println("There is already a file by that name. Overwrite?");
		    		if (Main.haveUserChooseOption("yes", "no")) {
		    			file.delete();
		    			file.createNewFile();
		    		} else {
		    			return;
		    		}
		    	}
			    
			    PrintWriter outFile = new PrintWriter(new FileWriter(file, true));
			    
			    Iterator<Entertainment> iter = list.iterator();
			    StringBuilder sb = new StringBuilder();
			    while(iter.hasNext()) {
			    	Entertainment item = iter.next();
			    	String type = null;
			    	String title = item.getTitle();
			    	String rating = item.getOfficialRating().getRating();
			    	String otherVar = null;
			    	if (item instanceof Movie) {
			    		type = "Movie";
			    		otherVar = ((Movie) item).getRunTime().getTime();
			    	} else if (item instanceof TVShow) {
			    		type = "TVShow";
			    		otherVar = Integer.toString(((TVShow) item).getNumbOfSeasons());
			    	}
			    	sb.append(String.format("\"%s %s %s %s\"", type, title.replace(" ", "_"), rating, otherVar));
			    	if (iter.hasNext()) {
			    		sb.append(" ");
			    	}
			    }
			    
				outFile.printf("%s %s", list.getName(), sb.toString());
				outFile.close();
			    
		    } catch(IOException e) {
		    	System.out.println("IOException. Save not successful.");
		    }
		    			
		}
	);
	
	public static final Command sortby = new Command("sortby",
		(SingleArg) (arg) -> {
			switch(arg.toLowerCase()) {
			case "title":
				comparator.setCompareType(CompareType.TITLE);
				break;
			case "type":
				comparator.setCompareType(CompareType.TYPE);
				break;
			case "ratings":
				comparator.setCompareType(CompareType.OFFICIAL_RATING);
			}
			
			if (list != null) {
				list.sort(comparator);
			}
		}
	);
	
	public static final Command sortOptions = new Command("sortoptions",
		(EmptyArgs) () -> {
			currentMenu = MENU.SORT_OPTIONS;
		}
	);
	
	public static final Command back = new Command("back",
		(EmptyArgs) () -> {
			if (currentMenu == MENU.LIST || list == null) {
				currentMenu = MENU.START;
			} else currentMenu = MENU.LIST;
		}
	);
	
	public static final Command add = new Command("add", 2, 2,
		(Args) (args) -> {
			
			Entertainment item = Entertainment.createSubclassFrom(args[0]);
			item.setTitle(args[1]);
			
			System.out.print("What is the rating?");
			StringBuilder sb = new StringBuilder(" (");
			Iterator<OfficialRating> iter = item.getRatingSystem().iterator();
			while(iter.hasNext()) {
				sb.append(iter.next().getRating());
				if (iter.hasNext()) {
					sb.append(", ");
				}
			} sb.append(')');
			String hint = sb.toString();
			System.out.println(hint);
			
			do {
				try {
					item.setOfficialRating(scanner.nextLine());
					break;
				} catch(Exception e) {
					System.out.print(e.getMessage());
					System.out.println(hint);
				}
			} while(true);
			
			if (item instanceof Movie) {
				System.out.println("What is the runtime? (hrs:mins)");
				do {
					String input = scanner.nextLine();
					args = input.split(":");
					
					try {
						Time time = new Time(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
						((Movie) item).setRunTime(time);
						break;
					} catch(NumberFormatException e) {
						System.out.println("Please enter 2 integer values separated by a colon.");
					} catch(IllegalArgumentException e) {
						System.out.println(e.getMessage());
					}
				} while(true);
				
			} else if (item instanceof TVShow) {
				System.out.println("How many seasons are there?");
				do {
					String input = scanner.nextLine();
					try {
						((TVShow) item).setNumbOfSeasons(Integer.parseInt(input));
						break;
					} catch(NumberFormatException e) {
						System.out.println("Please enter an integer value.");
					}
				} while(true);
			}
			
			list.add(item);		
		}
	);
	
	
	public static final Command include = new Command("include",
		(SingleArg) (arg) -> {
			
			if (arg.equals("all")) {
				excludedTypes.clear();
				return;
			}
			
			if (!excludedTypes.remove(arg)) {
				System.out.printf("No types by the name of %s are in the excluded list.\n", arg);
			}
		}
	);
	
	
	public static final Command exclude = new Command("exclude",
		(SingleArg) (arg) -> {
			switch(arg) {
			case "movie":
			case "tvshow":
				excludedTypes.add(arg);
				break;
			default:
				throw new IllegalArgumentException("Can only exclude types. (\"movie\" or \"tvshow\")");
			}
			
		}
	);
	
	public static final Command delete = new Command("delete",
		(EmptyArgs) () -> {
			
			String item = null;
			if (currentMenu == MENU.LIST) {
				item = list.getName();
			} else if (currentMenu == MENU.ITEM_SELECTION) {
				item = list.getSelection().getTitle();
			} else {
				throw new IllegalArgumentException();
			}
			
			System.out.printf("Are you sure you want to delete \"%s\"?\n", item);
			if (Main.haveUserChooseOption("yes", "no")) {
				if (currentMenu == MENU.LIST) {
					list = null;
				} else if (currentMenu == MENU.ITEM_SELECTION) {
					list.remove(list.getSelection());
				}
				back.execute();
			}
		}
	);
	
	//there used to be more in here, normally I wouldn't make a list that has just two elements.
	public static final Command[] elements = {
		delete,
		print,
	};
}

