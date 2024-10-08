package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import me.ninjamandalorian.implodusports.helper.StringHelper;
import me.ninjamandalorian.implodusports.object.Port;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

class CommandDestroy {
	
	private CommandDestroy(){}

	//TODO command doesn't work
	public static CommandAPICommand registerCommandDestroy() {
		return new CommandAPICommand("destroy")
			.withPermission("implodusports.admin.destroy")
			.withShortDescription("Deletes a port")
			.withArguments(new StringArgument("port")
				.replaceSuggestions(ArgumentSuggestions.strings(new ArrayList<>(Port.getPorts().keySet()))))
			.executesPlayer((player, args) -> {
				deletePortCommand(player, args);
			});
	}

	private static void deletePortCommand(Player player, CommandArguments args) {
		player.sendMessage("%s[IPorts]%s Attempting to delete port...".formatted(ChatColor.RED, ChatColor.WHITE));
		if(args.count() < 1){
			player.sendMessage("%s[IPorts]%s Didn't provide a port to delete, exiting command.".formatted(ChatColor.RED, ChatColor.WHITE));
		} else if (args.count() > 1) {
			player.sendMessage("%s[IPorts]%s Additional non-necessary arguments provided, exiting command.".formatted(ChatColor.RED, ChatColor.WHITE));
		} else {
			// Getting the port by turning the command argument into port ID format
			Port portToDestroy = Port.getPort(args.get(0).toString().toLowerCase());
			if (portToDestroy == null) {
				player.sendMessage("%s[IPorts]%s Port didn't exist.".formatted(ChatColor.RED, ChatColor.WHITE));
			} else {
				Port.portDestroy(player, portToDestroy);
			}
		}

	}
}


