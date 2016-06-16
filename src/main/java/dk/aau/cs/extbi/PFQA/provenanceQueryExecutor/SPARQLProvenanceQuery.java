package dk.aau.cs.extbi.PFQA.provenanceQueryExecutor;

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

public class SPARQLProvenanceQuery extends ProvenanceQuery {

	private Query query;

	public SPARQLProvenanceQuery(String name, Query query){
		super(name);
		this.query = query;
	}
	
	public ContextSet execute() {
		Logger logger = Logger.getInstance();
		ContextSet contextSet = new ContextSet();
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.READ) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset) ;
		
		logger.startProvenanceQueryExecution();
		ResultSet results = qexec.execSelect() ;
		
		for ( ; results.hasNext() ; ) {
			  QuerySolution soln = results.nextSolution() ;
			  contextSet.add(soln.get("?s").toString());
			}
		logger.endProvenanceQueryExecution();
		
		dataset.commit();
		dataset.end();
		return contextSet;
	}
}
