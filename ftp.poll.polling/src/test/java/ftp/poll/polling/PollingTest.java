package ftp.poll.polling;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import ftp.poll.commander.ScriptInteractor;
import ftp.poll.commander.ScriptRunner;
import ftp.poll.ftp.FTPClient;
import ftp.poll.ftp.FTPClientImpl;
import ftp.poll.mockftp.MockFTPServer;
import ftp.poll.queue.FileQueue;
import ftp.poll.util.Utils;

class PollingTest {

	/**
	 * Creates a mock FTP server and a queue. Adds three files to the FTP server and
	 * checks that poller picks them up by checking that the script sums the file
	 * contents for each file.
	 */
	@Test
	void test() {

		// Get the test resource directory for test files
		String testResourcesPath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

		// Test properties
		String ftpHost = "localhost";
		String username = "user";
		String password = "pass";
		int ftpPort = 1234;
		String script = "example_socket.py";
		String filestore = testResourcesPath + "\\" + "filestore";
		String[] filenames = new String[] { "example1.dat", "example2.dat", "example3.dat" };
		String[] contents = new String[] { "1\n2\n3", "4\n5\n6", "7\n8\n9" };
		int[] sums = new int[] { 6, 15, 24 };
		String ftpDir = "C:/data";
		if (!Utils.isWindows()) {
			ftpDir = "/data";
		}

		// Create mock FTP server
		MockFTPServer ftpServer = new MockFTPServer(ftpPort, username, password, ftpDir);
		ftpServer.start();
		ftpServer.addDirectory(ftpDir);

		// Create the FTP client
		FTPClient ftpClient = new FTPClientImpl(ftpHost, ftpPort, username, password);
		ftpClient.connect();
		ftpClient.login();

		// Create the script runner
		ScriptRunner runner = new ScriptRunner();
		String scriptPath = testResourcesPath + "\\" + script;
		runner.runPythonScript(scriptPath);

		// Create the script interactor
		ScriptInteractor interactor = new ScriptInteractor();
		interactor.createSocket(9999);

		// Create the file queue
		FileQueue queue = new FileQueue(interactor);

		// Poll for files added to the FTP server
		FileListener listener = new FileListener(queue, ftpClient, ftpDir, filestore);
		FTPPoller poller = new FTPPoller(listener, ftpClient, ftpDir, 2);
		poller.start();

		// Wait for poller timer to start
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Check that all files are processed by the script by checking all returned sum
		// vales
		for (int i = 0; i < filenames.length; i++) {
			ftpServer.addFile(ftpDir + "\\" + filenames[i], contents[i]);

			// Wait for script to finish with file before assertions
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Check that sum was calculate correctly for given file
			int counter = 0;
			List<String> consoleOutput = runner.getConsoleOutput();
			String expected = "Sum= " + sums[counter];
			assertTrue(consoleOutput.contains(expected.trim()));
		}

		// Close connections
		interactor.closeSocket();
		runner.stop();
		ftpClient.disconnect();
		ftpServer.stop();
	}

}
