package net.mms_projects.irc.channel_bots.pb.command_arguments;

import net.mms_projects.irc.channel_bots.pb.CommandArgument;

public class Text extends CommandArgument {

	public Text(String name) {
		super(name);
	}
	
	public Text(String name, String[] defaults) {
		super(name, defaults);
	}
	
}
