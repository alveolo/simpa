package org.alveolo.simpa.query;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.ManagedType;

import org.alveolo.simpa.jdbc.QueryCallbacks;
import org.alveolo.simpa.metamodel.SingularAttributeImpl;


public class OrderBuilder<T> extends BaseSelectableBuilder<T> {
	public OrderBuilder(QueryCallbacks callbacks, Query<T> query) {
		super(callbacks, query);
	}

	public OrderBuilder<T> asc(String path) {
		return group(getPath(path), false);
	}

	public OrderBuilder<T> desc(String path) {
		return group(getPath(path), true);
	}

	private <Y> OrderBuilder<T> group(Attribute<?, Y> attribute, boolean reverse) {
		if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
			SingularAttributeImpl<?, Y> singular = (SingularAttributeImpl<?, Y>) attribute;
			ManagedType<Y> type = (ManagedType<Y>) singular.getType();

			for (Attribute<? super Y, ?> a : type.getAttributes()) {
				group(a, reverse);
			}
		} else {
			query.order.add(new AttrOrder(attribute, reverse));
		}

		return this;
	}

	public OrderBuilder<T> raw(String sql, Object... values) {
		query.order.add(new Raw(sql, values));
		return this;
	}
}
