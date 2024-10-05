package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import me.ninjamandalorian.implodusports.handler.TravelHandler;
import me.ninjamandalorian.implodusports.helper.PortHelper;
import me.ninjamandalorian.implodusports.helper.StringHelper;
import me.ninjamandalorian.implodusports.object.Port;
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
				changeSizeCommand(player, StringHelper.remFirst(args.rawArgs()));
			});
	}


	private static void changeSizeCommand(Player player, String[] args) {
		int newSize = 0;
		try {
			newSize = Math.max(Math.min(4, Integer.parseInt(args[0])), 1);
		} catch (NumberFormatException e) {
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
