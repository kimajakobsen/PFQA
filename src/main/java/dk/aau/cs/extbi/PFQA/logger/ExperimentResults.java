package dk.aau.cs.extbi.PFQA.logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.jena.query.ResultSetFormatter;

import dk.aau.cs.extbi.PFQA.helper.Config;

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
	
	private void timeDataSizeChart() throws FileNotFoundException, UnsupportedEncodingException {
		HashMap<String, ArrayList<String>> timeDataSize = new HashMap<String, ArrayList<String>>();
		populateTimeDataSizeArray(timeDataSize);
		
		PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
		for (Entry<String, ArrayList<String>> analyticalQueryResult : timeDataSize.entrySet()) {
			writer.print(analyticalQueryResult.getKey()+"\t");
			
			for (String strategy : analyticalQueryResult.getValue()) {
				writer.print(strategy+"\t");
			}
			writer.println();
		}
		writer.close();
	}

	public void populateTimeDataSizeArray(HashMap<String, ArrayList<String>> timeDataSize) {
		timeDataSize.put("Dataset Size", Config.getStrategies());
		
		for (AnalyticalQueryResult analyticalQueryResult : results) {
			String dataset = analyticalQueryResult.getDataset().getKey();
			
			if (timeDataSize.containsKey(dataset)) {
				ArrayList<String> strategies = timeDataSize.get(dataset);
				int index = getStrategyIndex(timeDataSize,analyticalQueryResult.getStrategyName());
				long total = analyticalQueryResult.getTotalDuration();
				
				try { //If result alraedy exist then concat new value
					String existingValue = strategies.get( index );
					strategies.set(index, String.valueOf(existingValue+","+total));
				} catch ( IndexOutOfBoundsException e ) {
					strategies.set(index, String.valueOf(total));
				}
				
				timeDataSize.put(dataset,strategies);
			} else {
				
			}
		}
	}

	private int getStrategyIndex(HashMap<String, ArrayList<String>> timeDataSize, String inputStrategyName) {
		int index = 0;
		for (String strategyName : timeDataSize.get("Dataset Size")) {
			if (strategyName.equals(inputStrategyName)) {
				return index;
			}
			index++;
		}
		throw new IllegalArgumentException("The strategy "+ inputStrategyName + " does not exist in the list of expected stretegies: " + timeDataSize );
	}

}
