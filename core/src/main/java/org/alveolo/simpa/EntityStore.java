package org.alveolo.simpa;

import javax.persistence.metamodel.Metamodel;

import org.alveolo.simpa.query.SqlBuilder;
import org.alveolo.simpa.query.WhereBuilder;


public interface EntityStore {
	Metamodel getMetamodel();

	void insert(Object entity);
	void update(Object entity);

	void delete(Object entity);
	<T> void delete(Class<T> type, Object identity);

	<T> T find(Class<T> type, Object identity);

	<T> WhereBuilder<T> from(Class<T> type);
	<T> SqlBuilder sql(String sql, Object... values);
}
