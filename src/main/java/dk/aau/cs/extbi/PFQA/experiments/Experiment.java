package dk.aau.cs.extbi.PFQA.experiments;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndex;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndexBuilder;
import dk.aau.cs.extbi.PFQA.provenanceQeuryExecutor.ProvenanceQueryExecutor;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategy;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategyBuilder;

public class Experiment {
	private ArrayList<SimpleEntry<String, Query>> analyticalQueries = new ArrayList<SimpleEntry<String, Query>>();
	private ArrayList<SimpleEntry<String, Query>> provenanceQueries = new ArrayList<SimpleEntry<String, Query>>();
	private ArrayList<String> optimizationStrategies = new ArrayList<String>();
	private ArrayList<String> provenanceIndices = new ArrayList<String>();
	private ArrayList<SimpleEntry<String, String>> datasets;
	private int numberOfExperimentRuns;
	private ProvenanceIndex pi;
	
	public Experiment(ArrayList<SimpleEntry<String, Query>> analyticalQueries2,
			ArrayList<SimpleEntry<String, Query>> provenanceQueries2,
			ArrayList<String> optimizationStrategies,
			ArrayList<String> provenanceIndices, 
			ArrayList<SimpleEntry<String, String>> datasets, 
			int numberOfExperimentRuns) {
		this.analyticalQueries = analyticalQueries2;
		this.provenanceQueries = provenanceQueries2;
		this.optimizationStrategies = optimizationStrategies;
		this.provenanceIndices = provenanceIndices;
		this.datasets = datasets;
		this.numberOfExperimentRuns = numberOfExperimentRuns;
	}

	public void run() {
		Logger logger = Logger.getInstance();
		
		for (SimpleEntry<String, String> dataset : datasets) {
			logger.startDataset(dataset);
			Config.setDatasetLocation(dataset.getValue());
			
			for (String index : provenanceIndices) {
				logger.startProvenanceIndexContext(index);
				
				ProvenanceIndexBuilder provenanceIndexBuilder = new ProvenanceIndexBuilder(index);
				pi = provenanceIndexBuilder.build(); 
		
				for (SimpleEntry<String, Query> analyticalQuery : analyticalQueries) {
					logger.startAnalyticalQueryContex(analyticalQuery);
					
					for (SimpleEntry<String, Query> provenanceQuery : provenanceQueries) {
						logger.startProvenanceQueryContext(provenanceQuery);
						
						for (String strategyString : optimizationStrategies) {
							logger.startOptimizationStrategyContext(strategyString);
						
							if (isStrategyIndexCombinationLegal(strategyString,index)) {
								Config.setStrategyName(strategyString+index);
								
								for (int i = 0; i < numberOfExperimentRuns; i++) {
									logger.startExperimentRun(i+1);
									
									ProvenanceQueryExecutor provenaceQueryExecutor = new ProvenanceQueryExecutor();
									ContextSet contextSetPQ = provenaceQueryExecutor.getContext(provenanceQuery.getValue());
									
									QueryOptimizationStrategyBuilder queryOptimizerStrategyBuilder = new QueryOptimizationStrategyBuilder(strategyString,analyticalQuery, pi);
									QueryOptimizationStrategy strategy = queryOptimizerStrategyBuilder.build(contextSetPQ);
									String result =  strategy.execute(analyticalQuery.getValue());
									logger.setResult(result);
									logger.commitResult();
								}
							}
						}
					}
				}
				logger.commitStartupTime();
			}
		}
		//logger.printToSystemOut();
		//logger.printToFile();
		logger.writeToDB();
	}

	public boolean isStrategyIndexCombinationLegal(String strategyString, String index) {
		if (strategyString.equals("Basic") && !index.equals("none")) {
			return false;
		} else {
			return true;
		}
	}
}
