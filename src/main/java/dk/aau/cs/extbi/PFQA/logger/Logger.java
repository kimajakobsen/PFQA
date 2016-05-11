package dk.aau.cs.extbi.PFQA.logger;

import java.time.Duration;
import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;

public class Logger {
	
	private static Logger logger = new Logger();
	private ExperimentResults results = new ExperimentResults();
	private AnalyticalQueryResult result;
	private SimpleEntry<String, Query> analyticalQuery;
	private SimpleEntry<String, Query> provenanceQuery;
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
	private ResultSet resultSet;
	private SimpleEntry<String, String> dataset;
	
	private Logger(){}
	
	public static Logger getInstance() {
		return logger;
	}

	public void startAnalyticalQueryContex(SimpleEntry<String, Query> analyticalQuery2) {
		this.analyticalQuery = analyticalQuery2;
	}
	
	public void startProvenanceQueryContext(SimpleEntry<String, Query> provenanceQuery2) {
		this.provenanceQuery = provenanceQuery2;
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

	public void setResult(ResultSet result) {
		this.resultSet = result;
	}

	public void commitResult() {
		result = new AnalyticalQueryResult(analyticalQuery, provenanceQuery,strategy,index, resultSet);
		result.addProvenanceQueryExecution(provenanceQueryExecutionDuration);
		result.addBuildIndex(buildIndexDuration);
		result.addReadIndex(readIndexDuration);
		result.addWriteIndexToDisk(writeIndexToDiskDuration);
		result.addBuildQueryProfile(buildQueryProfileDuration);
		result.addIndexLookup(indexLookupDuration);
		result.addIntersectContextSet(intersectContextSetDuration);
		result.addPrepairOptimizationStrategy(prepairOptimizationStrategyDuration);
		result.addExecuteAnalyticalQuery(executeAnalyticalQueryDuration);
		result.addDataset(dataset);
		
		results.add(result);
		clearTempoaryVariables();
	}
	
	public void printToSystemOut() {
		results.printToSystemOut();
	}
	
	private void clearTempoaryVariables() {
		provenanceQueryExecutionStart = null;
		provenanceQueryExecutionDuration = 0;
		readIndexStart = null;
		readIndexDuration = 0;
		buildIndexStart = null;
		buildIndexDuration = 0;
		writeIndexToDiskStart = null;
		writeIndexToDiskDuration = 0;
		buildQueryProfileStart = null;
		buildQueryProfileDuration = 0;
		indexLookupStart = null;
		indexLookupDuration = 0;
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
}
