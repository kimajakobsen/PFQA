package dk.aau.cs.extbi.PFQA.logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.provenanceQueryExecutor.ProvenanceQuery;

public class QueryTimes{
	private SimpleEntry<String,Query> analyticalQuery;
	private ProvenanceQuery provenanceQuery;
	private String strategy;
	private String index;
	private String result;
	private long provenanceQueryExecutionDuration;
	private long readIndexDuration;
	private long buildQueryProfileDuration;
	private long indexLookupDuration;
	private long intersectContextSetDuration;
	private long prepairOptimizationStrategyDuration;
	private long executeAnalyticalQueryDuration;

	private SimpleEntry<String, String> dataset;
	private int experimentRun;
	private LocalDateTime timePoint;
	
	public QueryTimes(SimpleEntry<String,Query> analyticalQuery, ProvenanceQuery provenanceQuery, String strategy, String index, String resultSet, LocalDateTime time) {
		this.analyticalQuery = analyticalQuery;
		this.provenanceQuery = provenanceQuery;
		this.strategy = strategy;
		this.index = index;
		this.result = resultSet;
		setTimePoint(time);
	}
	
	public void addProvenanceQueryExecution(long provenanceQueryExecutionDuration) {
		this.provenanceQueryExecutionDuration = (provenanceQueryExecutionDuration);
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

	public SimpleEntry<String, Query> getAnalyticalQuery() {
		return analyticalQuery;
	}

	public ProvenanceQuery getProvenanceQuery() {
		return provenanceQuery;
	}

	public String getStrategy() {
		return strategy;
	}

	public String getIndex() {
		return index;
	}
	
	public int getExperimentRunNumber() {
		return experimentRun;
	}

	public String getResult() {
		return result;
	}

    public long getProvenanceQueryExecutionDuration() {
        return provenanceQueryExecutionDuration;
    }
    
    public SimpleEntry<String, String> getDataset() {
        return dataset;
    }
    
    public String getDatasetKey() {
    	return getDataset().getValue();
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
    
    
    public long getTotalDuration() {
    	long total = provenanceQueryExecutionDuration + 
    			buildQueryProfileDuration + 
    			indexLookupDuration + 
    			intersectContextSetDuration + 
    			prepairOptimizationStrategyDuration + 
    			executeAnalyticalQueryDuration + 
    			readIndexDuration;
    	
		return total;
    }

	public void addDataset(SimpleEntry<String, String> dataset) {
		this.dataset = dataset;
	}

	public void setExperimentRunNumber(int experimentRun) {
		this.experimentRun = experimentRun;
	}
	
	public String getStrategyName() {
		return strategy+index;
		
	}

	public LocalDateTime getTimePoint() {
		return timePoint;
	}
	
	public long getUnixTimestamp() {
		ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
		return timePoint.atZone(zoneId).toEpochSecond();
	}

	public void setTimePoint(LocalDateTime timePoint) {
		this.timePoint = timePoint;
	}
}
