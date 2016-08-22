package dk.aau.cs.extbi.PFQA.experiments;

import java.time.Duration;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryCancelledException;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndex;
import dk.aau.cs.extbi.PFQA.provenanceIndex.ProvenanceIndexBuilder;
import dk.aau.cs.extbi.PFQA.provenanceQueryExecutor.ProvenanceQuery;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategy;
import dk.aau.cs.extbi.PFQA.queryOptimizationStrategy.QueryOptimizationStrategyBuilder;

public class Experiment {
	private ArrayList<SimpleEntry<String, Query>> analyticalQueries = new ArrayList<SimpleEntry<String, Query>>();
	private ArrayList<ProvenanceQuery> provenanceQueries = new ArrayList<ProvenanceQuery>();
	private ArrayList<String> optimizationStrategies = new ArrayList<String>();
	private ArrayList<String> provenanceIndices = new ArrayList<String>();
	private ArrayList<SimpleEntry<String, String>> datasets;
	private int numberOfExperimentRuns;
	private ProvenanceIndex pi;
	private ArrayList<String> ignoreStrategies;
	
	public Experiment(ArrayList<SimpleEntry<String, Query>> analyticalQueries,
			ArrayList<ProvenanceQuery> provenanceQueries,
			ArrayList<String> optimizationStrategies,
			ArrayList<String> provenanceIndices, 
			ArrayList<SimpleEntry<String, String>> datasets, 
			int numberOfExperimentRuns, 
			ArrayList<String> ignoreStrategies) {
		this.analyticalQueries = analyticalQueries;
		this.provenanceQueries = provenanceQueries;
		this.optimizationStrategies = optimizationStrategies;
		this.provenanceIndices = provenanceIndices;
		this.datasets = datasets;
		this.numberOfExperimentRuns = numberOfExperimentRuns;
		this.ignoreStrategies = ignoreStrategies;
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
					
					for (ProvenanceQuery provenanceQuery : provenanceQueries) {
						logger.startProvenanceQueryContext(provenanceQuery);
						deleteFullyMaterializedCube();
						
						for (String strategyString : optimizationStrategies) {
							logger.startOptimizationStrategyContext(strategyString);
						
							if (isStrategyIndexCombinationLegal(strategyString,index)) {
								Config.setStrategyName(strategyString+index);
								
								try {
									for (int i = 0; i < numberOfExperimentRuns; i++) {
										logger.startExperimentRun(i+1);
										Instant start = Instant.now();
										
										ContextSet contextSetPQ = provenanceQuery.execute();
										
										QueryOptimizationStrategyBuilder queryOptimizerStrategyBuilder = new QueryOptimizationStrategyBuilder(strategyString,analyticalQuery, pi);
										QueryOptimizationStrategy strategy = queryOptimizerStrategyBuilder.build(contextSetPQ);
									
										String result =  strategy.execute(analyticalQuery.getValue());
										logger.setResult(result);
										Duration brutoTime = Duration.between(start, Instant.now());
										int[] time = splitToComponentTimes(brutoTime.getSeconds());
										System.out.println("executing: "+strategyString+index+" #"+ (i+1) +" AQ: "+analyticalQuery.getKey()+" PQ:"+provenanceQuery.getName()+" on "+dataset.getKey()+ " BrutoTime: "+time[0]+":"+time[1]+":"+time[2] );
										System.out.println(result);
										logger.commitResult();
									}
								} catch (QueryCancelledException e) {
									System.out.println("executing: "+strategyString+index +" AQ: "+analyticalQuery.getKey()+" PQ:"+provenanceQuery.getName()+" on "+dataset.getKey()+ " Timedout after "+ Config.getTimeout() + " minutes");
								}
							}
						}
					}
				}
				logger.commitStartupTime();
			}
		}
		if (Config.isWriteToDatabase()) {
			logger.writeToDB();
		} else {
			logger.writeInsertStatementsToFile();
		}
		
	}

	private void deleteFullyMaterializedCube() {
		Dataset dataset = TDBFactory.createDataset(Config.getDatasetLocation()) ;
		dataset.begin(ReadWrite.WRITE) ;
    	UpdateRequest request = UpdateFactory.create() ;
    	request.add("DROP GRAPH <http://example.com/fullMaterilized>") ;

    	// And perform the operations.
    	UpdateAction.execute(request, dataset) ;
    	dataset.commit();
	}

	public boolean isStrategyIndexCombinationLegal(String strategyString, String index) {
		if (ignoreStrategies.contains(strategyString+index)) {
			return false;
		} else {
			return true;
		}
	}
	
	public int[] splitToComponentTimes(long l)
	{
	    int hours = (int) l / 3600;
	    int remainder = (int) l - hours * 3600;
	    int mins = remainder / 60;
	    remainder = remainder - mins * 60;
	    int secs = remainder;

	    int[] ints = {hours , mins , secs};
	    return ints;
	}
}
