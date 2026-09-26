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
public record Entry<T>(Coordinates coordinates, T value) {
	
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
	public boolean isInRows(int lower, int upper) {
		return this.coordinates.isInRows(lower, upper);
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
	public boolean isInColumns(int lower, int upper) {
		return this.coordinates.isInColumns(lower, upper);
	}
}
