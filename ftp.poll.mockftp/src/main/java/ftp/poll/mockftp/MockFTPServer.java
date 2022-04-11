package ftp.poll.mockftp;

import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.mockftpserver.fake.filesystem.WindowsFakeFileSystem;

import ftp.poll.util.Utils;

/**
 * 
 * Mocks an FTP server for testing purposes
 *
 * @author chriscoles
 */
public class MockFTPServer {

	/**
	 * The instance of FakeFTPServer that this class wraps.
	 */
	private FakeFtpServer fakeFtpServer;

	/**
	 * The FTP filesystem
	 */
	private FileSystem fileSystem;

	/**
	 * @param port     The FTP port
	 * @param username The FTP username
	 * @param password The FTP password
	 * @param root     The root directory for the FTP server
	 */
	public MockFTPServer(int port, String username, String password, String root) {
		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.setServerControlPort(port);
		fakeFtpServer.addUserAccount(new UserAccount(username, password, root));
		fileSystem = determineFilesystem();
		fakeFtpServer.setFileSystem(fileSystem);
	}

	/**
	 * Determines the host OS
	 * 
	 * @return The filesystem for the host OS
	 */
	private FileSystem determineFilesystem() {
		if (Utils.isWindows()) {
			return new WindowsFakeFileSystem();
		} else {
			return new UnixFakeFileSystem();
		}
	}

	/**
	 * Starts the mock FTP server
	 */
	public void start() {
		fakeFtpServer.start();
	}

	/**
	 * Stops the mock FTP server
	 */
	public void stop() {
		fakeFtpServer.stop();
	}

	/**
	 * Adds a directory to the mock FTP server
	 * 
	 * @param directory The directory to add
	 */
	public void addDirectory(String directory) {
		System.out.println("adding " + directory);
		fileSystem.add(new DirectoryEntry(directory));
	}

	/**
	 * Adds a file to the mmock FTP server
	 * 
	 * @param filename The filename
	 * @param content  The file contents
	 */
	public void addFile(String filename, String content) {
		fileSystem.add(new FileEntry(filename, content));
	}

	/**
	 * Checkes whether the given file exists
	 * @param path The file path
	 * @return 
	 */
	public boolean fileExists(String path) {
		return fileSystem.exists(path);
	}

}
