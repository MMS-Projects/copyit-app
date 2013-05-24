package net.mms_projects.copy_it.tests;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;
import net.mms_projects.copy_it.ApplicationLock;
import net.mms_projects.copy_it.ApplicationLock.LockException;

public class ApplicationLockTest extends TestCase {

	private ApplicationLock appLock;
	private File path;

	protected void setUp() throws Exception {
		super.setUp();

		this.path = createTempDirectory();
		this.appLock = new ApplicationLock(this.path);
	}

	public void testLockedApp() {
		try {
			this.appLock.lock();
		} catch (LockException e) {
			fail("Could not lock: " + e.getMessage());
		}

		assertTrue(this.appLock.isRunning());

		this.appLock.unlock();
	}

	public void testSecondLockException() {
		try {
			this.appLock.lock();
		} catch (LockException e) {
			fail("Could not lock the first time: " + e.getMessage());
		}

		LockException exception = null;
		try {
			this.appLock.lock();
		} catch (LockException e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("The app is already locked", exception.getMessage());
	}

	public void testUnresponsiveSocket() {
		File fakeLock = new File(this.path, ".lock");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fakeLock));
			writer.write(Integer.toString(500));
		} catch (IOException e) {
			fail("Could not create lock file: " + e.getMessage());
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
			}
		}

		assertFalse(this.appLock.isRunning());
	}

	public static File createTempDirectory() throws IOException {
		final File temp;

		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
	}

}
