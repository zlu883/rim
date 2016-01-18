package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

public class ReactionLibrary {
		
	private static Map<String, Reaction> _reactions;
	
	private ReactionLibrary() {}
	
	public static void init() {
		if (_reactions == null)
			_reactions = new HashMap<String, Reaction>();
	}
	
	public static void register(Reaction r, String name) {
		if (_reactions.containsKey(name)) {
			//errorcheck
		} else
			_reactions.put(name, r);
	} 
	
	public static Reaction getReactionByName(String name) {
		return _reactions.get(name);
	}

}
