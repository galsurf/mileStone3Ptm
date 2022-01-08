package test;

import test.Commands.DefaultIO;
import test.Server.ClientHandler;

import java.io.*;
import java.net.Socket;

public class AnomalyDetectionHandler implements ClientHandler{

	SocketIO sio;

	@Override
	public void handle(Socket client) {
		this.sio = new SocketIO(client);
		CLI clientCli = new CLI(this.sio);
		clientCli.start();
		sio.close();
	}

	public class SocketIO implements DefaultIO{
		BufferedReader in;
		PrintWriter out;

		public SocketIO(Socket socket) {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new PrintStream(socket.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public String readText() throws IOException {
			return in.readLine();
		}

		@Override
		public void write(String text) {
			out.print(text);
			out.flush();
		}

		@Override
		public float readVal() {
			float returnFloat = 0;
			try {
				returnFloat =  Float.parseFloat(in.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return returnFloat;
		}

		@Override
		public void write(float val) {
			out.print(val);
		}

		public void close() {
//			try {
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			out.close();
		}
	}

}
