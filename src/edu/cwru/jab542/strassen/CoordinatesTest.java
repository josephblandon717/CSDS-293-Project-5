package edu.cwru.jab542.strassen;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CoordinatesTest {

	@Test
	void testNegated() {
		Coordinates coordinates1 = new Coordinates(1, 2);
		Coordinates nullCoords = null;
		assertEquals(-1, Coordinates.negated(coordinates1).row());
		assertEquals(-2, Coordinates.negated(coordinates1).col());
		assertThrows(IllegalArgumentException.class, () -> {
			Coordinates.negated(nullCoords);
		});
	}
	
	@Test
	void testPlus() {
		Coordinates coordinates1 = new Coordinates(1, 2);
		Coordinates coordinates2 = new Coordinates(3, 4);
		Coordinates nullCoords = null;
		assertEquals(4, coordinates1.plus(coordinates2).row());
		assertEquals(6, coordinates1.plus(coordinates2).col());
		assertThrows(IllegalArgumentException.class, () -> {
			coordinates1.plus(nullCoords);
		});
	}
	
	@Test 
	void testMinus() {
		Coordinates coordinates1 = new Coordinates(1, 2);
		Coordinates coordinates2 = new Coordinates(3, 5);
		Coordinates nullCoords = null;
		assertEquals(-2, coordinates1.minus(coordinates2).row());
		assertEquals(-3, coordinates1.minus(coordinates2).col());
		assertThrows(IllegalArgumentException.class, () -> {
			coordinates1.minus(nullCoords);
		});
	}
	
	@Test
	void testTimes() {
		Coordinates coordinates1 = new Coordinates(3, 4);
		assertEquals(9, coordinates1.times(3).row());
		assertEquals(12, coordinates1.times(3).col());
	}
	
	@Test
	void testCompareTo() {
		Coordinates coordinates1 = new Coordinates(1, 2);
		Coordinates coordinates2 = new Coordinates(3, 5);
		Coordinates coordinates3 = new Coordinates(1, 2);
		Coordinates coordinates4 = new Coordinates(1, 4);
		Coordinates nullCoords = null;
		assertEquals(1, coordinates2.compareTo(coordinates1));
		assertEquals(-1, coordinates1.compareTo(coordinates2));
		assertEquals(-1, coordinates1.compareTo(coordinates4));
		assertEquals(1, coordinates4.compareTo(coordinates1));
		assertEquals(0, coordinates1.compareTo(coordinates3));
		assertThrows(IllegalArgumentException.class, () -> {
			coordinates1.compareTo(nullCoords);
		});
	}

	@Test
	public void testIsInSubMatrix() {
		Coordinates coordinates1 = new Coordinates(4, 4);
		assertEquals(false, coordinates1.isInSubMatrix(new Coordinates(1, 1), new Coordinates(2, 2)));
		assertEquals(false, coordinates1.isInSubMatrix(new Coordinates(1, 1), new Coordinates(4, 4)));
		assertEquals(true, coordinates1.isInSubMatrix(new Coordinates(1, 1), new Coordinates(5, 5)));
		assertThrows(IllegalArgumentException.class, () -> {
			coordinates1.isInSubMatrix(null, null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			coordinates1.isInSubMatrix(Coordinates.ORIGIN, null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			coordinates1.isInSubMatrix(null, Coordinates.ORIGIN);
		});
	}

}
