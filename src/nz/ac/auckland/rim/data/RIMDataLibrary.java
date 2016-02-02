package nz.ac.auckland.rim.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.auckland.rim.RIMException;
import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Static class which loads and stores all RIM data at program startup, and
 * provides an interface for querying the data.
 * 
 * @author Jonny Lu
 *
 */
public class RIMDataLibrary {
	
	private static List<Scenario> _scenarios;
	private static List<Reaction> _reactions;
	private static List<MotionPart> _motionParts;
	private static List<MotionUnit> _motionUnits;
	private static List<RobotType> _robotTypes;
	private static RobotType _activeRobotType;
	
	private static int[][] _scenarioToReactionMapping;
	private static int[][] _motionUnitToReactionMapping;
	
	private RIMDataLibrary() {}
	
	/**
	 * Load all relevant RIM data from the configurable files.
	 * Only called once at program startup.
	 */
	public static void init() {
		
		// Register all lists of data elements first, then register the mappings between them.
		
		// register list of scenarios
		_scenarios = new ArrayList<Scenario>();
		Document scenarioListDoc = XmlParser.parseToDocument("config/scenario_list.xml");
		NodeList scenarioListNodes = scenarioListDoc.getElementsByTagName("scenario");
		for (int i = 0; i < scenarioListNodes.getLength(); i++) {
			String scenarioName = ((Element)scenarioListNodes.item(i)).getAttribute("name");
			_scenarios.add(new Scenario(scenarioName));
		}
		
		// register list of reactions
		_reactions = new ArrayList<Reaction>();
		Document reactionListDoc = XmlParser.parseToDocument("config/reaction_list.xml");
		NodeList reactionListNodes = reactionListDoc.getElementsByTagName("reaction");
		for (int i = 0; i < reactionListNodes.getLength(); i++) {
			String reactionName = ((Element)reactionListNodes.item(i)).getAttribute("name");
			_reactions.add(new Reaction(reactionName));
		}
		
		// load list of motion parts + motion units
		Document motionPartAndUnitListDoc = XmlParser.parseToDocument("config/motion_part_and_motion_unit_list.xml");

		// register motion parts
		_motionParts = new ArrayList<MotionPart>();
		NodeList motionPartListNodes = motionPartAndUnitListDoc.getElementsByTagName("motionPart");
		for (int i = 0; i < motionPartListNodes.getLength(); i++) {
			String motionPartName = ((Element)motionPartListNodes.item(i)).getAttribute("name");
			_motionParts.add(new MotionPart(motionPartName));
		}
		
		// register motion units
		_motionUnits = new ArrayList<MotionUnit>();
		NodeList motionUnitListNodes = motionPartAndUnitListDoc.getElementsByTagName("motionUnit");
		for (int i = 0; i < motionUnitListNodes.getLength(); i++) {
			String motionUnitName = ((Element)motionUnitListNodes.item(i)).getAttribute("name");
			_motionUnits.add(new MotionUnit(motionUnitName));
		}
		
		// register two-way relationships between motion parts and their corresponding motion units
		for (int i = 0; i < motionPartListNodes.getLength(); i++) {
			Element motionPartNode = (Element)motionPartListNodes.item(i);
			String motionPartName = motionPartNode.getAttribute("name");
			MotionPart p = getMotionPart(motionPartName);
			NodeList motionUnitNodes = motionPartNode.getChildNodes();
			for (int j = 0; j < motionUnitNodes.getLength(); j++) {
				Element motionUnitNode = (Element)motionUnitNodes.item(j);
				if (motionUnitNode.getTagName().equals("motionUnit")) {
					String motionUnitName = motionUnitNode.getAttribute("name");
					MotionUnit u = getMotionUnit(motionUnitName);
					p.registerMotionUnit(u);
					u.registerActuator(p);
				}
			}
		}
		
		// register robot types
		_robotTypes = new ArrayList<RobotType>();
		Document robotTypeListDoc = XmlParser.parseToDocument("config/robotType_list.xml");
		NodeList robotTypeListNodes = motionPartAndUnitListDoc.getElementsByTagName("robotType");
		for (int i = 0; i < robotTypeListNodes.getLength(); i++) {
			String robotTypeName = ((Element)robotTypeListNodes.item(i)).getAttribute("name");
			_robotTypes.add(new RobotType(robotTypeName));
		}
		
		// register active robot type
		NodeList activeRobotTypeNode = robotTypeListDoc.getElementsByTagName("activeRobotType");
		if (activeRobotTypeNode.getLength() == 1) {
			String activeRobotTypeName = ((Element)activeRobotTypeNode.item(0)).getAttribute("name");
			_activeRobotType = getRobotType(activeRobotTypeName);
		} else {
			throw new RIMException("No or more than one active robot type found");
		}
		
		// register robot type to motion part mapping
		Document robotTypeMotionPartMappingDoc = XmlParser.parseToDocument("config/robot_type_to_motion_part_mapping.xml");
		NodeList mappedRobotTypeNodes = robotTypeMotionPartMappingDoc.getElementsByTagName("robotType");
		for (int i = 0; i < mappedRobotTypeNodes.getLength(); i++) {
			Element mappedRobotTypeNode = (Element)mappedRobotTypeNodes.item(i);
			String robotTypeName = mappedRobotTypeNode.getAttribute("name");
			RobotType t = getRobotType(robotTypeName);
			NodeList mappedMotionPartNodes = mappedRobotTypeNode.getChildNodes();
			for (int j = 0; j < mappedMotionPartNodes.getLength(); j++) {
				Element mappedMotionPartNode = (Element)mappedMotionPartNodes.item(j);
				if (mappedMotionPartNode.getTagName().equals("motionPart")) {
					String motionPartName = mappedMotionPartNode.getAttribute("name");
					MotionPart p = getMotionPart(motionPartName);
					t.registerMotionPart(p);
				}
			}
		}
	
		// register scenario to reaction chance mapping
		Document scenarioReactionMappingDoc = XmlParser.parseToDocument("config/scenario_to_reaction_mapping.xml");
		_scenarioToReactionMapping = new int[_scenarios.size()][_reactions.size()]; // all values default to 0
		NodeList mappedScenarioNodes = scenarioReactionMappingDoc.getElementsByTagName("scenario");
		// loop through each scenario and each of their mapped reaction chances
		for (int i = 0; i < mappedScenarioNodes.getLength(); i++) {
			String scenarioName = ((Element)mappedScenarioNodes.item(i)).getAttribute("name");
			Scenario mappedScenario = getScenario(scenarioName);
			NodeList mappedReactionNodes = mappedScenarioNodes.item(i).getChildNodes();
			for (int j = 0; j < mappedReactionNodes.getLength(); j++) {
				Element mappedReactionNode = ((Element)mappedReactionNodes.item(j));
				if (mappedReactionNode.getTagName().equals("reaction")) {
					String reactionName = mappedReactionNode.getAttribute("name");
					Reaction mappedReaction = getReaction(reactionName);
					int reactionChance = Integer.parseInt(mappedReactionNode.getAttribute("chance"));
					/* put the chance value in the mapping matrix, its position in respect to
					 * the indices of the scenario and reaction lists */
					int scenarioId = _scenarios.indexOf(mappedScenario);
					int reactionId = _reactions.indexOf(mappedReaction);
					_scenarioToReactionMapping[scenarioId][reactionId] = reactionChance;
				}
			}
		}
		
		// register motion unit to reaction strength mapping
		Document motionUnitReactionMappingDoc = XmlParser.parseToDocument("config/motion_unit_to_reaction_mapping.xml");
		_motionUnitToReactionMapping = new int[_motionUnits.size()][_reactions.size()]; // all values default to 0
		NodeList mappedMotionUnitNodes = motionUnitReactionMappingDoc.getElementsByTagName("motionUnit");
		// loop through each motion unit and each of their mapped reaction strengths
		for (int i = 0; i < mappedMotionUnitNodes.getLength(); i++) {
			String motionUnitName = ((Element) mappedMotionUnitNodes.item(i)).getAttribute("name");
			MotionUnit mappedMotionUnit = getMotionUnit(motionUnitName);
			NodeList mappedReactionNodes = mappedMotionUnitNodes.item(i).getChildNodes();
			for (int j = 0; j < mappedReactionNodes.getLength(); j++) {
				Element mappedReactionNode = ((Element)mappedReactionNodes.item(j));
				if (mappedReactionNode.getTagName().equals("reaction")) {
					String reactionName = mappedReactionNode.getAttribute("name");
					Reaction mappedReaction = getReaction(reactionName);
					int reactionStrength = Integer.parseInt(mappedReactionNode.getAttribute("strength"));
					int motionUnitId = _motionUnits.indexOf(mappedMotionUnit);
					int reactionId = _reactions.indexOf(mappedReaction);
					_motionUnitToReactionMapping[motionUnitId][reactionId] = reactionStrength;
				}
			}
		}
	}
	
	// Getter methods for retrieving various data objects.
				
	public static RobotType getRobotType(String name) {
		if (_robotTypes == null) {
			throw new RIMException("RIM data library not initialized");
		}
		for (RobotType t : _robotTypes) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		throw new RIMException("Attempt to get unregistered robot type: " + name);
	}
	
	public static MotionPart getMotionPart(String name) {
		if (_motionParts == null) {
			throw new RIMException("RIM data library not initialized");
		}
		for (MotionPart p : _motionParts) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		throw new RIMException("Attempt to get unregistered motion part: " + name);
	}
	
	public static MotionUnit getMotionUnit(String name) {
		if (_motionUnits == null) {
			throw new RIMException("RIM data library not initialized");
		}
		for (MotionUnit u : _motionUnits) {
			if (u.getName().equals(name)) {
				return u;
			}
		}
		throw new RIMException("Attempt to get unregistered motion unit: " + name);
	}
	
	public static Reaction getReaction(String name) {
		if (_reactions == null) {
			throw new RIMException("RIM data library not initialized");
		}
		for (Reaction r : _reactions) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		throw new RIMException("Attempt to get unregistered reaction: " + name);
	}
	
	public static Scenario getScenario(String name) {
		if (_scenarios == null) {
			throw new RIMException("RIM data library not initialized");
		}
		for (Scenario s : _scenarios) {
			if (s.getName().equals(name)) {
				return s;
			}
		}
		throw new RIMException("Attempt to get unregistered scenario: " + name);
	}
	
	public static RobotType getActiveRobotType() {
		if (_activeRobotType == null) {
			throw new RIMException("Active robot type not registered");
		}
		return _activeRobotType;
	}
	
	// Getter methods for the lists of data objects. Always returns a clone of the stored list.
	
	public static List<Scenario> getScenarioList() {
		List<Scenario> listToReturn = new ArrayList<Scenario>();
		listToReturn.addAll(_scenarios);
		return listToReturn;
	}
	
	public static List<Reaction> getReactionList() {
		List<Reaction> listToReturn = new ArrayList<Reaction>();
		listToReturn.addAll(_reactions);
		return listToReturn;
	}
	
	public static List<MotionPart> getMotionPartList() {
		List<MotionPart> listToReturn = new ArrayList<MotionPart>();
		listToReturn.addAll(_motionParts);
		return listToReturn;
	}
	
	public static List<MotionUnit> getMotionUnitList() {
		List<MotionUnit> listToReturn = new ArrayList<MotionUnit>();
		listToReturn.addAll(_motionUnits);
		return listToReturn;
	}
	
	public static List<RobotType> getRobotTypeList() {
		List<RobotType> listToReturn = new ArrayList<RobotType>();
		listToReturn.addAll(_robotTypes);
		return listToReturn;
	}
	
	/**
	 * Retrieves the reaction chance vector for a scenario (the corresponding
	 * row in the scenario-reaction mapping matrix). Always return a clone
	 * of the vector.
	 * @param s the scenario
	 * @return a vector of chance values, each corresponding to the reaction
	 * at the same position in the reaction list
	 */
	public  static int[] getReactionChanceVector(Scenario s) {
		int scenarioId = _scenarios.indexOf(s);
		int[] chanceVector = _scenarioToReactionMapping[scenarioId];
		return Arrays.copyOf(chanceVector, chanceVector.length);
	}
	
	/**
	 * Retrieves the reaction strength vector for a motion unit (the corresponding
	 * row in the motion unit-reaction mapping matrix). Always return a clone
	 * of the vector.
	 * @param u the motion unit
	 * @return a vector of strength values, each corresponding to the reaction
	 * at the same position in the reaction list
	 */
	public static int[] getReactionStrengthVector(MotionUnit u) {
		int motionUnitId = _motionUnits.indexOf(u);
		int[] strengthVector =  _motionUnitToReactionMapping[motionUnitId];
		return Arrays.copyOf(strengthVector, strengthVector.length);
	}
	
}
