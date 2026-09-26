/* A class that represents a matrix using an EntryMap.
 * 
 */
package edu.cwru.jab542.strassen;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Class Matrix.
 */
public final class Matrix {

	/** The representation of the matrix. */
	private final EntryMap<Float> representation;

	/**
	 * Private constructor, instantiates a new matrix.
	 *
	 * @param entryMap the entry map
	 */
	private Matrix(EntryMap<Float> entryMap) {
		this.representation = entryMap;
	}

	/**
	 * Returns the private representation
	 * 
	 * @return the EntryMap representation of the matrix
	 */
	public EntryMap<Float> getRepresentation() {
		return this.representation;
	}

	/**
	 * Creates a matrix from a map.
	 *
	 * @param entryMap the entry map
	 * @return the matrix
	 */
	public static Matrix from(Map<Coordinates, Float> entryMap) {
		if (!entryMap.isEmpty())
			return new Matrix(EntryMap.from(entryMap));
		else
			throw new IllegalArgumentException("Entry map given is empty.");
	}

	/**
	 * Creates a matrix from an EntryMap.
	 *
	 * @param entryMap the entry map
	 * @return the matrix
	 */
	public static Matrix from(EntryMap<Float> entryMap) {
		// EntryMap requires a non-null map
		return new Matrix(entryMap);
	}

	/**
	 * Returns the size of a matrix rounded to the nearest power of two.
	 *
	 * @return the size
	 */
	public int getSize() {
		return this.representation.sizeRounded();
	}

	/**
	 * Gets the value located at the origin.
	 *
	 * @return the value
	 */
	public float get() {
		if (representation.get(Coordinates.ORIGIN) == null)
			return 0;
		else
			return representation.get(Coordinates.ORIGIN);
	}

	/**
	 * Gets the value located at the coordinates.
	 *
	 * @param coordinates the coordinates
	 * @return the value
	 */
	public float get(Coordinates coordinates) {
		if (representation.get(coordinates) == null)
			return 0;
		else
			return representation.get(coordinates);
	}

	/**
	 * Checks if the reference coordinates is within specified subMatrix.
	 * Inclusive to the lower bound, exclusive to the higher bound.
	 *
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @return true, if coordinates are in sub matrix; false otherwise
	 */
	private static boolean isInSubMatrix(Coordinates lower, Coordinates upper) {
		Objects.requireNonNull(lower);
		Objects.requireNonNull(upper);
		return (lower.isInRows(lower.row(), upper.row()) && upper.isInColumns(lower.col(), upper.col()));
	}
	
	/**
	 * Negates all values in the matrix.
	 *
	 * @return the negated matrix
	 */
	public Matrix negated() {
		Function<Coordinates, Coordinates> doesNothing = coordinates -> coordinates;
		Function<Float, Float> negates = value -> value * -1;
		return new Matrix(representation.remap(doesNothing, negates));
	}

	/**
	 * Returns a sub matrix
	 *
	 * @param origin the origin of the sub matrix
	 * @param bound  the bound of the sub matrix
	 * @return the sub matrix
	 */
	public Matrix subMatrix(Coordinates origin, Coordinates bound) {
		return Matrix.from(this.representation.stream()
				.filter(entry -> (Matrix.isInSubMatrix(origin, bound)))
				.collect(Collectors.toMap((entry -> entry.coordinates().minus(origin)), (entry -> entry.value()))));
	}

	/**
	 * Returns a matrix that is the sum of the reference matrix and other matrix.
	 * 
	 * @param <T>
	 *
	 * @param other the other matrix
	 * @return the sum matrix
	 */
	public Matrix plus(Matrix other) {
		InconsistentSizeException.validate(this.getSize(), other);
		List<Entry<Float>> referenceEntries = this.representation.stream().toList();
		NavigableMap<Coordinates, Float> sumMap = new TreeMap<Coordinates, Float>();
		for (Entry<Float> entry : referenceEntries) {
			sumMap.put(entry.coordinates(), (entry.value() + other.get(entry.coordinates())));
		}
		List<Entry<Float>> otherEntries = other.representation.stream().toList();
		for (Entry<Float> entry : otherEntries) {
			sumMap.put(entry.coordinates(), (entry.value() + this.get(entry.coordinates())));
		}
		return new Matrix(EntryMap.from(sumMap));
	}

	/**
	 * Returns a matrix that is the difference of the reference matrix and other
	 * matrix.
	 * 
	 * @param <T>
	 *
	 * @param other the other matrix
	 * @return the difference matrix
	 */
	public Matrix minus(Matrix other) {
		return this.plus(other.negated());
	}

	/**
	 * Returns a submatrix that is a quadrant of the reference matrix.
	 * 
	 * @param horizontalOffset true if you are looking for a quadrant in the right
	 *                         half of the matrix, false otherwise
	 * @param verticalOffset   true if you are looking for a quadrant in the top
	 *                         half of the matrix, false otherwise
	 * @return a submatrix that is a quadrant of the reference matrix
	 */
	public Matrix quadrant(boolean horizontalOffset, boolean verticalOffset) {
		int horizontalCoordinate = 0;
		int verticalCoordinate = 0;

		if (horizontalOffset) {
			horizontalCoordinate = (this.getSize() / 2);
		}

		if (verticalOffset) {
			verticalCoordinate = (this.getSize() / 2);
		}

		Coordinates origin = new Coordinates(horizontalCoordinate, verticalCoordinate);

		// The bound will be the origin offset by a quarter of the reference matrix's
		// size
		// Math.ceilDiv(a, b) returns a / b rounded to the next whole integer.
		Coordinates bound = origin
				.plus(new Coordinates(Math.ceilDiv(this.getSize(), 2), Math.ceilDiv(this.getSize(), 2)));
		return this.subMatrix(origin, bound);
	}

	/**
	 * Returns a map of coordinates and the quadrants of the reference matrix Helper
	 * for Matrix::times
	 * 
	 * @return Quadrants object
	 */
	private Quadrant makeQuadrants() {
		Map<Coordinates, Matrix> quadrantsMap = new TreeMap<Coordinates, Matrix>();
		quadrantsMap.put(Coordinates.ORIGIN, this.quadrant(false, false));
		quadrantsMap.put(Coordinates.VERTICAL_UNIT, this.quadrant(false, true));
		quadrantsMap.put(Coordinates.HORIZONTAL_UNIT, this.quadrant(true, false));
		quadrantsMap.put(Coordinates.DIAGONAL_UNIT, this.quadrant(true, true));
		return Quadrant.from(quadrantsMap);
	}

	/**
	 * Multiplies the reference matrix by the other matrix.
	 * 
	 * @param other the other matrix
	 * @return the matrix product of this and the other matrix
	 */
	public Matrix times(Matrix other) {

		InconsistentSizeException.validate(this.getSize(), other);

		// If both matrices are size 1, will return a one element matrix of the products
		// of the reference and other matrix.
		if (this.getSize() == 1 && other.getSize() == 1) {
			NavigableMap<Coordinates, Float> sizeOneMatrix = new TreeMap<Coordinates, Float>();

			sizeOneMatrix.put(Coordinates.ORIGIN, this.get() * other.get());

			return Matrix.from(sizeOneMatrix);
		}

		EntryMap<Matrix> referenceQuadrants = this.makeQuadrants().getQuadrants();
		EntryMap<Matrix> otherQuadrants = other.makeQuadrants().getQuadrants();
		
		NavigableMap<Coordinates, Matrix> productsMap = Matrix.strassenProduct(referenceQuadrants, otherQuadrants);

		return Quadrant.from(productsMap).toMatrix();
	}

	/**
	 * Calculates the strassen products and builds the product matrix.
	 * Helper for Matrix::times
	 * 
	 * @param referenceQuadrants the quadrants of the reference matrix
	 * @param otherQuadrants the quadrants of the other matrix
	 * @return a NavigableMap of the product of the reference and other matrix
	 */
	private static NavigableMap<Coordinates, Matrix> strassenProduct(EntryMap<Matrix> referenceQuadrants, EntryMap<Matrix> otherQuadrants) {

		// Strassen Products
		Matrix strassenOne = referenceQuadrants.get(Coordinates.ORIGIN).times(
				otherQuadrants.get(Coordinates.HORIZONTAL_UNIT).minus(otherQuadrants.get(Coordinates.DIAGONAL_UNIT)));
		Matrix strassenTwo = referenceQuadrants.get(Coordinates.ORIGIN)
				.plus(referenceQuadrants.get(Coordinates.HORIZONTAL_UNIT))
				.times(otherQuadrants.get(Coordinates.DIAGONAL_UNIT));
		Matrix strassenThree = referenceQuadrants.get(Coordinates.VERTICAL_UNIT)
				.plus(referenceQuadrants.get(Coordinates.DIAGONAL_UNIT)).times(otherQuadrants.get(Coordinates.ORIGIN));
		Matrix strassenFour = referenceQuadrants.get(Coordinates.DIAGONAL_UNIT)
				.times(otherQuadrants.get(Coordinates.VERTICAL_UNIT).minus(otherQuadrants.get(Coordinates.ORIGIN)));
		Matrix strassenFive = referenceQuadrants.get(Coordinates.ORIGIN)
				.plus(referenceQuadrants.get(Coordinates.DIAGONAL_UNIT))
				.times(otherQuadrants.get(Coordinates.ORIGIN).plus(otherQuadrants.get(Coordinates.DIAGONAL_UNIT)));
		Matrix strassenSix = referenceQuadrants.get(Coordinates.HORIZONTAL_UNIT)
				.minus(referenceQuadrants.get(Coordinates.DIAGONAL_UNIT)).times(otherQuadrants
						.get(Coordinates.VERTICAL_UNIT).plus(otherQuadrants.get(Coordinates.DIAGONAL_UNIT)));
		Matrix strassenSeven = referenceQuadrants.get(Coordinates.ORIGIN)
				.minus(referenceQuadrants.get(Coordinates.VERTICAL_UNIT))
				.times(otherQuadrants.get(Coordinates.ORIGIN).plus(otherQuadrants.get(Coordinates.HORIZONTAL_UNIT)));

		// Product quadrants build from the sums of strassen products
		NavigableMap<Coordinates, Matrix> productsMap = new TreeMap<Coordinates, Matrix>();
		productsMap.put(Coordinates.ORIGIN, strassenFive.plus(strassenSix).plus(strassenFour).minus(strassenTwo));
		productsMap.put(Coordinates.HORIZONTAL_UNIT, strassenOne.plus(strassenTwo));
		productsMap.put(Coordinates.VERTICAL_UNIT, strassenThree.plus(strassenFour));
		productsMap.put(Coordinates.DIAGONAL_UNIT,strassenOne.minus(strassenSeven).minus(strassenThree).plus(strassenFive));
		
		return productsMap;
	}
}
