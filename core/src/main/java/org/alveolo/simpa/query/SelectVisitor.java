package org.alveolo.simpa.query;


public interface SelectVisitor {
	void visit(AttrSelect attr);
	void visit(RawSelect raw);
}
