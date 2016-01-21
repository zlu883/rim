package nz.ac.auckland.rim;

import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;

public class RIMMain {

	public static void main(String[] args) {
		
		RIMDataLibrary.init();
		WebSocketManager.init();
		 
		// REGISTER ROBOT MOTION MODULES HERE
		
		RIMDataLibrary.getRobotType("FuroiHome").registerMotionModule(FuroiHomeMotionModule.getInstance());
		RIMDataLibrary.getRobotType("FuroiParrot").registerMotionModule(FuroiParrotMotionModule.getInstance());
		
		for (int i = 0; i < 100; i++) {System.out.println(ReactionGenerator.generateReaction("MainMenu").getName());}
	}
	
	public static void process(String scenario) {
		
	}

}
