package dk.aau.cs.extbi.PFQA.logger;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.provenanceQueryExecutor.ProvenanceQuery;

public class Logger {
	
	private static Logger logger = new Logger();
	private ExperimentResults results = new ExperimentResults();
	private QueryTimes result;
	private StartupTimes startup;
	private SimpleEntry<String, Query> analyticalQuery;
	private ProvenanceQuery provenanceQuery;
	private String strategy;
	private String index;
    private Instant provenanceQueryExecutionStart;
    private long provenanceQueryExecutionDuration;
    private Instant readIndexStart;
    private long readIndexDuration;
    private Instant buildIndexStart;
    private long buildIndexDuration;
    private Instant writeIndexToDiskStart;
    private long writeIndexToDiskDuration;
    private Instant buildQueryProfileStart;
    private long buildQueryProfileDuration;
    private Instant indexLookupStart;
    private long indexLookupDuration;
    private Instant intersectContextSetStart;
    private long intersectContextSetDuration;
    private Instant prepairOptimizationStrategyStart;
    private long prepairOptimizationStrategyDuration;
    private Instant executeAnalyticalQueryStart;
    private long executeAnalyticalQueryDuration;
	private String resultSet;
	private SimpleEntry<String, String> dataset;
	private int experimentRun;
	private LocalDateTime time; 
	
	private Logger(){
		time = LocalDateTime.now();
	}
	
	public static Logger getInstance() {
		return logger;
	}

	public void startAnalyticalQueryContex(SimpleEntry<String, Query> analyticalQuery) {
		this.analyticalQuery = analyticalQuery;
	}
	
	public void startProvenanceQueryContext(ProvenanceQuery provenanceQuery) {
		this.provenanceQuery = provenanceQuery;
	}

	public void startOptimizationStrategyContext(String strategyString) {
		this.strategy = strategyString;
	}

	public void startProvenanceIndexContext(String index) {
		this.index = index;
	}

	public void startReadIndex() {
		readIndexStart = Instant.now();
	}

	public void startProvenanceQueryExecution() {
		provenanceQueryExecutionStart = Instant.now();
	}

	public void endProvenanceQueryExecution() {
		Instant end = Instant.now();
		provenanceQueryExecutionDuration = Duration.between(provenanceQueryExecutionStart, end).toMillis();
		provenanceQueryExecutionStart = null;
	}
	
	public void endReadIndex() {
		Instant end = Instant.now();
		readIndexDuration = Duration.between(readIndexStart, end).toMillis();
		readIndexStart = null;
	}

	public void startBuildIndex() {
		buildIndexStart = Instant.now();
	}

	public void endBuildIndex() {
		Instant end = Instant.now();
		buildIndexDuration = Duration.between(buildIndexStart, end).toMillis();
		buildIndexStart = null;
	}

	public void startWriteIndexToDisk() {
		writeIndexToDiskStart = Instant.now();
		
	}

	public void endWriteIndexToDisk() {
		Instant end = Instant.now();
		writeIndexToDiskDuration = Duration.between(writeIndexToDiskStart, end).toMillis();
		writeIndexToDiskStart = null;
	}

	public void startBuildQueryProfile() {
		buildQueryProfileStart = Instant.now();
	}

	public void endBuildQueryProfile() {
		Instant end = Instant.now();
		buildQueryProfileDuration = Duration.between(buildQueryProfileStart, end).toMillis();
		buildQueryProfileStart = null;
	}

	public void startIndexLookup() {
		indexLookupStart = Instant.now();
	}

	public void endIndexLookup() {
		Instant end = Instant.now();
		indexLookupDuration = Duration.between(indexLookupStart, end).toMillis();
		indexLookupStart = null ;
	}

	public void startIntersectContextSet() {
		intersectContextSetStart = Instant.now();
	}

	public void endIntersectContextSet() {
		Instant end = Instant.now();
		intersectContextSetDuration = Duration.between(intersectContextSetStart, end).toMillis();
		intersectContextSetStart = null;
	}

	public void startPrepareOptimizationStrategy() {
		prepairOptimizationStrategyStart = Instant.now();
	}

	public void endPrepareOptimizationStrategy() {
		Instant end = Instant.now();
		prepairOptimizationStrategyDuration = Duration.between(prepairOptimizationStrategyStart, end).toMillis();
		prepairOptimizationStrategyStart= null;
	}

	public void startExecuteQuery() {
		executeAnalyticalQueryStart = Instant.now();
	}

	public void endExecuteQuery() {
		Instant end = Instant.now();
		executeAnalyticalQueryDuration = Duration.between(executeAnalyticalQueryStart, end).toMillis();
		executeAnalyticalQueryStart = null;
	}

	public void setResult(String result) {
		this.resultSet = result;
	}

	public void commitResult() {
		result = new QueryTimes(analyticalQuery, provenanceQuery,strategy,index, resultSet, time);
		result.addProvenanceQueryExecution(provenanceQueryExecutionDuration);
		result.addReadIndex(readIndexDuration);
		result.addBuildQueryProfile(buildQueryProfileDuration);
		result.addIndexLookup(indexLookupDuration);
		result.addIntersectContextSet(intersectContextSetDuration);
		result.addPrepairOptimizationStrategy(prepairOptimizationStrategyDuration);
		result.addExecuteAnalyticalQuery(executeAnalyticalQueryDuration);
		result.addDataset(dataset);
		result.setExperimentRunNumber(experimentRun);
		results.add(result);

		clearTempoaryResultVariables();
	}
	
	public void printToSystemOut() {
		results.printToSystemOut();
	}
	
	private void clearTempoaryResultVariables() {
		provenanceQueryExecutionStart = null;
		provenanceQueryExecutionDuration = 0;
		intersectContextSetStart = null;
		intersectContextSetDuration = 0;
		prepairOptimizationStrategyStart = null;
		prepairOptimizationStrategyDuration = 0;
		executeAnalyticalQueryStart = null;
		executeAnalyticalQueryDuration = 0;
	}

	public void startDataset(SimpleEntry<String, String> dataset) {
		this.dataset = dataset;
	}

	public void startExperimentRun(int experimentRun) {
		this.experimentRun = experimentRun;
	}

	public void printToFile() {
		results.printToFile();
	}

	public void writeToDB() {
		results.writeToDB();
	}

	public void commitStartupTime() {
		startup = new StartupTimes(time,dataset,index);
		startup.setBuildIndex(buildIndexDuration);
		startup.setWriteIndexToDisk(writeIndexToDiskDuration);
		startup.setReadIndex(readIndexDuration);
		results.add(startup);
		
		clearTempoaryStartupVariables();
	}

	private void clearTempoaryStartupVariables() {
		buildIndexStart = null;
		buildIndexDuration = 0;
		writeIndexToDiskStart = null;
		writeIndexToDiskDuration = 0;
		readIndexStart = null;
		readIndexDuration = 0;
	}
}
