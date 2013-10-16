package net.mms_projects.irc.channel_bots.pb;

public class CommandSyntax {

	public CommandArgument[] arguments;

	public CommandSyntax(CommandArgument... arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public String toString() {
		String syntax = "";
		for (CommandArgument argument : this.arguments) {
			syntax += argument.name + " ";
		}
		return syntax;
	}
	
}
