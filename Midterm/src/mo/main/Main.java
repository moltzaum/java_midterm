package mo.main;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

import mo.entertainment.EntertainmentComparator;
import mo.objects.UserList;

//Matthew Moltzau
public class Main {
	
	public static Scanner scanner = new Scanner(System.in);
	public static EntertainmentComparator comparator = new EntertainmentComparator();
	public static MENU currentMenu = MENU.START;
	public static boolean exitFlag = false;
	public static UserList list;
	public static Set<String> excludedTypes = new HashSet<>();
	
	public static void main(String[] args) {
		do {
			System.out.print(currentMenu);
			args = getArgsFromLine(scanner.nextLine());
			try {
				currentMenu.runCommand(args);
			} catch(Exception e) {
				if (e.getMessage() == null) {
					throw e;
				} else {
					System.out.println(e.getMessage());
				}
			}
		} while(exitFlag == false);
	}
	
	public static String[] getArgsFromLine(String line) {
		
		Scanner scanner = new Scanner(line);
		LinkedList<String> list = new LinkedList<String>();
		
		do {
			//String str = scanner.findInLine("\"[\\w| ]*\"|\\S+"); //word only
			String str = scanner.findInLine("\"[^\"]*\"|\\S+"); //now allows punctuation!
			if (str != null) {
				list.add(str.replace("\"", ""));
			} else break;
		} while(true);
		
		String[] args = new String[list.size()];
		for (int i = 0; i < args.length; i++) {
			args[i] = list.get(i);
		} scanner.close();	
		return args;
	}
	
	/**
	 * Returns true for option 1, otherwise option 2.
	 * */
	public static boolean haveUserChooseOption(String option1, String option2) {
		do {
			String line = scanner.nextLine();
			if (line.equals(option1)) {
				return true;
			} else if (line.equals(option2)) {
				return false;
			} else {
				System.out.printf("Please answer either \"%s\" or \"%s\"\n", option1, option2);
			}
		} while(true);
	}
}