package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ReactionGenerator {
	
	private static Map<String, Scenario> _scenarios;
	
	private ReactionGenerator() {}
	
	public static void init() {
		
		if (_scenarios == null) {
			
			// load scenario-reaction mapping config
			Document doc = XmlParser.parseToDocument("Scenario_Reaction_Mapping.xml");

			// register scenarios
			_scenarios = new HashMap<String, Scenario>();
			NodeList scenarios = doc.getElementsByTagName("scenario");
			String scenarioName;
			for (int i = 0; i < scenarios.getLength(); i++) {
				Name = ((Element) robotTypes.item(i)).getAttribute("name");
				_robotTypes.put(robotTypeName, new RobotType(robotTypeName));
			}

			// register active robot type
			NodeList activeRobotType = doc.getElementsByTagName("activeRobotType");
			if (activeRobotType.getLength() == 1) {
				robotTypeName = ((Element) activeRobotType.item(0))
						.getAttribute("name");
				if (_robotTypes.containsKey(robotTypeName)) {
					_activeRobotType = _robotTypes.get(robotTypeName);
				} else {
					// unregistered robot type
				}
			} else {
				// not unique
			}

	}
	
	public static ReactionGenerator getInstance() {
		return INSTANCE;
	}
	
	private void loadScenarioReactionMapping() {
		
	}
	
}
