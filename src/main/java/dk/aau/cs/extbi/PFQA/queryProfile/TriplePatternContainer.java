package dk.aau.cs.extbi.PFQA.queryProfile;

public class TriplePatternContainer {

	private String subject;
	private String predicate;
	private String object;
	
	public TriplePatternContainer(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	public String getSubject() {
		return subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public String getObject() {
		return object;
	}
	
	public String toString() { 
	    return subject+" "+predicate+" "+object;
	} 
	
}
