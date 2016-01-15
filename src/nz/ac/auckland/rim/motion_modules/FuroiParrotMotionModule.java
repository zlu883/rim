package nz.ac.auckland.rim.motion_modules;

import nz.ac.auckland.rim.RobotType;

public class FuroiParrotMotionModule implements RobotMotionModule {

	private static FuroiParrotMotionModule INSTANCE;
	
	private RobotType _robotType;
	
	private FuroiParrotMotionModule() {
		
	}
	
	public static FuroiParrotMotionModule getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FuroiParrotMotionModule();
		return INSTANCE;
	}
}
