package org.alveolo.simpa.query;


public interface Select {
	void accept(SelectVisitor visitor);
}
