package ftp.poll.util;

/**
 * Common utilities for this application
 * 
 * @author chriscoles
 *
 */
public class Utils {

	/**
	 * Private constructor prevents initialisation
	 */
	private Utils() {

	}

	/**
	 * Sleep utility. Sleeps for a given time in milliseconds.
	 * 
	 * @param millis the time to sleep in milliseconds.
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Checks whether the running operating system is windows.
	 * 
	 * @return true is operating system is windows.
	 */
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return os.contains("win");
	}
}
