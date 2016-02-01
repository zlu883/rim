package nz.ac.auckland.rim.data;

import java.util.ArrayList;
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
		
		// register scenario to reaction chance mapping
		Document scenarioReactionMappingDoc = XmlParser.parseToDocument("config/scenario_to_reaction_mapping.xml");
		
		
	}
	
	// Getter methods for retrieving various data objects using their name.
				
	public static RobotType getRobotType(String name) {
		for (RobotType t : _robotTypes) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		throw new RIMException("Attempt to get unregistered robot type: " + name);
	}
	
	public static MotionPart getMotionPart(String name) {
		for (MotionPart p : _motionParts) {
			if (p.getName().equals(name)) {
				return p;
			}
		}
		throw new RIMException("Attempt to get unregistered motion part: " + name);
	}
	
	public static MotionUnit getMotionUnit(String name) {
		for (MotionUnit u : _motionUnits) {
			if (u.getName().equals(name)) {
				return u;
			}
		}
		throw new RIMException("Attempt to get unregistered motion unit: " + name);
	}
	
	public static Reaction getReaction(String name) {
		for (Reaction r : _reactions) {
			if (r.getName().equals(name)) {
				return r;
			}
		}
		throw new RIMException("Attempt to get unregistered reaction: " + name);
	}
	
	public static Scenario getScenario(String name) {
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
	
}
