package ftp.poll.mockftp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ftp.poll.util.Utils;

class MockFTPServerTest {

	/**
	 * Creates a mock FTP server, adds a directory and file then checks that the
	 * file exists.
	 */
	@Test
	void test() {
		
		// Test properties
		String username = "user";
		String password = "pass";
		int ftpPort = 1234;
		String filename = "example.dat";
		String fileContents = "1\n2\n3";
		String ftpDir = "C:/data";
		if(!Utils.isWindows()) {
			ftpDir = "/data";
		}
		
		// Create mock FTP server
		MockFTPServer ftpServer = new MockFTPServer(ftpPort, username, password, ftpDir);
		ftpServer.start();
		ftpServer.addDirectory(ftpDir);
		
		// Add a file to the mock FTP server
		ftpServer.addFile(ftpDir + "\\" + filename, fileContents);
		
		// Check that the file exists on the mock FTP server
		boolean exists = ftpServer.fileExists(ftpDir + "\\" + filename);
		assertTrue(exists);
		
		// Stop the mock FTP server
		ftpServer.stop();
	}

}
