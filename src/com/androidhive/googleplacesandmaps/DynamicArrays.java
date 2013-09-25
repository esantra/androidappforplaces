package com.androidhive.googleplacesandmaps;

//code source: http://theoryapp.com/dynamic-array-class-in-java/

import java.util.Arrays;

public class DynamicArrays {
	// The storage for the elements.
	// The capacity is the length of this array.
	private Place[] data;

	// The number of elements (logical size).
	// It must be smaller than the capacity.
	private int size;

	// Constructs an empty DynamicArray
	public DynamicArrays() {
		data = new Place[100];
		size = 0;
	}

	// Constructs an empty DynamicArray with the
	// specified initial capacity.
	public DynamicArrays(int capacity) {
		if (capacity < 100)
			capacity = 100;
		data = new Place[capacity];
		size = 0;
	}

	// Returns the logical size
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	// Checks if the given index is in range.
	private void rangeCheck(int index) {
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ size);
	}

	// Returns the element at the specified position.
	public Place get(int index) {
		rangeCheck(index);
		return data[index];
	}

	// Appends the specified element to the end.
	public boolean add(Place element) {
		// ensureCapacity(size + 1);
		data[size++] = element;
		return true;
	}

	// Removes all of the elements.
	public void clear() {
		size = 0;
	}

	// Replaces the element at the specified position
	public Place set(int index, Place element) {
		rangeCheck(index);
		Place oldValue = data[index];
		data[index] = element;
		return oldValue;
	}

	public int capacity() {
		return data.length;
	}
}