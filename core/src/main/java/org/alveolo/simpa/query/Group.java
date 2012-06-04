package org.alveolo.simpa.query;


public interface Group {
	void accept(GroupVisitor visitor);
}
