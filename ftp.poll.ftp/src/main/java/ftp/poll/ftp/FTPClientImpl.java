package ftp.poll.ftp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.Vector;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A client for an FTP server.
 * 
 * @author chriscoles
 *
 */
public class FTPClientImpl implements FTPClient {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(FTPClientImpl.class);

	/**
	 * The Apache FTP client wrapped by this class
	 */
	private org.apache.commons.net.ftp.FTPClient ftp = null;

	/**
	 * The FTP host
	 */
	private String host;
	
	/**
	 * The FTP username
	 */
	private String user;
	
	/**
	 * The FTP password
	 */
	private String pass;

	/**
	 * @param host The FTP host
	 * @param port The FTP port
	 * @param user The FTP username
	 * @param pass The FTP password
	 */
	public FTPClientImpl(String host, int port, String user, String pass) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		ftp = new org.apache.commons.net.ftp.FTPClient();
		ftp.setDefaultPort(port);
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}

	/**
	 * {@inheritdoc}
	 */
	public boolean connect() {
		try {
			ftp.connect(host);
		} catch (SocketException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		int reply = ftp.getReplyCode();

		logger.info("FTP Connection Reply: {}", reply);

		if (!FTPReply.isPositiveCompletion(reply)) {
			try {
				ftp.disconnect();
				return false;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return true;
	}

	/**
	 * {@inheritdoc}
	 */
	public void login() {
		try {
			ftp.login(user, pass);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void setFileType() {
		try {
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void setMode() {
		ftp.enterLocalPassiveMode();
	}

	/**
	 * {@inheritdoc}
	 */
	public void downloadFile(String remoteFilePath, String localFilePath) {
		logger.info("Downloading file from {}, to {}", remoteFilePath, localFilePath);
		setFileType();
		setMode();
		try {
			FileOutputStream fos = new FileOutputStream(localFilePath);
			String streamString = fos.toString();
			logger.info("streamString: {}", streamString);
			logger.info("fos");
			this.ftp.retrieveFile(remoteFilePath, fos);
			logger.info("retrieveFile");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritdoc}
	 */
	public void disconnect() {
		if (this.ftp.isConnected()) {
			try {
				this.ftp.logout();
				this.ftp.disconnect();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * {@inheritdoc}
	 */
	@Override
	public Vector<FTPPollFile> ls(String dir) {
		org.apache.commons.net.ftp.FTPFile[] files = null;
		try {
			files = ftp.listFiles(dir);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		Vector<FTPPollFile> vector = new Vector<>();
		for (org.apache.commons.net.ftp.FTPFile f : files) {
			FTPPollFile ftpFile = new FTPPollFile(f.getTimestamp().getTimeInMillis(), f.isDirectory(), f.getName(),
					f.getSize());
			vector.add(ftpFile);
		}
		return vector;
	}

	/**
	 * {@inheritdoc}
	 */
	public boolean checkFileComplete(String dir, String filename) {
		return true;
	}

}
