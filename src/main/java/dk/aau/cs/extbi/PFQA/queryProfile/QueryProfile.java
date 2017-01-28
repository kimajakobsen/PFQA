package dk.aau.cs.extbi.PFQA.queryProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.logger.Logger;

public class QueryProfile {
	private PredicatePath predicatePaths = new PredicatePath();
	private Map<String,String>  prefixes = new HashMap<String, String>();
	
	public QueryProfile(Query q) {
		Logger logger = Logger.getInstance();
		prefixes.put("skos", "http://www.w3.org/2004/02/skos/core#");
		prefixes.put("", "http://example.com/");
		
		logger.startBuildQueryProfile();
		Pattern whereClausePattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
		Matcher whereClauseMatcher = whereClausePattern.matcher(q.toString());
		
		if (whereClauseMatcher.find()) {
		    String[] split = whereClauseMatcher.group(1).split("FILTER");
		    String whereClause = split[0];
		    String[] starPatterns = whereClause.split("\\.");
		    
		    ArrayList<TriplePatternContainer> tripleContainers = new ArrayList<TriplePatternContainer>();
		    for (String triplesSimicolonSeperated : starPatterns) {
	    		String[] triples = triplesSimicolonSeperated.split(";");
	    		for (String triple : triples) {
	    			triple = triple.trim();
	    			List<String> elements = new ArrayList<String>();
	    			Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"@en|\"[^\"]*\"|'[^']*'@en|'[^']*'");
	    			Matcher regexMatcher = regex.matcher(triple);
	    			while (regexMatcher.find()) {
	    				elements.add(regexMatcher.group());
	    			} 
	    			if (elements.size() == 3) {
	    				tripleContainers.add(new TriplePatternContainer(createPrefix(elements.get(0)),createPrefix(elements.get(1)),createPrefix(elements.get(2))));
					} else if (elements.size() == 2) {
						tripleContainers.add(new TriplePatternContainer(createPrefix(tripleContainers.get(tripleContainers.size()-1).getSubject()),createPrefix(elements.get(0)),createPrefix(elements.get(1))));
					} else {
						System.out.println(elements);
						throw new IllegalArgumentException("A triple pattern, from the analytical query, does not match the expected length/format. The comma seperator and blank nodes are not supported", null);
					}
				}
			}
 		    for (TriplePatternContainer triplePattern : tripleContainers) {
		    	predicatePaths.addTriplePatternContainer(triplePattern);
			}
		}
		logger.endBuildQueryProfile();
	} 
	
	private String createPrefix(String string) {
		if (string.contains(":")) {
			String[] split = string.split(":");
			String prefix = prefixes.get(split[0]);
			
			return prefix+split[1];
		}
		return string;
	}

	public PredicatePath getPredicatePaths() {
		return predicatePaths;
	}
}
