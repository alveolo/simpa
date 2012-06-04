package org.alveolo.simpa.query;


public interface GroupVisitor {
	void visit(AttrGroup ag);
	void visit(Raw condition);
}
