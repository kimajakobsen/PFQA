package dk.aau.cs.extbi.PFQA.provenanceIndex;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;

public abstract class ProvenanceIndex {

	public abstract ContextSet getContext(QueryProfile qp);
}
