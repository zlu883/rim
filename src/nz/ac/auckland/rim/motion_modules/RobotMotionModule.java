package nz.ac.auckland.rim.motion_modules;

import nz.ac.auckland.rim.MotionUnit;
import nz.ac.auckland.rim.RobotType;

public interface RobotMotionModule {

	public RobotType getRobotType();
	
	public void executeMotion(MotionUnit u);
	
}
