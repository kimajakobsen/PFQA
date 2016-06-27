package dk.aau.cs.extbi.PFQA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import dk.aau.cs.extbi.PFQA.experiments.Experiment;
import dk.aau.cs.extbi.PFQA.experiments.ExperimentBuilder;
import dk.aau.cs.extbi.PFQA.helper.Config;

public class App 
{
	public static void main( String[] args )
    {
		// create the command line parser
		CommandLineParser parser = new DefaultParser();
	
		// create the Options
		Options options = new Options();
		options.addOption("h", "help", false, "Display this message." );
		options.addOption("a", "aq", true, "use analytical queries");
		options.addOption("s", "strategies", true, "use strategies");
		options.addOption("i", "index", true, "use indexes");
		options.addOption("p", "pq", true, "use provenance queries");
		options.addOption("d", "dataset", true, "add dataset");
		options.addOption("c", "config", true, "use a config file");
		options.addOption("r", "runs", true, "number of runs");
		options.addOption("u", "username", true, "local psql username");
		options.addOption("w", "password", true, "local psql password");
		options.addOption("g", "ignore", true, "ignore a named strategy");
		options.addOption("f", "pf", true, "load files or folder with context values");
	
		ExperimentBuilder experimentBuilder = new ExperimentBuilder();
		try {
		    CommandLine line = parser.parse( options, args );
		    
		    if (line.hasOption( "help" )) {
		    	printHelp(null,options);
		    	System.exit(0);
			} 
		    if (line.hasOption("aq")) {
		    	experimentBuilder.addAnalyticalQueries(Arrays.asList(line.getOptionValue("aq").split(",")));
			}
		    if (line.hasOption( "pq" )) {
		    	experimentBuilder.addProvenanceQueries(Arrays.asList(line.getOptionValue("pq").split(",")));
		    }
		    if (line.hasOption( "dataset" )) {
		    	experimentBuilder.addDatasets(Arrays.asList(line.getOptionValue("dataset").split(",")));
		    }
		    if (line.hasOption( "index" )) {
		    	experimentBuilder.addProvenanceIndexes(Arrays.asList(line.getOptionValue("index").split(",")));
		    }
		    if (line.hasOption( "runs" )) {
		    	experimentBuilder.setNumberOfExperimentRuns(Integer.valueOf(line.getOptionValue("runs")));
		    }
		    if (line.hasOption( "strategies" )) {
		    	experimentBuilder.addOptimizationStrategies(Arrays.asList(line.getOptionValue("strategies").split(",")));
		    }
		    if (line.hasOption( "username" )) {
		    	Config.setPsqlUsername(line.getOptionValue("username"));
		    }
		    if (line.hasOption( "password" )) {
		    	Config.setPsqlPassword(line.getOptionValue("password"));
		    }
		    if (line.hasOption( "ignore" )) {
		    	experimentBuilder.addIgnoreStrategies(Arrays.asList(line.getOptionValue("ignore").split(",")));
		    }
		    if (line.hasOption( "pf" )) {
		    	experimentBuilder.addContextValueFiles(Arrays.asList(line.getOptionValue("pf").split(",")));
		    }
		    if (line.hasOption("config")) {
		    	try (BufferedReader br = new BufferedReader(new FileReader(line.getOptionValue("config")))) {

					String fileLine;
					while ((fileLine = br.readLine()) != null) {
						if (fileLine.startsWith("aq")) {
							experimentBuilder.addAnalyticalQuery(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("pq")) {
							experimentBuilder.addProvenanceQuery(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("dataset")) {
							experimentBuilder.addDataset(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("strategy")) {
							experimentBuilder.addOptimizationStrategy(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("runs")) {
							experimentBuilder.setNumberOfExperimentRuns(Integer.valueOf(fileLine.split(" ")[1]));
						}
						if (fileLine.startsWith("index")) {
							experimentBuilder.addProvenanceIndex(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("username")) {
							Config.setPsqlUsername(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("password")) {
							Config.setPsqlPassword(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("ignore")) {
							experimentBuilder.addIgnoreStrategy(fileLine.split(" ")[1]);
						}
						if (fileLine.startsWith("pf")) {
							experimentBuilder.addContextValueFile(fileLine.split(" ")[1]);
						}
					}
		    	}
		    }

		} catch (IOException e) {
			e.printStackTrace();
		}
		catch( ParseException exp ) {
			printHelp(exp, options);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		
		//labtopTest();
		Experiment experiments = experimentBuilder.build();
		experiments.run();
    }
	
	private static void printHelp(ParseException exp, Options options) {
		String header = "";
		HelpFormatter formatter = new HelpFormatter();
		if (exp != null) {
			header = "Unexpected exception:" + exp.getMessage();
		}
		formatter.printHelp("myapp", header, options, null, true);
	}
}
