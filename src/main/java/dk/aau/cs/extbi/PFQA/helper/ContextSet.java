package dk.aau.cs.extbi.PFQA.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.aau.cs.extbi.PFQA.logger.Logger;

public class ContextSet {
	List<String> contextValues = new ArrayList<String>();

	public void add(String contextValue) {
		contextValues.add(contextValue);
	}

	public void add(ContextSet contextSet) {
		contextValues.addAll(contextSet.getValues());
	}
	
	public List<String> getValues() {
		return contextValues;
	}

	public ContextSet intersect(ContextSet contextSetPQ) {
		Logger logger = Logger.getInstance();
		
		logger.startIntersectContextSet();
		List<String> intersection = new ArrayList<String>();
		HashMap<String,Boolean> map = new HashMap<String, Boolean>();
		if (contextValues.size() > contextSetPQ.getValues().size()) {
			for (String string : contextSetPQ.getValues()) {
				map.put(string, true);
			}
			for (String string : contextValues) {
				if (map.containsKey(string)) {
					intersection.add(string);
				}
			}
			
		} else {
			for (String string : contextValues) {
				map.put(string, true);
			}
			for (String string : contextSetPQ.getValues()) {
				if (map.containsKey(string)) {
					intersection.add(string);
				}
			}
		}
		
		contextValues = intersection;
		logger.endIntersectContextSet();
		
		return this;
	}

}
