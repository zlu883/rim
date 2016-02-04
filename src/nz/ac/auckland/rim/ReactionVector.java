package nz.ac.auckland.rim;

import java.util.List;

import nz.ac.auckland.rim.data.Reaction;

/**
 * A structure consisting of two lists that serve to contain a vector of reactions
 * with their associated weights.
 * 
 * @author zlu883
 *
 */
public class ReactionVector {
	
	private List<Reaction> _reactions;
	private List<Double> _weights;
	
	public ReactionVector(List<Reaction> reactions, List<Double> weights) {
		_reactions = reactions;
		_weights = weights;
	}
	
	public List<Reaction> getReactions() {
		return _reactions;
	}
	
	public List<Double> getWeights() {
		return _weights;
	}
}
