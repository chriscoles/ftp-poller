package ftp.poll.ftp;

/**
 * 
 * Represents basic properties of a file stored on an FTP server.
 *
 * @author chriscoles
 */
public class FTPPollFile {

	/**
	 * The last modified time in milliseconds
	 */
	private long lastModified;

	/**
	 * Is the file a directory.
	 */
	private boolean isDirectory;

	/**
	 * The filename with extension
	 */
	private String name;

	/**
	 * The file size in bytes
	 */
	private long size;

	/**
	 * @param lastModified The last modified time in milliseconds
	 * @param isDirectory  Is the file a directory
	 * @param name         The filename including extension
	 * @param size         The last modified time in milliseconds
	 */
	public FTPPollFile(long lastModified, boolean isDirectory, String name, long size) {
		this.lastModified = lastModified;
		this.isDirectory = isDirectory;
		this.name = name;
		this.size = size;
	}

	/**
	 * Gets the last modified time in milliseconds
	 * 
	 * @return The last modified time in milliseconds
	 */
	public long lastModified() {
		return lastModified;
	}

	/**
	 * Checks whether this file is a directory
	 * 
	 * @return true if directory, false if regular file
	 */
	public boolean isDirectory() {
		return isDirectory;
	}

	/**
	 * Gets the filename including extension
	 * 
	 * @return The filename
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the file size in bytes
	 * 
	 * @return The file size
	 */
	public long getSize() {
		return size;
	}
}
