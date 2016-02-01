package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import nz.ac.auckland.rim.data.MotionPart;
import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RIMDataLibrary;
import nz.ac.auckland.rim.data.Reaction;

public class MotionGenerator {

	private MotionGenerator() {}
	
	public static List<MotionUnit> generateMotion(Reaction reaction) {
		
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
	
}
