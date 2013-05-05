package org.alveolo.simpa.test;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.alveolo.simpa.test.beans.Extended;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class ExtendedTest extends AbstractQueryTest {
	@Test
	public void insertEntity() throws SQLException {
		sql("SELECT nextval('sch_test.seq_simple')");
		results();
		next(); number(321L);
		endQuery();

		sql("INSERT INTO sch_test.tbl_extended (simple_id,simple_name,value) VALUES (?,?,?)");
		param(321L);
		param("ExtendedTest");
		param("ExtendedValue");
		endUpdate(1);

		replay();

		Extended entity = new Extended();
		entity.setName("ExtendedTest");
		entity.setValue("ExtendedValue");

		es.insert(entity);

		Assert.assertEquals(321L, entity.getId());

		verify();
	}

	@Test
	public void insertEntityWithNull() throws SQLException {
		sql("SELECT nextval('sch_test.seq_simple')");
		results();
		next(); number(321L);
		endQuery();

		sql("INSERT INTO sch_test.tbl_extended (simple_id,simple_name,value) VALUES (?,?,?)");
		param(321L);
		paramNull(Types.VARCHAR);
		paramNull(Types.VARCHAR);
		endUpdate(1);

		replay();

		Extended entity = new Extended();
		entity.setName(null);
		entity.setValue(null);

		es.insert(entity);

		Assert.assertEquals(321L, entity.getId());

		verify();
	}

	@Test
	public void updateEntity() throws SQLException {
		sql("UPDATE sch_test.tbl_extended SET simple_name=?,value=? WHERE simple_id=?");
		param("ExtendedTest");
		param("ExtendedValue");
		param(123L);
		endUpdate(1);

		replay();

		Extended entity = new Extended();
		entity.setId(123L);
		entity.setName("ExtendedTest");
		entity.setValue("ExtendedValue");

		es.update(entity);

		verify();
	}

	@Test
	public void updateEntityWithNull() throws SQLException {
		sql("UPDATE sch_test.tbl_extended SET simple_name=?,value=? WHERE simple_id=?");
		paramNull(Types.VARCHAR);
		paramNull(Types.VARCHAR);
		param(123L);
		endUpdate(1);

		replay();

		Extended entity = new Extended();
		entity.setId(123L);
		entity.setName(null);
		entity.setValue(null);

		es.update(entity);

		verify();
	}

	@Test @Ignore
	public void updateEntitiesByIds() throws SQLException {
//		Simple entity = new Simple(); // template
//		es.update(entity, 123L, 456L);
	}

	@Test @Ignore
	public void updateEntitiesByName() throws SQLException {
//		Simple entity = new Simple(); // template
//		es.from(Simple.class).eq("name", "SimpleTest").update(entity);
	}

	@Test
	public void deleteEntity() throws SQLException {
		sql("DELETE FROM sch_test.tbl_extended WHERE simple_id=?");
		param(123L);
		endUpdate(1);

		replay();

		es.delete(new Extended(123L));

		verify();
	}

	@Test
	public void deleteEntityById() throws SQLException {
		sql("DELETE FROM sch_test.tbl_extended WHERE simple_id=?");
		param(123L);
		endUpdate(1);

		replay();

		es.delete(Extended.class, 123L);

		verify();
	}

	@Test @Ignore
	public void deleteEntitiesByIds() throws SQLException {
	}

	@Test @Ignore
	public void deleteEntitiesByName() throws SQLException {
	}

	@Test
	public void findEntity() throws SQLException {
		sql("SELECT simple_id,simple_name,value FROM sch_test.tbl_extended WHERE simple_id=?");
		param(123L);
		results();
		next(); number(123L); string("ExtendedTest"); string("ExtendedValue");
		endQuery();

		replay();

		Extended entity = es.find(Extended.class, 123L);

		Assert.assertNotNull(entity);
		Assert.assertEquals(123L, entity.getId());
		Assert.assertEquals("ExtendedTest", entity.getName());
		Assert.assertEquals("ExtendedValue", entity.getValue());

		verify();
	}

	@Test
	public void fromEntityByIds() throws SQLException {
		sql("SELECT simple_id,simple_name,value FROM sch_test.tbl_extended WHERE simple_id IN (?,?)");
		param(123L);
		param(456L);
		results();
		next(); number(123L); string("ExtendedTest1"); string("ExtendedValue1");
		next(); number(456L); string("ExtendedTest2"); string("ExtendedValue2");
		endQuery();

		replay();

		List<Extended> list = es.from(Extended.class).in("id", 123L, 456L).list();

		Assert.assertEquals(2, list.size());

		Extended entity1 = list.get(0);

		Assert.assertEquals(123L, entity1.getId());
		Assert.assertEquals("ExtendedTest1", entity1.getName());
		Assert.assertEquals("ExtendedValue1", entity1.getValue());

		Extended entity2 = list.get(1);

		Assert.assertEquals(456L, entity2.getId());
		Assert.assertEquals("ExtendedTest2", entity2.getName());
		Assert.assertEquals("ExtendedValue2", entity2.getValue());

		verify();
	}

	@Test
	public void fromEntityByName() throws SQLException {
		sql("SELECT simple_id,simple_name,value FROM sch_test.tbl_extended WHERE simple_name=?");
		param("test");
		results();
		next(); number(123L); string("ExtendedTest"); string("ExtendedValue");
		endQuery();

		replay();

		List<Extended> list = es.from(Extended.class).eq("name", "test").list();

		Assert.assertEquals(1, list.size());

		Extended entity = list.get(0);

		Assert.assertEquals(123L, entity.getId());
		Assert.assertEquals("ExtendedTest", entity.getName());
		Assert.assertEquals("ExtendedValue", entity.getValue());

		verify();
	}

	@Test
	public void fromEntityWithGrouping() throws SQLException {
		sql("SELECT simple_id,simple_name,value FROM sch_test.tbl_extended" +
			" WHERE simple_name=? AND simple_id=? AND (simple_id=? OR simple_name=?)" +
			" GROUP BY simple_id,simple_name,count(simple_id)" +
			" HAVING simple_id=? AND simple_name=?" +
			" ORDER BY simple_id,simple_name DESC,count(simple_id)" +
			" OFFSET 60 LIMIT 20");
		param("test");
		param(123L);
		param(123L);
		param("test");
		param(123L);
		param("test");
		results();
		next(); number(123L); string("ExtendedTest"); string("ExtendedValue");
		endQuery();

		replay();

		List<Extended> list = es.from(Extended.class)
				.eq("name", "test").raw("simple_id=?", 123L).disjunction().eq("id", 123L).eq("name", "test").end()
				.group().by("id").by("name").raw("count(simple_id)")
				.having().eq("id", 123L).raw("simple_name=?", "test")
				.order().asc("id").desc("name").raw("count(simple_id)")
				.list(60, 20);

		Assert.assertEquals(1, list.size());

		Extended entity = list.get(0);

		Assert.assertEquals(123L, entity.getId());
		Assert.assertEquals("ExtendedTest", entity.getName());
		Assert.assertEquals("ExtendedValue", entity.getValue());

		verify();
	}

	@Test
	public void fromEntityAttribute() throws SQLException {
		sql("SELECT simple_id FROM sch_test.tbl_extended");
		results();
		next(); number(123L);
		endQuery();

		replay();

		List<Long> list = es.from(Extended.class).select().attribute(Long.class, "id").list(); // map(), map(k), map(k,v)

		Assert.assertEquals(1, list.size());
		Assert.assertEquals(123L, list.get(0).longValue());

		verify();
	}

	@Test
	public void fromEntityAggregate() throws SQLException {
		sql("SELECT count(*) FROM sch_test.tbl_extended");
		results();
		next(); number(3);
		endQuery();

		replay();

		int count = es.from(Extended.class).select().raw(Integer.class, "count(*)").find();

		Assert.assertEquals(3, count);

		verify();
	}

	@Test
	public void fromEntityMap() throws SQLException {
		sql("SELECT simple_id,simple_name,value FROM sch_test.tbl_extended");
		results();
		next(); number(123L); string("ExtendedTest"); string("ExtendedValue");
		endQuery();

		replay();

		Map<?, Extended> map = es.from(Extended.class).map();

		Assert.assertEquals(1, map.size());

		Extended entity = map.get(123L);

		Assert.assertEquals(123L, entity.getId());
		Assert.assertEquals("ExtendedTest", entity.getName());
		Assert.assertEquals("ExtendedValue", entity.getValue());

		verify();
	}

	@Test
	public void fromEntityMap1() throws SQLException {
		sql("SELECT simple_id,simple_name,value FROM sch_test.tbl_extended");
		results();
		next(); number(123L); string("ExtendedTest"); string("ExtendedValue");
		endQuery();

		replay();

		Map<?, Extended> map = es.from(Extended.class).map("name");

		Assert.assertEquals(1, map.size());

		Extended entity = map.get("ExtendedTest");

		Assert.assertEquals(123L, entity.getId());
		Assert.assertEquals("ExtendedTest", entity.getName());
		Assert.assertEquals("ExtendedValue", entity.getValue());

		verify();
	}

	@Test
	public void fromEntityMap2() throws SQLException {
		sql("SELECT simple_name,value FROM sch_test.tbl_extended");
		results();
		next(); string("ExtendedTest"); string("ExtendedValue");
		endQuery();

		replay();

		Map<?, ?> map = es.from(Extended.class).map("name", "value");

		Assert.assertEquals(1, map.size());
		Assert.assertEquals("ExtendedValue", map.get("ExtendedTest"));

		verify();
	}
}
