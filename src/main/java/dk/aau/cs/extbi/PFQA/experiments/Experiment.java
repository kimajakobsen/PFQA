package dk.aau.cs.extbi.PFQA.experiments;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;
import dk.aau.cs.extbi.PFQA.provenanceQeuryExecutor.ProvenanceQueryExecutor;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategy;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategyBuilder;

public class Experiment {
	private ArrayList<Query> analyticalQueries = new ArrayList<Query>();
	private ArrayList<Query> provenanceQueries = new ArrayList<Query>();
	private ArrayList<String> optimizationStrategies = new ArrayList<String>();
	private ArrayList<String> provenanceIndices = new ArrayList<String>();
	
	public Experiment(ArrayList<Query> analyticalQueries,
			ArrayList<Query> provenanceQueries,
			ArrayList<String> optimizationStrategies,
			ArrayList<String> provenanceIndices) {
		this.analyticalQueries = analyticalQueries;
		this.provenanceQueries = provenanceQueries;
		this.optimizationStrategies = optimizationStrategies;
		this.provenanceIndices = provenanceIndices;
	}

	public void run() {
		Logger logger = Logger.getInstance();
		
		for (Query analyticalQuery : analyticalQueries) {
			logger.startAnalyticalQueryContex(analyticalQuery);
			
			for (Query provenanceQuery : provenanceQueries) {
				logger.startProvenanceQueryContext(provenanceQuery);
				ProvenanceQueryExecutor provenaceQueryExecutor = new ProvenanceQueryExecutor();
				ContextSet contextSetPQ = provenaceQueryExecutor.getContext(provenanceQuery);
				
				for (String strategyString : optimizationStrategies) {
					logger.startOptimizationStrategyContext(strategyString);
					
					for (String index : provenanceIndices) {
						logger.startProvenanceIndexContext(index);
						QueryOptimizationStrategyBuilder queryOptimizerStrategyBuilder = new QueryOptimizationStrategyBuilder(strategyString,analyticalQuery, index);
						QueryOptimizationStrategy strategy = queryOptimizerStrategyBuilder.build(contextSetPQ);
						ResultSet result =  strategy.execute(analyticalQuery);
						logger.setResult(result);
						logger.commitResult();
					}
				}
			}
		}
		logger.printToSystemOut();
	}
}
