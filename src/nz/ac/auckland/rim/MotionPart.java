package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

public class MotionPart {

	private Map<String, MotionUnit> _motionUnits;
	
	public MotionPart() {
		_motionUnits = new HashMap<String, MotionUnit>();
	}
	
	public void registerMotionUnit(MotionUnit u, String name) {
		if (_motionUnits.containsKey(name)) {
			//errorcheck
		} else
			_motionUnits.put(name, u);
	} 
	
	public MotionUnit getMotionUnitByName(String name) {
		return _motionUnits.get(name);
	}
}
