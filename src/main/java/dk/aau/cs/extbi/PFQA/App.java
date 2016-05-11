package dk.aau.cs.extbi.PFQA;

import dk.aau.cs.extbi.PFQA.experiments.Experiment;
import dk.aau.cs.extbi.PFQA.experiments.ExperimentBuilder;

public class App 
{
	public static void main( String[] args )
    {
		ExperimentBuilder experimentBuilder = new ExperimentBuilder();
		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query1.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query2.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query3.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query4.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query5.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query6.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query7.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query8.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query9.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query10.sparql");
//		//experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query11.sparql");
//		experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query12.sparql");
//		//experimentBuilder.addAnalyticalQuery("src/test/resources/analyticalQueries/query13.sparql");
		experimentBuilder.addProvenanceIndex("contextTreeIndex");
		experimentBuilder.addProvenanceIndex("none");
		experimentBuilder.addProvenanceQueries("src/test/resources/provenanceQueries/allContexts.sparql");
		experimentBuilder.addOptimizationStrategy("FullMaterilization");
		experimentBuilder.addDataset("../../provenanceGenerator/sf50l/");
		experimentBuilder.addDataset("../../provenanceGenerator/sf100l/");
		experimentBuilder.setNumberOfExperimentRuns(3);
		
		Experiment experiments = experimentBuilder.build();
		experiments.run();
    }
}
