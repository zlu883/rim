package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.rim.motion_modules.*;

public class RobotMotionModules {

	private static List<RobotMotionModule> _motionModules;
	private static RobotMotionModule _activeModule;
	
	public static void init() {
		if (_motionModules == null) {
			_motionModules = new ArrayList<RobotMotionModule>();
			_motionModules.add(FuroiHomeMotionModule.getInstance());
			_motionModules.add(FuroiParrotMotionModule.getInstance());
		}
	}
}
