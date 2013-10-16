package net.mms_projects.copy_it.swt;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

import net.mms_projects.copy_it.ui.swt.SwtUtils;

import org.eclipse.swt.widgets.Display;

public class SwtExecutorService extends AbstractExecutorService {

	private boolean shutdown;

	final private Display display;

	public SwtExecutorService(Display display) {
		this.display = display;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit)
			throws InterruptedException {
		return true;
	}

	@Override
	public boolean isShutdown() {
		return this.shutdown;
	}

	@Override
	public boolean isTerminated() {
		return this.shutdown;
	}

	@Override
	public void shutdown() {
		this.shutdown = true;
	}

	@Override
	public List<Runnable> shutdownNow() {
		return null;
	}

	@Override
	public void execute(Runnable runnable) {
		WrappedRunnable wrappedRunnable = new WrappedRunnable(runnable);

		if (SwtUtils.isUIThread()) {
			wrappedRunnable.run();
		} else {
			this.display.asyncExec(wrappedRunnable);
		}
	}

	private class WrappedRunnable implements Runnable {

		final private Runnable runnable;

		public WrappedRunnable(Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void run() {
			System.out.println("Running wrapped runnable...");

			this.runnable.run();
		}

	}

}
