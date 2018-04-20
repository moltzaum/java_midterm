package mo.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import mo.objects.Command;

/**
 * This enum represents menus which are specific to Main. In future implementation,
 * Menu will be a class so that a heiarchy can be made and the back button will be
 * more intuitive.
 * */
public enum MENU {
	
	START(),
	LIST(),
	SORT_OPTIONS(),
	ITEM_SELECTION();
	
	//unformatted representation of menu. becomes formatted witj information when printed 
	private final String display;
	private final String name;
	//commands that this menu can run
	private List<Command> commands = new ArrayList<>();
	
	MENU() {
		name = this.name().toLowerCase().replace('_', ' ');
		display = loadMenu();
	}
	
	public String getName() {
		return name;
	}
	
	public void runCommand(String[] args) {
		boolean commandNameRecognized = false;
		for (Command c: commands) {
			if (c.getName().equals(args[0].toLowerCase())) {
				commandNameRecognized = true;
				if (c.argsLengthMatchCmnd(args.length - 1)) {
					args = c.removeFirstArg(args);
					c.execute(args);
					return;
				}
			}
		}
		
		if (commandNameRecognized) {
			throw new IllegalArgumentException("Arguments Not Recognized.");
		} else throw new IllegalArgumentException(String.format("%s cannot run %s", this.getName(), args[0]));
	}
	
	/**
	 * Reads a line to add commands, and appends the line to the StringBuilder if
	 * applicable for the given command.
	 * */
	private void processLine(String line, StringBuilder sb) {
		
		int index1 = line.indexOf('/');
		int index2 = line.lastIndexOf('/');
		boolean appendLine = true;
		
		//if there is one /, index1 == index2
		//if there is no /, both index1 and index2 are -1
		if (!(index1 == -1 || index1 == index2)) { //if there is a command to add
			
			String command = line.substring(index1 + 1, index2);
			
			if (command.indexOf('%') != -1) {
				//this is an exception where there are / / but it isn't a command
				sb.append(line + "\n");
				return;
			}
			line = line.replaceFirst("/ ", " ");
						
			try {
				this.getClass().getClassLoader().loadClass("mo.main.Commands");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			switch(command) {
			case "ElementCommands":
				for (Command c: Commands.elements) {
					commands.add(c);
				}
				sb.append(
					" /Delete <>\n" +
					" /Print <\"\":\"detailed\">\n");
				appendLine = false;
				break;
			case "Load": //might be able to use java reflect to do this better
				commands.add(Commands.load);
				break;
			case "Save":
				commands.add(Commands.save);
				break;
			case "New":
				commands.add(Commands.new_);
				break;
			case "List":
				commands.add(Commands.list_);
				break;
			case "Select":
				commands.add(Commands.select);
				break;
			case "Exit":
				commands.add(Commands.exit);
				break;
			case "SortBy":
				commands.add(Commands.sortby);
				break;
			case "Delete":
				commands.add(Commands.delete);
				break;
			case "Print":
				commands.add(Commands.print);
				break;
			case "SortOptions":
				commands.add(Commands.sortOptions);
				break;
			case "Include":
				commands.add(Commands.include);
				break;
			case "Exclude":
				commands.add(Commands.exclude);
				break;
			case "Back":
				commands.add(Commands.back);
				break;
			case "Add":
				commands.add(Commands.add);
				break;
			default:
				throw new IllegalArgumentException(String.format("Command \"%s\" not recognized.", command));
			}
		} if (appendLine) sb.append(line + "\n");
	}
	
	/**
	 * Loads commands based off a text file and returns the unformatted
	 * final display string.
	 * */
	public String loadMenu() {
		String section = this.getName();
		StringBuilder sb = new StringBuilder();
		try {
			Scanner file = new Scanner(new FileReader("Menu.txt"));
			boolean scanning = false;
			boolean end = false;
			
			while (file.hasNextLine() && !end) {
				String line = file.nextLine();
				if (line.equals(section)) { //start scanning at section
					scanning = true;
				} else if (scanning && line.equals("end")) { //end
					end = true;
				} else if (scanning) {
					processLine(line, sb);
				}
			} file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return sb.toString();
	}
	
	public String toString() {
		
		Object[] args = null;
		
		switch(this) {
		case ITEM_SELECTION:
			args = new String[2];
			args[1] = Main.list.getSelection().getTitle();
		case LIST:
			if (args == null) {
				args = new String[1];
			}
			args[0] = Main.list.getName();
			break;
		case SORT_OPTIONS:
			args = new String[2];
			args[0] = Main.comparator.getPrimaryCompareType().name().toLowerCase();
			
			StringBuilder sb = new StringBuilder();
			Iterator<String> iter = Main.excludedTypes.iterator();
			while (iter.hasNext()) {
				sb.append(iter.next());
				if (iter.hasNext()) {
					sb.append(", ");
				}
			} args[1] = sb.toString();
			
			if (args[1].equals("")) {
				args[1] = "None";
			}
			break;
		case START: //start display menu has no %s
		}
		
		return String.format(display, args) + ">>? ";
	}
}
