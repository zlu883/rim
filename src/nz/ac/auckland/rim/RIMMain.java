package nz.ac.auckland.rim;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

import nz.ac.auckland.rim.data.RIMDataLibrary;
import nz.ac.auckland.rim.motion_modules.FuroiHomeMotionModule;
import nz.ac.auckland.rim.motion_modules.FuroiParrotMotionModule;
import nz.ac.auckland.rim.websocket.WebSocketManager;

public class RIMMain {

	public static void main(String[] args) {
		
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
		
		/*for (int j = 0; j < 100; j++ ) {
		List<MotionUnit> u = MotionGenerator.generateMotion(RIMDataLibrary.getReaction("greet"));
		for (int i = 0; i < u.size(); i++) {
			System.out.println(u.get(i).getName());
		}
		System.out.println();
		}*/
		
		WebSocketManager.sendMessage("hohoh");
	}
	
	public static void process(String scenario) {
		
	}

}
