package org.alveolo.simpa.query;


public class Disjunction extends Junction {
	@Override
	public void accept(ConditionVisitor visitor) {
		visitor.visit(this);
	}
}
