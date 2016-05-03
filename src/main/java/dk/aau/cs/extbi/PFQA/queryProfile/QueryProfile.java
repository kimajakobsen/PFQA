package dk.aau.cs.extbi.PFQA.queryProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.Query;

public class QueryProfile {
	private PredicatePath predicatePaths = new PredicatePath();
	private Filter filters = new Filter();
	private Map<String,String>  prefixes = new HashMap<String, String>();
	
	public QueryProfile(Query q) {
		
		
//		Pattern prefixPattern = Pattern.compile("PREFIX.*:.*<(.*)>", Pattern.DOTALL);
//		Matcher prefixMatcher = prefixPattern.matcher(q.toString());
//		int index = 1;
//		while (prefixMatcher.find()) {
//			System.out.println("match "+prefixMatcher.group(1));
//			System.out.println("match "+prefixMatcher.group(2));
//			
//			//addPrefix(prefixMatcher.group(1));
//			//addPrefix(prefixMatcher.group(2));
//			index++;
//		}
		prefixes.put("skos", "http://www.w3.org/2004/02/skos/core#");
		prefixes.put("", "http://example.com/");
		
		Pattern whereClausePattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
		Matcher whereClauseMatcher = whereClausePattern.matcher(q.toString());
		
		if (whereClauseMatcher.find()) {
		    String[] split = whereClauseMatcher.group(1).split("FILTER");
		    String filterClause = null;
		    if (split.length == 2) {
		    	filterClause = split[1];
			}
		    String whereClause = split[0];
		    String[] starPatterns = whereClause.split("\\.");
		    
		    ArrayList<TriplePatternContainer> tripleContainers = new ArrayList<TriplePatternContainer>();
		    for (String triplesSimicolonSeperated : starPatterns) {
	    		String[] triples = triplesSimicolonSeperated.split(";");
	    		for (String triple : triples) {
	    			triple = triple.trim();
	    			String[] elements = triple.split("[^a-zA-Z0-9\\?:]+");
	    			if (elements.length == 3) {
	    				tripleContainers.add(new TriplePatternContainer(createPrefix(elements[0]),createPrefix(elements[1]),createPrefix(elements[2])));
					} else if (elements.length == 2) {
						tripleContainers.add(new TriplePatternContainer(createPrefix(tripleContainers.get(tripleContainers.size()-1).getSubject()),createPrefix(elements[0]),createPrefix(elements[1])));
					} else {
						throw new IllegalArgumentException("A triple pattern, from the analytical query, does not match the expected length/format. The comma seperator and blank nodes are not supported", null);
					}
				}
			}
		    for (TriplePatternContainer triplePattern : tripleContainers) {
		    	predicatePaths.addTriplePatternContainer(triplePattern);
		    	
			}
		    System.out.println(predicatePaths);
		}
	} 
	
	private String createPrefix(String string) {
		if (string.contains(":")) {
			String[] split = string.split(":");
			String prefix = prefixes.get(split[0]);
			
			return prefix+split[1];
		}
		return string;
	}

	private void addPrefix(String group) {
		String trim = group.trim().replaceAll(" +", " ");
		
		String[] split = trim.split(" ");
		String url = split[2].substring(1, split[2].length()-1);
		System.out.println(url);
		prefixes.put(split[1], split[2]);
		
		
	}

	public PredicatePath getPredicatePaths() {
		return predicatePaths;
	}
	

	
}
