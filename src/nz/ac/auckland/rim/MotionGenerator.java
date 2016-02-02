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
	
	public static enum AlgorithmType {SINGLE, COMPOSITE}
	
	private static AlgorithmType activeAlgorithm = AlgorithmType.SINGLE;
	
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
	public static List<MotionUnit> generateMotion(Map<Reaction, Integer> reactionStrengthVector) {
				
		// Decide which algorithm to use depending on the configured active algorithm.
		// Single is the default.
		if (activeAlgorithm.equals(AlgorithmType.SINGLE)) {
			return singleReactionAlgorithm(reactionStrengthVector);
		} else if (activeAlgorithm.equals(AlgorithmType.COMPOSITE)) {
			return compositeReactionAlgorithm(reactionStrengthVector);
		} else {
			return singleReactionAlgorithm(reactionStrengthVector);
		}
	}
	
	public static List<MotionUnit> singleReactionAlgorithm(Map<Reaction, Integer> reactionStrengthVector) {
		
		List<MotionPart> availableParts = RIMDataLibrary.getActiveRobotType().getMotionParts();
		List<MotionUnit> motionGenerated = new ArrayList<MotionUnit>();
		for (MotionPart p : availableParts) {
			int chanceSum = 0;
			List<MotionUnit> availableMotion = p.getMotionUnits();
			List<MotionUnit> possibleMotion = new ArrayList<MotionUnit>();
			List<Integer> motionChances = new ArrayList<Integer>();
			for (MotionUnit u : reaction.getPossibleMotion()) {
				if (availableMotion.contains(u)) {
					possibleMotion.add(u);
					int chance = reaction.getMotionChance(u);
					chanceSum += chance;
					motionChances.add(chance);
				}
			}
			int rand = new Random().nextInt(chanceSum) + 1;
			chanceSum = 0;
			for (int i = 0; i < motionChances.size(); i++) {
				chanceSum += motionChances.get(i);
				if (rand <= chanceSum) {
					motionGenerated.add(possibleMotion.get(i));
					break;
				}
			}
		}
		
		return motionGenerated;
	}
	
	public static List<MotionUnit> compositeReactionAlgorithm(Map<Reaction, Integer> reactionStrengthVector) {

	}
	
}
