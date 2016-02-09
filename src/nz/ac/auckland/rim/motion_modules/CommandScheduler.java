package nz.ac.auckland.rim.motion_modules;

import java.util.ArrayList;
import java.util.List;

import nz.ac.auckland.rim.websocket.WebSocketManager;

/**
 * Represents a queue of robot commands, with their repective durations,
 * ready to be executed.
 * @author Jonny Lu
 *
 */
public class CommandScheduler {

	private List<String> _commands;
	private List<Long> _durations;
	private CommandExecutionThread _executionThread;
	
	public CommandScheduler() {
		_commands = new ArrayList<String>();
		_durations = new ArrayList<Long>();
	}
	
	public void queueCommand(String command, long duration) {
		_commands.add(command);
		_durations.add(duration);
	}
	
	/**
	 * Run the queued commands in their specific order. Each command lasts its
	 * corresponding duration.
	 * @param numberOfimes number of times you wish to repeat the run
	 */
	public void runCommands(int numberOfTimes) {
		_executionThread = new CommandExecutionThread(_commands, _durations, false, numberOfTimes);
		_executionThread.start();
	}
	
	/**
	 * Run the queued commands in repeat indefinitely (until external termination).
	 */
	public void runCommandsInRepeat() {
		_executionThread = new CommandExecutionThread(_commands, _durations, true, 0);
		_executionThread.start();
	}
	
	/**
	 * Terminates the thread running the scheduled commands.
	 */
	public void stopCommands() {
		_executionThread.interrupt();
	}
	
	/**
	 * Thread implementation that handles the running of scheduled commands,
	 * by sending them away via WebSocketManager.
	 */
	private class CommandExecutionThread extends Thread {
		
		private List<String> _commands;
		private List<Long> _durations;
		private boolean _runInRepeat;
		private int _numberOfRepeat;
				
		public CommandExecutionThread(List<String> commands, List<Long> durations, boolean runInRepeat,
				int numberOfRepeat) {
			_commands = commands;
			_durations = durations;
			_runInRepeat = runInRepeat;
			_numberOfRepeat = numberOfRepeat;
		}
		
		public void run() {
		
			if (_runInRepeat) {
				
				threadLoop:
				while (true) {					
					for (int i = 0; i < _commands.size(); i++) {						
						if (isInterrupted()) {
							break threadLoop;
						}
						WebSocketManager.sendMessage(_commands.get(i));
						try {
							Thread.sleep(_durations.get(i));
						} catch (InterruptedException e) {
							break threadLoop;
						}
					}
				}
				
			} else {
				
				threadLoop:
				for (int repeat = 0; repeat < _numberOfRepeat; repeat++) {
					for (int i = 0; i < _commands.size(); i++) {						
						if (isInterrupted()) {
							break threadLoop;
						}
						WebSocketManager.sendMessage(_commands.get(i));
						try {
							Thread.sleep(_durations.get(i));
						} catch (InterruptedException e) {
							break threadLoop;
						}
					}
				}
				
			}
		}
		
	}
}
