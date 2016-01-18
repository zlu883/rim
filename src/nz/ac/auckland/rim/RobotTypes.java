package nz.ac.auckland.rim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;
import nz.ac.auckland.rim.motion_modules.RobotMotionModule;

public class RobotTypes {

	private static Map<String, RobotType> _robotTypes;
	private static RobotType _activeRobotType;
	
	public static void init() {
		
		if (_robotTypes == null) {
			
			// load robot type config file
			Document doc = XmlParser.parseToDocument("RobotType_Config.xml");
			
			// register robot types
			_robotTypes = new HashMap<String, RobotType>();
			NodeList robotTypes = doc.getElementsByTagName("robotType");
			String robotTypeName;
			for (int i = 0; i < robotTypes.getLength(); i++) {
				robotTypeName = ((Element)robotTypes.item(i)).getAttribute("name");
				_robotTypes.put(robotTypeName, new RobotType(robotTypeName));
			}
			
			// register active robot type
			NodeList activeRobotType = doc.getElementsByTagName("activeRobotType");
			if (activeRobotType.getLength() == 1) {
				robotTypeName = ((Element)activeRobotType.item(0)).getAttribute("name");
				if (_robotTypes.containsKey(robotTypeName)) {
					_activeRobotType = _robotTypes.get(robotTypeName);
				} else {
					// unregistered robot type
				}
			} else {
				// not unique
			}
		}
		
		// REGISTER ROBOT MOTION MODULES HERE
		
		_robotTypes.get("FuroiHome").registerMotionModule(FuroiHomeMotionModule.getInstance());
		_robotTypes.get("FuroiParrot").registerMotionModule(FuroiParrotMotionModule.getInstance());

	}
	
	public RobotType getActiveRobotType() {
		return _activeRobotType;
	}
}
