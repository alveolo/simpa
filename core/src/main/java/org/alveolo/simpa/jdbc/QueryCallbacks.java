package org.alveolo.simpa.jdbc;

import java.util.List;

import org.alveolo.simpa.Page;
import org.alveolo.simpa.metamodel.Metamodel;
import org.alveolo.simpa.query.Query;


public interface QueryCallbacks {
	Metamodel getMetamodel();

	<T> int delete(Query<T> query);
	<T> T find(Query<T> query);
	<T> List<T> list(Query<T> query);
	<T> Page<T> page(Query<T> query);
}
