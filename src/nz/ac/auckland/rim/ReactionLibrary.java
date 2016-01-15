package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

public class ReactionLibrary {
	
	private static ReactionLibrary INSTANCE;
	
	private Map<String, Reaction> _reactions;
	
	private ReactionLibrary() {
		_reactions = new HashMap<String, Reaction>();
	}
	
	public static ReactionLibrary getInstance() {
		return INSTANCE;
	}
	
	public static void init() {
		if (INSTANCE == null)
			INSTANCE = new ReactionLibrary();
	}
	
	public void register(Reaction r, String name) {
		if (_reactions.containsKey(name)) {
			//errorcheck
		} else
			_reactions.put(name, r);
	} 
	
	public Reaction getReactionByName(String name) {
		return _reactions.get(name);
	}

}
