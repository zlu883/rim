package nz.ac.auckland.rim.data;

public class MotionUnit {

	private String _name;
	private MotionPart _actuator;
	
	public MotionUnit(String name) {
		_name = name;
	}
	
	public String getName() {
		return _name;
	}
	
	public void registerActuator(MotionPart p) {
		_actuator = p;
	}
	
	public MotionPart getActuator() {
		return _actuator;
	}
}
