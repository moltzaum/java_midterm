package mo.objects;


/**
 * This class allows the representation of time in hours, minutes, and seconds.
 * Hours do not have a maximum capacity, so this class is not like a clock.
 * No negative times are allowed.
 * */
public class Time {
	
	private int hours = 0;
	private int minutes = 0;
	private int seconds = 0;
	
	public Time() {
		// does nothing
	}
	
	//implement in future:
	//public Time String ... args
	//public Time int ... args
	
	public Time(String str) {
		String[] vars = str.split(":");
		switch(vars.length) {
		case 1:
			setMinutes(Integer.parseInt(vars[0]));
			break;
		case 2:
			setHours(Integer.parseInt(vars[0]));
			setMinutes(Integer.parseInt(vars[1]));
			break;
		case 3:
			setTime(Integer.parseInt(vars[0]), Integer.parseInt(vars[1]), Integer.parseInt(vars[2]));
			break;
		}
	}
	
	/**
	 * Create a time in hours, minutes, and seconds.
	 * */
	public Time(int hours, int minutes, int seconds) {
		setTime(hours, minutes, seconds);
	}
	
	/**
	 * Create a time in minutes and seconds.
	 * */
	public Time(int minutes, int seconds) {
		setMinutes(minutes);
		setSeconds(seconds);
	}
	
	//SETTERS
	
	/**
	 * Set the time in hours, minutes, seconds.
	 * */
	public void setTime(int hours, int minutes, int seconds) {
		setHours(hours);
		setMinutes(minutes);
		setSeconds(seconds);
	}
	
	//Because there isn't a field for hours to fill into, hours can be
	//represented by a number larger than 24
	public void setHours(int i) {
		if (i < 0) {
			throw new IllegalArgumentException("Negative time.");
		} hours = i;
	}
	
	public void setMinutes(int i) {
		if (i < 0) {
			throw new IllegalArgumentException("Negative time.");
		} else if (i >= 60) {
			throw new IllegalArgumentException("Specified minutes are out of bounds.");
		} minutes = i;
	}
	
	public void setSeconds(int i) {
		if (i < 0) {
			throw new IllegalArgumentException("Negative time.");
		} else if (i >= 60) {
			throw new IllegalArgumentException("Specified seconds are out of bounds.");
		} seconds = i;
	}
	
	//GETTERS
	
	public int getHours() {return hours;}
	public int getMinutes() {return minutes;}
	public int getSeconds() {return seconds;}
	
	/**
	 * Return a string representation of time, different from toString.
	 * This includes variables separated by colons. Returns hrs:min if
	 * there are hours and min:sec if there are no hours.
	 * */
	public String getTime() {
		if (hours == 0) {
			return minutes + ":" + seconds;
		} else {
			return hours + ":" + minutes;
		}
	}
	
	/**
	 * Displays the time in a way that (hopefully) makes sense. <p>
	 * It is implemented like so: <p>
	 * if hours = 0, <p>
	 *   add minutes if possible <p>
	 *   add seconds if possible <p>
	 *   if time is not set, say something <p>
	 * else <p>
	 *   add hours <p>
	 *   add minutes if possible <p>
	 * 
	 * If a value is 0, it is not considered to not be set and it is therefore not displayed.
	 * If hours are displayed, seconds become insignifigant and are not displayed.
	 * */
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		if (hours == 0) { //if there are no hours
			
			if (minutes == 0 && seconds == 0) {
				sb.append("Time is not set.");
			}
			
			boolean addAnd = false;
			
			if (minutes != 0) { //add minutes if possible
				sb.append(minutes + " minutes");
				addAnd = true;
			}
			
			if (seconds != 0) { //add seconds if possible
				sb.append(((addAnd) ? " and " : "") + seconds + " seconds");
			}
			
		} else {
			sb.append(hours + " hours"); //add hours
			if (minutes != 0) { //add minutes if possible
				sb.append(" and " + minutes + " minutes");
			}
		} return sb.toString();
	}
}
