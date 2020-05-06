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

/**
 * 
 * @author Alexandra and Graeme
 *
 */
public class Server {

	// Global attributes.
	private static final int port = 40138;
	
	//HashMap of user names and location of valid driver. 
	private HashMap<String,String>  validClientsDrivers= new HashMap<String,String>();
	//Hashmap stores valid Drivers and their corresponding ClientHandlers (so we can call them later)
	private HashMap<String,ClientHandler> validDriverMap= new HashMap<String,ClientHandler>();
	//ArrayList of all Drivers
	private ArrayList<ClientHandler> driverClients = new ArrayList<>();
	
	//for executing threads
	private ExecutorService pool = Executors.newFixedThreadPool(100);
	
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

	/**Initialising server socket
	 * 
	 * @throws IOException
	 */
	private void initialise() throws IOException {
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Accepting a clients. Making client threads. Adding the client to the pool of threads.
	 * Initialises method to make a list of valid drivers.
	 * 
	 * @throws IOException
	 */
	private void clientListener() throws IOException {
		while(true) {

			//program pauses here and waits for a client to connect
			Socket clientSocket = serverSocket.accept();
			ClientHandler clientThread = new ClientHandler(clientSocket);
			pool.execute(clientThread);
			
			//Pauses this thread for one second to allow the other threads to complete. 
			try{
				Thread.sleep(1000);
			}catch(Exception e) {
				e.printStackTrace();
			}

			//if the current clientThread is a ride notification, then send notification
			if(clientThread.checkForRide()) {

				sendNotification(clientThread);
			}else {

				//check user type equals driver then add to list
				if(!identifyUserType(clientThread)) {
					driverClients.add(clientThread); 
				}

				else {
					makeListOfValidDrivers(clientThread.getLocation());
					clientThread.writeToClient(validClientsDrivers);

				}
			}
		}
	}




	/**
	 * Identify passengers from drivers.
	 * 
	 * @param clientThread a clientThread.
	 * @return true if passenger, false if driver.
	 */
	public boolean identifyUserType(ClientHandler clientThread) {

		if(clientThread.getUserType().equals("Passenger")) {
			return true;
		}else {
			return false;
		}

	}

	/**
	 * Make a list of the drivers that have the same location than the passenger.
	 * Loops through list of all drivers adds ones with same location as the parameter to the HashMaps
	 * 
	 * @param passengerLocation passengers location
	 */
	public void makeListOfValidDrivers(String passengerLocation){
		for(int i=0; i<driverClients.size();i++){
			
			if(driverClients.get(i).getLocation().equals(passengerLocation)&&driverClients.get(i).connectedUser()==true){
				
				//Hash map with user names as IDs and locations as values.
				//We have chosen a hashmap due to time complexity. We will use it for retrieving drivers in future updates.  
				validClientsDrivers.put(driverClients.get(i).getUserName(), driverClients.get(i).getLocation());

				//store the clientThreads of the drivers
				validDriverMap.put(driverClients.get(i).getUserName(),driverClients.get(i));
			}
		}
	}
	
	/**
	 * Send a notification to the selected driver.
	 * Gets the correct clientThread from the hashmap and sends the message to them
	 * 
	 * @param clientThread
	 */
	public void sendNotification(ClientHandler clientThread){

		if(validDriverMap.containsKey(clientThread.getSelectedDriver())) {
			validDriverMap.get(clientThread.getSelectedDriver()).writeBackToDriver(clientThread.getLocation(), clientThread.getUserName(), clientThread.getPassengerDestination());
		}
	}
}

