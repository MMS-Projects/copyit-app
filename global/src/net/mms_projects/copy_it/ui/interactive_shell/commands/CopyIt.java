package net.mms_projects.copy_it.ui.interactive_shell.commands;

import net.mms_projects.copy_it.ClipboardListener;
import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.irc.channel_bots.pb.Command;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class CopyIt extends Command {
    private final SyncManager syncManager;
    private final ClipboardManager clipboardManager;

    public CopyIt(CommandHandler commandHandler, SyncManager syncManager, ClipboardManager clipboardManager) {
        super("copyit", "Pushes content to the server", commandHandler);

        this.syncManager = syncManager;
        this.clipboardManager = clipboardManager;

        this.addHelp();
    }

    @Override
    public void run(String rawdata) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        String content = this.clipboardManager.getContent();

        this.syncManager.addListener(new SyncListener() {
            @Override
            public void onPushed(String content, Date date) {
                                     countDownLatch.countDown();
            }

            @Override
            public void onPulled(String content, Date date) {
            }
        });
        this.syncManager.doPush(content, new Date());

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
        }

        this.reply("Pushed: " + content);
    }
}
