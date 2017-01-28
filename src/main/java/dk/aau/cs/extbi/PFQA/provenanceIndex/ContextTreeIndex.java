package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import org.apache.jena.query.Query;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.logger.Logger;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;
import dk.aau.cs.extbi.PFQA.queryProfile.TriplePatternContainer;


public class ContextTreeIndex extends ProvenanceIndex implements java.io.Serializable {
	private static final long serialVersionUID = 1L; //update when class is changed.
	private ContextTreeIndexNode<String> root;

	public ContextTreeIndex(ContextTreeIndexNode<String> root2) {
		this.root = root2;
	}
	
	@Override
	public ContextSet getContext(SimpleEntry<String, Query> analyticalQuery, ContextSet contextSetPQ) {
		Logger logger = Logger.getInstance();
		ContextSet contextSet = new ContextSet();
		
		QueryProfile queryProfile = new QueryProfile(analyticalQuery.getValue());
		
		logger.startIndexLookup();
		ArrayList<ArrayList<TriplePatternContainer>> pathStructure = queryProfile.getPredicatePaths().getPaths();
		
		for (ArrayList<TriplePatternContainer> predicatePath : pathStructure) {
			contextSet.add(getTreeContextValue(predicatePath, 0, root));
		}
		logger.endIndexLookup();
		
		ContextSet contextSetMinumum = contextSet.intersect(contextSetPQ);
		System.out.println("provenance query: "+contextSetPQ.size());
		System.out.println("index: " + contextSet.size());
		System.out.println("Interseciton: "+contextSetMinumum.size());
		
		return contextSetMinumum;
	}
	
	private ContextSet getTreeContextValue(ArrayList<TriplePatternContainer> predicatePath, int index, ContextTreeIndexNode<String> tree) {
		//System.out.println("Node: "+ tree +" Index "+ index + " has children: "+ tree.getChildren());
		if (predicatePath.size() > index) {
			ContextTreeIndexNode<String> child = tree.getChild(predicatePath.get(index).getPredicate());
			//System.out.println("Parent: " + tree + " Child: " + child + " looking for predicatePath "+ predicatePath.get(index).getPredicate());
			return getTreeContextValue(predicatePath,index+1,child);
		} else {
			ContextSet contextSet = new ContextSet();
			//System.out.println("Tree node: "+tree.toString());
			//System.out.println("Has number of children "+tree.getChildren().size());
			for (ContextTreeIndexNode<String> contextValue : tree.getChildren()) {
				contextSet.add(contextValue.getData());
			}
			//System.out.println(predicatePath.get(index-1).getPredicate() + " is added, now there is "+ contextSet.getValues().size() + " contexts");
			return contextSet;
		}
	}
}
