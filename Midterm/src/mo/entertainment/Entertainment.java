package mo.entertainment;

import java.io.Serializable;
import java.util.Iterator;

import mo.objects.RatingSystem;
import mo.objects.RatingSystem.OfficialRating;

//I don't implement the comparable interface, but I have made a comparator.
public abstract class Entertainment implements Serializable {
	
	private static final long serialVersionUID = -397787612325317466L;
	
	private String title;
	
	protected static RatingSystem ratingSystem;
	
	protected OfficialRating rating; //the particular rating for an instance
	
	Entertainment() {
		//do nothing
	}
	
	Entertainment(String title, String officalRating) {
		setTitle(title);
		setOfficialRating(officalRating);
	}
	
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
	//meant to be used with the annotation processor I made.
	public RatingSystem getRatingSystem() {
		if (this instanceof Movie) {
			return Movie.ratingSystem;
		} else if (this instanceof TVShow) {
			return TVShow.ratingSystem;
		} else return null;
		/*
		try {
			return (RatingSystem) this.getClass().getField("ratingSystem").get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			System.exit(1);
		} return null; //this is dead code but eclipse doesn't recognize it */
	}
	
	public OfficialRating getOfficialRating() {
		return rating;
	}
	
	public void setOfficialRating(String rating) {
		Iterator<OfficialRating> iter = this.getRatingSystem().iterator();
		while (iter.hasNext()) {
			OfficialRating officialRating = iter.next();
			if (officialRating.getRating().toLowerCase().equals(rating.toLowerCase())) {
				this.rating = officialRating;
				return;
			}
		} throw new IllegalArgumentException("Rating not recognized.");
	}
	
	public static Entertainment createSubclassFrom(String str) {
		switch(str.toLowerCase()) {
		case "movie":
			return new Movie();
		case "tvshow":
			return new TVShow();
		default:
			throw new IllegalArgumentException("Input not recognized as an entertainment object.");
		}
	}
	
	public String toSimpleString() {
		return String.format("[%s:%s]",
			getClass().getSimpleName(),
			getTitle());
	}
	
	public String toString(boolean b) {
		if (b) return toString();
		else return toSimpleString();
	}
}

