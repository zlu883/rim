package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.List;

public class MotionGenerator {

	private static MotionGenerator INSTANCE;
	
	private MotionPartLibrary _motionPartLibrary;
	private ReactionLibrary _reactionLibrary;
	
	private MotionGenerator() {
		_motionPartLibrary = MotionPartLibrary.getInstance();
		_reactionLibrary = ReactionLibrary.getInstance();
		loadReactionMotionMapping();
	}
	
	public static void init() {
		if (INSTANCE == null)
			INSTANCE = new MotionGenerator();
	}
	
	public static MotionGenerator getInstance() {
		return INSTANCE;
	}
	
	private void loadReactionMotionMapping() {
		
	}
	
}
