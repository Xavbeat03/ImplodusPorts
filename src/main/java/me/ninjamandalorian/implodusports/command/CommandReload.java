package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;
import me.ninjamandalorian.implodusports.settings.Settings;

class CommandReload {
	
	private CommandReload(){}

	//TODO command doesn't work
	public static CommandAPICommand registerCommandReload() {
		return new CommandAPICommand("reload")
			.withPermission("implodusports.admin.reload")
			.withShortDescription("Reloads the configuration file")
			.executes((sender, args) -> {
				Settings.reloadConfig();
			});
	}
}
