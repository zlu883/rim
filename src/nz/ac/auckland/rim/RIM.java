package nz.ac.auckland.rim;

public class RIM {

	public static void main(String[] args) {
		
		ReactionLibrary.init();
		MotionPartLibrary.init();
		MotionUnitLibrary.init();
		//RobotMotionModules.init();
		RobotTypes.init();
		
		ReactionGenerator.init();
		MotionGenerator.init();
		
		//initialise web socket stuff next

	}

}
