package net.mms_projects.irc.channel_bots.pb.command_arguments;

import net.mms_projects.irc.channel_bots.pb.Command;
import net.mms_projects.irc.channel_bots.pb.CommandArgument;

public class SubCommand extends CommandArgument {

	public SubCommand(String name, Command[] subCommands) {
		super(name, getSubCommands(subCommands));
	}
	
	public static String[] getSubCommands(Command[] subCommands) {
		String[] defaults = new String[subCommands.length];
		int i = 0;
		for (Command subCommand : subCommands) {
			defaults[i] = subCommand.command;
			++i;
		}
		return defaults;
	}

}
