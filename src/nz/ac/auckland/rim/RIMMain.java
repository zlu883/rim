package nz.ac.auckland.rim;

import java.util.List;

import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;

public class RIMMain {

	public static void main(String[] args) {
		
		RIMDataLibrary.init();
		WebSocketManager.init();
		 
		// REGISTER ROBOT MOTION MODULES HERE
		
		RIMDataLibrary.getRobotType("FuroiHome").registerMotionModule(FuroiHomeMotionModule.getInstance());
		RIMDataLibrary.getRobotType("FuroiParrot").registerMotionModule(FuroiParrotMotionModule.getInstance());
		
		for (int j = 0; j < 100; j++ ) {
		List<MotionUnit> u = MotionGenerator.generateMotion(RIMDataLibrary.getReaction("greet"));
		for (int i = 0; i < u.size(); i++) {
			System.out.println(u.get(i).getName());
		}
		System.out.println();
		}
	}
	
	public static void process(String scenario) {
		
	}

}
