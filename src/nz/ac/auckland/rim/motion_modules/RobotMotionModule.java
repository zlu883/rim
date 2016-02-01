package nz.ac.auckland.rim.motion_modules;

import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RobotType;

public interface RobotMotionModule {

	public void setRobotType(RobotType t);
	
	public RobotType getRobotType();
	
	public void executeMotion(MotionUnit u);
	
}
