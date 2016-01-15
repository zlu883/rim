package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

public class MotionUnitLibrary {

	private static MotionUnitLibrary INSTANCE;
	
	private Map<String, MotionUnit> _motionUnits;
	
	private MotionUnitLibrary() {
		_motionUnits = new HashMap<String, MotionUnit>();
	}
	
	public static MotionUnitLibrary getInstance() {
		return INSTANCE;
	}
	
	public static void init() {
		if (INSTANCE == null)
			INSTANCE = new MotionUnitLibrary();
	}
	
	public void register(MotionUnit u, String name) {
		if (_motionUnits.containsKey(name)) {
			//errorcheck
		} else
			_motionUnits.put(name, u);
	} 
	
	public MotionUnit getMotionPartByName(String name) {
		return _motionUnits.get(name);
	}
	
}
