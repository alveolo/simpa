package org.alveolo.simpa.test;

import java.sql.SQLException;
import java.util.List;

import org.alveolo.simpa.test.beans.BasicEnum;
import org.alveolo.simpa.test.beans.CompositeEnum;
import org.alveolo.simpa.test.beans.CompositeEnumPK;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class CompositeTest extends AbstractQueryTest {
	@Test
	public void insertEntity() throws SQLException {
		sql("INSERT INTO sch_test.tbl_composite (long_id,enum_id,enum_default,enum_string,enum_ordinal)" +
			" VALUES (?,?,?,?,?)");
		param(123L);
		param("THREE");
		param(1);
		param("DFLT");
		param(2);
		endUpdate(1);

		replay();

		CompositeEnum entity = new CompositeEnum(new CompositeEnumPK(123, BasicEnum.THREE));
		entity.setEnumDefault(BasicEnum.ONE);
		entity.setEnumString(BasicEnum.DFLT);
		entity.setEnumOrdinal(BasicEnum.TWO);

		es.insert(entity);

		verify();
	}

	@Test
	public void updateEntity() throws SQLException {
		sql("UPDATE sch_test.tbl_composite SET enum_default=?,enum_string=? WHERE long_id=? AND enum_id=?");
		param(1);
		param("DFLT");
		param(123L);
		param("THREE");
		endUpdate(1);

		replay();

		CompositeEnum entity = new CompositeEnum(new CompositeEnumPK(123, BasicEnum.THREE));
		entity.setEnumDefault(BasicEnum.ONE);
		entity.setEnumString(BasicEnum.DFLT);
		entity.setEnumOrdinal(BasicEnum.TWO);

		es.update(entity);

		verify();
	}

	@Test @Ignore
	public void updateEntitiesByIds() throws SQLException {
	}

	@Test @Ignore
	public void updateEntitiesByName() throws SQLException {
	}

	@Test
	public void deleteEntity() throws SQLException {
		sql("DELETE FROM sch_test.tbl_composite WHERE long_id=? AND enum_id=?");
		param(123L);
		param("THREE");
		endUpdate(1);

		replay();

		es.delete(new CompositeEnum(123L, BasicEnum.THREE));

		verify();
	}

	@Test
	public void deleteEntityById() throws SQLException {
		sql("DELETE FROM sch_test.tbl_composite WHERE long_id=? AND enum_id=?");
		param(123L);
		param("THREE");
		endUpdate(1);

		replay();

		es.delete(CompositeEnum.class, new CompositeEnumPK(123, BasicEnum.THREE));

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
		sql("SELECT long_id,enum_id,enum_default,enum_string,enum_ordinal" +
				" FROM sch_test.tbl_composite WHERE (long_id=? AND enum_id=?)");
		param(123L);
		param("THREE");
		results();
		next(); number(123L); string("THREE"); number(1); string("DFLT"); number(2);
		endQuery();

		replay();

		CompositeEnum entity = es.find(CompositeEnum.class, new CompositeEnumPK(123L, BasicEnum.THREE));
		Assert.assertNotNull(entity);

		CompositeEnumPK id = entity.getId();
		Assert.assertEquals(123, id.getLongId());
		Assert.assertEquals(BasicEnum.THREE, id.getEnumId());
		Assert.assertEquals(BasicEnum.ONE, entity.getEnumDefault());
		Assert.assertEquals(BasicEnum.DFLT, entity.getEnumString());
		Assert.assertEquals(BasicEnum.TWO, entity.getEnumOrdinal());

		verify();
	}

	@Test
	public void fromEntityByEqId() throws SQLException {
		sql("SELECT long_id,enum_id,enum_default,enum_string,enum_ordinal" +
			" FROM sch_test.tbl_composite WHERE (long_id=? AND enum_id=?)");
		param(123L);
		param("THREE");
		results();
		next(); number(123L); string("THREE"); number(1); string("DFLT"); number(2);
		endQuery();

		replay();

		List<CompositeEnum> list = es.from(CompositeEnum.class)
				.eq("id", new CompositeEnumPK(123L, BasicEnum.THREE))
				.list();

		Assert.assertEquals(1, list.size());

		CompositeEnum entity = list.get(0);

		CompositeEnumPK id = entity.getId();
		Assert.assertEquals(123L, id.getLongId());
		Assert.assertEquals(BasicEnum.THREE, id.getEnumId());
		Assert.assertEquals(BasicEnum.ONE, entity.getEnumDefault());
		Assert.assertEquals(BasicEnum.DFLT, entity.getEnumString());
		Assert.assertEquals(BasicEnum.TWO, entity.getEnumOrdinal());

		verify();
	}

	@Test
	public void fromEntityByNeqId() throws SQLException {
		sql("SELECT long_id,enum_id,enum_default,enum_string,enum_ordinal" +
			" FROM sch_test.tbl_composite WHERE (long_id!=? OR enum_id!=?)");
		param(123L);
		param("THREE");
		results();
		next(); number(123L); string("THREE"); number(1); string("DFLT"); number(2);
		endQuery();

		replay();

		List<CompositeEnum> list = es.from(CompositeEnum.class)
				.neq("id", new CompositeEnumPK(123L, BasicEnum.THREE))
				.list();

		Assert.assertEquals(1, list.size());

		CompositeEnum entity = list.get(0);

		CompositeEnumPK id = entity.getId();
		Assert.assertEquals(123L, id.getLongId());
		Assert.assertEquals(BasicEnum.THREE, id.getEnumId());
		Assert.assertEquals(BasicEnum.ONE, entity.getEnumDefault());
		Assert.assertEquals(BasicEnum.DFLT, entity.getEnumString());
		Assert.assertEquals(BasicEnum.TWO, entity.getEnumOrdinal());

		verify();
	}

	@Test
	public void fromEntityByIds() throws SQLException {
		sql("SELECT long_id,enum_id,enum_default,enum_string,enum_ordinal" +
				" FROM sch_test.tbl_composite WHERE (long_id,enum_id) IN ((?,?),(?,?))");
		param(123L);
		param("THREE");
		param(456L);
		param("DFLT");
		results();
		next(); number(123L); string("THREE"); number(1); string("DFLT"); number(2);
		next(); number(456L); string("DFLT"); number(1); string("DFLT"); number(2);
		endQuery();

		replay();

		List<CompositeEnum> list = es.from(CompositeEnum.class)
				.in("id", new CompositeEnumPK(123L, BasicEnum.THREE), new CompositeEnumPK(456L, BasicEnum.DFLT))
				.list();

		Assert.assertEquals(2, list.size());

		CompositeEnum entity1 = list.get(0);

		CompositeEnumPK id1 = entity1.getId();
		Assert.assertEquals(123L, id1.getLongId());
		Assert.assertEquals(BasicEnum.THREE, id1.getEnumId());
		Assert.assertEquals(BasicEnum.ONE, entity1.getEnumDefault());
		Assert.assertEquals(BasicEnum.DFLT, entity1.getEnumString());
		Assert.assertEquals(BasicEnum.TWO, entity1.getEnumOrdinal());

		CompositeEnum entity2 = list.get(1);

		CompositeEnumPK id2 = entity2.getId();
		Assert.assertEquals(456L, id2.getLongId());
		Assert.assertEquals(BasicEnum.DFLT, id2.getEnumId());
		Assert.assertEquals(BasicEnum.ONE, entity2.getEnumDefault());
		Assert.assertEquals(BasicEnum.DFLT, entity2.getEnumString());
		Assert.assertEquals(BasicEnum.TWO, entity2.getEnumOrdinal());

		verify();
	}

	@Test
	public void fromEntityByName() throws SQLException {
		sql("SELECT long_id,enum_id,enum_default,enum_string,enum_ordinal" +
			" FROM sch_test.tbl_composite WHERE enum_string=? AND enum_default=?");
		param("DFLT");
		param(1);
		results();
		next(); number(123L); string("THREE"); number(1); string("DFLT"); number(2);
		endQuery();

		replay();

		List<CompositeEnum> list = es.from(CompositeEnum.class)
				.eq("enumString", BasicEnum.DFLT)
				.eq("enumDefault", BasicEnum.ONE)
				.list();

		Assert.assertEquals(1, list.size());

		CompositeEnum entity = list.get(0);

		CompositeEnumPK id = entity.getId();
		Assert.assertEquals(123, id.getLongId());
		Assert.assertEquals(BasicEnum.THREE, id.getEnumId());
		Assert.assertEquals(BasicEnum.ONE, entity.getEnumDefault());
		Assert.assertEquals(BasicEnum.DFLT, entity.getEnumString());
		Assert.assertEquals(BasicEnum.TWO, entity.getEnumOrdinal());

		verify();
	}
}
