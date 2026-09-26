/*
 * Provides a representation of matrix quadrants.
 * 
 * @author Joseph Blandon
 */
package edu.cwru.jab542.strassen;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

/**
 * The Class Quadrant.
 */
public final class Quadrant {
	
	/** The map of quadrants. */
	private final EntryMap<Matrix> quadrants;
	
	/** The size of the underlying matrix. */
	private int size;
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the EntryMap of the quadrants.
	 *
	 * @return the quadrants
	 */
	public EntryMap<Matrix> getQuadrants(){
		return this.quadrants;
	}
	
	/**
	 * Instantiates a new quadrant object.
	 *
	 * @param quadrants the map of quadrants of the underlying matrix
	 */
	private Quadrant(Map<Coordinates, Matrix> quadrants) {
		this.quadrants = EntryMap.from(quadrants);
		this.size = quadrants.get(new Coordinates(0, 0)).getRepresentation().sizeRounded() * 2;
	}
	
	/**
	 * Creates a Quadrant object from a Map of quadrants. 
	 *
	 * @param quadrants the quadrants of the underlying matrix
	 * @return the quadrant
	 */
	public static Quadrant from(Map<Coordinates, Matrix> quadrants) {
		int referenceSize = quadrants.get(Coordinates.ORIGIN).getSize();
		for(Entry<Matrix> entry : EntryMap.from(quadrants).stream().toList()) 
			InconsistentSizeException.validate(referenceSize, entry.value(), Optional.ofNullable(entry.coordinates()));
		
		return new Quadrant(quadrants);
	}
	
	/**
	 * Converts the quadrants to a matrix.
	 *
	 * @return the matrix
	 */
	public Matrix toMatrix() {
		NavigableMap<Coordinates, Float> newMap = new TreeMap<Coordinates, Float>();
		
		List<Entry<Matrix>> quadrantsList = this.quadrants.stream().toList();
		
		for(Entry<Matrix> entry : quadrantsList) {
			this.putInMap(newMap, entry);
		}
		
		return Matrix.from(newMap);
		
		
	}
	
	/**
	 * Puts all elements in a quadrant into the map of the new matrix. 
	 *
	 * @param entryMap the entry map of the new matrix
	 * @param quadrant the quadrant being put in the map 
	 */
	private void putInMap(NavigableMap<Coordinates, Float> entryMap, Entry<Matrix> quadrant) {
		
		// Determines the Coordinates that the quadrant will start to 
		int horizontalOffset = 0;
		int verticalOffset = 0;
		
		if(quadrant.coordinates().col() == 1)
			horizontalOffset = this.getSize() / 2;
		if(quadrant.coordinates().row() == 1)
			verticalOffset = this.getSize() / 2;
		
		Coordinates offset = new Coordinates(verticalOffset, horizontalOffset);
		
		// Puts each value in the quadrant in the proper place. 
		List<Entry<Float>> quadrantList = quadrant.value().getRepresentation().stream().toList();
		
		for(Entry<Float> entry : quadrantList) {
			entryMap.put(entry.coordinates().plus(offset), entry.value());
		}
	}
	
}
