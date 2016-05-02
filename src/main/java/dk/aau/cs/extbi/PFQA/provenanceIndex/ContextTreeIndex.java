package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.util.ArrayList;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;
import dk.aau.cs.extbi.PFQA.queryProfile.TriplePatternContainer;


public class ContextTreeIndex extends ProvenanceIndex implements java.io.Serializable {
	private static final long serialVersionUID = 1L; //update when class is changed.
	private ContextTreeIndexNode<String> root;

	public ContextTreeIndex(ContextTreeIndexNode<String> root2) {
		this.root = root2;
	}
	
	@Override
	public ContextSet getContext(QueryProfile qp) {
		ContextSet contextSet = new ContextSet();
		ArrayList<ArrayList<TriplePatternContainer>> pathStructure = qp.getPredicatePaths().getPaths();
		for (ArrayList<TriplePatternContainer> predicatePath : pathStructure) {
			contextSet.add(getTreeContextValue(predicatePath, 0, root));
		}
		return contextSet;
	}
	
	private ContextSet getTreeContextValue(ArrayList<TriplePatternContainer> predicatePath, int index, ContextTreeIndexNode<String> tree) {
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
			return contextSet;
		}
	}
}
