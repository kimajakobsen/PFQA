package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndex;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndexBuilder;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;

public class QueryOptimizationStrategyBuilder {
	private String type;
	private Query analyticalQuery;
	private String provenanceIndex;
	
	public QueryOptimizationStrategyBuilder(String type, Query analyticalQuery, String provenanceIndex) {
		this.type = type;
		this.analyticalQuery = analyticalQuery;
		this.provenanceIndex = provenanceIndex;
	}
	
	public QueryOptimizationStrategy build(ContextSet contextSetPQ) {
		if (type.equals("FullMaterilization")) {
			ProvenanceIndexBuilder provenanceIndexBuilder = new ProvenanceIndexBuilder(provenanceIndex);
			ProvenanceIndex pi = provenanceIndexBuilder.build(); 
			
			QueryProfile queryProfile = new QueryProfile(analyticalQuery);
			
			ContextSet contextSetAP = pi.getContext(queryProfile);
			ContextSet contextSetMinumum = contextSetAP.intersect(contextSetPQ);
			
			return new FullMaterilization(contextSetMinumum);
		} else {
			throw new IllegalArgumentException("The strategy "+type+" is not known.");
		}
	}
}
