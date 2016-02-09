package nz.ac.auckland.rim.motion_modules;

import java.util.List;

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
	public void executeMotions(List<MotionUnit> motions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
