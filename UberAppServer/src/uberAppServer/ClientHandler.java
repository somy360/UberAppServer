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

public class ClientHandler implements Runnable{

	//Global variables.
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;

	//Global attributes for getting user's data.
	private String userName, location;


	/** Constructor initialises global attributes
	 * @param clientSocket  connected client. 
	 .*/
	public ClientHandler(Socket clientSocket) throws IOException {

		this.client = clientSocket;
		out = new PrintWriter(clientSocket.getOutputStream(), true);                   
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	    
	}


	/** Run method is executed by thread handler.
	 * Waits for input from client. Then, it parses the input and stores in global variables. 
	 * .*/
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				//Print to console to test if our server receives the message.
				//System.out.println(inputLine);
				String[] values = inputLine.split(",");
				userName = values[0];
				location = values[1];
			}
		}catch (IOException e) {
			System.out.println("Exception caught while listening for a connection");
			System.out.println(e.getMessage());
			//Printing the stack trace for testing purposes. 
			e.printStackTrace();
		}finally {
			out.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/** Get the user name.*/
	public String getUserName() {
		return userName;
	}

	/** Get user's location.*/
	public String getLocation() {
		return location;
	}

	/**Nice display of user's data.*/
	public String toString(){

		return userName +" "+ location;
	}



}