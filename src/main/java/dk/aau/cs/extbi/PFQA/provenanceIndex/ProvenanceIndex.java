package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public abstract class ProvenanceIndex implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public abstract ContextSet getContext(SimpleEntry<String, Query> analyticalQuery, ContextSet contextSetPQ);
}
