package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndex;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndexBuilder;

public class QueryOptimizationStrategyBuilder {
	private String type;
	private SimpleEntry<String, Query> analyticalQuery;
	private String provenanceIndex;
	
	public QueryOptimizationStrategyBuilder(String type, SimpleEntry<String, Query> analyticalQuery2, String provenanceIndex) {
		this.type = type;
		this.analyticalQuery = analyticalQuery2;
		this.provenanceIndex = provenanceIndex;
	}
	
	public QueryOptimizationStrategy build(ContextSet contextSetPQ) {
		if (type.equals("FullMaterilization")) {
			ProvenanceIndexBuilder provenanceIndexBuilder = new ProvenanceIndexBuilder(provenanceIndex);
			ProvenanceIndex pi = provenanceIndexBuilder.build(); 
			
			ContextSet contextSetMinumum = pi.getContext(analyticalQuery,contextSetPQ);
			
			return new FullMaterilization(contextSetMinumum);
		} else if (type.equals("QueryRewriting")) {
			ProvenanceIndexBuilder provenanceIndexBuilder = new ProvenanceIndexBuilder(provenanceIndex);
			ProvenanceIndex pi = provenanceIndexBuilder.build(); 
			
			ContextSet contextSetMinumum = pi.getContext(analyticalQuery,contextSetPQ);
			
			return new QueryRewriting(contextSetMinumum);
		} else {
			throw new IllegalArgumentException("The strategy "+type+" is not known.");
		}
	}
}
