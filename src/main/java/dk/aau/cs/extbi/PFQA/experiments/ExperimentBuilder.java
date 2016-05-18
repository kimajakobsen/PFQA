package dk.aau.cs.extbi.PFQA.experiments;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

public class ExperimentBuilder {
	private ArrayList<SimpleEntry<String,Query>> analyticalQueries = new ArrayList<SimpleEntry<String,Query>>();
	private ArrayList<SimpleEntry<String,String>> datasets = new ArrayList<SimpleEntry<String,String>>();
	private ArrayList<SimpleEntry<String,Query>> provenanceQueries = new ArrayList<SimpleEntry<String,Query>>();
	private ArrayList<String> optimizationStrategies = new ArrayList<String>();
	private ArrayList<String> provenanceIndices = new ArrayList<String>();
	private int numberOfExperimentRuns;

	public void addAnalyticalQueries(List<String> aq) {
		for (String string : aq) {
			addAnalyticalQuery(string);
		}
	}
	
	public void addAnalyticalQuery(String aq) {
		analyticalQueries.add(new SimpleEntry<String,Query>(getFileName(aq),QueryFactory.read(aq)));
	}
	
	public void addProvenanceQueries(List<String> pq) {
		for (String string : pq) {
			addProvenanceQuery(string);
		}
	}
	
	public void addProvenanceQuery(String pq) {
		provenanceQueries.add(new SimpleEntry<String,Query>(getFileName(pq),QueryFactory.read(pq)));
	}
	
	public void addOptimizationStrategies(List<String> list) {
		optimizationStrategies.addAll(list);
	}
	
	public void addOptimizationStrategy(String strategy) {
		optimizationStrategies.add(strategy);
	}
	
	public void addProvenanceIndices(ArrayList<String> provenanceIndices) {
		this.provenanceIndices.addAll(provenanceIndices);
	}
	
	public void addProvenanceIndex(String provenanceIndex) {
		provenanceIndices.add(provenanceIndex);
	}
	
	public Experiment build() {
		return new Experiment(analyticalQueries, provenanceQueries, optimizationStrategies, provenanceIndices, datasets, numberOfExperimentRuns);
	}

	private String getFileName(String path) {
		String[] split = path.split("/");
		return split[split.length-1];
	}

	public void addDataset(String path) {
		datasets.add(new SimpleEntry<String, String>(getFileName(path), path));
	}

	public void setNumberOfExperimentRuns(int numberOfExperimentRuns) {
		this.numberOfExperimentRuns = numberOfExperimentRuns;
	}

	public void addProvenanceIndexes(List<String> indexes) {
		for (String index : indexes) {
			addProvenanceIndex(index);
		}
	}

	public void addDatasets(List<String> datasets) {
		for (String dataset : datasets) {
			addDataset(dataset);
		}
	}
}
