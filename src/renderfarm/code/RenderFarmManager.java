package renderfarm.code;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RenderFarmManager {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	public void start(int port) {

		// Create a new port to listen for incoming connections
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			ErrorLogger.logStack(e, "IOException");
		}

		// Initiates connection when a incoming client attempts to connect
		try {
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			ErrorLogger.logStack(e, "IOException");
		}

		// Creates output stream from server to client
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			ErrorLogger.logStack(e, "IOException");
		}

		// Creates input stream from client to server
		try {
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			ErrorLogger.logStack(e, "IOException");
		}

		boolean termanateServer = false;

		while (!termanateServer) {
			String incomingData = null;

			// Sets any information received to variable greeting
			try {
				incomingData = in.readLine();
			} catch (IOException e) {
				ErrorLogger.logStack(e, "IOException");
			}

			if ("hello server".equals(incomingData)) {
				out.println("hello client");
			} else {
				out.println("unrecognised greeting");
			}

			// end connection on client request
			if ("TerminateConnection".equals(incomingData)) {

				stop();
				out.close();

			}

			if ("BootTimeConnection".equals(incomingData)) {
				out.println("ServerAlreadyRunning");
				stop();
				out.close();

			}
		}

	}

	public void stop() {

		//
		try {
			in.close();
		} catch (IOException e) {
			ErrorLogger.logStack(e, "IOException");

		}

		out.close();

		//
		try {
			clientSocket.close();
		} catch (IOException e) {
			ErrorLogger.logStack(e, "IOException");
		}

		//
		try {
			serverSocket.close();
		} catch (IOException e) {
			ErrorLogger.logStack(e, "IOException");
		}
	}
}
