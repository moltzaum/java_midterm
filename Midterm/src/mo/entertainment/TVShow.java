package mo.entertainment;
import mo.objects.RatingSystem;

public class TVShow extends Entertainment {
	
	private static final long serialVersionUID = -9074356494827550176L;
		
	//a season is a list of episodes, but this will just have the number and not an actual list
	private int seasons;
	
	/**
	 * Rating, Description. <p>
	 * e.j. "G", "General Audiences"
	 * */
	protected static RatingSystem ratingSystem;
	
	static { //init the rating system for TVShow (US) //http://transition.fcc.gov/vchip/
		ratingSystem = new RatingSystem(
			"TV-Y", "All Children",
			"TV-Y7", "Directed to Older Children", //7 and above
			"TV-G", "General Audience",
			"TV-PG", "Parental Guidance Suggested",
			"TV-14", "Parents Strongly Cautioned",
			"TV-MA", "Mature Audience Only");
	}
	
	public TVShow() {
		super();
	}
	
	public TVShow(String title, String rating, int seasons) {
		super(title, rating);
		setNumbOfSeasons(seasons);
	}
	
	public void setNumbOfSeasons(int seasons) {
		this.seasons = seasons;
	}
	
	public int getNumbOfSeasons() {
		return seasons;
	}
	
	public String toString() { //ratings.get(rating)
		return String.format("[Type: %s, Title: %s, Rating: %s, #Of Seasons: %s]",
			getClass().getSimpleName(),
			getTitle(),
			getOfficialRating(),
			//this.
			getNumbOfSeasons());
	}
}
