package mo.entertainment;
import mo.objects.RatingSystem;
import mo.objects.Time;

public class Movie extends Entertainment {
		
	private static final long serialVersionUID = -8926636298322120458L;
	
	private Time runtime;
	
	/**
	 * Rating, Description. <p>
	 * e.j. "G", "General Audiences"
	 * */
	protected static RatingSystem ratingSystem;
	
	static { //init the rating system for Movie (US) //http://filmratings.com/how.html
		ratingSystem = new RatingSystem(
			"G", "General Audiences",
			"PG", "Parental Guidance",
			"PG-13", "Parents Strongly Cautioned",
			"R", "Restricted",
			"NC-17", "No one under 17 admitted",
			"NR", "Not Rated");
	}
	
	public Movie() {
		super();
	}
	
	public Movie(String title, String rating, Time runtime) {
		super(title, rating);
		this.runtime = runtime;
	}
	
	public void setRunTime(Time runtime) {this.runtime = runtime;}
	public Time getRunTime() {return runtime;}
	
	public String toString() {
		return String.format("[Type: %s, Title: %s, Rating: %s, Runtime: %s]",
			getClass().getSimpleName(),
			getTitle(),
			getOfficialRating(),
			getRunTime());
	}
}
