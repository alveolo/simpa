package org.alveolo.simpa.test;

import static org.easymock.EasyMock.expect;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RawTest extends AbstractQueryTest {
	protected ResultSetMetaData meta;

	@Override @Before
	public void before() {
		super.before();
		meta = EasyMock.createMock(ResultSetMetaData.class);
	}

	@Override
	public void replay() {
		super.replay();
		EasyMock.replay(meta);
	}

	@Override
	public void verify() {
		super.verify();
		EasyMock.verify(meta);
	}

	@Test
	public void fromRawEntity() throws SQLException {
		sql("SELECT simple_id, simple_name, count(*) cnt FROM sch_test.tbl_simple" +
			" WHERE simple_id<?");
		param(200L);
		results();
		expect(rset.getMetaData()).andReturn(meta);
		expect(meta.getColumnCount()).andReturn(3);
		expect(meta.getColumnLabel(1)).andReturn("simple_id");
		expect(meta.getColumnLabel(2)).andReturn("simple_name");
		expect(meta.getColumnLabel(3)).andReturn("cnt");
		next(); number(123L); string("SimpleTest"); number(1);
		endQuery();

		replay();

		RawValue value = es.sql("SELECT simple_id, simple_name, count(*) cnt FROM sch_test.tbl_simple WHERE")
				.sql("simple_id<?", 200L).find(RawValue.class);

		Assert.assertEquals(123L, value.id);
		Assert.assertEquals("SimpleTest", value.name);
		Assert.assertEquals(1, value.count);

		verify();
	}
}
