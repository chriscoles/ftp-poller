package ftp.poll.ftp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Paths;
import java.util.Vector;

import org.junit.jupiter.api.Test;

import ftp.poll.mockftp.MockFTPServer;
import ftp.poll.util.Utils;

class FTPClientTest {

	/**
	 * 
	 */
	@Test
	void test() {

		// Get the test resource directory for test files
		String testResourcesPath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

		// Test properties
		String username = "user";
		String password = "pass";
		String ftpHost = "localhost";
		int ftpPort = 1234;
		String fileContents = "1\n2\n3";
		String file = "example.dat";
		String ftpDir = "C:/data";
		if (!Utils.isWindows()) {
			ftpDir = "/data";
		}

		// Create the mock FTP server
		MockFTPServer ftpServer = new MockFTPServer(ftpPort, username, password, ftpDir);
		ftpServer.start();
		ftpServer.addDirectory(ftpDir);
		ftpServer.addFile(ftpDir + "\\" + file, fileContents);
		boolean exists = ftpServer.fileExists(ftpDir + "\\" + file);
		assertTrue(exists);

		// Create the FTP client
		FTPClient ftpClient = new FTPClientImpl(ftpHost, 1234, username, password);
		ftpClient.connect();
		ftpClient.login();
		String localFile = testResourcesPath + "\\" + file;

		// Download a file from the FTP
		ftpClient.downloadFile(file, localFile);

		// Check that the file as downloaded
		File f = new File(localFile);
		assertTrue(f.exists());
		assertTrue(!f.isDirectory());

		// List the FTP directory and verify contents
		Vector<FTPPollFile> files = ftpClient.ls(ftpDir);
		assertEquals(files.size(), 1);
		FTPPollFile ftpPollFile = files.get(0);
		String name = ftpPollFile.getName();
		assertEquals(name, file);

		// Stop server and client
		ftpClient.disconnect();
		ftpServer.stop();
	}

}
