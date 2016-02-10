package nz.ac.auckland.rim.motion_modules;

import java.util.List;

import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RobotType;

/**
 * The component responsible for sending robot-specific commands to the
 * Robot Manager. A separate module should be implemented for each robot
 * type to control their motions. 
 * 
 * @author Jonny Lu
 *
 */
public interface RobotMotionModule {

	/**
	 * Registers the robot type this motion module controls.
	 * @param t the robot type
	 */
	public void setRobotType(RobotType t);
	
	public RobotType getRobotType();
	
	/**
	 * Goes through each motion unit and translate it into
	 * corresponding control commands.
	 * @param motions
	 */
	public void executeMotions(List<MotionUnit> motions);
	
	/**
	 * Terminates all motions in running, and send commands to the robot to return it
	 * to a neutral resting state.
	 */
	public void reset();
	
}
