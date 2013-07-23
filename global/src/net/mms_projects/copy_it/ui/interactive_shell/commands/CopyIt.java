package net.mms_projects.copy_it.ui.interactive_shell.commands;

import net.mms_projects.copy_it.ClipboardManager;
import net.mms_projects.copy_it.SyncListener;
import net.mms_projects.copy_it.SyncManager;
import net.mms_projects.irc.channel_bots.pb.Command;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
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
        String content = "";

        InputStream inputStream = System.in;
        try {
            if (inputStream.available() > 0) {
                content = IOUtils.toString(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ("".equals(content)) {
            content = this.clipboardManager.getContent();
        }

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        this.syncManager.addListener(new SyncListener() {
            @Override
            public void onPushed(String content, Date date) {
                countDownLatch.countDown();
            }

            @Override
            public void onRemoteContentChange(String content, Date date) {
            }
        });
        this.syncManager.updateRemoteContentAsync(content, new Date());

        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }

        this.reply("Pushed: " + content);
    }
}
