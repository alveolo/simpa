package org.alveolo.simpa.query;

import java.util.ArrayList;
import java.util.List;


public abstract class Junction implements Condition {
	public final List<Condition> conditions = new ArrayList<>();
}
