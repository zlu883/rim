package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReactionGenerator {
	
	private ReactionGenerator() {}
	
	public static Reaction generateReaction(String scenarioName) {
		
		return singleReactionAlgorithm(scenarioName);
	}
	
	private static Reaction singleReactionAlgorithm(String scenarioName) {
		
		ArrayList<Reaction> possibleReactions = new ArrayList<Reaction>();
		ArrayList<Integer> reactionChances = new ArrayList<Integer>();
		int chanceSum = 0;
		Scenario s = RIMDataLibrary.getScenario(scenarioName);
		for (Reaction r : s.getPossibleReactions()) {
			possibleReactions.add(r);
			int chance = s.getReactionChance(r);
			reactionChances.add(chance);
			chanceSum += chance; 
		}
		int rand = new Random().nextInt(chanceSum) + 1;
		chanceSum = 0;
		for (int i = 0; i < reactionChances.size(); i++) {
			chanceSum += reactionChances.get(i);
			if (rand <= chanceSum) {
				return possibleReactions.get(i);
			}
		}
		
		return null;
	}
	
}
