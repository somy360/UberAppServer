/*
 * This class listens for new connections and passes them to the client handler. Identifies client drivers and client passengers.
 * Makes a list of connected drivers and filters them to return a list of valid drivers to the passenger clients. 	 
 */

package uberAppServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	// Global attributes.
	private static final int port = 40138;
	//HashMap of user names and location of valid driver. 
	private HashMap<String,String>  validClientsDrivers= new HashMap<String,String>();
	private ArrayList<ClientHandler> allClients = new ArrayList<>();
	private ExecutorService pool = Executors.newFixedThreadPool(10);
	private ServerSocket serverSocket;

	/** Constructor initialises global attributes and method to listen for the clients.*/
	Server(){
		try {
			initialise();
			clientListener();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	/**Initialising server socket.*/
	private void initialise() throws IOException {
		serverSocket = new ServerSocket(port);
	}

	/** Accepting a clients. Making client threads. Adding the client to the pool of threads.
	 * Initialises method to make a list of valid drivers.*/
	private void clientListener() throws IOException {
		while(true) {
			Socket clientSocket = serverSocket.accept();
			ClientHandler clientThread = new ClientHandler(clientSocket);
			pool.execute(clientThread);
			allClients.add(clientThread); 
			//Pauses this thread for one second to allow the other threads to complete. 
			try{
				Thread.sleep(1000);
			}catch(Exception e) {
				e.printStackTrace();
			}
			makeListOfValidDrivers();
		}
	}


	/**Make a list of the drivers that have the same location than the passenger.*/
	public void makeListOfValidDrivers(){
		for(int i=0; i<allClients.size();i++){
			//Glasgow location was put just for testing purposes. 
			if(allClients.get(i).getLocation().equals("Glasgow")){
				//Hash map with user names as IDs and locations as values.
				//We have chosen a hashmap due to time complexity. We will use it for retrieving drivers in future updates.  
				validClientsDrivers.put(allClients.get(i).getUserName(), allClients.get(i).getLocation());
			}
		}

		//Print just for testing purposes. 
		//		System.out.println("Array of all clients");
		//		for(ClientHandler temp:allClients) {
		//			System.out.println(temp);	
		//		}
		//
		//		System.out.println("Valid drivers with location Glasgow");
		//
		//		for (String temp:validClientsDrivers.keySet()) {
		//			System.out.println(temp + " " + validClientsDrivers.get(temp));	
		//		}


	}

}

