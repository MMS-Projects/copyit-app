package net.mms_projects.copy_it.ui.interactive_shell.commands;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.irc.channel_bots.pb.Command;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

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
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        final String[] clipboardContent = {null};

        this.syncManager.addListener(new SyncListener() {
            @Override
            public void onPushed(String content, Date date) {
            }

            @Override
            public void onPulled(String content, Date date) {
                clipboardContent[0] = content;

                countDownLatch.countDown();
            }
        });
        this.syncManager.doPull();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.clipboardManager.setContent(clipboardContent[0]);

        this.reply(clipboardContent[0]);
    }
}
