package renderfarm.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class RenderFarmClient {

	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	public void startConnection(String ip, int port) {

		//
		try {
			clientSocket = new Socket(ip, port);
			clientSocket.connect(new InetSocketAddress(ip, port), 1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ErrorLogger.logStack(e, "ClientSocket connection failure");
			e.printStackTrace();
		}

		// Creates an output stream from the client to the server
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Creates an input stream from the server to the client
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Call to communicate data to the server
	// first it sends message, waits for response, then returns servers response

	public String sendMessage(String msg) {

		// send data to the server
		out.println(msg);

		// initiates variable to store data received from the server
		String response = null;

		// attempt to receive data from the server
		try {
			response = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public void stopConnection() {

		//
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		out.close();

		//
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
