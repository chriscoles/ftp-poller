package ftp.poll.ftp;

import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import ftp.poll.util.Utils;

/**
 * A client for an SFTP server.
 * 
 * @author chriscoles
 *
 */
public class SFTPClientImpl implements FTPClient {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(SFTPClientImpl.class);

	/**
	 * An instance of the Java Secure Channel. Used to create connection to SFTP
	 * server.
	 */
	private JSch jsch;

	/**
	 * The SFTP Session provided by JSch
	 */
	private Session session;

	/**
	 * The SFTP Channel provided by JSch
	 */
	private ChannelSftp channel;

	/**
	 * SFTP Client constructor.
	 * 
	 * @param host The SFTP host
	 * @param user The SFTP username
	 * @param pass The SFTP password
	 * @param port the SFTP port
	 * @param key  The SFTP key
	 */
	public SFTPClientImpl(String host, String user, String pass, int port, String key) {

		jsch = new JSch();

		if (!key.isEmpty()) {
			try {
				jsch.addIdentity(key);
			} catch (JSchException e) {
				logger.error("Error setting key. {}", e.getMessage());
			}
		}
		try {
			session = jsch.getSession(user, host, port);
		} catch (JSchException e) {
			logger.error("Error getting session {}", e.getMessage());
		}
		session.setConfig("StrictHostKeyChecking", "no");
		if (!pass.isEmpty()) {
			session.setPassword(pass);
		}
	}

	/**
	 * {@inheritdoc}
	 */
	public boolean connect() {
		try {
			session.connect();
			if (session.isConnected()) {
				logger.info("SFTP Connected");
				return true;
			}
		} catch (JSchException e) {
			logger.error("Error connecting {}", e.getMessage());
		}
		return false;
	}

	/**
	 * {@inheritdoc}
	 */
	public void disconnect() {
		logger.info("Disconnecting Session");
		session.disconnect();
	}

	/**
	 * {@inheritdoc}
	 */
	public void login() {
		logger.info("Opening Channel");
		try {
			channel = (ChannelSftp) session.openChannel("sftp");
		} catch (JSchException e) {
			logger.error("Error opening channel {}", e.getMessage());
		}

		try {
			channel.connect();
		} catch (JSchException e) {
			logger.error("Error connecting to channel {}", e.getMessage());
		}
	}

	/**
	 * Closes the channel
	 */
	public void closeChannel() {
		logger.info("Closing Channel");
		channel.exit();
	}

	/**
	 * Change to the given directory
	 * 
	 * @param dir The directory to change to
	 */
	public void cd(String dir) {
		logger.info("cd {}", dir);
		try {
			channel.cd(dir);
		} catch (SftpException e) {
			logger.error("Error cd {}", e.getMessage());
		}
	}

	/**
	 * {@inheritdoc}
	 */
	public Vector<FTPPollFile> ls(String dir) {
		Vector<FTPPollFile> ls = null;
		try {
			ls = channel.ls(dir);
		} catch (SftpException e) {
			logger.error("Error ls {}", e.getMessage());
		}
		return ls;
	}

	/**
	 * {@inheritdoc}
	 */
	public void downloadFile(String remoteFilePath, String localFilePath) {
		logger.info("Downloading file from {} to {}", remoteFilePath, localFilePath);
		try {
			channel.get(remoteFilePath, localFilePath);
		} catch (SftpException e) {
			logger.error("Error downloading {}", e.getMessage());
		}
	}

	/**
	 * {@inheritdoc}
	 */
	public boolean checkFileComplete(String dir, String filename) {
		long size = 0;
		long lastSize = 0;
		boolean finished = false;

		logger.info("Checking file size");

		while (!finished) {
			Vector<FTPPollFile> ls = ls(dir);

			if (ls == null) {
				finished = true;
			}

			for (FTPPollFile l : ls) {
				String file = l.getName();
				if (file.contains(filename)) {
					size = l.getSize();
					long deltaSize = Math.abs(size - lastSize);
					logger.info("Checking file status: {}, size: {}, change in size {}", filename, size, deltaSize);
					if (size > 0 && deltaSize == 0) {
						logger.info("File complete.");
						return true;
					}
					lastSize = size;
				}
				Utils.sleep(1000);
			}

		}
		return false;
	}

}
