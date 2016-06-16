package dk.aau.cs.extbi.PFQA.experiments;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

import dk.aau.cs.extbi.PFQA.provenanceQueryExecutor.ProvenanceQuery;
import dk.aau.cs.extbi.PFQA.provenanceQueryExecutor.ProvenanceQueryBuilder;

public class ExperimentBuilder {
	private ArrayList<SimpleEntry<String,Query>> analyticalQueries = new ArrayList<SimpleEntry<String,Query>>();
	private ArrayList<SimpleEntry<String,String>> datasets = new ArrayList<SimpleEntry<String,String>>();
	private ArrayList<ProvenanceQuery> provenanceQueries = new ArrayList<ProvenanceQuery>();
	private ArrayList<String> optimizationStrategies = new ArrayList<String>();
	private ArrayList<String> ignoreStrategies = new ArrayList<String>();
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
		provenanceQueries.add(ProvenanceQueryBuilder.build(getFileName(pq),QueryFactory.read(pq)));
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
		return new Experiment(analyticalQueries, provenanceQueries, optimizationStrategies, provenanceIndices, datasets, numberOfExperimentRuns, ignoreStrategies);
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

	public void addIgnoreStrategies(List<String> asList) {
		for (String string : asList) {
			addIgnoreStrategy(string);
		}
	}
	
	public void addIgnoreStrategy(String strategy) {
		ignoreStrategies.add(strategy);
	}

	public void addContextValueFiles(List<String> fileNames) {
		for (String fileName : fileNames) {
			addContextValueFile(fileName);
		}
	}

	public void addContextValueFile(String filename) {
		File file = new File(filename);
		if (file.isDirectory()) {
			for (File entry : file.listFiles()) {
				provenanceQueries.add(ProvenanceQueryBuilder.build(entry.toString()));
			}
		} else {
			provenanceQueries.add(ProvenanceQueryBuilder.build(filename));
		}
	}
}
