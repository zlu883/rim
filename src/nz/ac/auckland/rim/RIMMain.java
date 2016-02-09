package nz.ac.auckland.rim;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import org.w3c.dom.Document;

import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RIMDataLibrary;
import nz.ac.auckland.rim.data.XmlParser;
import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;
import nz.ac.auckland.rim.motion_modules.RobotMotionModule;
import nz.ac.auckland.rim.websocket.WebSocketManager;

/**
 * Entry point for the initialization of the Robot Interaction Manager.
 * 
 * @author Jonny Lu
 *
 */
public class RIMMain {

	public static void main(String[] args) {
		
		// Load general configurations
		Document RIMConfigDoc = XmlParser.parseToDocument("config/rim_general_config.xml");
		
		String reactionGenScheme = RIMConfigDoc.getElementsByTagName("reactionGenerationScheme").item(0).getTextContent();
		ReactionGenerator.setAlgorithmType(ReactionGenerator.AlgorithmType.valueOf(reactionGenScheme));
		
		String motionGenScheme = RIMConfigDoc.getElementsByTagName("motionGenerationScheme").item(0).getTextContent();
		MotionGenerator.setAlgorithmType(MotionGenerator.AlgorithmType.valueOf(motionGenScheme));
		
		String RMServerAddress = RIMConfigDoc.getElementsByTagName("RMserverAddress").item(0).getTextContent();
		WebSocketManager.RobotManagerURI = RMServerAddress;
		
		String RIMServerPort = RIMConfigDoc.getElementsByTagName("RMserverAddress").item(0).getTextContent();
		WebSocketManager.RIMServerPort = new InetSocketAddress(Integer.parseInt(RIMServerPort));

		// Initialize components
		RIMDataLibrary.init();
		
		try {
			WebSocketManager.init();
		} catch (UnknownHostException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// REGISTER ROBOT MOTION MODULES HERE
		
		RIMDataLibrary.getRobotType("FuroiHome").registerMotionModule(FuroiHomeMotionModule.getInstance());
		RIMDataLibrary.getRobotType("FuroiParrot").registerMotionModule(FuroiParrotMotionModule.getInstance());
		
	}
	
	/**
	 * This method is the central execution flow of RIM. Called everytime a scenario string is received from
	 * the client application.
	 * @param scenario input scenario from client application
	 */
	public static void process(String scenario) {
		
		RobotMotionModule activeMotionModule = RIMDataLibrary.getActiveRobotType().getMotionModule();
		activeMotionModule.reset();
		List<WeightedReaction> generatedReactions = ReactionGenerator.generateReaction(scenario);
		List<MotionUnit> generatedMotions = MotionGenerator.generateMotion(generatedReactions);
		activeMotionModule.executeMotions(generatedMotions);
		
	}

}
