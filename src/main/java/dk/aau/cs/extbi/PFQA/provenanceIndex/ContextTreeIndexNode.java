package dk.aau.cs.extbi.PFQA.provenanceIndex;

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;

public class ContextTreeIndexNode<T>  {

	T data;
    ContextTreeIndexNode<T> parent;
    List<ContextTreeIndexNode<T>> children;

    public ContextTreeIndexNode(T data) {
        this.data = data;
        this.children = new LinkedList<ContextTreeIndexNode<T>>();
    }

    public ContextTreeIndexNode<T> addChild(T child) {
    	ContextTreeIndexNode<T> childNode = new ContextTreeIndexNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        
        return childNode;
    }
    
    public List<ContextTreeIndexNode<T>> getChildren() {
		return children;
    }

    public T getData() {
    	return data;
    }
    
    public ContextTreeIndexNode<T> getChild(Resource predicate) {
		return getChild(predicate.getURI());
    }
    
    public ContextTreeIndexNode<T> getChild(String predicate) {
    	for (ContextTreeIndexNode<T> child : children) {
			if (child.getData().toString().equals(predicate)) {
				return child;
			}
		}
    	throw new IllegalArgumentException("The predicatePath ("+ predicate + ") does not match any children in the ContextTreeIndex " + data);
    }
    
    @Override
    public String toString() {
		return data.toString();
    	
    }
}
