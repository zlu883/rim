package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

public class Reaction {

	private String name;
	private Map<Motion,Integer> motionProbMap;
	
	public Reaction(String reactionName) {
		name = reactionName;
		motionProbMap = new HashMap<Motion,Integer>();
	}
	
}
