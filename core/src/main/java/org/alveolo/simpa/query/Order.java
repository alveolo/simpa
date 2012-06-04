package org.alveolo.simpa.query;


public interface Order {
	void accept(OrderVisitor visitor);
}
