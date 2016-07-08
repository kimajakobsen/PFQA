package dk.aau.cs.extbi.PFQA.queryOptimizationStrategy;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public class FullMaterializationUnion extends FullMaterialization {

	public FullMaterializationUnion(ContextSet contextSetMinumum) {
		super(contextSetMinumum);
	}
	
	@Override
	protected String createQuery() {
		String query = "" +
			"CONSTRUCT { " +
			" ?s ?p ?o . "+
			"} ";
		query += "FROM <urn:x-arq:UnionGraph> ";
		query += "WHERE {";
		for (String URI : contextSet.getValues()) {
			query += 
			"{ GRAPH <"+URI+"> { "+
			"?s ?p ?o"+
			"} } " +
			"union ";
		}
		query += 
			"{ GRAPH <"+Config.getCubeInstanceGraphName()+"> { "+
			"?s ?p ?o"+
			"} } " +
			"union " +
			"{ GRAPH <"+Config.getCubeStructureGraphName()+"> { "+
			"?s ?p ?o"+
			"} } " +
			"}";
		System.out.println(query);
		return query;
	}
}
