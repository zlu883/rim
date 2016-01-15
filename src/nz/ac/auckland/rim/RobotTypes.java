package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;
import nz.ac.auckland.rim.motion_modules.RobotMotionModule;

public class RobotTypes {

	private static List<RobotType> _robotTypes;
	private static RobotType _activeRobotType;
	
	public static void init() {
		if (_robotTypes == null) {
			_robotTypes = new ArrayList<RobotType>();
			_robotTypes.add(new RobotType(""));
		}
	}
	
	public RobotType getActiveRobotType() {
		return _activeRobotType;
	}
}
