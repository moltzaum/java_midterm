package mo.entertainment;


import java.util.Comparator;
import java.util.Iterator;

import mo.objects.RatingSystem.OfficialRating;

public class EntertainmentComparator implements Comparator<Entertainment> {
	
	public enum CompareType {
		TYPE,
		TITLE,
		OFFICIAL_RATING,
	}
	
	private CompareType primaryType = CompareType.TYPE;
	private CompareType secondaryType = CompareType.TITLE;
	
	//If I had a much bigger enum, this would be cumbersome. In a similar
	//situation in the future, lists might be helpful
	public void setCompareType(CompareType compareType) {
		this.primaryType = compareType;
		switch(primaryType) {
		case TITLE:
			secondaryType = CompareType.TYPE;
			break;
		case TYPE:
			secondaryType = CompareType.TITLE;
			break;
		case OFFICIAL_RATING:
			secondaryType = CompareType.TYPE;
			break;
		}
	}
	
	/**
	 * Because there are different kinds of rating systems, this method
	 * returns a percentage representing the rating's position in it's
	 * rating system.
	 * */
	public float getValueOfRating(Entertainment e) {
		Iterator<OfficialRating> iter = e.getRatingSystem().iterator();
		float length = e.getRatingSystem().size();
		OfficialRating rating = e.getOfficialRating();
		float pos = 0;
		while (iter.hasNext()) {
			pos++; //increment position
			if (iter.next() == rating) {
				break;
			}
		} return length / pos;
	}
	
	public CompareType getPrimaryCompareType() {
		return primaryType;
	}
	
	public int compare(Entertainment e1, Entertainment e2) {
		
		int types = e1.getClass().getSimpleName().compareTo(e2.getClass().getSimpleName());
		int titles = e1.getTitle().compareTo(e2.getTitle());
		
		float f1 = getValueOfRating(e1);
		float f2 = getValueOfRating(e2);
		int rating = Float.compare(f1, f2);
		
		int[] array = {types, titles, rating};
				
		int i = array[primaryType.ordinal()];
		if (i != 0) {
			return i;
		} else { //if first type is undecisive
			i = array[secondaryType.ordinal()];
			if (i != 0) {
				return i;
			} else {
				
				//get the odd-ordinal out
				for (CompareType type: CompareType.values()) {
					if (type.ordinal() != primaryType.ordinal()
							&& secondaryType.ordinal() != secondaryType.ordinal()) {
						i = array[type.ordinal()];
						
					}
				} return i;
			}
		}
		
		//this should work, test later
		
		//sgn = signum
		
		//From the javadoc:
		//The implementor must ensure that sgn(compare(x, y)) == -sgn(compare(y, x)) for all x and y.
		//(This implies that compare(x, y) must throw an exception if and only if compare(y, x) throws
		//an exception.)

		//The implementor must also ensure that the relation is transitive: ((compare(x, y)>0) &&
		//(compare(y, z)>0)) implies compare(x, z)>0.

		//Finally, the implementor must ensure that compare(x, y)==0 implies that sgn(compare(x, z))=
		//sgn(compare(y, z)) for all z.
	}
	
}
