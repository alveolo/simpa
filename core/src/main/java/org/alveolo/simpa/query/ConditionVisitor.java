package org.alveolo.simpa.query;


public interface ConditionVisitor {
	void visit(Conjunction conjunction);
	void visit(Disjunction disjunction);
	void visit(AttrCondition condition);
	void visit(InCondition condition);
	void visit(IsNullCondition condition);
	void visit(IsNotNullCondition condition);
	void visit(Raw condition);
}
