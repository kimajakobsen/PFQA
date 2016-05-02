package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public abstract class QueryOptimizationStrategy {

	protected ContextSet contextSet;

	public abstract ResultSet execute(Query aq) ;
	
	public ContextSet getContextSet() {
		return contextSet;
	}

	public void setContextSet(ContextSet contextSet) {
		this.contextSet = contextSet;
	}


}
