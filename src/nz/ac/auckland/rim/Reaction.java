package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Reaction {

	private String _name;
	private Map<MotionUnit, Integer> _motionChanceMap;
	
	public Reaction(String reactionName) {
		_name = reactionName;
		_motionChanceMap = new HashMap<MotionUnit, Integer>();
	}
	
	public String getName() {
		return _name;
	}
	
	public void registerMotionChance(MotionUnit u, int chance) {
		_motionChanceMap.put(u, chance);
	}
	
	public int getMotionChance(MotionUnit u) {
		return _motionChanceMap.get(u);
	}
	
	public Set<MotionUnit> getPossibleMotion() {
		return _motionChanceMap.keySet();
	}
}
