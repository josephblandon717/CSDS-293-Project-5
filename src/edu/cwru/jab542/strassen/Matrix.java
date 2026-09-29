/* A class that represents a matrix using an EntryMap.
 * 
 */
package edu.cwru.jab542.strassen;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		Objects.requireNonNull(entryMap);
		return new Matrix(EntryMap.from(entryMap));
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
		return this.get(Coordinates.ORIGIN);
	}

	/**
	 * Gets the value located at the coordinates.
	 *
	 * @param coordinates the coordinates
	 * @return the value
	 */
	public float get(Coordinates coordinates) {
		return this.representation.getOrDefault(coordinates, (float) 0);
	}

	/**
	 * Checks if the reference coordinates is within specified subMatrix.
	 * Inclusive to the lower bound, exclusive to the higher bound.
	 * @param <T>
	 *
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @return true, if coordinates are in sub matrix; false otherwise
	 */
	private static boolean isInSubMatrix(Entry<Float> entry, Coordinates lower, Coordinates upper) {
		Objects.requireNonNull(lower);
		Objects.requireNonNull(upper);
		return (entry.coordinates().isInRows(lower.row(), upper.row()) && entry.coordinates().isInColumns(lower.col(), upper.col()));
	}
	
	/**
	 * Negates all values in the matrix.
	 *
	 * @return the negated matrix
	 */
	public Matrix negated() {
		Function<Float, Float> negates = value -> value * -1;
		return new Matrix(representation.remap(Function.identity(), negates));
	}

	/**
	 * Returns a sub matrix
	 *
	 * @param origin the origin of the sub matrix
	 * @param bound  the bound of the sub matrix
	 * @return the sub matrix
	 */
	public Matrix subMatrix(Coordinates origin, Coordinates bound) {
		return Matrix.from(this.stream()
				.filter(entry -> (Matrix.isInSubMatrix(entry, origin, bound)))
				.collect(Collectors.toMap((entry -> entry.coordinates().minus(origin)), (Entry::value))));
	}
	
	/**
	 * Returns a stream from the Matrix
	 * 
	 * @return the stream of the matrix
	 */
	public Stream<Entry<Float>> stream() {
		return this.representation.stream();
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
	
		return Matrix.from(Stream.concat(this.stream(), other.stream()) // Merges the streams made by this and the other matrix
				.collect(Collectors.toMap( // Collects each entry of the stream to a map
						Entry::coordinates, 
						Entry::value, 
						// Merge function, resolves duplicate keys by adding the values of each duplicate entry
						(referenceValue, otherValue) -> referenceValue + otherValue))); 
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
	 * Returns a value depending on if the offset is true or not.
	 * Helper for quadrant.
	 * 
	 * 
	 * @param offset true if the quadrant is offset, false otherwise
	 * @return the amount the quadrant is offset by
	 */
	private Coordinates getOriginOffset(boolean horizontalOffset, boolean verticalOffset) {
		return new Coordinates(Boolean.compare(horizontalOffset, false), Boolean.compare(verticalOffset, false))
				.times(this.getSize() / 2);
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
		
		Coordinates origin = this.getOriginOffset(horizontalOffset, verticalOffset);
		
		// Math.ceilDiv(a, b) returns a / b rounded to the next whole integer, so quadrant size can't be less than 1.
		int quadrantSize = Math.ceilDiv(this.getSize(), 2);

		// The bound will be the origin offset by half of the reference matrix's size
		Coordinates bound = origin
				.plus(new Coordinates(quadrantSize, quadrantSize));
		return this.subMatrix(origin, bound);
	}

	/**
	 * Returns a map of coordinates and the quadrants of the reference matrix Helper
	 * for Matrix::times
	 * 
	 * @return Quadrants object
	 */
	private Quadrant makeQuadrants() {
		return Quadrant.from(Map.of(
						Coordinates.ORIGIN, this.quadrant(false, false),
						Coordinates.VERTICAL_UNIT, this.quadrant(false, true), 
						Coordinates.HORIZONTAL_UNIT, this.quadrant(true, false),
						Coordinates.DIAGONAL_UNIT, this.quadrant(true, true)));
	}

	/**
	 * Multiplies the reference matrix by the other matrix.
	 * 
	 * @param other the other matrix
	 * @return the matrix product of this and the other matrix
	 */
	public Matrix times(Matrix other) {
		
		int size = this.getSize();

		InconsistentSizeException.validate(size, other);

		// If both matrices are size 1, will return a one element matrix of the products
		// of the reference and other matrix.
		if (size == 1) {
			return Matrix.from(this.stream()
					.collect(Collectors.toMap(Entry::coordinates, entry -> entry.value() * other.get())));
		}

		Quadrant referenceQuadrants = this.makeQuadrants();
		Quadrant otherQuadrants = other.makeQuadrants();

		return Matrix.strassenProduct(referenceQuadrants, otherQuadrants).toMatrix();
	}

	/**
	 * Calculates the strassen products and builds the product matrix.
	 * Helper for Matrix::times
	 * 
	 * @param referenceQuadrants the quadrants of the reference matrix
	 * @param otherQuadrants the quadrants of the other matrix
	 * @return a NavigableMap of the product of the reference and other matrix
	 */
	private static Quadrant strassenProduct(Quadrant referenceQuadrants, Quadrant otherQuadrants) {

		Matrix a = referenceQuadrants.get(Coordinates.ORIGIN);
		Matrix b = referenceQuadrants.get(Coordinates.HORIZONTAL_UNIT);
		Matrix c = referenceQuadrants.get(Coordinates.VERTICAL_UNIT);
		Matrix d = referenceQuadrants.get(Coordinates.DIAGONAL_UNIT);
		
		Matrix e = otherQuadrants.get(Coordinates.ORIGIN);
		Matrix f = otherQuadrants.get(Coordinates.HORIZONTAL_UNIT);
		Matrix g = otherQuadrants.get(Coordinates.VERTICAL_UNIT);
		Matrix h = otherQuadrants.get(Coordinates.DIAGONAL_UNIT);
		
		// Strassen Products
		Matrix strassenOne = a.times(f.minus(h));
		Matrix strassenTwo = a.plus(b).times(h);
		Matrix strassenThree = c.plus(d).times(e);
		Matrix strassenFour = d.times(g.minus(e));
		Matrix strassenFive = a.plus(d).times(e.plus(h));
		Matrix strassenSix = b.minus(d).times(g.plus(h));
		Matrix strassenSeven = a.minus(c).times(e.plus(f));

		// Product quadrants build from the sums of strassen products
		return Quadrant.from(Map.of(
				Coordinates.ORIGIN, strassenFive.plus(strassenSix).plus(strassenFour).minus(strassenTwo), 
				Coordinates.HORIZONTAL_UNIT, strassenOne.plus(strassenTwo), 
				Coordinates.VERTICAL_UNIT, strassenThree.plus(strassenFour), 
				Coordinates.DIAGONAL_UNIT,strassenOne.minus(strassenSeven).minus(strassenThree).plus(strassenFive)));
	}
	
	/**
	 * Turns this matrix to a string.
	 * 
	 * @return string of this matrix
	 */
	public String toString() {
		StringBuilder matrixString = new StringBuilder();
		int size = this.getSize();
		
		for(int row = 0; row < size; row++) {
			matrixString.append("[ ");
			for(int col = 0; col < size; col++) {
				matrixString.append(this.get(new Coordinates(row, col)));
				matrixString.append(" ");
			}
			matrixString.append("]");
			matrixString.append(System.lineSeparator());
		}
		
		return matrixString.toString();
	}
	
	/**
	 * 
	 * 
	 */
	public static void main(String[] args) {
		Matrix firstMatrix = readMatrix();
		Matrix secondMatrix = readMatrix();
		
		System.out.println(firstMatrix.times(secondMatrix));
	}
}
