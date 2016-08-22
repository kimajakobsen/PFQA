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
		ContextSet contextSetMinumum = pi.getContext(analyticalQuery,contextSetPQ);
		
		if (type.equals("FullMaterialization")) {
			return new FullMaterialization(contextSetMinumum);
			
		} else if (type.equals("QueryRewriting")) {
			return new NativeQuerying(contextSetMinumum);
			
		} else if (type.equals("FullMaterializationU")) {
			return new FullMaterializationUnion(contextSetMinumum);
			
		} else if (type.equals("InMemory")) {
			return new InMemory(contextSetMinumum);
			
		}else {
			throw new IllegalArgumentException("The strategy "+type+" is not known.");
		}
	}
}
