package edu.cwru.jab542.strassen;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EntryTest {

	@Test 
	void testTranslated() {
		Entry<Integer> entry = new Entry<Integer>(new Coordinates(1, 2), 4);
		assertEquals(3, entry.translated(new Coordinates(2, 3)).coordinates().row());
		assertEquals(5, entry.translated(new Coordinates(2, 3)).coordinates().col());
		assertEquals(4, entry.translated(new Coordinates(2, 3)).value());
	}

	@Test
	public void testIsInSubMatrix() {
		Entry<Integer> entry1 = new Entry<Integer>(new Coordinates(4, 4), 1);
		assertEquals(false, entry1.isInSubMatrix(new Coordinates(1, 1), new Coordinates(2, 2)));
		assertEquals(false, entry1.isInSubMatrix(new Coordinates(1, 1), new Coordinates(4, 4)));
		assertEquals(true, entry1.isInSubMatrix(new Coordinates(1, 1), new Coordinates(5, 5)));
		assertThrows(IllegalArgumentException.class, () -> {
			entry1.isInSubMatrix(null, null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			entry1.isInSubMatrix(Coordinates.ORIGIN, null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			entry1.isInSubMatrix(null, Coordinates.ORIGIN);
		});
	}

	@Test
	void testCompareTo() {
		Entry<Integer> entry1 = new Entry<Integer>(new Coordinates(1, 2), 1);
		Entry<Integer> entry2 = new Entry<Integer>(new Coordinates(3, 5), 1);
		Entry<Integer> entry3 = new Entry<Integer>(new Coordinates(1, 2), 1);
		Entry<Integer> entry4 = new Entry<Integer>(new Coordinates(1, 4), 1);
		assertEquals(1, entry2.compareTo(entry1));
		assertEquals(-1, entry1.compareTo(entry2));
		assertEquals(-1, entry1.compareTo(entry4));
		assertEquals(1, entry4.compareTo(entry1));
		assertEquals(0, entry1.compareTo(entry3));
		assertThrows(IllegalArgumentException.class, () -> {
			entry1.compareTo(null);
		});
	}

}
