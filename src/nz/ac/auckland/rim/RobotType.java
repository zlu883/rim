package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

import nz.ac.auckland.rim.motion_modules.RobotMotionModule;

public class RobotType {

	private String _name;
	private Map<String, MotionPart> _motionParts;
	private RobotMotionModule _motionModule;
	
	public RobotType(String name) {
		_name = name;
		_motionParts = new HashMap<String, MotionPart>();
	}
	
	public void registerMotionModule(RobotMotionModule m) {
		_motionModule = m;
		m.setRobotType(this);
	}
}
