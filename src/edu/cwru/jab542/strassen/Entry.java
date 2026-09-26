/**
 *  Provides a representation of an entry in an EntryMap.
 *  
 *  @author Joseph Blandon
 * 
 */
package edu.cwru.jab542.strassen;

import java.util.Comparator;
import java.util.Objects;

/**
 * The Record Entry.
 *
 * @param <T> the generic type
 * @param coordinates the coordinates
 * @param value the value
 */
public record Entry<T>(Coordinates coordinates, T value) implements Comparable<Entry<T>>{
	
	/** The comparator for entries. */
	public static Comparator<Entry<?>> COMPARATOR = Comparator
			.<Entry<?>>comparingInt(entry -> entry.coordinates.row())
			.thenComparingInt(entry -> entry.coordinates.col());
	
	/**
	 * Adds the offset coordinates to the reference coordinates.
	 *
	 * @param offset the offset 
	 * @return entry with the sum of the offset and reference coordinates
	 */
	public Entry<T> translated(Coordinates offset){
		return new Entry<T>(coordinates.plus(offset), value);
	}
	
	/**
	 * Checks if the reference entry is in within the specified rows.
	 * Inclusive to the lower bound, exclusive to the higher bound.
	 * Helper function for isInSubMatrix.
	 *
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @return true, if entry is within the specified rows; false otherwise
	 */
	private boolean isInRows(int lower, int upper) {
		return ((lower <= this.coordinates.row()) && (this.coordinates.row() < upper));
	}
	
	/**
	 * Checks if the reference entry is in within the specified rows.
	 * Inclusive to the lower bound, exclusive to the higher bound.
	 * Helper function for isInSubMatrix.
	 *
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @return true, if entry is within the specified columns; false otherwise
	 */
	private boolean isInColumns(int lower, int upper) {
		return ((lower <= this.coordinates.col()) && (this.coordinates.col() < upper));
	}
	
	/**
	 * Checks if the reference entry is within specified subMatrix.
	 * Inclusive to the lower bound, exclusive to the higher bound.
	 *
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @return true, if entry is in sub matrix; false otherwise
	 */
	public boolean isInSubMatrix(Coordinates lower, Coordinates upper) {
		Objects.requireNonNull(lower);
		Objects.requireNonNull(upper);
		return (this.isInRows(lower.row(), upper.row()) && this.isInColumns(lower.col(), upper.col()));
	}
	
	/**
	 * Compare the reference entry's coordinates to the other entry's coordinates.
	 *
	 * @param other the other entry
	 * @return 1 if the reference is greater than the other, -1 if the reference is less than the other, 
	 * 0 if the reference is equal to the other. 
	 */
	@Override
	public int compareTo(Entry<T> other) {
		Objects.requireNonNull(other);
		return Entry.COMPARATOR.compare(this, other);
	}
}
