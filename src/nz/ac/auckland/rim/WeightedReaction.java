package nz.ac.auckland.rim;

import java.util.List;

import nz.ac.auckland.rim.data.Reaction;

/**
 * A structure consisting of a reaction with its associated weight value.
 * 
 * @author Jonny Lu
 *
 */
public class WeightedReaction {
	
	private Reaction _reaction;
	private int _weight;
	
	public WeightedReaction(Reaction reaction, int weight) {
		_reaction = reaction;
		_weight = weight;
	}
	
	public Reaction getReaction() {
		return _reaction;
	}
	
	public int getWeight() {
		return _weight;
	}
}
