package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;

public abstract class QueryOptimizationStrategy {

	public abstract ResultSet execute(Query aq) ;

}
