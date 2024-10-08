package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import me.ninjamandalorian.implodusports.handler.TravelHandler;
import me.ninjamandalorian.implodusports.helper.PortHelper;
import me.ninjamandalorian.implodusports.helper.StringHelper;
import me.ninjamandalorian.implodusports.object.Port;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

class CommandChangeSize {
	
	private static final ArgumentSuggestions intSuggestion = ArgumentSuggestions.strings("1", "2", "3", "4");
	
	private CommandChangeSize(){}
	
	//TODO command doesn't work
	public static CommandAPICommand registerCommandChangeSize() {
		return new CommandAPICommand("changesize")
			.withPermission("implodusports.admin.changesize")
			.withShortDescription("Changes the size of the player")
			.withArguments((new IntegerArgument("size")).replaceSuggestions(intSuggestion))
			.executesPlayer((player, args) -> {
				changeSizeCommand(player, args);
			});
	}


	private static void changeSizeCommand(Player player, CommandArguments args) {
		int newSize = 0;
		if(args.count() < 1){
			player.sendMessage("%s[IPorts]%s Didn't provide a size to change to, exiting command.".formatted(ChatColor.RED, ChatColor.WHITE));
			return;
		} else if (args.count() > 1) {
			player.sendMessage("%s[IPorts]%s Additional non-necessary arguments provided, exiting command.".formatted(ChatColor.RED, ChatColor.WHITE));
			return;
		}
		
		try {
			newSize = Math.max(Math.min(4, (Integer) args.get(0)), 1);
		} catch (NumberFormatException | NullPointerException e) {
			return;
		}
		
		Block targetBlock = player.getTargetBlock(null, 5);
		if (targetBlock == null) return;
		Port port = Port.getPort(targetBlock);
		if (port == null) return;
		port.changeSize(newSize);

		Sign sign = (Sign) targetBlock.getState();
		List<String> signList = PortHelper.formatSign(port);
		for (int i = 0; i < 4; i++) {
			sign.setLine(i, signList.get(i));
		}
		sign.update(true);

		return;
	}
}
