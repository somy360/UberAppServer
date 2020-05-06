/*
 * Building the server side of the UberApp program. The server handles the connection of multiple clients through multithreading.
 *  Server makes list of connected Drivers. Server sends back a list of connected valid Drivers to the clients.
 *  Server sends message to Driver.

 	@author  Alexandra Durón, Graeme Somerville.

 */


package uberAppServer;

/**
 * 
 * @author Alexandra and Graeme
 *
 */
public class Main {

	public static void main(String[] args) {

		//Initialise the server.
		Server server = new Server();

	}

}
