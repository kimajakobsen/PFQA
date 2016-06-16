package dk.aau.cs.extbi.PFQA.logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import dk.aau.cs.extbi.PFQA.helper.Config;

public class ExperimentResults {
	private ArrayList<QueryTimes> results = new ArrayList<QueryTimes>();
	private ArrayList<StartupTimes> startups = new ArrayList<StartupTimes>();
	
	public void add(QueryTimes result) {
		results.add(result);
	}
	
	public void add(StartupTimes startup) {
		startups.add(startup);
	}
	
	public void writeToDB() {
		Connection c = null;
		try {
			Class.forName("org.postgresql.Driver");
		    c = DriverManager.getConnection("jdbc:postgresql://localhost/results",Config.getPsqlUsername(), Config.getPsqlPassword());
		    c.setAutoCommit(false);
		    System.out.println("Opened database successfully");
		    
		    Statement stmt = c.createStatement();
		    for (StartupTimes startup : startups) {
		    	String sql = "INSERT INTO startup VALUES ("+
		    			startup.getUnixTimestamp()+",'"+
		    			startup.getIndex()+"','"+
		    			startup.getDataset()+"',"+
		    			startup.getBuildIndexDuration()+","+
		    			startup.getWriteIndexToDiskDuration()+","+
		    			startup.getReadIndex()+");";
		    	System.out.println(sql);
		        stmt.executeUpdate(sql);
			}
		    
		    stmt = c.createStatement();
		    for (QueryTimes analyticalQueryResult : results) {
		        String sql = "INSERT INTO Results VALUES ("+
		        		analyticalQueryResult.getUnixTimestamp()+",'"+
		        		analyticalQueryResult.getStrategyName()+"','"+
		        		analyticalQueryResult.getDatasetKey()+"',"+
		        		analyticalQueryResult.getExperimentRunNumber()+",'"+
		        		analyticalQueryResult.getAnalyticalQuery().getKey()+"','"+
		        		analyticalQueryResult.getProvenanceQuery().getName()+"','"+
		        		analyticalQueryResult.getStrategy()+"','"+
		        		analyticalQueryResult.getIndex()+"',"+
		        		analyticalQueryResult.getProvenanceQueryExecutionDuration()+","+
		        		analyticalQueryResult.getBuildQueryProfileDuration()+","+
		        		analyticalQueryResult.getIndexLookupDuration()+","+
		        		analyticalQueryResult.getIntersectContextSetDuration()+","+
		        		analyticalQueryResult.getPrepairOptimizationStrategyDuration()+","+
		        		analyticalQueryResult.getExecuteAnalyticalQueryDuration()+","+
		        		analyticalQueryResult.getTotalDuration()+");";
		        System.out.println(sql);
		        stmt.executeUpdate(sql);
		    }
		    		
		    stmt.close();
		    c.commit();
	        c.close();
		} catch (Exception e) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
		    System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	public void printToFile() {
		PrintWriter writer;
		try {
			writer = new PrintWriter("the-file-name.txt", "UTF-8");
			writer.print("UnixTimestamp\t");
			writer.print("StrategyName\t");
			writer.print("Dataset\t");
			writer.print("ExperimentRunNumber\t");
			writer.print("AnalyticalQuery\t");
			writer.print("ProvenanceQuery\t");
			writer.print("Strategy\t");
			writer.print("Index\t");
			writer.print("ProvenanceQueryExecutionDuration\t");
			writer.print("ReadIndexDuration\t");
			writer.print("BuildQueryProfileDuration\t");
			writer.print("IndexLookupDuration\t");
			writer.print("IntersectContextSetDuration\t");
			writer.print("PrepairOptimizationStrategyDuration\t");
			writer.print("ExecuteAnalyticalQueryDuration\t");
			writer.print("TotalDuration");
		    writer.println("");
			
			for (QueryTimes analyticalQueryResult : results) {
				writer.print(analyticalQueryResult.getUnixTimestamp()+"\t");
				writer.print(analyticalQueryResult.getStrategyName()+"\t");
				writer.print(analyticalQueryResult.getDataset().getKey()+"\t");
				writer.print(analyticalQueryResult.getExperimentRunNumber()+"\t");
				writer.print(analyticalQueryResult.getAnalyticalQuery().getKey()+"\t");
				writer.print(analyticalQueryResult.getProvenanceQuery().getName()+"\t");
				writer.print(analyticalQueryResult.getStrategy()+"\t");
				writer.print(analyticalQueryResult.getIndex()+"\t");
			    writer.print(analyticalQueryResult.getProvenanceQueryExecutionDuration()+"\t");
			    writer.print(analyticalQueryResult.getReadIndexDuration()+"\t");
			    writer.print(analyticalQueryResult.getBuildQueryProfileDuration()+"\t");
			    writer.print(analyticalQueryResult.getIndexLookupDuration()+"\t");
			    writer.print(analyticalQueryResult.getIntersectContextSetDuration()+"\t");
			    writer.print(analyticalQueryResult.getPrepairOptimizationStrategyDuration()+"\t");
			    writer.print(analyticalQueryResult.getExecuteAnalyticalQueryDuration()+"\t");
			    writer.print(analyticalQueryResult.getTotalDuration());
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
		for (QueryTimes analyticalQueryResult : results) {
			System.out.println("Dataset:"+ analyticalQueryResult.getDataset().getKey());
			
			System.out.println("Analytical Query: " + analyticalQueryResult.getAnalyticalQuery().getKey());
			
			System.out.println("Provenance Query: " + analyticalQueryResult.getProvenanceQuery().getName());
			
			System.out.println("Using Index: "+analyticalQueryResult.getIndex());
			System.out.println("using Strategy: "+analyticalQueryResult.getStrategy());
			
			System.out.println("Query run: "+ analyticalQueryResult.getExperimentRunNumber());
			
			System.out.println("Provance Query Execution: "+ analyticalQueryResult.getProvenanceQueryExecutionDuration());
			System.out.println("Reading existing prov index file: "+ analyticalQueryResult.getReadIndexDuration());
			System.out.println("Build Query Profile: "+analyticalQueryResult.getBuildQueryProfileDuration());
			System.out.println("Prov Index Lookup: "+analyticalQueryResult.getIndexLookupDuration());
			System.out.println("Intersect Context Values: " + analyticalQueryResult.getIntersectContextSetDuration());
			System.out.println("Prepair "+analyticalQueryResult.getStrategy() + ": " + analyticalQueryResult.getPrepairOptimizationStrategyDuration());
			System.out.println("Execute query: "+ analyticalQueryResult.getExecuteAnalyticalQueryDuration());
			System.out.println("Total: " + analyticalQueryResult.getTotalDuration());
			
			System.out.println((analyticalQueryResult.getResult()));
		}
	}
}
