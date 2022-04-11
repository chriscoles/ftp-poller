package ftp.poll.polling;

import java.io.IOException;

import com.github.drapostolos.rdp4j.spi.FileElement;

import ftp.poll.ftp.FTPPollFile;

/**
 * Represents a file on the FTP server
 * 
 * @author chriscoles
 *
 */
public class FTPPollerFile implements FileElement {

	/**
	 * 
	 */
	private final FTPPollFile file;
	
	/**
	 * 
	 */
	private final String name;
	
	/**
	 * 
	 */
	private final boolean isDirectory;

	/**
	 * @param file
	 */
	public FTPPollerFile(FTPPollFile file) {
		this.file = file;
		this.name = file.getName();
		this.isDirectory = file.isDirectory();
	}

	@Override
	public long lastModified() throws IOException {
		return file.lastModified();
	}

	@Override
	public boolean isDirectory() {
		return isDirectory;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return file.toString();
	}
}