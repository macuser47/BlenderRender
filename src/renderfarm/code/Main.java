package renderfarm.code;

public class Main {

	public static void main(String[] args) {
		int serverPort = 7000;
		String serverIP = "192.168.0.25";
		String defualtClientMessage = "BootTimeConnection";

		// Create new Client instance and attempt to connect to serve
		// if server is found remain as client
		// if server not found start a server instance

		RenderFarmClient client = new RenderFarmClient();
		boolean connectionRecieved = false;

		// attempts to check if the server is already running 5 times
		// if it is running, it then continues to be a client
		// else it will start the server

		System.out.println("Attempting to connect");

		try {
			for (int attempt = 0; attempt <= 5; attempt++) {

				System.out.println("Attempting to connect to: " + serverIP + ":" + serverPort + " " + attempt);

				client.startConnection(serverIP, serverPort);

				if (client.sendMessage(defualtClientMessage).equals("ServerAlreadyRunning")) {

					connectionRecieved = true;
					attempt = 5;

				}

			}
		} catch (java.lang.NullPointerException e) {
			ErrorLogger.logStack(e, "ServerNotFound");
		}
		
		
		System.out.println("Program has progressed past the connection attempt phase.");

		
		
		if (connectionRecieved == true) {
			System.out.println("Starting Client");
			client.startConnection(serverIP, serverPort);

		} else if (connectionRecieved == false) {
			System.out.println("Starting Server");
			RenderFarmManager server = new RenderFarmManager();
			server.start(serverPort);

		} else {

			ErrorLogger.logString("Unable to determine if connection was recieved. Main.java", "UknownConnectionError");

		}

	}

}
