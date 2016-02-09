package nz.ac.auckland.rim.websocket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class WebSocketManager {

	public static String RobotManagerURI = null;
	public static InetSocketAddress RIMServerPort = null;
	
	private static RIMWebSocketClient _RIMclient;
	private static RIMWebSocketServer _RIMserver;
	
	private WebSocketManager() {}
	
	public static void init() throws URISyntaxException, UnknownHostException {
		
		_RIMclient = new RIMWebSocketClient(new URI(RobotManagerURI));
		_RIMclient.connect();
		_RIMserver = new RIMWebSocketServer(RIMServerPort);
		_RIMserver.start();
	}
	
	public static void sendMessage(String command) {
		if (_RIMclient.isOpen())
			_RIMclient.send(command);
	}
}
