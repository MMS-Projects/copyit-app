package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Config;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.ui.interactive_shell.commands.CopyIt;
import net.mms_projects.copy_it.ui.interactive_shell.commands.PasteIt;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;
import net.mms_projects.irc.channel_bots.pb.commands.Help;

import java.io.PrintStream;

public class SingleCommandUi extends AbstractUi {

    private SyncManager syncManager;
    private ClipboardManager clipboardManager;
    private String command;

    public SingleCommandUi(Config settings, SyncManager syncManager, ClipboardManager clipboardManager, String command) {
        super(settings);

        this.syncManager = syncManager;
        this.clipboardManager = clipboardManager;
        this.command = command;
    }

    @Override
    public void open() {
        PrintStream printStream = new PrintStream(System.out);

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.setPrintStream(printStream);
        commandHandler.addCommand(new Help(commandHandler));
        commandHandler.addCommand(new CopyIt(commandHandler, this.syncManager, this.clipboardManager));
        commandHandler.addCommand(new PasteIt(commandHandler, this.syncManager, this.clipboardManager));
        commandHandler.handle(this.command);
    }
}
