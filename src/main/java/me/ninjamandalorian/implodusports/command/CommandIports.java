package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;

public class CommandIports {
	public static CommandAPICommand registerCommandIports() {
		return new CommandAPICommand("implodusports")
			.withAliases("iports")
			.withShortDescription("Main command for Implodusports")
			.withSubcommands(
				CommandNext.registerCommandNext(),
				CommandDestroy.registerCommandDestroy(),
				CommandReload.registerCommandReload(),
				CommandChangeSize.registerCommandChangeSize()
			);
		
	}
}
