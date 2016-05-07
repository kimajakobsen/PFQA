package dk.aau.cs.extbi.PFQA;

import dk.aau.cs.extbi.PFQA.experiments.Experiment;
import dk.aau.cs.extbi.PFQA.experiments.ExperimentBuilder;

public class App 
{
	public static void main( String[] args )
    {
		ExperimentBuilder experimentBuilder = new ExperimentBuilder();
		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/test1.sparql");
		experimentBuilder.addProvenanceIndex("contextTreeIndex");
		experimentBuilder.addProvenanceQueries("src/test/resources/provenanceQueries/allContexts.sparql");
		experimentBuilder.addOptimizationStrategy("FullMaterilization");
		
		Experiment experiments = experimentBuilder.build();
		experiments.run();
    }
}
