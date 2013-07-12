package net.mms_projects.irc.channel_bots.pb.commands;

import net.mms_projects.irc.channel_bots.pb.Command;
import net.mms_projects.irc.channel_bots.pb.CommandArgument;
import net.mms_projects.irc.channel_bots.pb.CommandHandler;
import net.mms_projects.irc.channel_bots.pb.CommandSyntax;
import net.mms_projects.irc.channel_bots.pb.command_arguments.Text;

public class Help extends Command {

	private Command subject;

	public Help(CommandHandler handler, Command subject) {
		super(
				"help", "Shows some help?", handler); //$NON-NLS-1$ //$NON-NLS-2$

		this.subject = subject;
		this.showInList = false;
	}

	public Help(CommandHandler handler) {
		super(
				"help", "Shows some help?", handler); //$NON-NLS-1$ //$NON-NLS-2$

		this.addSyntax(new CommandSyntax(new Text("subject"))); //$NON-NLS-1$
		this.setLongDescription("This is the help. It will give you information about the available commands. As the help is new it might not contain all information yet."); //$NON-NLS-1$

		this.subject = this;
		this.addHelp();
	}

	@Override
	public boolean match(String rawdata) {
		return rawdata.equalsIgnoreCase(this.getFullCommand());
	}

	@Override
	public void run(String rawdata) {
		boolean helped = false;
		CommandHandler subject = null;
		if (this.subject == this) {
			subject = this.handler;
		} else {
			subject = this.subject;
		}
		if (this.subject.longDescription != null) {
			this.reply(this.subject.longDescription);
			this.reply("- "); //$NON-NLS-1$
			helped = true;
		}
		if (subject.hasCommands()) {
			this.reply(
					String.format(
							"For more information on a specific command, type \\u0002/msg %1$s \\u001Fcommand\\u001F\\u0002.", "bot", //$NON-NLS-1$
							this.getFullCommand()), 200);
			subject.showCommands(2);
			helped = true;
		}
		for (CommandSyntax syntax : this.subject.syntaxes) {
			String syntaxText = ""; //$NON-NLS-1$
			for (CommandArgument argument : syntax.arguments) {
				syntaxText += ""; //$NON-NLS-1$
				if ((argument.defaults != null)
						&& (argument.defaults.length != 0)) {
					syntaxText += "["; //$NON-NLS-1$
					for (String example : argument.defaults) {
						syntaxText += example + "/"; //$NON-NLS-1$
					}
					syntaxText = syntaxText.substring(0,
							syntaxText.length() - 1) + "]"; //$NON-NLS-1$
				} else {
					syntaxText += argument.name;
				}
				syntaxText += " "; //$NON-NLS-1$
			}
			this.reply(
					"Syntax: " + this.subject.getFullCommand() + " " + syntaxText + ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			helped = true;
		}
		if (!helped) {
			this.reply("No help available"); //$NON-NLS-1$
		}
	}

	@Override
	public String getFullCommand() {
		if (this.subject == this) {
			return super.getFullCommand();
		}
		return super.getFullCommand() + " " + this.subject.getFullCommand(); //$NON-NLS-1$
	}

}
