package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

public class MotionPartLibrary {
	
	private static MotionPartLibrary INSTANCE;
	
	private Map<String, MotionPart> _motionParts;
	
	private MotionPartLibrary() {
		_motionParts = new HashMap<String, MotionPart>();
	}
	
	public static MotionPartLibrary getInstance() {
		return INSTANCE;
	}
	
	public static void init() {
		if (INSTANCE == null)
			INSTANCE = new MotionPartLibrary();
	}
	
	public void register(MotionPart p, String name) {
		if (_motionParts.containsKey(name)) {
			//errorcheck
		} else
			_motionParts.put(name, p);
	} 
	
	public MotionPart getMotionPartByName(String name) {
		return _motionParts.get(name);
	}
}
