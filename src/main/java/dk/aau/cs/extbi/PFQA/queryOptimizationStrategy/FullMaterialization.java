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

public class FullMaterialization extends QueryOptimizationStrategy {
	private String modelName = Config.getNamespace()+"fullMaterilized";
	private Logger logger;

	public FullMaterialization(ContextSet contextSetMinumum) {
		super(contextSetMinumum);
		
		logger = Logger.getInstance();
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.WRITE) ;
		logger.startPrepareOptimizationStrategy();
		
		String queryString = createQuery();
		
		Query query = QueryFactory.create(queryString) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset) ;
		Model resultModel = qexec.execConstruct() ;
		qexec.close() ;
		
		dataset.addNamedModel(modelName, resultModel);
		
		dataset.commit();
		dataset.end();
		logger.endPrepareOptimizationStrategy();
	}
	
	private String createQuery() {
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
	public String execute(Query aq) {
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		logger.startExecuteQuery();

		dataset.begin(ReadWrite.READ) ;
		Model model = dataset.getNamedModel(modelName);
		QueryExecution qexec = QueryExecutionFactory.create(aq, model) ;
		qexec.setTimeout(Config.getTimeout(), TimeUnit.MINUTES);
		ResultSet results = qexec.execSelect() ;
		String result = ResultSetFormatter.asText(results);
		dataset.end();

		logger.endExecuteQuery();
		return result;
	}
}
