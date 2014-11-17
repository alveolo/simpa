package org.alveolo.simpa.test.beans;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.alveolo.simpa.Array;
import org.alveolo.simpa.Column;
import org.alveolo.simpa.Entity;
import org.alveolo.simpa.GeneratedValue;
import org.alveolo.simpa.GenerationType;
import org.alveolo.simpa.Id;
import org.alveolo.simpa.SequenceGenerator;
import org.alveolo.simpa.Table;


@Entity @Table(schema="sch_test", name="tbl_simple_array")
@SequenceGenerator(schema="sch_test", name="simple_array_sequence", sequenceName="seq_simple_array", allocationSize=10)
public class SimpleArray {
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="simple_array_sequence")
	@Column(name="simple_id")
	private long id;

	// TODO: Multidimensional
	// TODO: Collections/arrays of Enums, custom/composite types/structs
	// TODO: UUID
	
	// Arrays of objects and primitives

	@Array @Column(name="simple_strings")
	private String[] strings;

	@Array @Column(name="simple_longs")
	private Long[] longs;

	@Array @Column(name="simple_ints")
	private int[] ints;

	// Collection interfaces

	@Array @Column(name="simple_collection")
	private Collection<String> collection;

	@Array @Column(name="simple_list")
	private List<String> list;

	@Array @Column(name="simple_set")
	private Set<String> set;

	// Custom collection class

	@Array @Column(name="simple_linked")
	private LinkedList<Integer> linked;

	public SimpleArray() {}

	public SimpleArray(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String[] getStrings() {
		return strings;
	}

	public void setStrings(String... strings) {
		this.strings = strings;
	}

	public Long[] getLongs() {
		return longs;
	}

	public void setLongs(Long... longs) {
		this.longs = longs;
	}

	public int[] getInts() {
		return ints;
	}

	public void setInts(int... ints) {
		this.ints = ints;
	}

	public Collection<String> getCollection() {
		return collection;
	}

	public void setCollection(Collection<String> collection) {
		this.collection = collection;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Set<String> getSet() {
		return set;
	}

	public void setSet(Set<String> set) {
		this.set = set;
	}

	public LinkedList<Integer> getLinked() {
		return linked;
	}

	public void setLinked(LinkedList<Integer> linked) {
		this.linked = linked;
	}
}
