package ftp.poll.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Access properties file
 * 
 * @author chriscoles
 */
public class FTPPollProperties {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(FTPPollProperties.class);

	/**
	 * The persisted properties
	 */
	private static Properties properties = null;

	/**
	 * The input stream for the properties file
	 */
	private static InputStream stream;

	public static final String FTP_HOST = "ftp_host";
	public static final String FTP_PORT = "ftp_port";
	public static final String FTP_USER = "ftp_user";
	public static final String FTP_PASS = "ftp_pass";
	public static final String FTP_POLL = "ftp_poll";
	public static final String FTP_WORKING_DIR = "ftp_directory";
	public static final String FILESTORE = "filestore";
	public static final String SCRIPT_PORT = "script_port";
	public static final String SCRIPT = "script";
	public static final String LOG_FILE = "log_file";

	/**
	 * Private constructor to prevent instantiation
	 */
	private FTPPollProperties() {
		// Do nothing
	}

	/**
	 * Gets a property string for a given key
	 * 
	 * @param key the property key
	 * @return the property string
	 */
	public static String getProperty(String key) {
		try {
			// Avoid loading the file all the time
			if (stream == null) {
				File f = null;
				String externalFileName = System.getProperty("app.properties");
				if (externalFileName != null) {
					f = new File(externalFileName);
					if (f.exists() && !f.isDirectory()) {
						logger.info("Using properties file defined as parameter: {}", externalFileName);
						stream = new FileInputStream(new File(externalFileName));
					}
				} else {
					stream = FTPPollProperties.class.getClassLoader().getResourceAsStream("properties.properties");
				}
			}

			// Avoid loading the properties all the time
			if (properties == null) {
				properties = new Properties();
				if (stream != null) {
					properties.load(stream);
					stream.close();
				}
			}
			return properties.getProperty(key);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

}
