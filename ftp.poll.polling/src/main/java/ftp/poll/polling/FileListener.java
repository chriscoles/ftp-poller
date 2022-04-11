package ftp.poll.polling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.drapostolos.rdp4j.DirectoryListener;
import com.github.drapostolos.rdp4j.FileAddedEvent;
import com.github.drapostolos.rdp4j.FileModifiedEvent;
import com.github.drapostolos.rdp4j.FileRemovedEvent;
import com.github.drapostolos.rdp4j.InitialContentEvent;
import com.github.drapostolos.rdp4j.InitialContentListener;
import com.github.drapostolos.rdp4j.IoErrorCeasedEvent;
import com.github.drapostolos.rdp4j.IoErrorListener;
import com.github.drapostolos.rdp4j.IoErrorRaisedEvent;

import ftp.poll.ftp.FTPClient;
import ftp.poll.queue.FileQueue;

/**
 * File listener which reacts to changes on a file system
 * 
 * @author chriscoles
 *
 */
public class FileListener implements DirectoryListener, IoErrorListener, InitialContentListener {

	/**
	 * The logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(FileListener.class);

	/**
	 * The file queue
	 */
	private FileQueue queue;

	/**
	 * The FTP client
	 */
	private FTPClient client;

	/**
	 * The FTP folder to monitor.
	 */
	private String dir;

	/**
	 * The folder where files will be downloaded to from the FTP server.
	 */
	private String filestore;

	/**
	 * @param queue     The file queue
	 * @param client    The FTP client
	 * @param dir       The FTP directory to monitor
	 * @param filestore The folder where files will be downloaded to from the FTP
	 *                  server.
	 */
	public FileListener(FileQueue queue, FTPClient client, String dir, String filestore) {
		this.queue = queue;
		this.client = client;
		this.dir = dir;
		this.filestore = filestore;
	}

	@Override
	public void fileAdded(FileAddedEvent event) {
		String filename = event.getFileElement().getName();
		logger.info("Added: {}", filename);
		if (client.checkFileComplete(dir, filename)) {
			logger.info("File complete, downloading.");
			client.downloadFile(dir + "\\" + filename, filestore + "\\" + filename);
			queue.add(filestore + "\\" + filename);
		}
	}

	@Override
	public void fileRemoved(FileRemovedEvent event) {
		String filename = event.getFileElement().getName();
		logger.info("Removed: {}", filename);
	}

	@Override
	public void fileModified(FileModifiedEvent event) {
		String filename = event.getFileElement().getName();
		logger.info("Modified: {}", filename);
	}

	@Override
	public void ioErrorCeased(IoErrorCeasedEvent event) {
		logger.info("I/O error ceased.");
	}

	@Override
	public void ioErrorRaised(IoErrorRaisedEvent event) {
		logger.info("I/O error raised!");
		event.getIoException().printStackTrace();
	}

	@Override
	public void initialContent(InitialContentEvent event) {
		logger.info("initial Content: ^");
	}
}