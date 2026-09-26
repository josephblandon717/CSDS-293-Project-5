/**
 *  Provides a representation of coordinates to be used in Entry, EntryMap, and Matrix.
 *  
 *  @author Joseph Blandon
 * 
 */

package edu.cwru.jab542.strassen;

import java.util.Comparator;
import java.util.Objects;

/**
 * The Record Coordinates.
 *
 * @param row the row
 * @param col the column
 */
public record Coordinates(int row, int col) implements Comparable<Coordinates>{
	
	/** The origin. */
	public static Coordinates ORIGIN = new Coordinates(0, 0);
	
	/** The horizontal unit. */
	public static Coordinates HORIZONTAL_UNIT = new Coordinates(1, 0);
	
	/** The vertical unit. */
	public static Coordinates VERTICAL_UNIT = new Coordinates(0, 1);
	
	/** The diagonal unit. */
	public static Coordinates DIAGONAL_UNIT = new Coordinates(1, 1);
	
	/** The negative horizontal unit. */
	public static Coordinates NEGATIVE_HORIZONTAL_UNIT = new Coordinates(-1, 0);
	
	/** The negative vertical unit. */
	public static Coordinates NEGATIVE_VERTICAL_UNIT = new Coordinates(0, -1);
	
	/** The negative diagonal unit. */
	public static Coordinates NEGATIVE_DIAGONAL_UNIT = new Coordinates(-1, -1);

	/** The comparator for coordinates. */
	public static Comparator<Coordinates> COMPARATOR = Comparator
			.<Coordinates>comparingInt(coordinates -> coordinates.row)
			.thenComparingInt(coordinates -> coordinates.col);
	
	/**
	 * Negates the coordinates.
	 *
	 * @param coordinates the coordinates of the new Coordinates object
	 * @return Coordinates object
	 */
	public static Coordinates negated(Coordinates coordinates) {
		Objects.requireNonNull(coordinates);
		return new Coordinates(coordinates.row * -1, coordinates.col * -1);
	}
	
	/**
	 * Adds the reference and offset coordinates.
	 *
	 * @param offset the offset
	 * @return sum of the reference and offset coordinates
	 */
	public Coordinates plus(Coordinates offset) {
		Objects.requireNonNull(offset);
		return new Coordinates(this.row + offset.row, this.col + offset.col);
	}
	
	/**
	 * Subtracts the offset coordinates from the reference coordinates.
	 *
	 * @param offset the offset
	 * @return the difference of the reference and offset coordinates
	 */
	public Coordinates minus(Coordinates offset) {
		Objects.requireNonNull(offset);
		return this.plus(Coordinates.negated(offset));
	}
	
	/**
	 * Scales the coordinates by the given integer.
	 *
	 * @param scale the scale
	 * @return the product of the reference coordinates multiplied by the scale
	 */
	public Coordinates times(int scale) {
		return new Coordinates(this.row * scale, this.col * scale);
	}
	
	
	/**
	 * Compare the reference coordinates to the other coordinates.
	 *
	 * @param other the other
	 * @return 1 if the reference is greater than the other, -1 if the reference is less than the other, 
	 * 0 if the reference is equal to the other. 
	 * 
	 */
	@Override
	public int compareTo(Coordinates other) {
		Objects.requireNonNull(other);
		return Coordinates.COMPARATOR.compare(this, other);
	}
	
	/**
	 * Checks if the given value is within the range given by the lower and upper value. 
	 */
	private static boolean isInRange(int value, int lower, int upper) {
		return ((lower <= value) && (value < upper));
	}
	
	/**
	 * Checks if the reference coordinates is within specified rows.
	 * Inclusive to the lower bound, exclusive to the higher bound.
	 * Helper function for isInSubMatrix.
	 *
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @return true, if they are within the specified rows; false otherwise
	 */
	public boolean isInRows(int lower, int upper) {
		return Coordinates.isInRange(this.row, lower, upper);
	}
	
	/**
	 * Checks if the reference coordinates is within specified columns.
	 * Inclusive to the lower bound, exclusive to the higher bound.
	 * Helper function for isInSubMatrix.
	 *
	 * @param lower the lower bound
	 * @param upper the upper bound 
	 * @return true, if coordinates are within the specified columns; false otherwise
	 */
	public boolean isInColumns(int lower, int upper) {
		return Coordinates.isInRange(this.col, lower, upper);
	}

}
