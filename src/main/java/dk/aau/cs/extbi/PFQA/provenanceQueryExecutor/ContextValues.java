package dk.aau.cs.extbi.PFQA.provenanceQueryExecutor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import dk.aau.cs.extbi.PFQA.helper.Config;
import dk.aau.cs.extbi.PFQA.helper.ContextSet;

public class ContextValues extends ProvenanceQuery {

	ContextValues(String filename) {
		super(filename);
	}

	@Override
	public ContextSet execute() {
		ContextSet contextSet = new ContextSet();
		
		try (BufferedReader br = new BufferedReader(new FileReader(Config.getDatasetLocation()+"/provenanceQuery/"+getName()))) {

			String line;
			while ((line = br.readLine()) != null) {
				contextSet.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return contextSet;
	}
}
