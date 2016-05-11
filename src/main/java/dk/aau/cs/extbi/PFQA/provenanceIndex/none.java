package dk.aau.cs.extbi.PFQA.provenanceIndex;

import dk.aau.cs.extbi.PFQA.helper.ContextSet;
import dk.aau.cs.extbi.PFQA.queryProfile.QueryProfile;

public class none extends ProvenanceIndex {

	private static final long serialVersionUID = 1L;

	@Override
	public ContextSet getContext(QueryProfile qp, ContextSet contextSetPQ) {
		return contextSetPQ;
	}

}
