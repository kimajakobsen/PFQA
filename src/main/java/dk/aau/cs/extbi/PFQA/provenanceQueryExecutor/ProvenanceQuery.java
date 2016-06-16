package dk.aau.cs.extbi.PFQA.provenanceQueryExecutor;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public abstract class ProvenanceQuery {

	private String name;

	ProvenanceQuery (String name){
		this.name = name;
	}
	
	public abstract ContextSet execute();

	public String getName() {
		return name;
	}
}
