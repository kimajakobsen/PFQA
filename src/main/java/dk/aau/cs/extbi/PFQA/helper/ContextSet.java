package dk.aau.cs.extbi.PFQA.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.rdf.model.Resource;

public class ContextSet {
	List<Resource> contextValues = new ArrayList<Resource>();

	public ContextSet intersect(ContextSet contextSetPQ) {
		throw new NotImplementedException("");
	}

	public void add(Resource contextValue) {
		contextValues.add(contextValue);
	}

	public void add(ContextSet contextSet) {
		contextValues.addAll(contextSet.getValues());
	}
	
	public List<Resource> getValues() {
		return contextValues;
	}

}
