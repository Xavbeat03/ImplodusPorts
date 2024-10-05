package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;
import me.ninjamandalorian.implodusports.handler.TravelHandler;
import org.bukkit.Sound;

class CommandNext {
	
	private CommandNext(){}

	//TODO command doesn't work	
	public static CommandAPICommand registerCommandNext() {
		return new CommandAPICommand("next")
			.withShortDescription("Starts journey to the next location")
			.executesPlayer((player, args) -> {
				player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				TravelHandler.scheduleNext(player);
			});
		
	}
}
