package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.List;

public class ReactionGenerator {

	private static ReactionGenerator INSTANCE;
	
	private List<String> _scenarios;
	private ReactionLibrary _reactionLibrary;
	
	private ReactionGenerator() {
		_scenarios = new ArrayList<String>();
		loadScenarioReactionMapping();
	}
	
	public static void init() {
		if (INSTANCE == null)
			INSTANCE = new ReactionGenerator();
	}
	
	public static ReactionGenerator getInstance() {
		return INSTANCE;
	}
	
	private void loadScenarioReactionMapping() {
		
	}
	
}
