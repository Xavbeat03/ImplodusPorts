package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import me.ninjamandalorian.implodusports.handler.TravelHandler;
import me.ninjamandalorian.implodusports.helper.StringHelper;
import me.ninjamandalorian.implodusports.object.Port;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CommandChangeSize {
	
	private static final ArgumentSuggestions intSuggestion = ArgumentSuggestions.strings("1", "2", "3", "4");
	
	private CommandChangeSize(){}
	
	public static CommandAPICommand registerCommandChangeSize() {
		return new CommandAPICommand("changesize")
			.withPermission("implodusports.admin.changesize")
			.withShortDescription("Changes the size of the player")
			.withArguments((new StringArgument("size")).replaceSuggestions(intSuggestion))
			.executesPlayer((player, args) -> {
				travelCommand(player, StringHelper.remFirst(args.rawArgs()));
			});
	}

	private static void travelCommand(Player player, String[] args) {
		//Bukkit.getLogger().info("Travel recieved with destinations: " + args.toString());

		@SuppressWarnings("unused")
		String[] extraParams = Arrays.copyOfRange(args, 2, args.length - 1);
		Port origin = Port.getPort(args[0]);
		Port destination = Port.getPort(args[1]);
		if (origin != null && destination != null) {
			TravelHandler.startJourney(player, origin, destination, args);
		}
		return;
	}
}
