package org.alveolo.simpa.query;


public class Conjunction extends Junction {
	@Override
	public void accept(ConditionVisitor visitor) {
		visitor.visit(this);
	}
}
