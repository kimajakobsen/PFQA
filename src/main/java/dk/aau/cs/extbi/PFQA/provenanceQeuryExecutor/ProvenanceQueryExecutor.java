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


public class ProvenanceQueryExecutor {

	public ContextSet getContext(Query pq) {
		ContextSet contextSet = new ContextSet();
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.READ) ;
		QueryExecution qexec = QueryExecutionFactory.create(pq, dataset) ;
		ResultSet results = qexec.execSelect() ;
		for ( ; results.hasNext() ; ) {
			  QuerySolution soln = results.nextSolution() ;
			  contextSet.add(soln.get("?s").toString());
			}
		
		dataset.commit();
		dataset.end();
		return contextSet;
	}

}
