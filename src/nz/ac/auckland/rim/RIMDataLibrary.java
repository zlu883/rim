package nz.ac.auckland.rim;

import java.util.HashMap;
import java.util.Map;

import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RIMDataLibrary {
	
	private static Map<String, Scenario> _scenarios;
	private static Map<String, Reaction> _reactions;
	private static Map<String, MotionPart> _motionParts;
	private static Map<String, MotionUnit> _motionUnits;
	private static Map<String, RobotType> _robotTypes;
	private static RobotType _activeRobotType;
	
	private RIMDataLibrary() {}
	
	/**
	 * Initialize all RIM data from the configurable files.
	 * The order in which the data are initialized is important.
	 * Only called once at program startup.
	 */
	public static void init() {
				
		// load all configurable files
		Document SRMapping = XmlParser.parseToDocument("config/Scenario_Reaction_Mapping.xml");
		Document MPMUMapping = XmlParser.parseToDocument("config/MotionPart_MotionUnit_Mapping.xml");
		Document RMMapping = XmlParser.parseToDocument("config/Reaction_Motion_Mapping.xml");
		Document RTConfig = XmlParser.parseToDocument("config/RobotType_Config.xml");

		// register motion units
		_motionUnits = new HashMap<String, MotionUnit>();
		NodeList motionUnits = MPMUMapping.getElementsByTagName("motionUnit");
		for (int i = 0; i < motionUnits.getLength(); i++) {
			String motionUnitName = ((Element)motionUnits.item(i)).getAttribute("name");
			_motionUnits.put(motionUnitName, new MotionUnit(motionUnitName));
		}
		
		// register motion parts
		_motionParts = new HashMap<String, MotionPart>();
		NodeList motionParts = MPMUMapping.getElementsByTagName("motionPart");
		for (int i = 0; i < motionParts.getLength(); i++) {
			String motionPartName = ((Element)motionParts.item(i)).getAttribute("name");
			MotionPart newPart = new MotionPart(motionPartName);
			_motionParts.put(motionPartName, newPart);
			NodeList availableMotionUnits = motionParts.item(i).getChildNodes();
			for (int j = 0; j < availableMotionUnits.getLength(); j++) {
				if (availableMotionUnits.item(j).getNodeType() == Node.ELEMENT_NODE) {
					MotionUnit u = _motionUnits.get(((Element)(availableMotionUnits.item(j))).getAttribute("name"));
					newPart.registerMotionUnit(u);
					u.registerActuator(newPart);
				}
			}
		}
		
		// register robot types
		_robotTypes = new HashMap<String, RobotType>();
		NodeList robotTypes = RTConfig.getElementsByTagName("robotType");
		String robotTypeName;
		for (int i = 0; i < robotTypes.getLength(); i++) {
			robotTypeName = ((Element)robotTypes.item(i)).getAttribute("name");
			RobotType newType = new RobotType(robotTypeName);
			_robotTypes.put(robotTypeName, newType);
			NodeList availableMotionParts = robotTypes.item(i).getChildNodes();
			for (int j = 0; j < availableMotionParts.getLength(); j++) {
				if (availableMotionParts.item(j).getNodeType() == Node.ELEMENT_NODE) {
					MotionPart p = _motionParts.get(((Element)availableMotionParts.item(j)).getAttribute("name"));
					newType.registerMotionPart(p);
				}
			}
		}
		
		// register active robot type
		NodeList activeRobotType = RTConfig.getElementsByTagName("activeRobotType");
		if (activeRobotType.getLength() == 1) {
			robotTypeName = ((Element)activeRobotType.item(0)).getAttribute("name");
			if (_robotTypes.containsKey(robotTypeName)) {
				_activeRobotType = _robotTypes.get(robotTypeName);
			} else {
				// unregistered robot type
				System.out.println("?????");
			}
		} else {
			// not unique
			System.out.println("??");

		}
		
		// register reactions
		_reactions = new HashMap<String, Reaction>();
		NodeList reactions = RMMapping.getElementsByTagName("reaction");
		String reactionName;
		for (int i = 0; i < reactions.getLength(); i++) {
			reactionName = ((Element) reactions.item(i)).getAttribute("name");
			Reaction newReaction = new Reaction(reactionName);
			_reactions.put(reactionName, newReaction);

			// register reaction-motion mapping data
			NodeList motions = reactions.item(i).getChildNodes();
			for (int j = 0; j < motions.getLength(); j++) {
				if (motions.item(j).getNodeType() == Node.ELEMENT_NODE) {
					newReaction.registerMotionChance(_motionUnits.get(((Element)motions.item(j)).getAttribute("name")), 
							Integer.parseInt(((Element)motions.item(j)).getAttribute("chance")));
				}
			}
		}				
		
		// register scenarios
		_scenarios = new HashMap<String, Scenario>();
		NodeList scenarios = SRMapping.getElementsByTagName("scenario");
		String scenarioName;
		for (int i = 0; i < scenarios.getLength(); i++) {
			scenarioName = ((Element) scenarios.item(i)).getAttribute("name");
			Scenario newScenario = new Scenario(scenarioName);
			_scenarios.put(scenarioName, newScenario);

			// register scenario-reaction mapping data
			reactions = scenarios.item(i).getChildNodes();
			for (int j = 0; j < reactions.getLength(); j++) {
				if (reactions.item(j).getNodeType() == Node.ELEMENT_NODE) {
					newScenario.registerReactionChance(_reactions.get(((Element) reactions.item(j)).getAttribute("name")),
							(Integer.parseInt(((Element) reactions.item(j)).getAttribute("chance"))));
				}
			}
		}				
	}
				
	public static RobotType getRobotType(String name) {
		return _robotTypes.get(name);
	}
	
	public static MotionPart getMotionPart(String name) {
		return _motionParts.get(name);
	}
	
	public static MotionUnit getMotionUnit(String name) {
		return _motionUnits.get(name);
	}
	
	public static Reaction getReaction(String name) {
		return _reactions.get(name);
	}
	public static Scenario getScenario(String name) {
		return _scenarios.get(name);
	}
	
	public static RobotType getActiveRobotType() {
		return _activeRobotType;
	}
	
}
