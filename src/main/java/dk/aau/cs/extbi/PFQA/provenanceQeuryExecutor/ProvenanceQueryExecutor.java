package dk.aau.cs.extbi.PFQA.provenanceQeuryExecutor;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.tdb.TDBFactory;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;


public class ProvenanceQueryExecutor {

	public ContextSet getContext(Query pq) {
		Logger logger = Logger.getInstance();
		ContextSet contextSet = new ContextSet();
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.READ) ;
		QueryExecution qexec = QueryExecutionFactory.create(pq, dataset) ;
		
		logger.startProvenanceQueryExecution();
		ResultSet results = qexec.execSelect() ;
		logger.endProvenanceQueryExecution();
		
		for ( ; results.hasNext() ; ) {
			  QuerySolution soln = results.nextSolution() ;
			  contextSet.add(soln.get("?s").toString());
			}
		
		dataset.commit();
		dataset.end();
		return contextSet;
	}

}
