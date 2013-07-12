package net.mms_projects.irc.channel_bots.pb;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler {

    protected List<Command> commands = new ArrayList<Command>();
    protected PrintStream printStream;

    public PrintStream getPrintStream() {
        return printStream;
    }

    public void setPrintStream(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void addCommand(Command command) {
        this.commands.add(command);
    }

    public boolean hasCommands() {
        return !this.commands.isEmpty();
    }

    final public void showCommands(int maxDepth) {
        this.showCommands("  ", 0, maxDepth);
    }

    final public void reply(String text) {
        this.reply( text, 50);
    }

    public void reply(String text, int wrap) {
        String[] words = text.split(" ");
        String line = "";
        int word = 0;
        while (word < words.length) {
            while (line.length() < wrap && word < words.length) {
                line += words[word] + " ";
                word++;
            }
            printStream.println(line);
            line = "";
        }
    }

    public void showCommands(String prefix, int depth,
                             int maxDepth) {
        if (depth >= maxDepth) {
            return;
        }
        for (Command command : this.commands) {
            if (!command.showInList) {
                continue;
            }
           this.reply(
                   prefix + String.format("%1$-10s", command.command)
                           + " - " + command.shortDescription);
            command.showCommands(prefix + "-  ", depth + 1, maxDepth);
        }
    }

    public boolean handle(String rawdata) {
        Command cmd = null;
        for (Command command : this.commands) {
            if (command.match(rawdata)) {
                cmd = command;
            }
        }
        if (cmd != null) {
            cmd.run(rawdata);
            return true;
        } else {
            return false;
        }
    }

}
