package edu.cwru.jab542.strassen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class QuadrantTest {

	@Test
	void testGetSize() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(Coordinates.ORIGIN, (float) 1);
		map1.put(Coordinates.HORIZONTAL_UNIT, (float) 2);
		map1.put(Coordinates.VERTICAL_UNIT, (float) 3);
		map1.put(Coordinates.DIAGONAL_UNIT, (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		
		Map<Coordinates, Matrix> quadrantsMap = new TreeMap<Coordinates, Matrix>();
		quadrantsMap.put(Coordinates.ORIGIN, matrix1.quadrant(false, false));
		quadrantsMap.put(Coordinates.VERTICAL_UNIT, matrix1.quadrant(false, true));
		quadrantsMap.put(Coordinates.HORIZONTAL_UNIT, matrix1.quadrant(true, false));
		quadrantsMap.put(Coordinates.DIAGONAL_UNIT, matrix1.quadrant(true, true));
		Quadrant quad1 = Quadrant.from(quadrantsMap);
		
		assertEquals(matrix1.getSize(), quad1.getSize());
	}

	@Test
	void testFrom() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(Coordinates.ORIGIN, (float) 1);
		map1.put(Coordinates.VERTICAL_UNIT, (float) 2);
		map1.put(Coordinates.HORIZONTAL_UNIT, (float) 3);
		map1.put(Coordinates.DIAGONAL_UNIT, (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		TreeMap<Coordinates, Matrix> quadMap1 = new TreeMap<>();
		quadMap1.put(Coordinates.ORIGIN, matrix1.quadrant(false, false));
		quadMap1.put(Coordinates.VERTICAL_UNIT, matrix1.quadrant(false, true));
		quadMap1.put(Coordinates.HORIZONTAL_UNIT, matrix1.quadrant(true, false));
		quadMap1.put(Coordinates.DIAGONAL_UNIT, matrix1.quadrant(true, true));
		
		assertNotNull(Quadrant.from(quadMap1));
		
		TreeMap<Coordinates, Float> map2 = new TreeMap<>();
		map2.put(Coordinates.ORIGIN, (float) 1);
		map2.put(Coordinates.VERTICAL_UNIT, (float) 2);
		map2.put(Coordinates.HORIZONTAL_UNIT, (float) 3);
		Matrix matrix2 = Matrix.from(EntryMap.from(map2));
		TreeMap<Coordinates, Matrix> quadMap2 = new TreeMap<>();
		quadMap2.put(Coordinates.ORIGIN, matrix2.quadrant(false, false));
		quadMap2.put(Coordinates.VERTICAL_UNIT, matrix2.quadrant(false, true));
		quadMap2.put(Coordinates.HORIZONTAL_UNIT, matrix2.quadrant(true, false));
		quadMap2.put(Coordinates.DIAGONAL_UNIT, null);
		
		assertThrows(IllegalArgumentException.class, () -> 
			Quadrant.from(quadMap2)
		);
		
		TreeMap<Coordinates, Float> map3 = new TreeMap<>();
		map3.put(Coordinates.ORIGIN, (float) 1);
		map3.put(new Coordinates(3, 0), (float) 2);
		map3.put(new Coordinates(0, 3), (float) 3);
		map3.put(new Coordinates(3, 3), (float) 4);
		Matrix matrix3 = Matrix.from(EntryMap.from(map3));
		
		Map<Coordinates, Matrix> quadrantsMap3 = new TreeMap<Coordinates, Matrix>();
		quadrantsMap3.put(Coordinates.ORIGIN, matrix3.quadrant(false, false));
		quadrantsMap3.put(Coordinates.VERTICAL_UNIT, matrix3.quadrant(false, true));
		quadrantsMap3.put(Coordinates.HORIZONTAL_UNIT, matrix3.quadrant(true, false));
		quadrantsMap3.put(Coordinates.DIAGONAL_UNIT, matrix3.quadrant(true, true));
		
		assertThrows(IllegalArgumentException.class, () -> 
		Quadrant.from(quadrantsMap3)
	);
	}

	@Test
	void testToMatrix() {
		TreeMap<Coordinates, Float> map1 = new TreeMap<>();
		map1.put(Coordinates.ORIGIN, (float) 1);
		map1.put(Coordinates.VERTICAL_UNIT, (float) 2);
		map1.put(Coordinates.HORIZONTAL_UNIT, (float) 3);
		map1.put(Coordinates.DIAGONAL_UNIT, (float) 4);
		Matrix matrix1 = Matrix.from(EntryMap.from(map1));
		
		Map<Coordinates, Matrix> quadrantsMap = new TreeMap<Coordinates, Matrix>();
		quadrantsMap.put(Coordinates.ORIGIN, matrix1.quadrant(false, false));
		quadrantsMap.put(Coordinates.HORIZONTAL_UNIT, matrix1.quadrant(true, false));
		quadrantsMap.put(Coordinates.VERTICAL_UNIT, matrix1.quadrant(false, true));
		quadrantsMap.put(Coordinates.DIAGONAL_UNIT, matrix1.quadrant(true, true));
		Quadrant quad1 = Quadrant.from(quadrantsMap);
		List<Entry<Float>> list1 = quad1.toMatrix().getRepresentation().stream().sorted().toList();
		List<Entry<Float>> list2 = matrix1.getRepresentation().stream().sorted().toList();
		assertTrue(list1.equals(list2));
		
		TreeMap<Coordinates, Float> map2 = new TreeMap<>();
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				map2.put(new Coordinates(i, j), (float) 1);
			}
		}
		Matrix matrix2 = Matrix.from(EntryMap.from(map2));
		
		Map<Coordinates, Matrix> quadrantsMap2 = new TreeMap<Coordinates, Matrix>();
		quadrantsMap2.put(Coordinates.ORIGIN, matrix2.quadrant(false, false));
		quadrantsMap2.put(Coordinates.VERTICAL_UNIT, matrix2.quadrant(false, true));
		quadrantsMap2.put(Coordinates.HORIZONTAL_UNIT, matrix2.quadrant(true, false));
		quadrantsMap2.put(Coordinates.DIAGONAL_UNIT, matrix2.quadrant(true, true));
		Quadrant quad2 = Quadrant.from(quadrantsMap2);
		List<Entry<Float>> list3 = quad2.toMatrix().getRepresentation().stream().sorted().toList();
		List<Entry<Float>> list4 = matrix2.getRepresentation().stream().sorted().toList();
		assertTrue(list3.equals(list4));
	}

}
