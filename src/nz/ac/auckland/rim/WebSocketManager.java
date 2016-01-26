package nz.ac.auckland.rim;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class WebSocketManager {

	public static final String RobotManagerURI = "ws://localhost:33333";
	public static final InetSocketAddress RIMServerPort = new InetSocketAddress(33336);
	
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
