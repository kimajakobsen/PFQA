package dk.aau.cs.extbi.PFQA.helper;

import java.util.ArrayList;

public class Config {

	private static String namespace = "http://example.com/";
	private static String provenanceNamespace = namespace+"provenance/";
	private static String cubeName = namespace+"cube/";
	private static String dataset;
	private static ArrayList<String> strategies = new ArrayList<String>();
	private static String psqlUsername;
	private static String psqlPassword;
	private static boolean writeToDatabase;
	private static long timeoutMinutes = 0;

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
		return dataset;
	}
	
	public static void setDatasetLocation(String dataset) {
		Config.dataset = dataset;
	}

	public static ArrayList<String> getStrategies() {
		return strategies;
	}
	
	public static void setStrategyName(String strategy) {
		Config.strategies.add(strategy);
	}

	public static void setPsqlUsername(String string) {
		Config.psqlUsername = string;
		Config.setWriteToDatabase(true);
	}

	public static void setPsqlPassword(String string) {
		Config.psqlPassword = string;
	}
	
	public static String getPsqlUsername() {
		return Config.psqlUsername;
	}
	
	public static String getPsqlPassword() {
		return Config.psqlPassword;
	}

	public static boolean isWriteToDatabase() {
		return writeToDatabase;
	}

	public static void setWriteToDatabase(boolean writeToDatabase) {
		Config.writeToDatabase = writeToDatabase;
	}
	
	public static void setTimeoutInMinutes(int minutes) {
		Config.timeoutMinutes = minutes;
	}

	public static long getTimeout() {
		return Config.timeoutMinutes;
	}
}
