package nz.ac.auckland.rim.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.auckland.rim.motion_modules.RobotMotionModule;

public class RobotType {

	private String _name;
	private List<MotionPart> _motionParts;
	private RobotMotionModule _motionModule;
	
	public RobotType(String name) {
		_name = name;
		_motionParts = new ArrayList<MotionPart>();
	}
	
	public void registerMotionPart(MotionPart p) {
		if (!_motionParts.contains(p)) {
			_motionParts.add(p);
		} else {
			// non unique
		}
	}
	
	public void registerMotionModule(RobotMotionModule m) {
		_motionModule = m;
		m.setRobotType(this);
	}
	
	public RobotMotionModule getMotionModule() {
		return _motionModule;
	}
	
	public String getName() {
		return _name;
	}
	
	public List<MotionPart> getMotionParts() {
		return _motionParts;
	}
}
