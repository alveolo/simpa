package org.alveolo.simpa.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.alveolo.simpa.jdbc.JdbcStore;
import org.springframework.jdbc.datasource.DataSourceUtils;


public class SpringJdbcStore extends JdbcStore {
	public SpringJdbcStore(DataSource ds, List<Class<?>> classes) {
		super(ds, classes);
	}

	@Override
	protected Connection acquireConnection() throws SQLException {
		return DataSourceUtils.getConnection(ds);
	}

	@Override
	protected void releaseConnection(Connection con) throws SQLException {
		DataSourceUtils.releaseConnection(con, ds);
	}
}
