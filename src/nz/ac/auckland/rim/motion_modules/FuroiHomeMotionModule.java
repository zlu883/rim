package nz.ac.auckland.rim.motion_modules;

import nz.ac.auckland.rim.RobotType;

public class FuroiHomeMotionModule implements RobotMotionModule {

	private static FuroiHomeMotionModule INSTANCE;
	
	private RobotType _robotType;
	
	private FuroiHomeMotionModule() {
		
	}
	
	public static FuroiHomeMotionModule getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FuroiHomeMotionModule();
		return INSTANCE;
	}
}
