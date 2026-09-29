package edu.cwru.jab542.strassen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class MatrixTest {

	@Test
	void testFromMapOfCoordinatesFloat() {
		Map<Coordinates, Float> map1 = new HashMap<Coordinates, Float>();
		map1.put(new Coordinates(1, 2), (float) 2);
		assertEquals(true, map1.equals(Matrix.from(map1).getRepresentation().entryMap));
		
		Map<Coordinates, Float> map2 = null;
		assertThrows(NullPointerException.class, () -> {
			Matrix.from(map2);
		});
	}

	@Test
	void testFromEntryMapOfFloat() {
		Map<Coordinates, Float> map1 = new HashMap<Coordinates, Float>();
		map1.put(new Coordinates(1, 2), (float) 2);
		assertEquals(true, map1.equals(Matrix.from(EntryMap.from(map1)).getRepresentation().entryMap));
		
		Map<Coordinates, Float> map2 = null;
		assertThrows(NullPointerException.class, () -> {
			Matrix.from(EntryMap.from(map2));
		});
	}

	@Test
	void testGetSize() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(16, 16), (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		TreeMap<Coordinates, Float> map2 = new TreeMap<>();
		map2.put(new Coordinates(15, 15), (float) 4);
		Matrix matrix2 = Matrix.from(EntryMap.from(map2));
		TreeMap<Coordinates, Float> map3 = new TreeMap<>();
		map3.put(new Coordinates(17, 17), (float) 4);
		Matrix matrix3 = Matrix.from(EntryMap.from(map3));
		assertEquals(32, matrix1.getSize());
		assertEquals(16, matrix2.getSize());
		assertEquals(32, matrix3.getSize());
	}

	@Test
	void testGet() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(16, 16), (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		map1.put(Coordinates.ORIGIN, (float) 4);
		Matrix matrix2 = Matrix.from(EntryMap.from(map1));
		assertEquals(0, matrix1.get());
		assertEquals(4, matrix2.get());
	}

	@Test
	void testGetCoordinates() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(16, 16), (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		assertEquals(4, matrix1.get(new Coordinates(16, 16)));
		assertEquals(0, matrix1.get(Coordinates.ORIGIN));
	}

	@Test
	void testNegated() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(16, 16), (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		assertEquals(-4, matrix1.negated().get(new Coordinates(16, 16)));
	}

	@Test
	void testSubMatrix() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(16, 16), (float) 16);
		map1.put(new Coordinates(4, 4), (float) 4);
		map1.put(new Coordinates(8, 8), (float) 8);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		Matrix subMatrix = matrix1.subMatrix(new Coordinates(4, 4), new Coordinates(17, 17));
		
		assertEquals(4, subMatrix.get());
		assertEquals(8, subMatrix.get(new Coordinates(8, 8).minus(new Coordinates(4, 4))));
		assertEquals(16, subMatrix.get(new Coordinates(16, 16).minus(new Coordinates(4, 4))));
	}

	@Test
	void testPlus() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(16, 16), (float) 16);
		map1.put(new Coordinates(4, 4), (float) 4);
		map1.put(new Coordinates(8, 8), (float) 8);
		Matrix matrix1 = Matrix.from(map1);
		
		TreeMap<Coordinates, Float> map2 = new TreeMap<>();
		map2.put(new Coordinates(16, 16), (float) 16);
		map2.put(new Coordinates(4, 4), (float) 4);
		map2.put(new Coordinates(5, 5), (float) 5);
		map2.put(new Coordinates(8, 8), (float) 8);
		Matrix matrix2 = Matrix.from(map2);
		Matrix sumMatrix = matrix1.plus(matrix2);
		
		TreeMap<Coordinates, Float> map3 = new TreeMap<>();
		map3.put(new Coordinates(4, 4), (float) 6);
		map3.put(new Coordinates(8, 8), (float) 10);
		Matrix matrix3 = Matrix.from(map3);
		
		assertEquals(8, sumMatrix.get(new Coordinates(4, 4)));
		assertEquals(5, sumMatrix.get(new Coordinates(5, 5)));
		assertEquals(16, sumMatrix.get(new Coordinates(8, 8)));
		assertEquals(32, sumMatrix.get(new Coordinates(16, 16)));
		
		assertThrows(NullPointerException.class, () -> {
			matrix1.plus(null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			matrix1.plus(matrix3);
		});
	}

	@Test
	void testMinus() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(16, 16), (float) 18);
		map1.put(new Coordinates(4, 4), (float) 6);
		map1.put(new Coordinates(8, 8), (float) 10);
		Matrix matrix1 = Matrix.from(map1);
		
		TreeMap<Coordinates, Float> map2 = new TreeMap<>();
		map2.put(new Coordinates(16, 16), (float) 16);
		map2.put(new Coordinates(4, 4), (float) 4);
		map2.put(new Coordinates(5, 5), (float) 5);
		map2.put(new Coordinates(8, 8), (float) 8);
		Matrix matrix2 = Matrix.from(map2);
		
		TreeMap<Coordinates, Float> map3 = new TreeMap<>();
		map3.put(new Coordinates(4, 4), (float) 6);
		map3.put(new Coordinates(8, 8), (float) 10);
		Matrix matrix3 = Matrix.from(map3);
		
		Matrix differenceMatrix = matrix1.minus(matrix2);
		assertEquals(2, differenceMatrix.get(new Coordinates(4, 4)));
		assertEquals(-5, differenceMatrix.get(new Coordinates(5, 5)));
		assertEquals(2, differenceMatrix.get(new Coordinates(8, 8)));
		assertEquals(2, differenceMatrix.get(new Coordinates(16, 16)));
		
		assertThrows(NullPointerException.class, () -> {
			matrix1.minus(null);
		});
		
		assertThrows(IllegalArgumentException.class, () -> {
			matrix1.minus(matrix3);
		});
		
	}

	@Test
	public void testQuadrant() {
		// size 2 matrix
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(0, 0), (float) 1);
		map1.put(new Coordinates(1, 0), (float) 2);
		map1.put(new Coordinates(0, 1), (float) 3);
		map1.put(new Coordinates(1, 1), (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		Matrix quadrant1 = matrix1.quadrant(false, false);
		assertEquals(1, quadrant1.get());
		Matrix quadrant2 = matrix1.quadrant(true, false);
		assertEquals(2, quadrant2.get());
		Matrix quadrant3 = matrix1.quadrant(false, true);
		assertEquals(3, quadrant3.get());
		Matrix quadrant4 = matrix1.quadrant(true, true);
		assertEquals(4, quadrant4.get());
		
		assertEquals(1, quadrant1.getSize());
		assertTrue((quadrant1.getSize() == quadrant2.getSize()) && 
				(quadrant2.getSize() == quadrant3.getSize()) && 
				(quadrant3.getSize() == quadrant4.getSize()));
		
		// size 4 matrix
		TreeMap<Coordinates, Float> map2 = new TreeMap<>();
		map2.put(new Coordinates(0, 0), (float) 1);
		map2.put(new Coordinates(1, 1), (float) 10);
		
		map2.put(new Coordinates(3, 0), (float) 2);
		map2.put(new Coordinates(2, 1), (float) 20);
		
		map2.put(new Coordinates(0, 3), (float) 3);
		map2.put(new Coordinates(1, 2), (float) 30);
		
		map2.put(new Coordinates(2, 2), (float) 40);
		map2.put(new Coordinates(3, 3), (float) 4);
		
		
		Matrix matrix2 = Matrix.from(EntryMap.from(map2));
		Matrix quadrant5 = matrix2.quadrant(false, false);

		assertEquals(1, quadrant5.get(new Coordinates(0, 0)));
		assertEquals(10, quadrant5.get(new Coordinates(1, 1)));
		Matrix quadrant6 = matrix2.quadrant(true, false);
		assertEquals(2, quadrant6.get(new Coordinates(1, 0)));
		assertEquals(20, quadrant6.get(new Coordinates(0, 1)));
		Matrix quadrant7 = matrix2.quadrant(false, true);
		assertEquals(3, quadrant7.get(new Coordinates(0, 1)));
		assertEquals(30, quadrant7.get(new Coordinates(1, 0)));
		Matrix quadrant8 = matrix2.quadrant(true, true);
		assertEquals(40, quadrant8.get(new Coordinates(0, 0)));
		assertEquals(4, quadrant8.get(new Coordinates(1, 1)));
		
		assertEquals(2, quadrant5.getSize());
		assertTrue((quadrant5.getSize() == quadrant6.getSize()) && 
				(quadrant6.getSize() == quadrant7.getSize()) && 
				(quadrant7.getSize() == quadrant8.getSize()));
		
		// size 16 matrix
		TreeMap<Coordinates, Float> map3 = new TreeMap<>();
		map3.put(new Coordinates(0, 0), (float) 1);
		map3.put(new Coordinates(7, 7), (float) 10);
			
		map3.put(new Coordinates(8, 0), (float) 2);
		map3.put(new Coordinates(15, 7), (float) 20);
			
		map3.put(new Coordinates(0, 8), (float) 3);
		map3.put(new Coordinates(7, 15), (float) 30);
				
		map3.put(new Coordinates(8, 8), (float) 4);
		map3.put(new Coordinates(15, 15), (float) 40);
			
		Matrix matrix3 = Matrix.from(EntryMap.from(map3));

		Matrix quadrant9 = matrix3.quadrant(false, false);
		assertEquals(1, quadrant9.get(new Coordinates(0, 0)));
		assertEquals(10, quadrant9.get(new Coordinates(7, 7)));
		Matrix quadrant10 = matrix3.quadrant(true, false);
		assertEquals(2, quadrant10.get(new Coordinates(0, 0)));
		assertEquals(20, quadrant10.get(new Coordinates(7, 7)));
		Matrix quadrant11 = matrix3.quadrant(false, true);
		assertEquals(3, quadrant11.get(new Coordinates(0, 0)));
		assertEquals(30, quadrant11.get(new Coordinates(7, 7)));
		Matrix quadrant12 = matrix3.quadrant(true, true);
		assertEquals(4, quadrant12.get(new Coordinates(0, 0)));
		assertEquals(40, quadrant12.get(new Coordinates(7, 7)));
		
		assertEquals(8, quadrant9.getSize());
		assertTrue((quadrant9.getSize() == quadrant10.getSize()) && 
				(quadrant10.getSize() == quadrant11.getSize()) && 
				(quadrant11.getSize() == quadrant12.getSize()));
				
	}

	@Test
	public void testTimes() {
		// Non-equal sized matrices
		Map<Coordinates, Float> map7 = new TreeMap<Coordinates, Float>();
		Map<Coordinates, Float> map8 = new TreeMap<Coordinates, Float>();
		
		map7.put(new Coordinates(0, 0), (float) 5);
		map8.put(new Coordinates(4, 4), (float) 6);
		 assertThrows(IllegalArgumentException.class, () -> {
			 Matrix.from(map7).times(Matrix.from(map8));
	        });
		
		// Size 1 Matrices
		Map<Coordinates, Float> map5 = new TreeMap<Coordinates, Float>();
		Map<Coordinates, Float> map6 = new TreeMap<Coordinates, Float>();
		
		map5.put(new Coordinates(0, 0), (float) 5);
		map6.put(new Coordinates(0, 0), (float) 6);
		
		Map<Coordinates, Float> answerMap3 = new TreeMap<Coordinates, Float>();
		
		answerMap3.put(new Coordinates(0, 0), (float) 30);
		
		List<Entry<Float>> answerList3 = Matrix.from(answerMap3).getRepresentation().stream().toList();
		List<Entry<Float>> productList3 = Matrix.from(map5).times(Matrix.from(map6)).getRepresentation().stream().toList();
		assertTrue(answerList3.equals(productList3));
		
		// Size 2 Matrices
		Map<Coordinates, Float> map1 = new TreeMap<Coordinates, Float>();
		Map<Coordinates, Float> map2 = new TreeMap<Coordinates, Float>();
		
		map1.put(new Coordinates(0, 0), (float) 1);
		map1.put(new Coordinates(1, 0), (float) 2);
		map1.put(new Coordinates(0, 1), (float) 3);
		map1.put(new Coordinates(1, 1), (float) 4);
		
		map2.put(new Coordinates(0, 0), (float) 5);
		map2.put(new Coordinates(1, 0), (float) 6);
		map2.put(new Coordinates(0, 1), (float) 7);
		map2.put(new Coordinates(1, 1), (float) 8);
		
		Matrix matrix1 = Matrix.from(map1);
		Matrix matrix2 = Matrix.from(map2);
		
		Map<Coordinates, Matrix> referenceQuadrants = new TreeMap<Coordinates, Matrix>();
		referenceQuadrants.put(new Coordinates(0, 0), matrix1.quadrant(false, false));
		referenceQuadrants.put(new Coordinates(0, 1), matrix1.quadrant(false, true));
		referenceQuadrants.put(new Coordinates(1, 0), matrix1.quadrant(true, false));
		referenceQuadrants.put(new Coordinates(1, 1), matrix1.quadrant(true, true));
		
		Map<Coordinates, Float> answerMap1 = new TreeMap<Coordinates, Float>();
		
		answerMap1.put(new Coordinates(0, 0), (float) 19);
		answerMap1.put(new Coordinates(1, 0), (float) 22);
		answerMap1.put(new Coordinates(0, 1), (float) 43);
		answerMap1.put(new Coordinates(1, 1), (float) 50);
	
		List<Entry<Float>> answerList1 = Matrix.from(answerMap1).getRepresentation().stream().toList();
		List<Entry<Float>> productList1 = matrix1.times(matrix2).getRepresentation().stream().toList();
		assertTrue(answerList1.equals(productList1));
		
		// Size 4 matrices
		Map<Coordinates, Float> map3 = new TreeMap<Coordinates, Float>();
		Map<Coordinates, Float> map4 = new TreeMap<Coordinates, Float>();
		
		for(int i = 0; i < 4; i++) {
			int k = 1;
			for(int j = 0; j < 4; j++) {
				map3.put(new Coordinates(i, j), (float) k);
				map4.put(new Coordinates(i, j), (float) k);
				k++;
			}
		}
		
		TreeMap<Coordinates, Float> answerMap2 = new TreeMap<>();

		answerMap2.put(new Coordinates(0, 0), (float) 10);
		answerMap2.put(new Coordinates(0, 1), (float) 20);
		answerMap2.put(new Coordinates(0, 2), (float) 30);
		answerMap2.put(new Coordinates(0, 3), (float) 40);

		answerMap2.put(new Coordinates(1, 0), (float) 10);
		answerMap2.put(new Coordinates(1, 1), (float) 20);
		answerMap2.put(new Coordinates(1, 2), (float) 30);
		answerMap2.put(new Coordinates(1, 3), (float) 40);

		answerMap2.put(new Coordinates(2, 0), (float) 10);
		answerMap2.put(new Coordinates(2, 1), (float) 20);
		answerMap2.put(new Coordinates(2, 2), (float) 30);
		answerMap2.put(new Coordinates(2, 3), (float) 40);

		answerMap2.put(new Coordinates(3, 0), (float) 10);
		answerMap2.put(new Coordinates(3, 1), (float) 20);
		answerMap2.put(new Coordinates(3, 2), (float) 30);
		answerMap2.put(new Coordinates(3, 3), (float) 40);

		List<Entry<Float>> answerList2 = Matrix.from(answerMap2).getRepresentation().stream().toList();
		List<Entry<Float>> productList2 = Matrix.from(map3).times(Matrix.from(map4)).getRepresentation().stream().toList();
		assertTrue(answerList2.equals(productList2));
	}

	@Test
	public void testToString() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(new Coordinates(1, 1), (float) 1);
		map1.put(new Coordinates(2, 2), (float) 2);
		map1.put(new Coordinates(7, 3), (float) 3);
		Matrix matrix1 = Matrix.from(map1);
		System.out.println(matrix1.toString());
	}

}
