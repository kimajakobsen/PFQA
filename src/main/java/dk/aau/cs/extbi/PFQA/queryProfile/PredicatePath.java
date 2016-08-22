package dk.aau.cs.extbi.PFQA.queryProfile;

import java.util.ArrayList;

public class PredicatePath {
	ArrayList<ArrayList<TriplePatternContainer>> paths = new ArrayList<ArrayList<TriplePatternContainer>>();

	public ArrayList<ArrayList<TriplePatternContainer>> getPaths() {
		return paths;
	}

	public void addTriplePatternContainer(TriplePatternContainer triplePatternContainer) {
		if (triplePatternMatchLastObjectInPaths(triplePatternContainer)) {
			addTriplePatternToPaths(triplePatternContainer);
		} else if(triplePatternMatchNoneLastObjectInPaths(triplePatternContainer)) {
			forkPathAndAddTriplePattern(triplePatternContainer);
		} else {
			addNewPath(triplePatternContainer);
		}
	}

	private void forkPathAndAddTriplePattern(TriplePatternContainer triplePatternContainer) {
		int indexPath = 0;
		int indexTPC;
		ArrayList<Integer> pathMatches = new ArrayList<Integer>();
		int TPCMatche = 0;
		for (ArrayList<TriplePatternContainer> path : paths) {
			ArrayList<String> objects = getAllPathObjects(path);
			indexTPC = 0;
			for (String object : objects) {
				if (object.equals(triplePatternContainer.getSubject())) {
					pathMatches.add(indexPath);
					TPCMatche = indexTPC;
				}
				indexTPC++;
			}
			indexPath++;
		}
		
		for (Integer pathIndex : pathMatches) {
			ArrayList<TriplePatternContainer> path = new ArrayList<TriplePatternContainer>();
			
			for (int i = 0; i <= TPCMatche; i++) {
				path.add(paths.get(pathIndex).get(i));
				path.add(triplePatternContainer);
			}
			addNewPath(path);
		}
	}

	private void addNewPath(ArrayList<TriplePatternContainer> path) {
		paths.add(path);
	}

	private boolean triplePatternMatchNoneLastObjectInPaths(TriplePatternContainer triplePatternContainer) {
		
		for (ArrayList<TriplePatternContainer> path : paths) {
			ArrayList<String> objects = getAllPathObjects(path);
			
			for (String object : objects) {
				if (object.equals(triplePatternContainer.getSubject())) {
					return true;
				}
			}
		}
		return false;
	}

	private ArrayList<String> getAllPathObjects(ArrayList<TriplePatternContainer> path) {
		ArrayList<String> objects = new ArrayList<String>();
		
		for (TriplePatternContainer triplePatternContainer : path) {
			objects.add(triplePatternContainer.getObject());
		}
		return objects;
	}

	private void addTriplePatternToPaths(TriplePatternContainer triplePatternContainer) {
		int index = 0;
		ArrayList<Integer> matches = new ArrayList<Integer>();
		
		for (ArrayList<TriplePatternContainer> path : paths) {
			String object = getPathLastObject(path);
			if (object.equals(triplePatternContainer.getSubject())) {
				matches.add(index);
			}
			index++;
		}
		
		for (Integer integer : matches) {
			paths.get(integer).add(triplePatternContainer);
		}
	}

	private boolean triplePatternMatchLastObjectInPaths(TriplePatternContainer triplePatternContainer) {
		for (ArrayList<TriplePatternContainer> path : paths) {
			String object = getPathLastObject(path);
			if (object.equals(triplePatternContainer.getSubject())) {
				return true;
			}
		}
		return false;
	}

	private void addNewPath(TriplePatternContainer triplePatternContainer) {
		ArrayList<TriplePatternContainer> newPath = new ArrayList<TriplePatternContainer>();
		newPath.add(triplePatternContainer);
		paths.add(newPath);
	}

	private String getPathLastObject(ArrayList<TriplePatternContainer> path) {
		return path.get(path.size()-1).getObject();
	}
	
	public String toString() {
		String output = "";
		for (ArrayList<TriplePatternContainer> arrayList : paths) {
			for (TriplePatternContainer triplePatternContainer : arrayList) {
				output += "< "+ triplePatternContainer.getSubject() + " " + triplePatternContainer.getPredicate() + " " + triplePatternContainer.getObject() + "> --> ";
			}
			output += "\n";
		}
		return output;
	}
}
