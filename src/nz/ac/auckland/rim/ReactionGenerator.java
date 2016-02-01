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

public class ReactionGenerator {
	
	private ReactionGenerator() {}
	
	public static enum AlgorithmType {SINGLE, VECTOR}
	
	private static AlgorithmType activeAlgorithm = AlgorithmType.SINGLE;
	
	public static void setAlgorithmType(AlgorithmType t) {
		activeAlgorithm = t;
	}
	
	/**
	 * The main reaction generation function.
	 * 
	 * @param scenarioName name of the triggering scenario
	 * @return a map of the generated reaction(s) linked to their respective strength value
	 */
	public static Map<Reaction, Double> generateReaction(String scenarioName) {
		
		Scenario triggerScenario = RIMDataLibrary.getScenario(scenarioName);
		
		// Decide which algorithm to use depending on the configured active algorithm.
		// Single is the default.
		if (activeAlgorithm.equals(AlgorithmType.SINGLE)) {
			return singleReactionAlgorithm(triggerScenario);
		} else if (activeAlgorithm.equals(AlgorithmType.VECTOR)) {
			return reactionVectorAlgorithm(triggerScenario);
		} else {
			return singleReactionAlgorithm(triggerScenario);
		}
	}
	
	/**
	 * Generates a single reaction for the scenario by randomly selecting one from its list
	 * of possible reactions, based on their chance factors.
	 * 
	 * @param triggerScenario the input scenario
	 * @return map of a single reaction linked to a strength of 1
	 */
	private static Map<Reaction, Double> singleReactionAlgorithm(Scenario triggerScenario) {
		
		ArrayList<Reaction> possibleReactions = new ArrayList<Reaction>();
		ArrayList<Integer> cumulativeChances = new ArrayList<Integer>();
		int chanceSum = 0;
		
		// Put the set of possible reactions for the scenario into a list.
		// Put their chance factors cumulatively into a corresponding list.
		for (Reaction r : triggerScenario.getPossibleReactions()) {
			possibleReactions.add(r);
			chanceSum += triggerScenario.getReactionChance(r);
			cumulativeChances.add(chanceSum);
		}
		
		// Roll a random number between 1 and the sum of chances and see where it falls
		// in the cumulative chance list. The corresponding reaction is chosen.
		Reaction chosenReaction = null;
		int rand = new Random().nextInt(chanceSum) + 1;
		for (int i = 0; i < cumulativeChances.size(); i++) {
			if (rand <= cumulativeChances.get(i)) {
				chosenReaction = possibleReactions.get(i);
				break;
			}
		}
		
		Map<Reaction, Double> generatedReaction = new HashMap<Reaction, Double>();
		generatedReaction.put(chosenReaction, 1.0);
		return generatedReaction;
		
	}
	
	/**
	 * Generates a vector of reaction strengths calculated from their chance factor
	 * for the given scenario. The strengths sum to 1. Returns the vector in the form
	 * of a map.
	 * 
	 * @param scenario the input scenario
	 * @return map of reactions linked to their respective strengths
	 */
	private static Map<Reaction, Double> reactionVectorAlgorithm(Scenario triggerScenario) {
		
		// Calculate the sum of the reaction chance factors for the scenario
		int chanceSum = 0;
		for (Reaction r : triggerScenario.getPossibleReactions()) {
			chanceSum += triggerScenario.getReactionChance(r);
		}
		
		// Create a copy of the reaction-chance mapping, and convert each chance factor 
		// to a strength value out of 1 by diving the factor by the sum
		Map<Reaction, Double> reactionVector = new HashMap<Reaction, Double>();
		for (Reaction r : triggerScenario.getPossibleReactions()) {
			Double strength = (double)triggerScenario.getReactionChance(r)/(double)chanceSum;
			reactionVector.put(r, strength);
		}
		
		return reactionVector;
		
	}
	
}
