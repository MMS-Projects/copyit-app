package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.clipboard_services.TestService;
import net.mms_projects.copy_it.ui.interactive_shell.Shell;
import net.mms_projects.copy_it.ui.interactive_shell.commands.CopyIt;
import net.mms_projects.copy_it.ui.interactive_shell.commands.PasteIt;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;
import net.mms_projects.irc.channel_bots.pb.commands.Help;

import java.io.PrintStream;

public class ShellUi extends AbstractUi {

    private SyncManager syncManager;
    private ClipboardManager clipboardManager;

    public ShellUi(Settings settings, SyncManager syncManager, ClipboardManager clipboardManager) {
        super(settings);

        this.syncManager = syncManager;
        this.clipboardManager = clipboardManager;

        this.clipboardManager.addPasteService(new TestService(this.clipboardManager));
    }

    @Override
    public void open() {
        PrintStream printStream = new PrintStream(System.out);

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.setPrintStream(printStream);
        commandHandler.addCommand(new Help(commandHandler));
        commandHandler.addCommand(new CopyIt(commandHandler, this.syncManager, this.clipboardManager));
        commandHandler.addCommand(new PasteIt(commandHandler, this.syncManager, this.clipboardManager));

        final Shell shell = new Shell(System.in, printStream, commandHandler);
        shell.setPrompt("> ");
        shell.loop();
    }
}
