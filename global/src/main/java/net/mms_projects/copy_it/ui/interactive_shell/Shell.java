package net.mms_projects.copy_it.ui.interactive_shell;

import net.mms_projects.copy_it.ui.interactive_shell.commands.CopyIt;
import net.mms_projects.copy_it.ui.interactive_shell.commands.PasteIt;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;
import net.mms_projects.irc.channel_bots.pb.commands.Help;

import java.io.*;

public class Shell {

    private String prompt;
    private InputStream inputStream;
    private OutputStream outputStream;
    private CommandHandler commandHandler;

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public Shell(InputStream inputStream, OutputStream outputStream, CommandHandler commandHandler) {
        this.setInputStream(inputStream);
        this.setOutputStream(outputStream);
        this.setCommandHandler(commandHandler);
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void loop() {
        PrintStream printStream = new PrintStream(this.outputStream);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));

        printStream.print(this.getPrompt());
        try {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                boolean handled = commandHandler.handle(line);
                if (!handled) {
                    printStream.println("Unknown command");
                }
                printStream.print(this.getPrompt());
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
