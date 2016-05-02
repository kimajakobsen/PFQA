package dk.aau.cs.extbi.PFQA.helper;

import java.util.ArrayList;
import java.util.List;

public class ContextSet {
	List<String> contextValues = new ArrayList<String>();

	public ContextSet intersect(ContextSet contextSetPQ) {
		//System.out.println("AQ cv: "+ contextValues.size());
		//System.out.println("PQ cv: "+ contextSetPQ.getValues().size());
		contextValues.retainAll(contextSetPQ.getValues());
		//System.out.println("intersect: "+this.getValues().size());
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
