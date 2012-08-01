package org.alveolo.simpa;

import java.util.List;


public class Page<T> {
	private final List<T> records;
	private final int size;

	public Page(List<T> records, int size) {
		this.records = records;
		this.size = size;
	}

	public List<T> getRecords() {
		return records;
	}

	public int getSize() {
		return size;
	}
}
