package ftp.poll.script;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import ftp.poll.commander.ScriptRunner;

class ScriptRunnerTest {

	@Test
	void test() {
		
		// Get the test resource directory for test files
		String testResourcesPath = Paths.get("src","test","resources").toFile().getAbsolutePath();

		// Test properties
		String script = "example.py";
		String expectedOutput = "Started Example Script";
		
		// Create the script runner
		ScriptRunner runner = new ScriptRunner();
		runner.runPythonScript(testResourcesPath + "\\" + script);
		
		//Wait for script to run
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Check for expected output
		List<String> consoleOutput = runner.getConsoleOutput();
		assertEquals(consoleOutput.get(0), expectedOutput);
		
		// Stop the script runner
		runner.stop();
	}

}
