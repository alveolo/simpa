package org.alveolo.simpa.query;

import org.alveolo.simpa.jdbc.QueryCallbacks;
import org.alveolo.simpa.metamodel.Attribute;
import org.alveolo.simpa.metamodel.Attribute.PersistentAttributeType;
import org.alveolo.simpa.metamodel.ManagedType;
import org.alveolo.simpa.metamodel.SingularAttribute;


public class GroupBuilder<T> extends BaseSelectableBuilder<T> {
	public GroupBuilder(QueryCallbacks callbacks, Query<T> query) {
		super(callbacks, query);
	}

	public GroupBuilder<T> by(String path) {
		return group(getPath(path));
	}

	private <Y> GroupBuilder<T> group(Attribute<?, Y> attribute) {
		if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
			SingularAttribute<?, Y> singular = (SingularAttribute<?, Y>) attribute;
			ManagedType<Y> type = (ManagedType<Y>) singular.getType();

			for (Attribute<? super Y, ?> a : type.getAttributes()) {
				group(a);
			}
		} else {
			query.groups.add(new AttrGroup(attribute));
		}

		return this;
	}

	public GroupBuilder<T> raw(String sql, Object... values) {
		query.groups.add(new Raw(sql, values));
		return this;
	}

	public HavingBuilder<T> having() {
		return new HavingBuilder<>(callbacks, query);
	}

	public OrderBuilder<T> order() {
		return new OrderBuilder<>(callbacks, query);
	}
}
