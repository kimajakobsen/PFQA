package dk.aau.cs.extbi.PFQA.logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.jena.query.ResultSetFormatter;

public class ExperimentResults {
	private ArrayList<AnalyticalQueryResult> results = new ArrayList<AnalyticalQueryResult>();
	
	public void add(AnalyticalQueryResult result) {
		results.add(result);
	}
	
	public void printToFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("the-file-name.txt", "UTF-8");
			for (AnalyticalQueryResult analyticalQueryResult : results) {
				writer.print(analyticalQueryResult.getUnixTimestamp()+"\t");
				writer.print(analyticalQueryResult.getStrategyName()+"\t");
				writer.print(analyticalQueryResult.getDataset().getKey()+"\t");
				writer.print(analyticalQueryResult.getTotalDuration()+"\t");
				writer.print(analyticalQueryResult.getExperimentRunNumber()+"\t");
				writer.print(analyticalQueryResult.getAnalyticalQuery().getKey()+"\t");
				writer.print(analyticalQueryResult.getProvenanceQuery().getKey()+"\t");
				writer.print(analyticalQueryResult.getStrategy()+"\t");
				writer.print(analyticalQueryResult.getIndex()+"\t");
			    writer.print(analyticalQueryResult.getProvenanceQueryExecutionDuration()+"\t");
			    writer.print(analyticalQueryResult.getReadIndexDuration()+"\t");
			    writer.print(analyticalQueryResult.getBuildIndexDuration()+"\t");
			    writer.print(analyticalQueryResult.getWriteIndexToDiskDuration()+"\t");
			    writer.print(analyticalQueryResult.getBuildQueryProfileDuration()+"\t");
			    writer.print(analyticalQueryResult.getIndexLookupDuration()+"\t");
			    writer.print(analyticalQueryResult.getIntersectContextSetDuration()+"\t");
			    writer.print(analyticalQueryResult.getPrepairOptimizationStrategyDuration()+"\t");
			    writer.print(analyticalQueryResult.getExecuteAnalyticalQueryDuration());
			    writer.println("");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void printToSystemOut() {
		for (AnalyticalQueryResult analyticalQueryResult : results) {
			System.out.println("Dataset: analyticalQueryResult.getDataset().getKey()");
			
			System.out.println("Analytical Query: " + analyticalQueryResult.getAnalyticalQuery().getKey());
			
			System.out.println("Provenance Query:");
			System.out.println(analyticalQueryResult.getProvenanceQuery().toString());
			
			System.out.println("Using Index: "+analyticalQueryResult.getIndex());
			System.out.println("using Strategy: "+analyticalQueryResult.getStrategy());
			
			System.out.println("Query run: "+ analyticalQueryResult.getExperimentRunNumber());
			
			System.out.println("Provance Query Execution: "+ analyticalQueryResult.getProvenanceQueryExecutionDuration());
			System.out.println("Reading existing prov index file: "+ analyticalQueryResult.getReadIndexDuration());
			System.out.println("Build prov index: "+ analyticalQueryResult.getBuildIndexDuration());
			System.out.println("Build Query Profile: "+analyticalQueryResult.getBuildQueryProfileDuration());
			System.out.println("Prov Index Lookup: "+analyticalQueryResult.getIndexLookupDuration());
			System.out.println("Intersect Context Values: " + analyticalQueryResult.getIntersectContextSetDuration());
			System.out.println("Prepair "+analyticalQueryResult.getStrategy() + ": " + analyticalQueryResult.getPrepairOptimizationStrategyDuration());
			System.out.println("Execute query: "+ analyticalQueryResult.getExecuteAnalyticalQueryDuration());
			System.out.println("Total: " + analyticalQueryResult.getTotalDuration());
			
			ResultSetFormatter.out(analyticalQueryResult.getResult());
		}
	}
}
