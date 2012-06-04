package org.alveolo.simpa.jdbc;

import java.util.List;

import javax.persistence.metamodel.Metamodel;

import org.alveolo.simpa.query.Raw;


public interface RawCallbacks {
	Metamodel getMetamodel();

	<T> T find(Class<T> javaType, List<Raw> fragments);
	<T> List<T> list(Class<T> javaType, List<Raw> fragments);
}
