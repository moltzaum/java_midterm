package mo.objects;

public class Command {
	
	private String name;
	private Method method;
	private int minArgs = -1;
	private int maxArgs = -1;
	
	//Note: Method is not a function interface, you can't do (Method) () -> {};
	private interface Method {}
	
	public interface EmptyArgs extends Method {
		void execute();
	}
	
	public interface SingleArg extends Method {
		void execute(String arg);
	}
	
	public interface Args extends Method {
		void execute(String ... args);
	}
	
	public Command(String name, Method method) {
		this.name = name;
		this.method = method;
		if (method instanceof EmptyArgs) {
			minArgs = 0;
			maxArgs = 0;
		} else if (method instanceof SingleArg) {
			minArgs = 1;
			maxArgs = 1;
		} else {
			throw new IllegalArgumentException("Max and min args should be specified.");
		}
	}
	
	public Command(String name, int minArgs, int maxArgs, Method method) {
		this.name = name;
		this.method = method;
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
		if (method instanceof EmptyArgs || method instanceof SingleArg) {
			throw new IllegalArgumentException("Max and min args for method type are constant.");
		}
		initOrCheckArgs();
	}
	
	private void initOrCheckArgs() {
		if (minArgs == -1) {
			throw new IllegalArgumentException("MinArgs should be specified.");
		}
		if (maxArgs == -1) {
			throw new IllegalArgumentException("MaxArgs should be specified.");
		}
		
		//if maxArgs == 0, method can only have EmptyArgs
		//if maxArgs == 1, method can be EmptyArgs or SingleArg
		//min can't be bigger than max
		//min can't be negative
		//max can't be negative
		//if max is negative, min is bigger than max 
		if (maxArgs == 0 || minArgs < 0 || minArgs > maxArgs){
			throw new IllegalArgumentException();
		}
	}

	public String getName() {
		return name;
	}

	public int getMinArgs() {
		return minArgs;
	}
	
	
	public int getMaxArgs() {
		return maxArgs;
	}
	
	public boolean hasEmptyArgs() {
		return method instanceof EmptyArgs;
	}
	
	public boolean hasSingleArg() {
		return method instanceof SingleArg;
	}
	
	public boolean hasArgs() {
		return method instanceof Args;
	}
	
	public final String[] removeFirstArg(String[] args) {
		String[] newArgs = new String[args.length - 1];
		for (int i = 0; i < newArgs.length; i++) {
			newArgs[i] = args[i + 1];
		} return newArgs;
	}
	
	public final boolean argsLengthMatchCmnd(int argsLength) {
		return argsLength >= minArgs && argsLength <= maxArgs;
	}
	
	public final void execute(String ... args) { //excludes args[0]
		if (!argsLengthMatchCmnd(args.length)) {
			throw new IllegalArgumentException("Arguments Not Recognized.");
		}
		if (method instanceof EmptyArgs) {
			((EmptyArgs) method).execute();
		} else if (method instanceof SingleArg) {
			((SingleArg) method).execute(args[0]);
		} else if (method instanceof Args) {
			((Args) method).execute(args);
		}
	}
}
