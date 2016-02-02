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
	
	public static enum AlgorithmType {SINGLE, COMPOSITE}
	
	private static AlgorithmType activeAlgorithm = AlgorithmType.SINGLE;
	
	public static void setAlgorithmType(AlgorithmType t) {
		activeAlgorithm = t;
	}
	
	/**
	 * The main reaction selection function.
	 * 
	 * @param scenarioName name of the triggering scenario
	 * @return a map of the selected reaction(s) linked to their respective strength value
	 */
	public static Map<Reaction, Double> generateReaction(String scenarioName) {
		
		Scenario triggerScenario = RIMDataLibrary.getScenario(scenarioName);
		
		// Decide which algorithm to use depending on the configured active algorithm.
		// Single is the default.
		if (activeAlgorithm.equals(AlgorithmType.SINGLE)) {
			return singleReactionAlgorithm(triggerScenario);
		} else if (activeAlgorithm.equals(AlgorithmType.COMPOSITE)) {
			return compositeReactionAlgorithm(triggerScenario);
		} else {
			return singleReactionAlgorithm(triggerScenario);
		}
	}
	
	/**
	 * Generates a single reaction for the scenario by randomly selecting one from its list
	 * of possible reactions, based on their chance values.
	 * 
	 * @param triggerScenario the input scenario
	 * @return map of a single reaction linked to a strength of 1
	 */
	private static Map<Reaction, Double> singleReactionAlgorithm(Scenario triggerScenario) {
		
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
		
		Map<Reaction, Double> generatedReaction = new HashMap<Reaction, Double>();
		generatedReaction.put(chosenReaction, 1.0);
		return generatedReaction;
		
	}
	
	/**
	 * Generates a vector of multiple reactions with their strength value calculated from their 
	 * chance value for the given scenario. The strengths sum to 1.
	 * 
	 * @param scenario the input scenario
	 * @return map of reactions linked to their respective strengths (possible reactions only)
	 */
	private static Map<Reaction, Double> compositeReactionAlgorithm(Scenario triggerScenario) {
		
		List<Reaction> reactionList = RIMDataLibrary.getReactionList();
		int[] reactionChanceVector = RIMDataLibrary.getReactionChanceVector(triggerScenario);
		
		// Calculate the sum of the chance values in the reaction chance vector.
		int chanceSum = 0;
		for (int i = 0; i < reactionChanceVector.length; i++) {
			if (reactionChanceVector[i] > 0) { // ignore all reactions with 0 chance
				chanceSum += reactionChanceVector[i];
			}
		}
		
		Map<Reaction, Double> reactionStrengthVector = new HashMap<Reaction, Double>();
		
		// Convert each chance value to a strength value out of 1 by diving it by the sum
		for (int i = 0; i < reactionChanceVector.length; i++) {
			if (reactionChanceVector[i] > 0) { // ignore all reactions with 0 chance
				Double strength = (double)reactionChanceVector[i]/(double)chanceSum;
				reactionStrengthVector.put(reactionList.get(i), strength);
			}
		}
		
		return reactionStrengthVector;
		
	}
	
}
