package ftp.poll.polling;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

import com.github.drapostolos.rdp4j.spi.FileElement;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;

import ftp.poll.ftp.FTPClient;
import ftp.poll.ftp.FTPPollFile;

/**
 * Represents a directory on the FTP server
 * 
 * @author chriscoles
 *
 */
public class FTPPollerDirectory implements PolledDirectory {

	/**
	 * 
	 */
	private FTPClient client;
	
	/**
	 * 
	 */
	private String workingDirectory;

	/**
	 * @param client
	 * @param workingDirectory
	 */
	public FTPPollerDirectory(FTPClient client, String workingDirectory) {
		this.client = client;
		this.workingDirectory = workingDirectory;
	}

	/**
	 *
	 */
	public Set<FileElement> listFiles() throws IOException {

		try {
			Vector<FTPPollFile> ls = client.ls(workingDirectory);
			Set<FileElement> result = new LinkedHashSet<>();

			if (ls != null) {
				for (FTPPollFile ftpPollFile : ls) {
					FileElement fileElement = new FTPPollerFile(ftpPollFile);
					result.add(fileElement);
				}
			}

			return result;

		} catch (Exception e) {
			throw new IOException(e);
		}

	}
}