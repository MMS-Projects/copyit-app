package net.mms_projects.copy_it;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationLock {

    /**
     * The timeout time when checking if the application is running.
     */
    static final int LOCK_CHECK_TIMEOUT = 100;

    private File lockFile;
    private ServerSocket server;
    private ServerThread thread;
    /**
     * The lock state of the application.
     */
    private boolean locked = false;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * @param path
     *            The path where the lock file needs to be stored
     */
    public ApplicationLock(final File path) {
        this.lockFile = new File(path, ".lock");
    }

    /**
     * This method locks the application.
     *
     * @throws LockException
     *             Thrown when something caused the lock to fail
     */
    public final void lock() throws LockException {
        log.info("Application locked");
        if (this.isLocked()) {
            throw new LockException("The app is already locked");
        }
        BufferedWriter writer = null;
        try {
            this.server = new ServerSocket(0);

            this.thread = new ServerThread();
            this.thread.setDaemon(true);
            this.thread.start();

            writer = new BufferedWriter(new FileWriter(this.lockFile));
            writer.write(Integer.toString(this.server.getLocalPort()));
        } catch (IOException e) {
            throw new LockException(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException exception) {
                throw new LockException("Could not close the lock file writer",
                        exception);
            }
        }
        this.locked = true;
    }

    /**
     * This method unlocks the application.
     *
     * @throws LockException Thrown when something caused the unlock to fail
     */
    public final void unlock() throws LockException {
        log.info("Application unlocked");
        if (!this.isLocked()) {
            return;
        }
        this.lockFile.delete();
        this.thread.interrupt();
        try {
            this.server.close();
        } catch (IOException exception) {
            throw new LockException("Could not locking server socket",
                    exception);
        }
        this.locked = false;
    }

    /**
     * This returns whenever the application has been locked or not.
     *
     * @return Returns true when the application has been locked
     */
    final boolean isLocked() {
        return this.locked;
    }

    /**
     * Returns whenever the application is already running or not.
     *
     * @return Returns true when the application is already running
     */
    public final boolean isRunning() {
        BufferedReader reader = null;
        Socket socket = null;
        try {
            reader = new BufferedReader(new FileReader(this.lockFile));
            String content = reader.readLine();
            if (content == null) {
                return false;
            }
            SocketAddress sockaddr = new InetSocketAddress("127.0.0.1",
                    Integer.parseInt(content));
            socket = new Socket();
            socket.connect(sockaddr, LOCK_CHECK_TIMEOUT);
            if (socket.isConnected()) {
                return true;
            }
        } catch (FileNotFoundException e) {
            /*
             * No lock file exists which means no application lock is in place.
             */
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                this.log.warn("Could not close the lock file reader");
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException exception) {
                this.log.warn("Could not close the lock checking socket");
            }
        }
        return false;
    }

    public class LockException extends Exception {
        private static final long serialVersionUID = -1567053088980704966L;

        public LockException(String message) {
            super(message);
        }

        public LockException(String message, Throwable clause) {
            super(message, clause);
        }

        public LockException(Throwable clause) {
            super(clause);
        }
    }

    private class ServerThread extends Thread {
        @Override
        public void run() {
            log.info("Lock server thread running");
            while (true) {
                if (this.isInterrupted()) {
                    log.info("Lock server thread interrupted");
                    break;
                }
                try {
                    if (!server.isClosed()) {
                        server.accept();
                    }
                } catch (IOException e) {
                }
            }
        }
    }

}
