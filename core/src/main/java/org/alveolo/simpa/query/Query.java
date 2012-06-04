package org.alveolo.simpa.query;

import java.util.ArrayList;
import java.util.List;


public class Query<T> {
	public final Class<T> type;
	public final Conjunction where = new Conjunction();
	public final List<Group> groups = new ArrayList<>();
	public final Conjunction having = new Conjunction();
	public final List<Order> order = new ArrayList<>();

	/** If only one attribute or a few needs to be selected, null when selecting whole entity */
	public List<Select> select;

	public Integer offset;
	public Integer fetch;

	public Query(Class<T> type) {
		this.type = type;
	}
}
