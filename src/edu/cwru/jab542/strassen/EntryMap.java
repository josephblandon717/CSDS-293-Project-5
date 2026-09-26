/**
 *  Provides a representation of a sparse matrix.
 *  
 *  @author Joseph Blandon
 * 
 */
package edu.cwru.jab542.strassen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * The Class EntryMap.
 *
 * @param <T> the generic type
 */
public final class EntryMap<T>{
	
	/** The entry map. */
	public final NavigableMap<Coordinates, T> entryMap;
	
	/**
	 * Private constructor that instantiates a new entry map.
	 *
	 * @param entryMap the entry map
	 */
	private EntryMap(NavigableMap<Coordinates, T> entryMap){
		this.entryMap = entryMap;
	}
	
	/**
	 * Creates an EntryMap from an object that implements Map..
	 *
	 * @param <T> the generic type
	 * @param entryMap the entry map
	 * @return EntryMap<T> object
	 */
	public static <T> EntryMap<T> from(Map<Coordinates, T> entryMap){
		if (!entryMap.isEmpty()) 
			return new EntryMap<T>(new TreeMap<Coordinates, T>(entryMap));
		else 
			throw new IllegalArgumentException("Entry map given is empty.");	
	}
	
	/**
	 * Returns the largest row that contains a value.
	 *
	 * @return the largest row in the entryMap
	 */
	public int rows() {
		return this.entryMap.descendingKeySet().getFirst().row() + 1;
	}
	
	/**
	 * Returns the largest column that contains a value.
	 *
	 * @return the largest column in the entryMap
	 */
	public int columns() {
		NavigableSet<Coordinates> keySet = this.entryMap.descendingKeySet();
		int columns = 0;
		for(Coordinates coordinates : keySet) {
			if(coordinates.col() > columns)
				columns = coordinates.col();
		}
		return columns + 1;
	}
	
	/**
	 * Gets the size of the entryMap.
	 *
	 * @return the size
	 */
	public int getSize() {
		return Math.max(this.rows(), this.columns());
	}
	
	/**
	 * Gets the value stored at a specific coordinate.
	 *
	 * @param coordinates the coordinates
	 * @return the value at the coordinates
	 */
	public T get(Coordinates coordinates) {
		return entryMap.get(coordinates);
	}
	
	/**
	 * Gets the value stored at a specific coordinate, if value is null, returns the default value.
	 *
	 * @param coordinates the coordinates
	 * @param defaultValue the default value
	 * @return the value at the coordinates or the default value
	 */
	public T getOrDefault(Coordinates coordinates, T defaultValue) {
		if(entryMap.get(coordinates) != null)
			return entryMap.get(coordinates);
		else 
			return defaultValue;
		
	}
	
	/**
	 * Creates a stream of entries from the reference EntryMap.
	 *
	 * @return the stream of entries
	 */
	public Stream<Entry<T>> stream() {
		List<Entry<T>> entries = new ArrayList<Entry<T>>();
		NavigableSet<Coordinates> keySet = this.entryMap.descendingKeySet();
		for (Coordinates coordinates : keySet) {
			entries.add(new Entry<T>(coordinates, this.get(coordinates)));
		}
		return entries.stream();
	}
	
	/**
	 * Remaps the reference EntryMap to another EntryMap using specified Functions.
	 *
	 * @param coordinatesMapper the coordinates mapper
	 * @param valueMapper the value mapper
	 * @return the remapped entry map
	 */
	public EntryMap<T> remap(
			Function<Coordinates, Coordinates> coordinatesMapper, 
			Function<T, T> valueMapper){
		Objects.requireNonNull(coordinatesMapper);
		Objects.requireNonNull(valueMapper);
		NavigableMap<Coordinates, T> newEntries = new TreeMap<Coordinates, T>();
		
		this.stream().forEach(entry -> 
		newEntries.put(
				coordinatesMapper.apply(entry.coordinates()), valueMapper.apply(entry.value())
				));
		
		return EntryMap.from(newEntries);
	}
	
	 /**
 	 * Returns the size of the EntryMap rounded to the nearest power of 2.
 	 *
 	 * @return the size rounded up to the nearest power of 2
 	 */
 	public int sizeRounded() {
		 int timesDivided = 0;
		 double dividend = this.getSize();
		 while(1 < dividend) {
			 dividend = dividend / 2;
			 timesDivided++;
		 }
		 return (int)Math.pow(2, timesDivided);
		 
	} 
}
