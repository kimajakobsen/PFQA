package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public abstract class QueryOptimizationStrategy {

	protected ContextSet contextSet;

	public QueryOptimizationStrategy(ContextSet contextSetMinumum) {
		this.contextSet = contextSetMinumum;
	}

	public abstract ResultSet execute(Query aq) ;
	
	public ContextSet getContextSet() {
		return contextSet;
	}
}
