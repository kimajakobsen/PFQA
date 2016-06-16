package dk.aau.cs.extbi.PFQA.provenanceQueryExecutor;

import org.apache.jena.query.Query;

public class ProvenanceQueryBuilder {

	public static ProvenanceQuery build(String name, Query query) {
		return new SPARQLProvenanceQuery(name, query);
	}
	
	public static ProvenanceQuery build(String filename) {
		return new ContextValues(filename);
	}

}
