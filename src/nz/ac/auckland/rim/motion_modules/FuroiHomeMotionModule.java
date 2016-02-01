package nz.ac.auckland.rim.motion_modules;

import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RobotType;

public class FuroiHomeMotionModule implements RobotMotionModule {

	private static FuroiHomeMotionModule INSTANCE;
	
	private RobotType _robotType;
	
	private FuroiHomeMotionModule() {}
	
	public static FuroiHomeMotionModule getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FuroiHomeMotionModule();
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
