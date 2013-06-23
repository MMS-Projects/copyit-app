package net.mms_projects.copy_it.ui.interactive_shell.commands;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.irc.channel_bots.pb.Command;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;

public class PasteIt extends Command {
    private final SyncManager syncManager;
    private final ClipboardManager clipboardManager;

    public PasteIt(CommandHandler commandHandler, SyncManager syncManager, ClipboardManager clipboardManager) {
        super("pasteit", "Pulls content from the server", commandHandler);

        this.syncManager = syncManager;
        this.clipboardManager = clipboardManager;

        this.addHelp();
    }

    @Override
    public void run(String rawdata) {
        this.reply("Not implemented yet");
    }
}
