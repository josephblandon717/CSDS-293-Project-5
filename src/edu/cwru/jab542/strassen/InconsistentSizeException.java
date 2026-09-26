/*
 * An exception thrown at matrix operations on matrices of unequal sizes.
 *  
 *  @author Joseph Blandon
 */
package edu.cwru.jab542.strassen;

import java.util.Optional;

/**
 * The Class InconsistentSizeException.
 */
public class InconsistentSizeException extends Exception{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The size of the reference matrix. */
	private final int referenceSize;
	
	/** The size of the other matrix. */
	private final int otherSize;
	
	/** The quadrant coordinates if it exists. */
	private final Optional<Coordinates> quadrant;
	
	/**
	 * Gets the reference matrix size.
	 *
	 * @return the reference matrix size
	 */
	public int getReferenceSize() {
		return referenceSize;
	}
	
	/**
	 * Gets the other size.
	 *
	 * @return the other matrix size
	 */
	public int getOtherSize() {
		return otherSize;
	}
	
	/**
	 * Gets the coordinates of the quadrant if it is present.
	 * 
	 * @return an Optional containing the quadrant coordinates or an empty Optional if no quadrant is there
	 */
	public Optional<Coordinates> getQuadrant(){
		return this.quadrant;
	}
	
	/**
	 * Constructor for InconsistentSizeException
	 * 
	 * @param referenceSize
	 * @param otherSize
	 * @param quadrant
	 */
	public InconsistentSizeException(int referenceSize, int otherSize, Optional<Coordinates> quadrant) {
		this.referenceSize = referenceSize;
		this.otherSize = otherSize;
		this.quadrant = quadrant;
	}
	
	/**
	 * Validates whether the current operation is valid or not. 
	 *
	 * @param referenceSize the size of the reference matrix
	 * @param otherMatrix the other matrix
	 * @throws IllegalArgumentException when matrices are of unequal sizes 
	 */
	public static void validate(int referenceSize, Matrix otherMatrix) throws IllegalArgumentException{
		if (otherMatrix == null)
			throw new IllegalArgumentException("Given matrix is null.");
		
		if (otherMatrix.getSize() != referenceSize) {
			 InconsistentSizeException exception = new InconsistentSizeException(
					 referenceSize, 
					 otherMatrix.getSize(), 
					 null);
			 throw new IllegalArgumentException(exception);
		}
	}
	
	/**
	 * Validates whether the current operation is valid or not.
	 * 
	 * @param referenceSize the size of the reference matrix
	 * @param otherMatrix the other matrix
	 * @param quadrant a possible null quadrant
	 * @throws IllegalArgumentException when matrices are of unequal sizes or when the quadrant is null
	 */
	public static void validate(
			int referenceSize, 
			Matrix otherMatrix, 
			Optional<Coordinates> quadrant) throws IllegalArgumentException{
		
		InconsistentSizeException.validate(referenceSize, otherMatrix);
		
		if(!quadrant.isPresent()) 
			throw new IllegalArgumentException("Given quadrant is null.");
		
		
	}
	
	
}
