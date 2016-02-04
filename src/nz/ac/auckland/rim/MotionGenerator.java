package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import nz.ac.auckland.rim.ReactionGenerator.AlgorithmType;
import nz.ac.auckland.rim.data.MotionPart;
import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RIMDataLibrary;
import nz.ac.auckland.rim.data.Reaction;
import nz.ac.auckland.rim.data.Scenario;

/**
 * Class that provides the function for selecting robot motions for some input reaction(s).
 * 
 * @author Jonny Lu
 *
 */
public class MotionGenerator {

	private MotionGenerator() {}
	
	public static enum AlgorithmType {RANDOM, WEIGHTED}
	
	private static AlgorithmType activeAlgorithm = AlgorithmType.WEIGHTED;
	
	public static void setAlgorithmType(AlgorithmType t) {
		activeAlgorithm = t;
	}
	
	/**
	 * The main motion selection function.
	 * 
	 * @param reactionStrengthVector a vector of the input reactions linked to their respective
	 * strength value 
	 * @return a list of the selected motion unit(s)
	 */
	public static List<MotionUnit> generateMotion(ReactionVector v) {
				
		// Decide which algorithm to use depending on the configured active algorithm.
		// Single is the default.
		if (activeAlgorithm.equals(AlgorithmType.RANDOM)) {
			return randomSelectionAlgorithm(v);
		} else if (activeAlgorithm.equals(AlgorithmType.WEIGHTED)) {
			return weightedMatchingAlgorithm(v);
		} else {
			return weightedMatchingAlgorithm(v);
		}
	}
	
	public static List<MotionUnit> randomSelectionAlgorithm(ReactionVector reactionVector) {
		
		List<MotionUnit> generatedMotions = new ArrayList<MotionUnit>();
		
		List<Reaction> selectedReactions = reactionVector.getReactions();
		if (selectedReactions.size() == 1) {
			Reaction selectedReaction = selectedReactions.get(0);
			int reactionId = RIMDataLibrary.getReactionList().indexOf(selectedReaction);
			List<MotionPart> availableParts = RIMDataLibrary.getActiveRobotType().getMotionParts();
			for (MotionPart availablePart : availableParts) {
				List<MotionUnit> availableUnits = availablePart.getMotionUnits();
				int strengthSum = 0;
				int[] motionStrengthVector = new int[availableUnits.size()];
				for (int i = 0; i < availableUnits.size(); i++) {
					int[] reactionStrengthVector = RIMDataLibrary.getReactionStrengthVector(availableUnits.get(i));
					strengthSum += reactionStrengthVector[reactionId];
					motionStrengthVector[i] = strengthSum;
				}
				int rand = new Random().nextInt(strengthSum) + 1;
				for (int i = 0; i < motionStrengthVector.length; i++) {
					if (rand <= motionStrengthVector[i]) {
						generatedMotions.add(availableUnits.get(i));
					}
				}
			}
		} else {
			throw new RIMException("Number of reaction selected for random algorithm not equal to 1");
		}
		
		return generatedMotions;
	}
	
	public static List<MotionUnit> weightedMatchingAlgorithm(ReactionVector reactionVector) {

		List<MotionUnit> generatedMotions = new ArrayList<MotionUnit>();

		List<Reaction> selectedReactions = reactionVector.getReactions();
		List<Double> selectedReactionWeights = reactionVector.getWeights();
		
		List<MotionPart> availableParts = RIMDataLibrary.getActiveRobotType().getMotionParts();
		
		
		
	}
	
	private static int vectorDifference(int[] vectorA, int[] vectorB) {
		
		int difference = 0;
		
		if ((vectorA.length == vectorB.length) && vectorA.length > 0) {
			for (int i = 0; i < vectorA.length; i++) {
				difference += Math.abs(vectorA[i] - vectorB[i]);
			}
		} else {
			throw new RIMException("Invalid vector input for comparison");
		}
		
		return difference;
	}
	
}
