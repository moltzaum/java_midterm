package mo.objects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RatingSystem {
	
	//This class has an ArrayList instead of extending ArrayList because if I extend ArrayList
	//I have no control over the add and remove methods unless I override them all.
	private List<OfficialRating> ratingSystem = new ArrayList<>();
	
	public RatingSystem(String ... strings) {
		if (strings.length % 2 != 0) {
			throw new IllegalArgumentException("Uneven amount of strings.");
		}
		for (int i = 0; i < strings.length; i += 2) {
			ratingSystem.add(new OfficialRating(strings[i], strings[i + 1]));
		}
	}
	
	public int size() {
		return ratingSystem.size();
	}
	
	public Iterator<OfficialRating> iterator() {
		return ratingSystem.iterator();
	}
	
	//an OfficalRating is a part of a rating system and shouldn't be made outside of this class
	public class OfficialRating implements Serializable {
		
		private static final long serialVersionUID = 2809305932855412938L;
		
		private final String rating;
		private final String description;
		
		private OfficialRating(String rating, String description) {
			this.rating = rating;
			this.description = description;
		}
		
		public String getRating() {
			return rating;
		}
		
		public String getDescription() {
			return description;
		}
		
		public String toString() {
			return '[' + rating + ", " + description + ']';
		}
	}
}
