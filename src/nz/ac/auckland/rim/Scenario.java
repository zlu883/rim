package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Scenario {

	private String _name;
	private Map<Reaction, Integer> _reactionChanceMap;
	
	public Scenario(String name) {
		_name = name;
		_reactionChanceMap = new HashMap<Reaction, Integer>();
	}
	
	public int getReactionChance(Reaction reaction) {
		return _reactionChanceMap.get(reaction);
	}
	
	public Set<Reaction> getPossibleReactions() {
		return _reactionChanceMap.keySet();
	}
	
	public String getName() {
		return _name;
	}
	
	public void registerReactionChance(Reaction reaction, int chance) {
		_reactionChanceMap.put(reaction, chance);
	}
	
}
