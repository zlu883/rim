package nz.ac.auckland.rim;

public class ReactionLibrary {
	
	private static ReactionLibrary INSTANCE;
	
	private ReactionLibrary() {
		init();
	}
	
	public ReactionLibrary getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ReactionLibrary();
		return INSTANCE;
	}
	
	private void init() {
		
	}

}
