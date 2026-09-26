package edu.cwru.jab542.strassen;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class EntryMapTest {

	@Test
	void testFrom() {
		Map<Coordinates, Double> map1 = new HashMap<Coordinates, Double>();
		map1.put(new Coordinates(1, 2), 2.0);
		assertEquals(true, new TreeMap<Coordinates, Double>(map1).equals(EntryMap.from(map1).entryMap));
		
		Map<Coordinates, Double> map2 = new HashMap<Coordinates, Double>();
		assertThrows(IllegalArgumentException.class, () -> {
			EntryMap.from(map2);
		});
	}

	@Test
	void testRows() {
		Map<Coordinates, Double> map1 = new HashMap<Coordinates, Double>();
		map1.put(new Coordinates(1, 2), 2.0);
		EntryMap<Double> entryMap1 = EntryMap.from(map1);
		assertEquals(2, entryMap1.rows());
	}

	@Test
	void testColumns() {
		Map<Coordinates, Double> map1 = new HashMap<Coordinates, Double>();
		map1.put(new Coordinates(1, 2), 2.0);
		EntryMap<Double> entryMap1 = EntryMap.from(map1);
		assertEquals(3, entryMap1.columns());
	}

	@Test
	void testGetSize() {
		Map<Coordinates, Double> map1 = new HashMap<Coordinates, Double>();
		map1.put(new Coordinates(1, 2), 2.0);
		EntryMap<Double> entryMap1 = EntryMap.from(map1);
		assertEquals(3, entryMap1.getSize());
	}
	
	@Test
	void testGet() {
		Map<Coordinates, Double> map = new HashMap<Coordinates, Double>();
		map.put(new Coordinates(1, 2), 2.0);
		map.put(new Coordinates(2, 2), null);
		EntryMap<Double> entryMap = EntryMap.from(map);
		assertEquals(2.0, entryMap.get(new Coordinates(1, 2)));
		assertEquals(null, entryMap.get(new Coordinates(2, 2)));
	}
	
	@Test
	void testGetOrDefault() {
		Map<Coordinates, Double> map = new HashMap<Coordinates, Double>();
		map.put(new Coordinates(1, 2), 2.0);
		map.put(new Coordinates(2, 2), null);
		EntryMap<Double> entryMap = EntryMap.from(map);
		assertEquals(2.0, entryMap.getOrDefault(new Coordinates(1, 2), 0.0));
		assertEquals(0, entryMap.getOrDefault(new Coordinates(2, 2), 0.0));
	}

	@Test
	public void stream() {
		Stream<Entry<Integer>> stream1 = Stream.of(
				new Entry<Integer>(new Coordinates(1, 1), 2),
				new Entry<Integer>(new Coordinates(3, 4), 8),
				new Entry<Integer>(new Coordinates(2, 5), 3),
				new Entry<Integer>(new Coordinates(4, 6), 5)
				);
		List<Entry<Integer>> list1 = stream1.sorted().toList();
		TreeMap<Coordinates, Integer> entryMap = new TreeMap<>();
		entryMap.put(new Coordinates(1, 1), 2);
		entryMap.put(new Coordinates(3, 4), 8);
		entryMap.put(new Coordinates(2, 5), 3);
		entryMap.put(new Coordinates(4, 6), 5);
		List<Entry<Integer>> list2 = (EntryMap.from(entryMap)).stream().sorted().toList();

		assertEquals(true, (list1.equals(list2)));
	}

	@Test
	public void remap() {
		NavigableMap<Coordinates, Integer> entryMap = new TreeMap<>();
		entryMap.put(new Coordinates(1, 1), 2);
		entryMap.put(new Coordinates(3, 4), 8);
		entryMap.put(new Coordinates(2, 5), 4);
		entryMap.put(new Coordinates(4, 6), 6);
		
		EntryMap<Integer> entryMap1 = EntryMap.from(entryMap);
		
		EntryMap<Integer> entryMap2 = entryMap1.remap(
				coordinates -> coordinates.plus(Coordinates.DIAGONAL_UNIT), 
				value -> value / 2);
		List<Entry<Integer>> list1 = entryMap1.stream().sorted().toList();
		List<Entry<Integer>> list2 = entryMap2.stream().sorted().toList();
		
		assertEquals(true, (list1.get(0).value() / 2) == list2.get(0).value());
		assertEquals(0,
				(list1.get(0).coordinates().plus(Coordinates.DIAGONAL_UNIT).compareTo(list2.get(0).coordinates())));
		assertEquals(true, (list1.get(1).value() / 2) == list2.get(1).value());
		assertEquals(0,
				(list1.get(1).coordinates().plus(Coordinates.DIAGONAL_UNIT).compareTo(list2.get(1).coordinates())));
		assertEquals(true, (list1.get(2).value() / 2) == list2.get(2).value());
		assertEquals(0,
				(list1.get(2).coordinates().plus(Coordinates.DIAGONAL_UNIT).compareTo(list2.get(2).coordinates())));
		assertEquals(true, (list1.get(3).value() / 2) == list2.get(3).value());
		assertEquals(0,
				(list1.get(3).coordinates().plus(Coordinates.DIAGONAL_UNIT).compareTo(list2.get(3).coordinates())));
		assertThrows(IllegalArgumentException.class, () -> {
			entryMap1.remap(null, null);
		});
		assertThrows(NullPointerException.class, () -> {
			entryMap1.remap(coordinates -> coordinates, null);
		});
		assertThrows(NullPointerException.class, () -> {
			entryMap1.remap(null, value -> value);
		});
	}

	@Test
	public void sizeRounded() {
		TreeMap<Coordinates, Integer> map1 = new TreeMap<>();
		map1.put(new Coordinates(14, 14), 4);
		EntryMap<Integer> entryMap1 = EntryMap.from(map1);
		TreeMap<Coordinates, Integer> map2 = new TreeMap<>();
		map2.put(new Coordinates(15, 15), 4);
		EntryMap<Integer> entryMap2 = EntryMap.from(map2);
		TreeMap<Coordinates, Integer> map3 = new TreeMap<>();
		map3.put(new Coordinates(18, 18), 4);
		EntryMap<Integer> entryMap3 = EntryMap.from(map3);
		assertEquals(16, entryMap1.sizeRounded());
		assertEquals(16, entryMap2.sizeRounded());
		assertEquals(32, entryMap3.sizeRounded());
		
	}

}
