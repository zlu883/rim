package nz.ac.auckland.rim;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class RIMWebSocketClient extends WebSocketClient {

	public RIMWebSocketClient(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// TODO Auto-generated method stub
		System.out.println("RIM client opened");	
	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		System.out.println("RIM client closed");	

	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		ex.printStackTrace();
	}

}
