package dk.aau.cs.extbi.PFQA.experiments;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;

public class ExperimentBuilder {
	private ArrayList<Query> analyticalQueries = new ArrayList<Query>();
	private ArrayList<Query> provenanceQueries = new ArrayList<Query>();
	private ArrayList<String> optimizationStrategies = new ArrayList<String>();
	private ArrayList<String> provenanceIndices = new ArrayList<String>();

	public void addAnalyticalQueries(ArrayList<String> aq) {
		for (String string : aq) {
			addAnalyticalQuery(string);
		}
	}
	
	public void addAnalyticalQuery(String aq) {
		analyticalQueries.add(QueryFactory.read(aq));
	}
	
	public void addProvenanceQueries(ArrayList<String> pq) {
		for (String string : pq) {
			addProvenanceQueries(string);
		}
	}
	
	public void addProvenanceQueries(String pq) {
		provenanceQueries.add(QueryFactory.read(pq));
	}
	
	public void addOptimizationStrategies(ArrayList<String> strategies) {
		optimizationStrategies.addAll(strategies);
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
		return new Experiment(analyticalQueries, provenanceQueries, optimizationStrategies, provenanceIndices);
	}
}
