package nz.ac.auckland.rim;

public class MotionUnit {

	private String _name;
	private int _duration;
	private MotionPart _actuator;
	
	public MotionUnit(String name, int duration, MotionPart actuator) {
		_name = name;
		_duration = duration;
		_actuator = actuator;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getDuration() {
		return _duration;
	}
}
