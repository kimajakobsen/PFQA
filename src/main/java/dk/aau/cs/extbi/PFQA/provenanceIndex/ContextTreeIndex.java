package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.util.ArrayList;

import org.apache.jena.rdf.model.Resource;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;
import dk.aau.cs.extbi.PFQA.queryProfile.TriplePatternContainer;


public class ContextTreeIndex extends ProvenanceIndex {
	private ContextTreeIndexNode<Resource> root;

	public ContextTreeIndex(ContextTreeIndexNode<Resource> root) {
		this.root = root;
	}
	
	@Override
	public ContextSet getContext(QueryProfile qp) {
		ContextSet contextSet = new ContextSet();
		ArrayList<ArrayList<TriplePatternContainer>> pathStructure = qp.getPredicatePaths().getPaths();
		for (ArrayList<TriplePatternContainer> predicatePath : pathStructure) {
			System.out.println(predicatePath);
			contextSet.add(getTreeContextValue(predicatePath, 0, root));
		}
		return contextSet;
	}
	
	private ContextSet getTreeContextValue(ArrayList<TriplePatternContainer> predicatePath, int index, ContextTreeIndexNode<Resource> tree) {
		if (predicatePath.size() > index) {
			ContextTreeIndexNode<Resource> child = tree.getChild(predicatePath.get(index).getPredicate());
			System.out.println("Parent: " + tree + " Child: " + child + " looking for predicatePath "+ predicatePath.get(index).getPredicate());
			return getTreeContextValue(predicatePath,index+1,child);
		} else {
			ContextSet contextSet = new ContextSet();
			System.out.println("Tree node: "+tree.toString());
			System.out.println("Has number of children "+tree.getChildren().size());
			for (ContextTreeIndexNode<Resource> contextValue : tree.getChildren()) {
				contextSet.add(contextValue.getData());
			}
			return contextSet;
		}
	}
}
