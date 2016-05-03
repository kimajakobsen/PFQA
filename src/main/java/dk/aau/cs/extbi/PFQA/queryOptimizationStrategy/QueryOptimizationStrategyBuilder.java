package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public class QueryOptimizationStrategyBuilder {
	private String type;
	
	public QueryOptimizationStrategyBuilder(String type) {
		this.type = type;
	}
	
	public QueryOptimizationStrategy build(ContextSet contextSetMinumum) {
		if (type.equals("FullMaterilization")) {
			return new FullMaterilization(contextSetMinumum);
		} else {
			throw new IllegalArgumentException("The strategy "+type+" is not known.");
		}
	}
}
