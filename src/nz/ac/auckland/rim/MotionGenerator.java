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
	public static List<MotionUnit> generateMotion(List<WeightedReaction> chosenReactions) {
				
		// Decide which algorithm to use depending on the configured active algorithm.
		// Single is the default.
		if (activeAlgorithm.equals(AlgorithmType.RANDOM)) {
			return randomSelectionAlgorithm(chosenReactions);
		} else if (activeAlgorithm.equals(AlgorithmType.WEIGHTED)) {
			return weightedMatchingAlgorithm(chosenReactions);
		} else {
			return weightedMatchingAlgorithm(chosenReactions);
		}
	}
	
	/**
	 * UNUSED
	 * @param chosenReactions
	 * @return
	 */
	public static List<MotionUnit> randomSelectionAlgorithm(List<WeightedReaction> chosenReactions) {
		
		List<MotionUnit> generatedMotions = new ArrayList<MotionUnit>();
		
		if (chosenReactions.size() == 1) {
			Reaction selectedReaction = chosenReactions.get(0).getReaction();
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
	
	/**
	 * Selects a set of motion units by comparing their reaction-representation vector with the
	 * input reaction-weight vector. Attempts to find the motion unit combination with the minimum
	 * difference when comparing the average of their combined vectors and the input vector.
	 * Uses the simulated annealing algorithm as presented in the paper "A Behavior Combination 
	 * Generating Method for Reflecting Emotional Probabilities using Simulated Annealing Algorithm"
	 * by Ho Seok Ahn et. al.
	 * 
	 * @param chosenReactions list of selected reactions to portray with their respective weight
	 * @return list of chosen motion units, one in each avilable motion part
	 */
	public static List<MotionUnit> weightedMatchingAlgorithm(List<WeightedReaction> chosenReactions) {
		
		// Put the weights of the input vector into a separate array
		int[] reactionWeights = new int[chosenReactions.size()];
		for (int i = 0; i < chosenReactions.size(); i++) {
			reactionWeights[i] = chosenReactions.get(i).getWeight();
		}
		
		// Decides on an initial motion combination by choosing one motion in each
		// available motion part that has the minimum reaction-representation difference with
		// the input vector.
		List<MotionUnit> initialMotionCombination = new ArrayList<MotionUnit>();
		List<MotionPart> availableParts = RIMDataLibrary.getActiveRobotType().getMotionParts();
		for (MotionPart availablePart : availableParts) {
			int minDifference = Integer.MAX_VALUE;
			List<MotionUnit> availableUnits = availablePart.getMotionUnits();
			MotionUnit bestMatchMotion = null;
			for (int i = 0; i < availableUnits.size(); i++) {
				int weightDifference = vectorDifference(reactionWeights, 
						RIMDataLibrary.getReactionStrengthVector(availableUnits.get(i)));
				if (weightDifference < minDifference) {
					minDifference = weightDifference;
					bestMatchMotion = availableUnits.get(i);
				}
			}
			initialMotionCombination.add(bestMatchMotion);
		}
		
		// Uses the simulated annealing algorithm to compare random candidate combinations
		// with the existing one. The better one is always chosen, but a worse one is
		// sometimes chosen under a probability that reduces as the temperature reduces.
		double optimalWeightError = averageWeightError(initialMotionCombination, reactionWeights);
		List<MotionUnit> optimalMotionCombination = initialMotionCombination;
		
		double temperature = 10.0; // initial temperature
		double temperatureReductionRate = 0.995;
		double terminationTemperature = 0.1;
		
		// Runs the simulated annealing algorithm until the temperature drops to the
		// termination temperature. The temperature reduces by the reduction rate
		// in every cycle.
		while (temperature > terminationTemperature) {
			List<MotionUnit> candidateMotionCombination = selectRandomMotions(availableParts);
			double candidateWeightError = averageWeightError(candidateMotionCombination, reactionWeights);
			if (candidateWeightError < optimalWeightError) {
				optimalWeightError = candidateWeightError;
				optimalMotionCombination = candidateMotionCombination;
			} else {
				// The probability of accepting a worse candidate is given by the following
				// formula
				double acceptanceProbability = Math.exp((optimalWeightError - candidateWeightError) / temperature);
				double rand = new Random().nextDouble();
				if (rand < acceptanceProbability) {
					optimalWeightError = candidateWeightError;
					optimalMotionCombination = candidateMotionCombination;
				}
			}
			temperature = temperature * temperatureReductionRate;
		}
		
		// Returns the selected motion combination at the end of the algorithm.
		return optimalMotionCombination;
	}
	
	/**
	 * Calculates the difference between two integer vectors.
	 * @param vectorA
	 * @param vectorB
	 * @return the sum of all components in the resulting vector
	 */
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
	
	/**
	 * Calculates the difference between the average of multiple
	 * reaction-representation vectors and an reaction-weight vector.
	 * @param motionCombination set of motions to obtain reaction-representation vectors from
	 * @param reactionWeights an array of integer weights corresponding to reactions
	 * @return the sum of all components in the resulting vector
	 */
	private static double averageWeightError(List<MotionUnit> motionCombination, int[] reactionWeights) {
		
		double sumWeightError = 0.0;
		for (MotionUnit u : motionCombination) {
			sumWeightError += vectorDifference(RIMDataLibrary.getReactionStrengthVector(u), reactionWeights);
		}
		double averageWeightError = sumWeightError / motionCombination.size();
		return averageWeightError;
	}
	
	/**
	 * Randomly pick one motion unit from each available motion part.
	 * @param availableParts list of available motion parts
	 * @return list of selected motion units
	 */
	private static List<MotionUnit> selectRandomMotions(List<MotionPart> availableParts) {
		
		List<MotionUnit> selectedMotions = new ArrayList<MotionUnit>();
		for (MotionPart availablePart : availableParts) {
			List<MotionUnit> availableUnits = availablePart.getMotionUnits();
			int rand = new Random().nextInt(availableUnits.size());
			selectedMotions.add(availableUnits.get(rand));
		}
		return selectedMotions;
	}
	
}
