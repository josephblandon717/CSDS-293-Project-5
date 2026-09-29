/*
 * Provides a representation of matrix quadrants.
 * 
 * @author Joseph Blandon
 */
package edu.cwru.jab542.strassen;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	 * Gets the quadrant size.
	 *
	 * @return the size of each quadrant
	 */
	private int getQuadrantSize() {
		return this.getSize() / 2;
	}
	
	/**
	 * Gets the reference size from a map.
	 * 
	 * @param quadrants the map of quadrants
	 * @return the size of the matrix at the origin of the map
	 */
	private static int getReferenceSize(Map<Coordinates, Matrix> quadrants) {
		Objects.requireNonNull(quadrants);
		Matrix quadrant = Objects.requireNonNull(quadrants.get(Coordinates.ORIGIN));
		return quadrant.getSize();
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
	private Quadrant(EntryMap<Matrix> quadrants, int quadrantSize) {
		this.quadrants = quadrants;
		this.size = quadrantSize * 2;
	}
	
	/**
	 * Creates a Quadrant object from a Map of quadrants. 
	 *
	 * @param quadrants the quadrants of the underlying matrix
	 * @return the quadrant
	 */
	public static Quadrant from(Map<Coordinates, Matrix> quadrants) {
		
		EntryMap<Matrix> quadrantsMap = EntryMap.from(quadrants);
		
		int referenceSize = Quadrant.getReferenceSize(quadrants);
		
		quadrantsMap.stream()
		.forEach(entry -> InconsistentSizeException.validate(
					referenceSize, 
					entry.value(), 
					Optional.of(entry.coordinates())));
		
		return new Quadrant(quadrantsMap, referenceSize);
	}
	
	/**
	 * Returns a stream of the quadrants.
	 * Helper for Quadrants::toMatrix
	 * @param <T>
	 *
	 * @return stream of quadrants
	 */
	private Stream<Entry<Matrix>> stream(){
		return this.quadrants.stream();
	}
	
	/**
	 * Returns a new coordinate that is the offset for each quadrant when being built into a matrix
	 * Helper for Quadrants::toMatrix
	 * 
	 * @param quadrant the quadrant we are determining the offset for
	 * 
	 * @return offset the offset of the quadrant
	 */
	private Coordinates getOffset(Entry<Matrix> quadrant) {
		return quadrant.coordinates().times(this.getQuadrantSize());
	}
	
	/**
	 * Converts the quadrants to a matrix.
	 *
	 * @return the matrix
	 */
	public Matrix toMatrix() {
		return Matrix.from(this.stream()
				.flatMap(quadrant -> quadrant.value().stream() // Creates a merged stream of all elements in each quadrant
						.map(entry -> entry.translated(this.getOffset(quadrant)))) // Translates each entry to move by the quadrant's offset
				.collect(Collectors.toMap(Entry::coordinates, Entry::value))); // Collects all entries to a map
	}
	
	/**
	 * Returns the quadrant at the specified coordinate.
	 * 
	 * @return the matrix at the quadrant
	 */
	public Matrix get(Coordinates coordinates) {
		return this.quadrants.get(coordinates);
	}
	
}
