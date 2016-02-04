package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import nz.ac.auckland.rim.data.RIMDataLibrary;
import nz.ac.auckland.rim.data.Reaction;
import nz.ac.auckland.rim.data.Scenario;

/**
 * Class that provides the function for selecting robot reactions for an input scenario.
 * 
 * @author Jonny Lu
 *
 */
public class ReactionGenerator {
	
	private ReactionGenerator() {}
	
	public static enum AlgorithmType {RANDOM, WEIGHTED}
	
	private static AlgorithmType activeAlgorithm = AlgorithmType.WEIGHTED;
	
	public static void setAlgorithmType(AlgorithmType t) {
		activeAlgorithm = t;
	}
	
	/**
	 * The main reaction selection function.
	 * 
	 * @param scenarioName name of the triggering scenario
	 * @return a map of the selected reaction(s) linked to their respective strength value
	 */
	public static ReactionVector generateReaction(String scenarioName) {
		
		Scenario triggerScenario = RIMDataLibrary.getScenario(scenarioName);
		
		// Decide which algorithm to use depending on the configured active algorithm.
		// Single is the default.
		if (activeAlgorithm.equals(AlgorithmType.RANDOM)) {
			return randomSelectionAlgorithm(triggerScenario);
		} else if (activeAlgorithm.equals(AlgorithmType.WEIGHTED)) {
			return weightedVectorAlgorithm(triggerScenario);
		} else {
			return weightedVectorAlgorithm(triggerScenario);
		}
	}
	
	/**
	 * Generates a single reaction for the scenario by randomly selecting one from its list
	 * of possible reactions, based on their chance values.
	 * 
	 * @param triggerScenario the input scenario
	 * @return vector of a single reaction with a strength of 10
	 */
	private static ReactionVector randomSelectionAlgorithm(Scenario triggerScenario) {
		
		List<Reaction> reactionList = RIMDataLibrary.getReactionList();
		int[] reactionChanceVector = RIMDataLibrary.getReactionChanceVector(triggerScenario);
		
		// Convert each chance value in the chance vector to a cumulative value (sum of all
		// values before it), and record the total sum of all chances.
		int chanceSum = 0;
		for (int i = 0; i < reactionChanceVector.length; i++) {
			if (reactionChanceVector[i] > 0) { // ignore all reactions with 0 chance
				chanceSum += reactionChanceVector[i];
				reactionChanceVector[i] = chanceSum;
			}
		}
		
		// Roll a random number between 1 and the sum of chances and see where it falls
		// between in the cumulative chance list. The corresponding reaction is chosen.
		Reaction chosenReaction = null;
		int rand = new Random().nextInt(chanceSum) + 1;
		for (int i = 0; i < reactionChanceVector.length; i++) {
			if (reactionChanceVector[i] > 0) { // ignore all reactions with 0 chance
				if (rand <= reactionChanceVector[i])
					chosenReaction = reactionList.get(i);
				break;
			}
		}
		
		List<Reaction> chosenReactions = new ArrayList<Reaction>();
		chosenReactions.add(chosenReaction);
		List<Double> reactionWeights = new ArrayList<Double>();
		reactionWeights.add(10.0);
		return new ReactionVector(chosenReactions, reactionWeights);
		
	}
	
	/**
	 * Returns a vector of multiple reactions with their strength value equivalent to their 
	 * chance value for the given scenario.
	 * 
	 * @param scenario the input scenario
	 * @return vector of reactions with their respective strengths
	 */
	private static ReactionVector weightedVectorAlgorithm(Scenario triggerScenario) {
		
		List<Reaction> reactionList = RIMDataLibrary.getReactionList();
		int[] reactionChanceVector = RIMDataLibrary.getReactionChanceVector(triggerScenario);
		
		List<Double> reactionWeights = new ArrayList<Double>();
		
		for (int i = 0; i < reactionChanceVector.length; i++) {
			Double strength = (double)reactionChanceVector[i];
			reactionWeights.add(strength);
		}
		
		return new ReactionVector(reactionList, reactionWeights);
		
	}
	
}
