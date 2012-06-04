package org.alveolo.simpa.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.persistence.PersistenceException;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.ManagedType;

import org.alveolo.simpa.metamodel.SingularAttributeImpl;
import org.alveolo.simpa.query.AttrCondition;
import org.alveolo.simpa.query.AttrGroup;
import org.alveolo.simpa.query.AttrOrder;
import org.alveolo.simpa.query.Condition;
import org.alveolo.simpa.query.ConditionVisitor;
import org.alveolo.simpa.query.Conjunction;
import org.alveolo.simpa.query.Disjunction;
import org.alveolo.simpa.query.GroupVisitor;
import org.alveolo.simpa.query.InCondition;
import org.alveolo.simpa.query.IsNotNullCondition;
import org.alveolo.simpa.query.IsNullCondition;
import org.alveolo.simpa.query.Junction;
import org.alveolo.simpa.query.OrderVisitor;
import org.alveolo.simpa.query.Raw;
import org.alveolo.simpa.util.EntityUtil;


public class JdbcSetParameterVisitor implements ConditionVisitor, GroupVisitor, OrderVisitor  {
	private final PreparedStatement stmt;

	private int index = 0;

	public JdbcSetParameterVisitor(PreparedStatement stmt) {
		this.stmt = stmt;
	}

	@Override
	public void visit(Conjunction conjunction) {
		visit0(conjunction);
	}

	@Override
	public void visit(Disjunction disjunction) {
		visit0(disjunction);
	}

	private void visit0(Junction junction) {
		for (Condition c : junction.conditions) {
			c.accept(this);
		}
	}

	public void visit(AttributeValue av) {
		setParameter(av.attribute, av.value);
	}

	@Override
	public void visit(AttrCondition ac) {
		setParameter(ac.attribute, ac.value);
	}

	@Override
	public void visit(InCondition condition) {
		for (Object value : condition.values) {
			setParameter(condition.attribute, value);
		}
	}

	private <Y> void setParameter(Attribute<?, Y> attribute, Object value) {
		try {
			if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
				SingularAttributeImpl<?, Y> singular = (SingularAttributeImpl<?, Y>) attribute;
				ManagedType<Y> type = (ManagedType<Y>) singular.getType();

				for (Attribute<? super Y, ?> a : type.getAttributes()) {
					setParameter(a, EntityUtil.getValue(a, value));
				}
			} else {
				JdbcUtil.setParameter(stmt, ++index, attribute, value);
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public void visit(IsNullCondition condition) {}

	@Override
	public void visit(IsNotNullCondition condition) {}

	@Override
	public void visit(AttrGroup ag) {}

	@Override
	public void visit(AttrOrder ao) {}

	@Override
	public void visit(Raw rc) {
		try {
			Object[] values = rc.values;
			if (values != null) {
				for (Object value : values) {
					JdbcUtil.setParameter(stmt, ++index, null, value);
				}
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}
