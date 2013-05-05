package org.alveolo.simpa.test;

import static org.easymock.EasyMock.expect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.alveolo.simpa.EntityStore;
import org.alveolo.simpa.jdbc.JdbcStore;
import org.alveolo.simpa.test.beans.CompositeEnum;
import org.alveolo.simpa.test.beans.CompositeEnumPK;
import org.alveolo.simpa.test.beans.Extended;
import org.alveolo.simpa.test.beans.Simple;
import org.easymock.EasyMock;
import org.junit.Before;


public class AbstractQueryTest {
	protected DataSource ds;
	protected Connection conn;
	protected PreparedStatement stmt;
	protected ResultSet rset;

	protected EntityStore es;
	protected int position;

	private <T> T createMock(Class<T> toMock) {
//		return EasyMock.createStrictMock(toMock);
		return EasyMock.createMock(toMock); // Useful sometimes for better diagnosis messages
	}

	@Before
	public void before() {
		ds = createMock(DataSource.class);
		conn = createMock(Connection.class);
		stmt = createMock(PreparedStatement.class);
		rset = createMock(ResultSet.class);

		List<Class<?>> classes = Arrays.asList(
				Simple.class, Extended.class,
				CompositeEnum.class, CompositeEnumPK.class);

		es = new JdbcStore(ds, classes);
	}

	protected void sql(String sql) throws SQLException {
		expect(ds.getConnection()).andReturn(conn);
		expect(conn.prepareStatement(sql)).andReturn(stmt);
	}

	protected void paramNull(int sqlType) throws SQLException {
		stmt.setNull(++position, sqlType);
	}

	protected void param(String string) throws SQLException {
		stmt.setString(++position, string);
	}

	protected void param(Integer integer) throws SQLException {
		stmt.setInt(++position, integer);
	}

	protected void param(Long l) throws SQLException {
		stmt.setLong(++position, l);
	}

	protected void results() throws SQLException {
		expect(stmt.executeQuery()).andReturn(rset);
	}

	protected void next() throws SQLException {
		expect(rset.next()).andReturn(true); position = 0;
	}

	protected void number(Number value) throws SQLException {
		expect(rset.getObject(++position)).andReturn(value);
	}

	protected void string(String string) throws SQLException {
		expect(rset.getString(++position)).andReturn(string);
	}

	protected void endQuery() throws SQLException {
		expect(rset.next()).andReturn(false); position = 0;
		rset.close();
		stmt.close();
		conn.close();
	}

	protected void endUpdate(int rows) throws SQLException {
		expect(stmt.executeUpdate()).andReturn(rows);
		stmt.close();
		conn.close();
	}

	public void replay() {
		EasyMock.replay(ds, conn, stmt, rset);
	}

	public void verify() {
		EasyMock.verify(ds, conn, stmt, rset);
	}
}
