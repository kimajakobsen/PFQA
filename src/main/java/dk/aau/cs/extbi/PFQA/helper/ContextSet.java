package dk.aau.cs.extbi.PFQA.helper;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.extbi.PFQA.logger.Logger;

public class ContextSet {
	List<String> contextValues = new ArrayList<String>();

	public ContextSet intersect(ContextSet contextSetPQ) {
		Logger logger = Logger.getInstance();
		
		logger.startIntersectContextSet();
		contextValues.retainAll(contextSetPQ.getValues());
		logger.endIntersectContextSet();
		
		return this;
	}

	public void add(String contextValue) {
		contextValues.add(contextValue);
	}

	public void add(ContextSet contextSet) {
		contextValues.addAll(contextSet.getValues());
	}
	
	public List<String> getValues() {
		return contextValues;
	}

}
