package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import java.util.concurrent.TimeUnit;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;

public class InMemory extends QueryOptimizationStrategy {

	private Logger logger;

	public InMemory(ContextSet contextSetMinumum) {
		super(contextSetMinumum);
	}
		
	protected String createQuery() {
		String query = "" +
			"CONSTRUCT { " +
			" ?s ?p ?o . "+
			"} ";
		
		for (String URI : contextSet.getValues()) {
			query += "FROM <"+URI+"> ";
		}
		query += 
			"FROM <"+Config.getCubeInstanceGraphName()+"> " +
			"FROM <"+Config.getCubeStructureGraphName()+"> " +
			"WHERE { "+
			"?s ?p ?o"+
			"}";
		return query;
	}
	
	@Override
	public String execute(Query originalQuery) {
		logger = Logger.getInstance();
		logger.startPrepareOptimizationStrategy();
		
		Query fetchQuery = QueryFactory.create(createQuery());
		//System.out.println(modifiedQuery);
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		
		dataset.begin(ReadWrite.READ) ;
		
		QueryExecution qexec = QueryExecutionFactory.create(fetchQuery, dataset) ;
		qexec.setTimeout(Config.getTimeout(), TimeUnit.MINUTES);
		Model model = qexec.execConstruct();
		
		logger.endPrepareOptimizationStrategy();
		
		logger.startExecuteQuery();
		QueryExecution combinedqexec = QueryExecutionFactory.create(originalQuery, model) ;
		combinedqexec.setTimeout(Config.getTimeout(), TimeUnit.MINUTES);
		ResultSet results = combinedqexec.execSelect() ;
		String result = ResultSetFormatter.asText(results);
		dataset.end();
		logger.endExecuteQuery();
		
		return result;
	}
}
