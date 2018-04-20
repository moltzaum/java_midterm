package mo.objects;

/**
 * A floating point number which can range from 1 to 5. This is not affiliated
 * with RatingSystem. This class is a user rating, and is unfortunately unused.
 * */
public class Rating {
	
	private float rating;
	
	public Rating(float rating) {
		setRating(rating);
	}
	
	/**
	 * Set the rating. Only numbers 1-5 is allowed.
	 * */
	public void setRating(float rating) {
		if (rating < 1 || rating > 5) {
			throw new IllegalArgumentException("Rating is out of bounds.");
		} this.rating = rating;
	}
	
	/**
	 * Get the rating.
	 * */
	public float getRating() {
		return rating;
	}
	
	/**
	 * All precision is lost. No rounding. Truncated.
	 * */
	public int getFlatRating() {
		return (int) rating;
	}
	
	/**
	 * Return rating rounded to a full integer.
	 * */
	public int getRoundRating() {
		return Math.round(rating);
	}
	
	/**
	 * Return rating rounded to the nth place.
	 * */
	public float getRoundRating(int n) {
		n = (int) Math.pow(10, n); //10^0 == 1
		rating *= n;
		rating = Math.round(rating);
		rating /= n;
		return rating;
	}
	
	/**
	 * Displayes rating to the hundredths place and does not round.
	 * */
	public String toString() {
		return String.format("%.2f", rating);
	}
	
	/*
	int comparison = String.format("%.2f", rating).compareTo(Float.toString(rating));
	if (comparison < 0) {
		//float is more than .2f
	} else if (comparison > 0) {
		//float is less than .2f
	} //*/
}
