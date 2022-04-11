package ftp.poll.commander;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ftp.poll.util.Utils;

/**
 * Runs python scripts
 * 
 * @author chriscoles
 *
 */
public class ScriptRunner {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(ScriptRunner.class);

	/**
	 * The console output from the script
	 */
	private List<String> consoleOutput;

	/**
	 * The thread that runs the python script
	 */
	private Thread thread;

	/**
	 * The proces containing the python script
	 */
	private Process process;

	/**
	 * @param pythonScriptPath
	 */
	public void runPythonScript(String pythonScriptPath) {

		consoleOutput = new ArrayList<String>();

		thread = new Thread() {

			@Override
			public void run() {
				logger.info("Python Script Started");

				ProcessBuilder processBuilder = null;

				if (Utils.isWindows()) {
					processBuilder = new ProcessBuilder("python", "-u", pythonScriptPath);
				} else {
					processBuilder = new ProcessBuilder(pythonScriptPath);
				}

				processBuilder.redirectErrorStream(true);
				try {
					process = processBuilder.start();
					InputStream inputStream = process.getInputStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
					String s = null;
					while ((s = in.readLine()) != null) {
						logger.info(s);
						consoleOutput.add(s);
					}

				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				logger.info("Python Script Finished");
			}
		};

		thread.start();
	}

	/**
	 * Stops the script
	 */
	public void stop() {
		process.destroyForcibly();
		thread.stop();
	}

	/**
	 * @return A list containing the console output from the script
	 */
	public List<String> getConsoleOutput() {
		return consoleOutput;
	}

}
