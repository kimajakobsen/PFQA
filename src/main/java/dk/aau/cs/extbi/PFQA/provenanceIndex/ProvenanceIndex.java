package dk.aau.cs.extbi.PFQA.provenanceIndex;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;

public abstract class ProvenanceIndex implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public abstract ContextSet getContext(QueryProfile qp);
}
