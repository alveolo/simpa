package org.alveolo.simpa.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.ManagedType;

import org.alveolo.simpa.jdbc.QueryCallbacks;
import org.alveolo.simpa.metamodel.SingularAttributeImpl;
import org.alveolo.simpa.util.EntityUtil;


public abstract class BaseJunctionBuilder<B extends BaseJunctionBuilder<B, T>, T> extends BaseSelectableBuilder<T> {
	public BaseJunctionBuilder(QueryCallbacks callbacks, Query<T> query) {
		super(callbacks, query);
	}

	@SuppressWarnings("unchecked")
	private final B builder() {
		return (B) this;
	}

	protected abstract List<Condition> getConditions();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected JunctionBuilder builder(BaseJunctionBuilder builder, Junction junction) {
		getConditions().add(junction);
		return new JunctionBuilder(callbacks, builder, junction.conditions);
	}

	public B eq(String path, Object value) {
		return eq(getPath(path), value);
	}

	public <Y> B eq(Attribute<?, Y> attribute, Object value) {
		if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
			SingularAttributeImpl<?, Y> singular = (SingularAttributeImpl<?, Y>) attribute;
			ManagedType<Y> type = (ManagedType<Y>) singular.getType();

			@SuppressWarnings("unchecked")
			BaseJunctionBuilder<B, T> cjb = builder(this, new Conjunction());
			for (Attribute<? super Y, ?> a : type.getAttributes()) {
				cjb.eq(a, EntityUtil.getValue(a, value));
			}
		} else {
			getConditions().add(new AttrCondition("=", attribute, value));
		}

		return builder();
	}

	public B neq(String path, Object value) {
		return neq(getPath(path), value);
	}

	public <Y> B neq(Attribute<?, Y> attribute, Object value) {
		if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
			SingularAttributeImpl<?, Y> singular = (SingularAttributeImpl<?, Y>) attribute;
			ManagedType<Y> type = (ManagedType<Y>) singular.getType();

			@SuppressWarnings("unchecked")
			BaseJunctionBuilder<B, T> djb = builder(this, new Disjunction());
			for (Attribute<? super Y, ?> a : type.getAttributes()) {
				djb.neq(a, EntityUtil.getValue(a, value));
			}
		} else {
			getConditions().add(new AttrCondition("!=", attribute, value));
		}

		return builder();
	}

	public B gt(String path, Object value) {
		getConditions().add(new AttrCondition(">", getPath(path), value));
		return builder();
	}

	public B gte(String path, Object value) {
		getConditions().add(new AttrCondition(">=", getPath(path), value));
		return builder();
	}

	public B lt(String path, Object value) {
		getConditions().add(new AttrCondition("<", getPath(path), value));
		return builder();
	}

	public B lte(String path, Object value) {
		getConditions().add(new AttrCondition("<=", getPath(path), value));
		return builder();
	}

	public B like(String path, Object value) {
		getConditions().add(new AttrCondition(" LIKE ", getPath(path), value));
		return builder();
	}

	public B in(String path, Object... value) {
		return in(path, Arrays.asList(value));
	}

	public B in(String path, Collection<?> values) {
		getConditions().add(new InCondition(getPath(path), values));
		return builder();
	}

	public B isNull(String path) {
		getConditions().add(new IsNullCondition(getPath(path)));
		return builder();
	}

	public B isNotNull(String path) {
		getConditions().add(new IsNotNullCondition(getPath(path)));
		return builder();
	}

	public B raw(String sql, Object... values) {
		getConditions().add(new Raw(sql, values));
		return builder();
	}
}
