package org.alveolo.simpa.query;

import org.alveolo.simpa.jdbc.QueryCallbacks;


public class BaseSelectableBuilder<T> extends BaseBuilder<T> {
	public BaseSelectableBuilder(QueryCallbacks callbacks, Query<T> query) {
		super(callbacks, query);
	}

	public SelectBuilder<T> select() {
		return new SelectBuilder<>(callbacks, query);
	}
}
