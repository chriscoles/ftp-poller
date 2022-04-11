package ftp.poll.commander;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides the interaction to scripts over a socket.
 * 
 * @author chriscoles
 *
 */
public class ScriptInteractor {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(ScriptInteractor.class);

	/**
	 * The socket to interact with the script
	 */
	private Socket socket;

	/**
	 * Creates a socket with the given port
	 * 
	 * @param port The socket port
	 */
	public void createSocket(int port) {
		try {
			socket = new Socket("localhost", port);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Sends a message to the script
	 * 
	 * @param message The message to send
	 * @return A message from the script to say it's finished the action
	 */
	public String send(String message) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.print(message + "\r\n");
			out.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		BufferedReader stdIn = null;
		try {
			stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		try {
			return stdIn.readLine();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * Closes the socket
	 */
	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
