package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.tdb.TDBFactory;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;

public class QueryRewriting extends QueryOptimizationStrategy {

	private Logger logger;

	public QueryRewriting(ContextSet contextSetMinumum) {
		super(contextSetMinumum);
	}
	
	private Query createQuery(Query query) {
		Query newQuery = QueryFactory.create(query);
		logger = Logger.getInstance();
		
		logger.startPrepareOptimizationStrategy();
		for (String URI : contextSet.getValues()) {
			newQuery.addGraphURI(URI);
		}
		newQuery.addGraphURI(Config.getCubeInstanceGraphName());
		newQuery.addGraphURI(Config.getCubeStructureGraphName());
		logger.endPrepareOptimizationStrategy();
		return newQuery;
		
	}
	
	@Override
	public String execute(Query originalQuery) {
		Query modifiedQuery = createQuery(originalQuery);
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.READ) ;
		logger.startExecuteQuery();
		QueryExecution qexec = QueryExecutionFactory.create(modifiedQuery, dataset) ;
		ResultSet results = qexec.execSelect() ;
		logger.endExecuteQuery();
		
		dataset.commit();
		dataset.end();
		return ResultSetFormatter.asText(results);
	}
}
