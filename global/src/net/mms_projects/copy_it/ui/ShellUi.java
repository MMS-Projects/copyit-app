package net.mms_projects.copy_it.ui;

import java.io.PrintStream;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.ui.interactive_shell.Shell;
import net.mms_projects.copy_it.ui.interactive_shell.commands.CopyIt;
import net.mms_projects.copy_it.ui.interactive_shell.commands.PasteIt;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;
import net.mms_projects.irc.channel_bots.pb.commands.Help;

public class ShellUi implements UserInterfaceImplementation {

	private SettingsUserInterface settingsUserInterface;
	private AboutUserInterface aboutUserInterface;

	private SyncManager syncManager;
	private ClipboardManager clipboardManager;

	public ShellUi(Config settings, SyncManager syncManager,
			ClipboardManager clipboardManager) {
		this.syncManager = syncManager;
		this.clipboardManager = clipboardManager;
		
		this.setSettingsUserInterface(new SettingsUserInterface() {
			
			@Override
			public void show() {
				System.out.println("No settings yet.");
			}
			
			@Override
			public void hide() {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.setAboutUserInterface(new AboutUserInterface() {
			
			@Override
			public void show() {
				System.out.println("CopyIt!");
			}
			
			@Override
			public void hide() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void open() {
		PrintStream printStream = new PrintStream(System.out);

		CommandHandler commandHandler = new CommandHandler();
		commandHandler.setPrintStream(printStream);
		commandHandler.addCommand(new Help(commandHandler));
		commandHandler.addCommand(new CopyIt(commandHandler, this.syncManager,
				this.clipboardManager));
		commandHandler.addCommand(new PasteIt(commandHandler, this.syncManager,
				this.clipboardManager));

		final Shell shell = new Shell(System.in, printStream, commandHandler);
		shell.setPrompt("> ");
		shell.loop();
	}

	@Override
	public void close() {
		System.out.println("No way of shutting down? :(");
	}

	@Override
	public void setSettingsUserInterface(
			SettingsUserInterface settingsUserInterface) {
		this.settingsUserInterface = settingsUserInterface;
	}

	@Override
	public SettingsUserInterface getSettingsUserInterface() {
		return this.settingsUserInterface;
	}

	@Override
	public void setAboutUserInterface(AboutUserInterface userInterface) {
		this.aboutUserInterface = userInterface;
	}

	@Override
	public AboutUserInterface getAboutUserInterface() {
		return this.aboutUserInterface;
	}
}
