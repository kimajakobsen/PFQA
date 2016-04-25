package dk.aau.cs.extbi.PFQA.helper;

public class Config {


	private static String namespace = "http://example.com/";
	private static String provenanceNamespace = namespace+"provenance/";
	private static String cubeName = namespace+"cube/";

	public static String getNamespace() {
		return namespace;
	}
	
	public static String getProvenanceNamespace() {
		return provenanceNamespace;
	}

	public static String getCubeName() {
		return cubeName;
	}

	public static String getCubeStructureGraphName() {
		return namespace+"CubeStructureMetadata/";
	}
	
	public static String getCubeInstanceGraphName() {
		return namespace+"CubeInstanceMetadata";
	}

	public static String getDatasetLocation() {
		return "../../provenanceGenerator/tdb/test/";
	}
}
