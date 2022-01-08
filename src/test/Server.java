package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {

	public interface ClientHandler{
		public void handle(Socket client);
	}

	volatile boolean stop;
	public Server() {
		stop=false;
	}

	private int port;
	private ClientHandler clientHandler;

	private void startServer(int port, ClientHandler ch) {
		//ServerSocket server = null;
		try {
			ServerSocket server = new ServerSocket(port);
			server.setSoTimeout(1000);
			while(!stop) {
				try {
					if (server.isClosed()) {
						break;
					}
					Socket aClient = server.accept();
					try {
						ch.handle(aClient);
						aClient.close();
					}
					catch (IOException e){
						e.printStackTrace();
					}
				}
				catch (SocketTimeoutException e){
					e.printStackTrace();
				}
				server.close();
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

		// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}
}
