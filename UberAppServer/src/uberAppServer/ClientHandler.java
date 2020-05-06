/*
 * This class handles each individual connection between the server and its clients. It receives the user's data, and
 * makes it available through getters. 
 */

package uberAppServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * 
 * @author Alexandra and Graeme
 *
 */
public class ClientHandler implements Runnable{

	//Global variables.
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;

	//Global attributes for getting user's data.
	private String userType, userName, location, selectedDriver,passengerDestination;
	
	//flags
	private boolean connected = true;
	private boolean ride = false;

	/**
	 * Constructor initialises global attributes
	 * 
	 * @param clientSocket clientSocket
	 * @throws IOException IOException if client is disconnected
	 */
	public ClientHandler(Socket clientSocket) throws IOException{

		this.client = clientSocket;
		out = new PrintWriter(clientSocket.getOutputStream(), true);                   
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	    
	}


	/** Run method is executed by thread handler.
	 * Waits for input from client. Then, it parses the input and stores in global variables. 
	 * .*/
	@Override
	public void run() {


		try {
			String inputLine;
			while ((inputLine = in.readLine()) != null){
				
				String[] values = inputLine.split(",");
				
				//if incoming message is a ride
				if(values[0].equals("ride")){
					ride = true;
					userName = values[1];
					location = values[2];
					selectedDriver = values[3];
					passengerDestination = values[4];
					
				//if incoming message is User information	
				}else {
					userType = values[0];
					userName = values[1];
					location = values[2];
				}
			}
		}catch (IOException e) {
			System.out.println(userName +" "+e.getMessage());
			connected = false;
			//Printing the stack trace for testing purposes. 
			//e.printStackTrace();
		}finally {
			out.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Method to write back to the client.
	 * 
	 * @param validClientDrivers valid client drivers
	 */
	public void writeToClient(HashMap<String, String> validClientDrivers) {
		System.out.println("This is the hash map with valid drivers.");

		System.out.println(validClientDrivers);
		out.println(validClientDrivers);
	}
	
	
	/**
	 * Method to send a notification to driver when a passenger selects them.
	 * 
	 * @param location passenger's location
	 * @param userName passenger's username
	 * @param destination passenger's destination
	 */
	public void writeBackToDriver(String location, String userName,String destination){
		out.println("Passenger user name: "+ userName+ " Location: "+location+" Destination:"+destination);
		
	}

	/**
	 * Get the user name.
	 * 
	 * @return username
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Get users location
	 * 
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/** Get user type.
	 * 
	 * @return userType
	 * */
	public String getUserType() {
		return userType;
	}

	/** Check if the client remains connected.
	 * 
	 * @return true if connected, false if disconnected*/
	public boolean connectedUser () {
		return connected;
	}
	
	/** Check if a ride has been made.
	 * 
	 * @return true if ride*/
	public boolean checkForRide() {
		return ride;
	}
	
	/**
	 * Get the selected driver.
	 * 
	 * @return selectedDriver.
	 */
	public String getSelectedDriver() {
		return selectedDriver;
	}
	
	/** Get the passenger's destination.
	 * @return passengerDestination. 
	 * */
	public String getPassengerDestination() {
		return passengerDestination;
	}

	/**Nice display of user's data
	 * @return user name and location.
	 * .*/
	public String toString(){

		return userName +" "+ location;
	}
}