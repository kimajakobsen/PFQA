package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndex;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndexBuilder;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;

public class QueryOptimizationStrategyBuilder {
	private String type;
	private QueryProfile queryProfile;
	private String provenanceIndex;
	
	public QueryOptimizationStrategyBuilder(String type, QueryProfile queryProfile, String provenanceIndex) {
		this.type = type;
		this.queryProfile = queryProfile;
		this.provenanceIndex = provenanceIndex;
	}
	
	public QueryOptimizationStrategy build(ContextSet contextSetPQ) {
		if (type.equals("FullMaterilization")) {
			ProvenanceIndexBuilder provenanceIndexBuilder = new ProvenanceIndexBuilder(provenanceIndex);
			ProvenanceIndex pi = provenanceIndexBuilder.build(); 
			
			ContextSet contextSetAP = pi.getContext(queryProfile);
			ContextSet contextSetMinumum = contextSetAP.intersect(contextSetPQ);
			
			return new FullMaterilization(contextSetMinumum);
		} else {
			throw new IllegalArgumentException("The strategy "+type+" is not known.");
		}
	}
}
