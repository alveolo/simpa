package org.alveolo.simpa.query;

import java.util.ArrayList;
import java.util.Arrays;

import org.alveolo.simpa.jdbc.QueryCallbacks;


public class SelectBuilder<T> extends BaseBuilder<T> {
	public SelectBuilder(QueryCallbacks callbacks, Query<T> query) {
		super(callbacks, query);
	}

	@SuppressWarnings("unchecked")
	public <Y> BaseBuilder<Y> attribute(Class<Y> javaType, String path) {
		if (query.select != null) {
			throw new IllegalStateException("Selection attributes are already set");
		}

		query.select = Arrays.asList((Select) new AttrSelect(getPath(path)));

		return new BaseBuilder<>(callbacks, (Query<Y>) query);
	}

	public BaseBuilder<T> attributes(String... paths) {
		if (query.select != null) {
			throw new IllegalStateException("Selection attributes are already set");
		}

		query.select = new ArrayList<>(paths.length);
		for (String path : paths) {
			query.select.add(new AttrSelect(getPath(path)));
		}

		return new BaseBuilder<>(callbacks, query);
	}

	@SuppressWarnings("unchecked")
	public <Y> BaseBuilder<Y> raw(Class<Y> javaType, String sql) {
		query.select = Arrays.asList((Select) new RawSelect(javaType, sql));
		return new BaseBuilder<>(callbacks, (Query<Y>) query);
	}
}
