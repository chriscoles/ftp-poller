package ftp.poll.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ftp.poll.commander.ScriptInteractor;

/**
 * Represents a queue of file names added to the FTP server ready for
 * processing.
 * 
 * @author chriscoles
 *
 */
public class FileQueue {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(FileQueue.class);

	/**
	 * A queue of file names
	 */
	private Queue<String> queue;

	/**
	 * Creates a new file queue that uses the given script interactor
	 * 
	 * @param interactor The script interactor
	 */
	public FileQueue(ScriptInteractor interactor) {

		queue = new LinkedList<>();

		TimerTask task = new TimerTask() {
			public void run() {
				if (getSize() > 0) {
					String nextFile = queue.remove();
					logger.info("Running Queued File: " + nextFile);
					interactor.send(nextFile);
					logger.info("Finished Queued File: " + nextFile);
				}
			}
		};
		Timer timer = new Timer("Timer");

		long delay = 1000L;
		timer.schedule(task, delay, 1000L);
	}

	/**
	 * Add a file name to the queue
	 * 
	 * @param file The file name
	 */
	public void add(String file) {
		logger.info("Added file to queue: {}", file);
		queue.add(file);
	}

	/**
	 * Remove and return the next file name in the queue
	 * 
	 * @return The name of the removed file
	 */
	public String remove() {
		logger.info("Removed file from queue");
		return queue.remove();
	}

	/**
	 * Gets the size of the queue
	 * 
	 * @return The queue size
	 */
	public int getSize() {
		return queue.size();
	}

}
