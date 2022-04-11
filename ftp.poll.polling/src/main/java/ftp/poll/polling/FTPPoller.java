package ftp.poll.polling;

import java.util.concurrent.TimeUnit;

import com.github.drapostolos.rdp4j.DirectoryPoller;
import com.github.drapostolos.rdp4j.DirectoryPollerBuilder;
import com.github.drapostolos.rdp4j.spi.PolledDirectory;

import ftp.poll.ftp.FTPClient;

/**
 * Polls an FTP server for changes to files.
 * 
 * @author chriscoles
 *
 */
public class FTPPoller {

	/**
	 * The Directory Poller Builder
	 */
	private DirectoryPollerBuilder pollerBuilder;
	
	/**
	 * The Directory Poller
	 */
	private DirectoryPoller directoryPoller;

	/**
	 * @param listener the file listener
	 * @param client the FTP client
	 * @param pollDirectory the FTP directory to poll
	 * @param pollInterval the poll interval in seconds
	 */
	public FTPPoller(FileListener listener, FTPClient client, String pollDirectory, int pollInterval) {
		PolledDirectory polledDirectory = new FTPPollerDirectory(client, pollDirectory);
		pollerBuilder = DirectoryPoller.newBuilder().addPolledDirectory(polledDirectory).addListener(listener)
				.setPollingInterval(pollInterval, TimeUnit.SECONDS);
	}

	/**
	 * Starts the poller
	 */
	public void start() {
		directoryPoller = pollerBuilder.start();
	}

	/**
	 * Stops the poller
	 */
	public void stop() {
		directoryPoller.stop();
	}

}
