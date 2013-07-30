package net.mms_projects.copy_it.ui.swt.forms;

import java.util.Date;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Messages;
import net.mms_projects.copy_it.ui.UserInterfaceImplementation.QueueUserInterface;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class DataQueue extends Dialog implements QueueUserInterface {

	protected Object result;
	protected Shell shell;
	private Table table;
	private TableItem tableItem;

	private ClipboardManager clipboardManager;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public DataQueue(Shell parent, int style, ClipboardManager clipboardManager) {
		super(parent, style);

		this.clipboardManager = clipboardManager;

		setText("SWT Dialog");
	}

	public void setup() {
		createContents();

		this.shell.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event e) {
				e.doit = false;
				shell.setVisible(false);
			}
		});
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new FormLayout());

		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 10);
		fd_table.left = new FormAttachment(0, 10);
		fd_table.bottom = new FormAttachment(100, -10);
		fd_table.right = new FormAttachment(100, -10);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnData = new TableColumn(table, SWT.NONE);
		tblclmnData.setWidth(336);
		tblclmnData.setText(Messages.getString("queue.column.data"));

		TableColumn tblclmnDate = new TableColumn(table, SWT.NONE);
		tblclmnDate.setWidth(100);
		tblclmnDate.setText(Messages.getString("queue.column.date"));

		Menu menu = new Menu(table);
		MenuItem itemPaste = new MenuItem(menu, SWT.PUSH);
		itemPaste.setText(Messages
				.getString("queue.context_menu.put_in_clipboard"));
		itemPaste.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				TableItem tableItem = table.getSelection()[0];
				String content = tableItem.getText(0);

				clipboardManager.setContent(content);

			}
		});
		table.setMenu(menu);
	}

	@Override
	public void open() {
		shell.setVisible(true);
	}

	@Override
	public void close() {
		if ((this.shell != null) && (this.shell.isVisible())) {
			this.shell.setVisible(false);
		}
	}

	@Override
	public void addContent(final String content, final Date date) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				open();
				tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(new String[] { content, date.toString() });
			}
		});
	}

}
