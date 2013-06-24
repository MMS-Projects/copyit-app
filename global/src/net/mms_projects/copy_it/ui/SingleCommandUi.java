package net.mms_projects.copy_it.ui;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.Settings;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.copy_it.clipboard_services.AwtService;
import net.mms_projects.copy_it.ui.interactive_shell.Shell;
import net.mms_projects.copy_it.ui.interactive_shell.commands.CopyIt;
import net.mms_projects.copy_it.ui.interactive_shell.commands.PasteIt;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;
import net.mms_projects.irc.channel_bots.pb.commands.Help;

import java.io.PrintStream;

public class SingleCommandUi extends AbstractUi {

    private SyncManager syncManager;
    private ClipboardManager clipboardManager;
    private String command;

    public SingleCommandUi(Settings settings, SyncManager syncManager, ClipboardManager clipboardManager, String command) {
        super(settings);

        this.syncManager = syncManager;
        this.clipboardManager = clipboardManager;
        this.command = command;

        AwtService awtService = new AwtService(this.clipboardManager);

        this.clipboardManager.addPasteService(awtService);
        this.clipboardManager.addCopyService(awtService);
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
