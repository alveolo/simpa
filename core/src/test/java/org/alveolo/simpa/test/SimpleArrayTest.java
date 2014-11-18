package org.alveolo.simpa.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.alveolo.simpa.test.beans.SimpleArray;
import org.junit.Assert;
import org.junit.Test;


public class SimpleArrayTest extends AbstractQueryTest {
	@Test
	public void insertEntity() throws SQLException {
		sql("SELECT nextval('sch_test.seq_simple_array')");
		results();
		next(); number(321L);
		endQuery();

		sql("INSERT INTO sch_test.tbl_simple_array (simple_id," +
				"simple_strings,simple_longs,simple_ints," +
				"simple_collection,simple_list,simple_set,simple_linked)" +
				" VALUES (?,?,?,?,?,?,?,?)");
		param(321L);
		paramArray("String1", "String2");
		paramArray(123L, null, 456L);
		paramArray(78, 90);
		paramArray("Collection1", "Collection2");
		paramArray("List1", "List2");
		paramArray("Set1", "Set2");
		paramArray(null, 34, 56);
		endUpdate(1);

		replay();

		SimpleArray entity = new SimpleArray();
		entity.setStrings("String1", "String2");
		entity.setLongs(123L, null, 456L);
		entity.setInts(78, 90);
		entity.setCollection(Arrays.asList("Collection1", "Collection2"));
		entity.setList(Arrays.asList("List1", "List2"));
		entity.setSet(new TreeSet<String>(Arrays.asList("Set1", "Set2")));
		entity.setLinked(new LinkedList<Integer>(Arrays.asList(null, 34, 56)));

		es.insert(entity);

		Assert.assertEquals(321L, entity.getId());

		verify();
	}

	@Test
	public void updateEntity() throws SQLException {
		sql("UPDATE sch_test.tbl_simple_array SET " +
				"simple_strings=?,simple_longs=?,simple_ints=?," +
				"simple_collection=?,simple_list=?,simple_set=?,simple_linked=?" +
				" WHERE simple_id=?");
		paramArray("String1", "String2");
		paramArray(123L, null, 456L);
		paramArray(78, 90);
		paramArray("Collection1", "Collection2");
		paramArray("List1", "List2");
		paramArray("Set1", "Set2");
		paramArray(null, 34, 56);
		param(123L);
		endUpdate(1);

		replay();

		SimpleArray entity = new SimpleArray();
		entity.setId(123L);
		entity.setStrings("String1", "String2");
		entity.setLongs(123L, null, 456L);
		entity.setInts(78, 90);
		entity.setCollection(Arrays.asList("Collection1", "Collection2"));
		entity.setList(Arrays.asList("List1", "List2"));
		entity.setSet(new TreeSet<String>(Arrays.asList("Set1", "Set2")));
		entity.setLinked(new LinkedList<Integer>(Arrays.asList(null, 34, 56)));

		es.update(entity);

		verify();
	}

	@Test
	public void findEntity() throws SQLException {
		sql("SELECT simple_id,simple_strings,simple_longs,simple_ints," +
				"simple_collection,simple_list,simple_set,simple_linked" +
				" FROM sch_test.tbl_simple_array WHERE simple_id=?");
		param(123L);
		results();
		next(); number(123L);
		stringArray("String1", "String2");
		numberArray(123L, null, 456L);
		nullArray();
		stringArray("Collection1", "Collection2");
		stringArray("List1", "List2");
		stringArray("Set1", "Set2");
		numberArray(null, 34, 56);
		endQuery();

		replay();

		SimpleArray entity = es.find(SimpleArray.class, 123L);

		Assert.assertNotNull(entity);
		Assert.assertEquals(123L, entity.getId());
		Assert.assertArrayEquals(new String[] {"String1", "String2"}, entity.getStrings());
		Assert.assertArrayEquals(new Long[] {123L, null, 456L}, entity.getLongs());
		Assert.assertNull(entity.getInts());
		Assert.assertEquals(new ArrayList<String>(Arrays.asList("Collection1", "Collection2")), entity.getCollection());
		Assert.assertEquals(new ArrayList<String>(Arrays.asList("List1", "List2")), entity.getList());
		Assert.assertEquals(new LinkedHashSet<String>(Arrays.asList("Set1", "Set2")), entity.getSet());
		Assert.assertEquals(new ArrayList<Integer>(Arrays.asList(null, 34, 56)), entity.getLinked());

		verify();
	}

	@Test
	public void fromEntityByString() throws SQLException {
		sql("SELECT simple_id,simple_strings,simple_longs,simple_ints," +
				"simple_collection,simple_list,simple_set,simple_linked" +
				" FROM sch_test.tbl_simple_array WHERE simple_strings@>?");
		paramArray("String1", "String2");
		results();
		next(); number(123L);
		stringArray("String1", "String2");
		numberArray(123L, null, 456L);
		numberArray(78, 90);
		stringArray("Collection1", "Collection2");
		stringArray("List1", "List2");
		stringArray("Set1", "Set2");
		nullArray();
		endQuery();

		replay();

		List<SimpleArray> list = es.from(SimpleArray.class).contains("strings", "String1", "String2").list();

		Assert.assertEquals(1, list.size());

		SimpleArray entity = list.get(0);

		Assert.assertEquals(123L, entity.getId());
		Assert.assertArrayEquals(new String[] {"String1", "String2"}, entity.getStrings());
		Assert.assertArrayEquals(new Long[] {123L, null, 456L}, entity.getLongs());
		Assert.assertArrayEquals(new int[] {78, 90}, entity.getInts());
		Assert.assertEquals(new ArrayList<String>(Arrays.asList("Collection1", "Collection2")), entity.getCollection());
		Assert.assertEquals(new ArrayList<String>(Arrays.asList("List1", "List2")), entity.getList());
		Assert.assertEquals(new LinkedHashSet<String>(Arrays.asList("Set1", "Set2")), entity.getSet());
		Assert.assertNull(entity.getLinked());

		verify();
	}

	@Test
	public void fromEntityByInt() throws SQLException {
		sql("SELECT simple_id,simple_strings,simple_longs,simple_ints," +
				"simple_collection,simple_list,simple_set,simple_linked" +
				" FROM sch_test.tbl_simple_array WHERE simple_ints<@?");
		paramArray(78, 90);
		results();
		next(); number(123L);
		stringArray("String1", "String2");
		numberArray(123L, null, 456L);
		numberArray(78, 90);
		stringArray("Collection1", "Collection2");
		stringArray("List1", "List2");
		stringArray("Set1", "Set2");
		numberArray(null, 34, 56);
		endQuery();

		replay();

		List<SimpleArray> list = es.from(SimpleArray.class).containedBy("ints", 78, 90).list();

		Assert.assertEquals(1, list.size());

		SimpleArray entity = list.get(0);

		Assert.assertEquals(123L, entity.getId());
		Assert.assertArrayEquals(new String[] {"String1", "String2"}, entity.getStrings());
		Assert.assertArrayEquals(new Long[] {123L, null, 456L}, entity.getLongs());
		Assert.assertArrayEquals(new int[] {78, 90}, entity.getInts());
		Assert.assertEquals(new ArrayList<String>(Arrays.asList("Collection1", "Collection2")), entity.getCollection());
		Assert.assertEquals(new ArrayList<String>(Arrays.asList("List1", "List2")), entity.getList());
		Assert.assertEquals(new LinkedHashSet<String>(Arrays.asList("Set1", "Set2")), entity.getSet());
		Assert.assertEquals(new ArrayList<Integer>(Arrays.asList(null, 34, 56)), entity.getLinked());

		verify();
	}
}
