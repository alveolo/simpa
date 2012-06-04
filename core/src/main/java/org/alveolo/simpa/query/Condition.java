package org.alveolo.simpa.query;


public interface Condition {
	void accept(ConditionVisitor visitor);
}
