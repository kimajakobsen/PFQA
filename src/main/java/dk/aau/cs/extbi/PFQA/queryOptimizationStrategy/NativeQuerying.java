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
import org.apache.jena.tdb.TDBFactory;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;

public class NativeQuerying extends QueryOptimizationStrategy {

	private Logger logger;

	public NativeQuerying(ContextSet contextSetMinumum) {
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
		//System.out.println(modifiedQuery);
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		logger.startExecuteQuery();
		
		dataset.begin(ReadWrite.READ) ;
		
		QueryExecution qexec = QueryExecutionFactory.create(modifiedQuery, dataset) ;
		qexec.setTimeout(Config.getTimeout(), TimeUnit.MINUTES);
		
		ResultSet results = qexec.execSelect() ;
		String result = ResultSetFormatter.asText(results);
		dataset.end();
		
		logger.endExecuteQuery();
		return result;
	}
}
