package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndex;

public class QueryOptimizationStrategyBuilder {
	private String type;
	private SimpleEntry<String, Query> analyticalQuery;
	private ProvenanceIndex pi;
	
	public QueryOptimizationStrategyBuilder(String type, SimpleEntry<String, Query> analyticalQuery2, ProvenanceIndex pi) {
		this.type = type;
		this.analyticalQuery = analyticalQuery2;
		this.pi = pi;
	}
	
	public QueryOptimizationStrategy build(ContextSet contextSetPQ) {
		if (type.equals("FullMaterialization")) {
			
			ContextSet contextSetMinumum = pi.getContext(analyticalQuery,contextSetPQ);
			
			return new FullMaterialization(contextSetMinumum);
		} else if (type.equals("QueryRewriting")) {
			
			ContextSet contextSetMinumum = pi.getContext(analyticalQuery,contextSetPQ);
			
			return new NativeQuerying(contextSetMinumum);
		} else {
			throw new IllegalArgumentException("The strategy "+type+" is not known.");
		}
	}
}
