package ftp.poll.script;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import ftp.poll.commander.ScriptInteractor;
import ftp.poll.commander.ScriptRunner;

class ScriptInteractorTest {

	/**
	 * Test that a script can be interacted with.
	 */
	@Test
	void test() {
		
		// Get the test resource directory for test files
		String testResourcesPath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

		// Test properties
		String script = "example_socket.py";
		String file = "example.dat";
		int scriptSocket = 9999;
		String expectedOutput = "Started Example Socket Script";
		
		// Create script runner
		ScriptRunner runner = new ScriptRunner();
		String scriptPath = testResourcesPath + "\\" + script;
		runner.runPythonScript(scriptPath);

		// Wait for script to run
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Check the conole for expected output
		List<String> consoleOutput = runner.getConsoleOutput();
		assertEquals(consoleOutput.get(0), expectedOutput);

		// Create the script interactor
		ScriptInteractor interactor = new ScriptInteractor();
		interactor.createSocket(scriptSocket);
		String path = testResourcesPath + "\\" + file;
		String response = interactor.send(path);
		assertEquals("Success", response);

		// Check console output
		consoleOutput = runner.getConsoleOutput();
		String expected = "Filename set to  " + path;
		assertTrue(consoleOutput.contains(expected.trim()));
		
		// Close connections
		interactor.closeSocket();
		runner.stop();
	}

}
