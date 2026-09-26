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

}
