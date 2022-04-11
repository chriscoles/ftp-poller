package ftp.poll.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ftp.poll.commander.ScriptInteractor;
import ftp.poll.commander.ScriptRunner;
import ftp.poll.ftp.FTPClient;
import ftp.poll.ftp.FTPClientImpl;
import ftp.poll.polling.FTPPoller;
import ftp.poll.polling.FileListener;
import ftp.poll.properties.FTPPollProperties;
import ftp.poll.queue.FileQueue;

/**
 * Monitors a directory on an FTP server. When a file is detected it is added to
 * a queue. When at the top of the queue, the file will be sent over a socket to
 * a given python script. The python script will then process the file based on
 * implementation. This application is configured by a properties file.
 * 
 * @author chriscoles
 */
public class FTPPollerMain {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(FTPPollerMain.class);

	/**
	 * Main entry point to FTP Poller
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Get config from properties file
		String ftpHost = FTPPollProperties.getProperty(FTPPollProperties.FTP_HOST);
		String ftpDir = FTPPollProperties.getProperty(FTPPollProperties.FTP_WORKING_DIR);
		String username = FTPPollProperties.getProperty(FTPPollProperties.FTP_USER);
		String password = FTPPollProperties.getProperty(FTPPollProperties.FTP_PASS);
		String script = FTPPollProperties.getProperty(FTPPollProperties.SCRIPT);
		String filestore = FTPPollProperties.getProperty(FTPPollProperties.FILESTORE);
		int ftpPort = Integer.parseInt(FTPPollProperties.getProperty(FTPPollProperties.FTP_PORT));
		int scriptPort = Integer.parseInt(FTPPollProperties.getProperty(FTPPollProperties.SCRIPT_PORT));
		int pollPeriod = Integer.parseInt(FTPPollProperties.getProperty(FTPPollProperties.FTP_POLL));

		logger.info("Setting up FTP connection");
		logger.info("FTP Host: {}", ftpHost);
		logger.info("FTP Port: {}", ftpHost);
		logger.info("Poll Period: {}", pollPeriod);
		logger.info("FTP Directory: {}", ftpDir);
		logger.info("Filestore Directory: {}", filestore);

		// Run the python script
		ScriptRunner runner = new ScriptRunner();
		runner.runPythonScript(script);

		// Create the FTP client and connect
		FTPClient ftpClient = new FTPClientImpl(ftpHost, ftpPort, username, password);
		ftpClient.connect();
		ftpClient.login();

		// Create the script interactor and connect
		ScriptInteractor interactor = new ScriptInteractor();
		interactor.createSocket(scriptPort);

		// Create a file queue
		FileQueue queue = new FileQueue(interactor);

		// Listen for new files on the FTP server
		FileListener listener = new FileListener(queue, ftpClient, ftpDir, filestore);
		FTPPoller poller = new FTPPoller(listener, ftpClient, ftpDir, 2);
		poller.start();

		// Keep Running
		while (true) {

		}
	}

}
