package net.mms_projects.irc.channel_bots.pb;

public abstract class CommandArgument {

	public String name;
	public String[] defaults;

	public CommandArgument(String name) {
		this.name = name;
	}
	
	public CommandArgument(String name, String[] defaults) {
		this.name = name;
		this.defaults = defaults;
	}

}
