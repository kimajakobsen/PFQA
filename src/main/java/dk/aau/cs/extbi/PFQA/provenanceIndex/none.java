package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public class none extends ProvenanceIndex {

	private static final long serialVersionUID = 1L;

	@Override
	public ContextSet getContext(SimpleEntry<String, Query> aq, ContextSet contextSetPQ) {
		return contextSetPQ;
	}
}
