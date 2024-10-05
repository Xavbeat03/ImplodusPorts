package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
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
				deletePortCommand(player, StringHelper.remFirst(args.rawArgs()));
			});
	}

	private static void deletePortCommand(Player player, String[] strings) {
		player.sendMessage("%s[IPorts]%s Attempting to delete port...".formatted(ChatColor.RED, ChatColor.WHITE));
		if(strings.length < 1){
			player.sendMessage("%s[IPorts]%s Didn't provide a port to delete, exiting command.".formatted(ChatColor.RED, ChatColor.WHITE));
		} else if (strings.length > 1) {
			player.sendMessage("%s[IPorts]%s Additional non-necessary arguments provided, exiting command.".formatted(ChatColor.RED, ChatColor.WHITE));
		} else {
			// Getting the port by turning the command argument into port ID format
			Port portToDestroy = Port.getPort(strings[0].toLowerCase());
			if (portToDestroy == null) {
				player.sendMessage("%s[IPorts]%s Port didn't exist.".formatted(ChatColor.RED, ChatColor.WHITE));
			} else {
				Port.portDestroy(player, portToDestroy);
			}
		}

	}
}


