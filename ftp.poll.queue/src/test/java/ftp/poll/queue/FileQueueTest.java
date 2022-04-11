package ftp.poll.queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import ftp.poll.commander.ScriptInteractor;
import ftp.poll.commander.ScriptRunner;

class FileQueueTest {

	/**
	 * Adds three files to the file queue and check that the script sums the file
	 * contents for each file.
	 */
	@Test
	void test() {

		// Get the test resource directory for test files
		String testResourcesPath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

		// Test properties
		String script = "example_socket.py";
		ScriptRunner runner = new ScriptRunner();
		String scriptPath = testResourcesPath + "\\" + script;
		runner.runPythonScript(scriptPath);
		String[] files = new String[] { "example1.dat", "example2.dat", "example3.dat" };
		int[] expectedSums = new int[] { 6, 15, 24 };

		// Wait for script to run
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Check that the script has started
		List<String> consoleOutput = runner.getConsoleOutput();
		assertEquals(consoleOutput.get(0), "Started Example Socket Script");

		// Create script interactor
		ScriptInteractor interactor = new ScriptInteractor();
		interactor.createSocket(9999);

		// Create the file queue
		FileQueue queue = new FileQueue(interactor);

		// Add files and check the script output
		int counter = 0;
		for (String file : files) {
			queue.add(testResourcesPath + "\\" + file);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			consoleOutput = runner.getConsoleOutput();
			String expected = "Sum= " + expectedSums[counter];
			counter++;
			assertTrue(consoleOutput.contains(expected.trim()));
		}

		// Close connections
		interactor.closeSocket();
		runner.stop();
	}

}
