package dk.aau.cs.extbi.PFQA.logger;

import java.util.ArrayList;

import org.apache.jena.query.ResultSetFormatter;

public class ExperimentResults {
	private ArrayList<AnalyticalQueryResult> results = new ArrayList<AnalyticalQueryResult>();
	
	public void add(AnalyticalQueryResult result) {
		results.add(result);
	}
	
	public void printToFile() {
		
	}
	
	public void printToSystemOut() {
		for (AnalyticalQueryResult analyticalQueryResult : results) {
			System.out.println("Analytical Query:");
			System.out.println(analyticalQueryResult.getAnalyticalQuery().toString());
			
			System.out.println("Provenance Query:");
			System.out.println(analyticalQueryResult.getProvenanceQuery().toString());
			
			System.out.println("Using Index: "+analyticalQueryResult.getIndex());
			System.out.println("using Strategy: "+analyticalQueryResult.getStrategy());
			
			System.out.println("Provance Query Execution: "+ analyticalQueryResult.getProvenanceQueryExecutionDuration());
			System.out.println("Reading existing prov index file: "+ analyticalQueryResult.getReadIndexDuration());
			System.out.println("Build prov index: "+ analyticalQueryResult.getBuildIndexDuration());
			System.out.println("Build Query Profile: "+analyticalQueryResult.getBuildQueryProfileDuration());
			System.out.println("Prov Index Lookup: "+analyticalQueryResult.getIndexLookupDuration());
			System.out.println("Intersect Context Values: " + analyticalQueryResult.getIntersectContextSetDuration());
			System.out.println("Prepair "+analyticalQueryResult.getStrategy() + ": " + analyticalQueryResult.getPrepairOptimizationStrategyDuration());
			System.out.println("Execute query: "+ analyticalQueryResult.getExecuteAnalyticalQueryDuration());
			
			ResultSetFormatter.out(analyticalQueryResult.getResult());
		}
	}

}
