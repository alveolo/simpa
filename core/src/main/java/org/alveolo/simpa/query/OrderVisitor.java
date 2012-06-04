package org.alveolo.simpa.query;


public interface OrderVisitor {
	void visit(AttrOrder ao);
	void visit(Raw condition);
}
