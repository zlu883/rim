package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

public class Scenario {

	private String _name;
	private Map<String, Integer> _reactionChanceMap;
	
	public Scenario(String name) {
		_name = name;
		_reactionChanceMap = new HashMap<String, Integer>();
	}
	
	public int getReactionChance(String reaction) {
		return _reactionChanceMap.get(reaction);
	}
	
	public String getName() {
		return _name;
	}
	
}
