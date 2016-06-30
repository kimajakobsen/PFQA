package dk.aau.cs.extbi.PFQA.helper;

import java.util.HashSet;

import dk.aau.cs.extbi.PFQA.logger.Logger;

public class ContextSet {
	HashSet<String> contextValues = new HashSet<String>();

	public void add(String contextValue) {
		contextValues.add(contextValue);
	}

	public void add(ContextSet contextSet) {
		contextValues.addAll(contextSet.getValues());
	}
	
	public HashSet<String> getValues() {
		return contextValues;
	}

	public ContextSet intersect(ContextSet contextSetPQ) {
		Logger logger = Logger.getInstance();
		
		
		logger.startIntersectContextSet();
		HashSet<String> intersection = new HashSet<String>();
		intersection.addAll(contextValues);
		intersection.retainAll(contextSetPQ.getValues());
		
		contextValues = intersection;
		logger.endIntersectContextSet();	
		
		return this;
	}
}
