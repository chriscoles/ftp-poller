package ftp.poll.ftp;

import java.util.Vector;

/**
 * 
 * Interface for FTP clients
 *
 * @author chriscoles
 */
public interface FTPClient {

	/**
	 * Connect to the FTP client
	 * 
	 * @return true if successful, false otherwise
	 */
	public boolean connect();

	/**
	 * Disconnect from the FTP client
	 */
	public void disconnect();

	/**
	 * Login to the FTP Clinet
	 */
	public void login();

	/**
	 * @param remoteFilePath
	 * @param localFilePath
	 */
	public void downloadFile(String remoteFilePath, String localFilePath);

	/**
	 * List directory of the given FTP folder
	 * 
	 * @param dir The FTP folder
	 * @return A vector of files
	 */
	public Vector<FTPPollFile> ls(String dir);

	/**
	 * Checks whether a given file has finished being uploaded to the FTP server
	 * 
	 * @param dir      The file directory
	 * @param filename The filename
	 * @return true if file has finished downloading
	 */
	public boolean checkFileComplete(String dir, String filename);
}
