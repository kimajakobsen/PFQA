package dk.aau.cs.extbi.PFQA;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndex;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndexBuilder;
import dk.aau.cs.extbi.PFQA.provenanceQeuryExecutor.ProvenanceQueryExecutor;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategy;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategyBuilder;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;

public class App 
{
	public static void main( String[] args )
    {
        Query aq = QueryFactory.read("src/test/resources/analyticalQueries/query1.sparql");
        QueryProfile qp = new QueryProfile(aq);

        ProvenanceIndexBuilder provenanceIndexBuilder = new ProvenanceIndexBuilder("contextTreeIndex.ser");
        ProvenanceIndex pi = provenanceIndexBuilder.build(); 
		ContextSet contextSetAP = pi.getContext(qp);	
		
		Query pq = QueryFactory.read("src/test/resources/provenanceQueries/allContexts.sparql");
		ProvenanceQueryExecutor provenaceQueryExecutor = new ProvenanceQueryExecutor();
		ContextSet contextSetPQ = provenaceQueryExecutor.getContext(pq);
        
		ContextSet contextSetMinumum = contextSetAP.intersect(contextSetPQ);
		
		QueryOptimizationStrategyBuilder queryOptimizerStrategyBuilder = new QueryOptimizationStrategyBuilder("FullMaterilization");
		QueryOptimizationStrategy strategy = queryOptimizerStrategyBuilder.build(contextSetMinumum);
		ResultSet result =  strategy.execute(aq);
		ResultSetFormatter.out(result);
    }
}
