package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MotionPart {

	private String _name;
	private List<MotionUnit> _motionUnits;
	
	public MotionPart(String name) {
		_motionUnits = new ArrayList<MotionUnit>();
		_name = name;
	}
	
	public void registerMotionUnit(MotionUnit u) {
		if (!_motionUnits.contains(u)) {
			_motionUnits.add(u);
		} else {
			// non unique
		}
	} 
	
	public String getName() {
		return _name;
	}
	
	public List<MotionUnit> getMotionUnits() {
		return _motionUnits;
	}
}
