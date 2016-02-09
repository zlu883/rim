package nz.ac.auckland.rim.motion_modules;

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.rim.data.MotionUnit;
import nz.ac.auckland.rim.data.RobotType;

public class FuroiHomeMotionModule implements RobotMotionModule {

	private static FuroiHomeMotionModule INSTANCE;
	
	private RobotType _robotType;
	private List<CommandScheduler> _commandSchedulers;
	
	private FuroiHomeMotionModule() {
		_commandSchedulers = new ArrayList<CommandScheduler>();
	}
	
	public static FuroiHomeMotionModule getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FuroiHomeMotionModule();
		return INSTANCE;
	}

	@Override
	public void setRobotType(RobotType t) {
		_robotType = t;
	}

	@Override
	public RobotType getRobotType() {
		return _robotType;
	}

	@Override
	public void executeMotions(List<MotionUnit> motions) {
		
		for (MotionUnit m : motions) {
			String motionName = m.getName();
			switch (motionName) {
				case "LEDshowHappiness": LEDshowHappiness(); break; 
				case "LEDshowSadness": LEDshowSadness(); break;
				case "LEDshowAlert": LEDshowAlert(); break;
				case "LEDshowFastProcessing": LEDshowFastProcessing(); break;
				case "LEDshowSlowProcessing": LEDshowSlowProcessing(); break;
				case "speechSayHello": speechSayHello(); break;
				case "speechSayGoodWork": speechSayGoodWork(); break;
				case "speechSayCanDoBetter": speechSayCanDoBetter(); break;
				case "speechSayReady": speechSayReady(); break;
				case "speechSayBusy": speechSayBusy(); break;
				case "speechSayPleaseWait": speechSayPleaseWait(); break;
				default: break;
			}
		}
		
	}
	
	private void LEDshowHappiness() {
		
	}
	
	private void LEDshowSadness() {
		
	}
	
	private void LEDshowAlert() {
		
	}
	
	private void LEDshowFastProcessing() {
		
	}
	
	private void LEDshowSlowProcessing() {
		
	}
	
	private void speechSayHello() {
		
	}
	
	private void speechSayGoodWork() {
		
	}
	
	private void speechSayCanDoBetter() {
		
	}
	
	private void speechSayReady() {
		
	}
	
	private void speechSayBusy() {
		
	}
	
	private void speechSayPleaseWait() {
		
	}

	@Override
	public void reset() {
		for (CommandScheduler s : _commandSchedulers) {
			s.stopCommands();
		}	
	}
}
