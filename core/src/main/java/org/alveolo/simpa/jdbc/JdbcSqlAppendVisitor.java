package org.alveolo.simpa.jdbc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.alveolo.simpa.metamodel.Attribute;
import org.alveolo.simpa.metamodel.Attribute.PersistentAttributeType;
import org.alveolo.simpa.metamodel.ManagedType;
import org.alveolo.simpa.metamodel.SingularAttribute;
import org.alveolo.simpa.query.AttrCondition;
import org.alveolo.simpa.query.AttrGroup;
import org.alveolo.simpa.query.AttrOrder;
import org.alveolo.simpa.query.AttrSelect;
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
import org.alveolo.simpa.query.RawSelect;
import org.alveolo.simpa.query.SelectVisitor;


public class JdbcSqlAppendVisitor implements SelectVisitor, ConditionVisitor, GroupVisitor, OrderVisitor {
	private final DefaultNaming naming = new DefaultNaming();

	private final StringBuilder sql;

	public JdbcSqlAppendVisitor(StringBuilder sql) {
		this.sql = sql;
	}

	public JdbcSqlAppendVisitor append(String str) {
		sql.append(str);
		return this;
	}

	@Override @SuppressWarnings({ "rawtypes", "unchecked" })
	public void visit(AttrSelect select) {
		Attribute<?, ?> attribute = select.attribute;

		if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
			SingularAttribute singular = (SingularAttribute) attribute;
			ManagedType type = (ManagedType) singular.getType();
			Set attributes = type.getAttributes();
			for (Iterator<Attribute> i = attributes.iterator(); i.hasNext();) {
				new AttrSelect(i.next()).accept(this);
				if (i.hasNext()) {
					sql.append(",");
				}
			}
		} else {
			sql.append(naming.getColumnName(attribute.getJavaMember()));
		}
	}

	@Override
	public void visit(RawSelect raw) {
		sql.append(raw.sql);
	}

	@Override
	public void visit(Conjunction conjunction) {
		append(conjunction, " AND ");
	}

	@Override
	public void visit(Disjunction disjunction) {
		append(disjunction, " OR ");
	}

	private void append(Junction junction, String join) {
		for (Iterator<Condition> i = junction.conditions.iterator(); i.hasNext();) {
			Condition c = i.next();

			boolean parens = (c instanceof Junction);

			if (parens) sql.append('(');
			c.accept(this);
			if (parens) sql.append(')');

			if (i.hasNext()) {
				sql.append(join);
			}
		}
	}

	@Override
	public void visit(AttrCondition ac) {
		sql.append(naming.getColumnName(ac.attribute.getJavaMember())).append(ac.op).append("?");
	}

	@Override
	public void visit(InCondition condition) {
		List<String> names = new ArrayList<>();
		collectColumnNames(names, condition.attribute);
		boolean parens = (names.size() > 1);

		if (parens) sql.append('(');
		for (Iterator<String> i = names.iterator(); i.hasNext();) {
			sql.append(i.next());
			if (i.hasNext()) {
				sql.append(',');
			}
		}
		if (parens) sql.append(')');

		sql.append(" IN (");

		for (Iterator<?> v = condition.values.iterator(); v.hasNext();) {
			v.next();

			if (parens) sql.append('(');
			for (Iterator<String> i = names.iterator(); i.hasNext();) {
				i.next();
				sql.append('?');
				if (i.hasNext()) {
					sql.append(',');
				}
			}
			if (parens) sql.append(')');

			if (v.hasNext()) {
				sql.append(',');
			}
		}

		sql.append(')');
	}

	private <Y> void collectColumnNames(List<String> names, Attribute<?, Y> attribute) {
		if (attribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
			SingularAttribute<?, Y> singular = (SingularAttribute<?, Y>) attribute;
			ManagedType<Y> type = (ManagedType<Y>) singular.getType();

			for (Attribute<? super Y, ?> a : type.getAttributes()) {
				collectColumnNames(names, a);
			}
		} else {
			names.add(naming.getColumnName(attribute.getJavaMember()));
		}
	}

	@Override
	public void visit(IsNullCondition condition) {
		sql.append(naming.getColumnName(condition.attribute.getJavaMember())).append(" IS NULL");
	}

	@Override
	public void visit(IsNotNullCondition condition) {
		sql.append(naming.getColumnName(condition.attribute.getJavaMember())).append(" IS NOT NULL");
	}

	@Override
	public void visit(AttrGroup ag) {
		sql.append(naming.getColumnName(ag.attribute.getJavaMember()));
	}

	@Override
	public void visit(AttrOrder ao) {
		sql.append(naming.getColumnName(ao.attribute.getJavaMember()));
		if (ao.reverse) {
			sql.append(" DESC");
		}
	}

	@Override
	public void visit(Raw rc) {
		sql.append(rc.sql);
	}
}
