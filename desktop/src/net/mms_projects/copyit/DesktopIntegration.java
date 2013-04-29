package net.mms_projects.copyit;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.exceptions.DBusException;

public interface DesktopIntegration extends DBusInterface {
	public class action_push extends DBusSignal {
		public action_push(String path) throws DBusException {
			super(path);
		}
	}
	public class action_pull extends DBusSignal {
		public action_pull(String path) throws DBusException {
			super(path);
		}
	}
	public class action_open_preferences extends DBusSignal {
		public action_open_preferences(String path) throws DBusException {
			super(path);
		}
	}
	public class action_open_about extends DBusSignal {
		public action_open_about(String path) throws DBusException {
			super(path);
		}
	}
	public class action_quit extends DBusSignal {
		public action_quit(String path) throws DBusException {
			super(path);
		}
	}
}