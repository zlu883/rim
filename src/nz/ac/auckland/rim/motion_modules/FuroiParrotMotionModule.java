package nz.ac.auckland.rim.motion_modules;

import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RobotType;

public class FuroiParrotMotionModule implements RobotMotionModule {

	private static FuroiParrotMotionModule INSTANCE;
	
	private RobotType _robotType;
	
	private FuroiParrotMotionModule() {}
	
	public static FuroiParrotMotionModule getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FuroiParrotMotionModule();
		return INSTANCE;
	}

	@Override
	public void setRobotType(RobotType t) {
		_robotType = t;
	}

	@Override
	public RobotType getRobotType() {
		return _robotType;
	}

	@Override
	public void executeMotion(MotionUnit u) {
		// TODO Auto-generated method stub
		
	}
}
