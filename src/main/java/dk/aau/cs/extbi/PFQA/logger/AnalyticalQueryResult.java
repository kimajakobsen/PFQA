package dk.aau.cs.extbi.PFQA.logger;

import org.apache.jena.query.Query;
import org.apache.jena.query.ResultSet;

public class AnalyticalQueryResult {
	private Query analyticalQuery;
	private Query provenanceQuery;
	private String strategy;
	private String index;
	private ResultSet result;
	private long provenanceQueryExecutionDuration;
	private long buildIndexDuration;
	private long writeIndexToDiskDuration;
	private long buildQueryProfileDuration;
	private long indexLookupDuration;
	private long intersectContextSetDuration;
	private long prepairOptimizationStrategyDuration;
	private long executeAnalyticalQueryDuration;
	private long readIndexDuration;
	
	public AnalyticalQueryResult(Query analyticalQuery, Query provenanceQuery, String strategy, String index, ResultSet resultSet) {
		this.analyticalQuery = analyticalQuery;
		this.provenanceQuery = provenanceQuery;
		this.strategy = strategy;
		this.index = index;
		this.result = resultSet;
	}
	
	public void addProvenanceQueryExecution(long provenanceQueryExecutionDuration) {
		this.provenanceQueryExecutionDuration = (provenanceQueryExecutionDuration);
	}

	public void addBuildIndex(long buildIndexDuration) {
		this.buildIndexDuration = (buildIndexDuration);
	}

	public void addWriteIndexToDisk(long writeIndexToDiskDuration) {
        this.writeIndexToDiskDuration = (writeIndexToDiskDuration);
    }
    
    public void addReadIndex(long readIndexDuration) {
        this.readIndexDuration = (readIndexDuration);
    }

    public void addBuildQueryProfile(long buildQueryProfileDuration) {
        this.buildQueryProfileDuration = (buildQueryProfileDuration);
    }

    public void addIndexLookup(long indexLookupDuration) {
        this.indexLookupDuration = (indexLookupDuration);
    }

    public void addIntersectContextSet(long intersectContextSetDuration) {
        this.intersectContextSetDuration = (intersectContextSetDuration);
    }

    public void addPrepairOptimizationStrategy(long prepairOptimizationStrategyDuration) {
        this.prepairOptimizationStrategyDuration = (prepairOptimizationStrategyDuration);
    }

    public void addExecuteAnalyticalQuery(long executeAnalyticalQueryDuration) {
        this.executeAnalyticalQueryDuration = (executeAnalyticalQueryDuration);
    }

	public Query getAnalyticalQuery() {
		return analyticalQuery;
	}

	public Query getProvenanceQuery() {
		return provenanceQuery;
	}

	public String getStrategy() {
		return strategy;
	}

	public String getIndex() {
		return index;
	}

	public ResultSet getResult() {
		return result;
	}

    public long getProvenanceQueryExecutionDuration() {
        return provenanceQueryExecutionDuration;
    }

	public long getBuildIndexDuration() {
        return buildIndexDuration;
    }

    public long getWriteIndexToDiskDuration() {
        return writeIndexToDiskDuration;
    }

    public long getBuildQueryProfileDuration() {
        return buildQueryProfileDuration;
    }

    public long getIndexLookupDuration() {
        return indexLookupDuration;
    }

    public long getIntersectContextSetDuration() {
        return (intersectContextSetDuration);
    }

    public long getPrepairOptimizationStrategyDuration() {
        return (prepairOptimizationStrategyDuration);
    }

    public long getExecuteAnalyticalQueryDuration() {
        return (executeAnalyticalQueryDuration);
    }
    
    public long getReadIndexDuration() {
    	return (readIndexDuration);
    }
}
