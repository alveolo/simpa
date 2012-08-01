package org.alveolo.simpa.query;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;

import org.alveolo.simpa.Page;
import org.alveolo.simpa.jdbc.QueryCallbacks;
import org.alveolo.simpa.util.EntityUtil;


public class BaseBuilder<T> {
	protected final QueryCallbacks callbacks;
	protected final Query<T> query;

	public BaseBuilder(QueryCallbacks callbacks, Query<T> query) {
		this.callbacks = callbacks;
		this.query = query;
	}

	// TODO: Refactor to Path<X>
	protected Attribute<? super T, ?> getPath(String path) {
		ManagedType<T> t = callbacks.getMetamodel().entity(query.type);

		while (true) {
			int index = path.indexOf('.');
			if (index < 0) {
				return t.getAttribute(path);
			}

			SingularAttribute<?, ?> a = t.getSingularAttribute(path.substring(0, index));
			@SuppressWarnings("unchecked") // Path<X> should solve the typing problem
			ManagedType<T> inner = (ManagedType<T>) a.getType();
			t = inner;

			path = path.substring(index + 1);
		}
	}

	public List<T> list() {
		return callbacks.list(query);
	}

	public List<T> list(int fetch) {
		query.fetch = fetch;

		return list();
	}

	public List<T> list(int offset, int fetch) {
		query.offset = offset;
		query.fetch = fetch;

		return list();
	}

	public Page<T> page(int offset, int fetch) {
		// TODO: not implemented
		throw new NoSuchMethodError("not implemented");
	}

	public Page<T> page(int offset, int fetch, int maxSize) {
		// TODO: not implemented
		throw new NoSuchMethodError("not implemented");
	}

	public Map<?, T> map() {
		EntityType<T> type = callbacks.getMetamodel().entity(query.type);

		Map<Object, T> map = new LinkedHashMap<>();
		for (T entity : list()) {
			map.put(EntityUtil.getId(type, entity), entity);
		}
		return map;
	}

	public Map<?, T> map(String keyPath) {
		Attribute<? super T, ?> attribute = getPath(keyPath);

		Map<Object, T> map = new LinkedHashMap<>();
		for (T entity : list()) {
			map.put(EntityUtil.getValue(attribute, entity), entity);
		}
		return map;
	}

	public Map<?, ?> map(String keyPath, String valuePath) {
		if (query.select != null) {
			throw new IllegalStateException("Selection attributes are already set");
		}

		Attribute<? super T, ?> keyAttr = getPath(keyPath);
		Attribute<? super T, ?> valueAttr = getPath(valuePath);

		query.select = Arrays.asList((Select) new AttrSelect(keyAttr), new AttrSelect(valueAttr));

		Map<Object, Object> map = new LinkedHashMap<>();
		for (T entity : list()) {
			map.put(EntityUtil.getValue(keyAttr, entity), EntityUtil.getValue(valueAttr, entity));
		}
		return map;
	}

	public T find() {
		return callbacks.find(query);
	}
}
