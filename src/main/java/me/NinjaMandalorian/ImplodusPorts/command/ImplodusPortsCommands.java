package me.NinjaMandalorian.ImplodusPorts.command;

import com.palmergames.bukkit.towny.utils.NameUtil;
import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.Logger;
import me.NinjaMandalorian.ImplodusPorts.handler.TravelHandler;
import me.NinjaMandalorian.ImplodusPorts.helper.PortHelper;
import me.NinjaMandalorian.ImplodusPorts.helper.StringHelper;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImplodusPortsCommands implements CommandExecutor, TabCompleter {

	private static final List<String> portTabCompletes = Arrays.asList(
		"changesize",
		"reload",
		"destroy"
	);


	public ImplodusPortsCommands() {
		ImplodusPorts plugin = ImplodusPorts.getInstance();
		plugin.getCommand("implodusports").setExecutor(this);
		plugin.getCommand("implodusports").setTabCompleter(this);
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

	private void deletePortCommand(Player player, String[] strings) {
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

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

		if(sender instanceof Player) {
			if(args.length == 0) {
				return portTabCompletes;
			}

			if(args.length <= 2) {
				switch (args[0].toLowerCase()) {
					case "changesize":
						return Arrays.asList(
							"1",
							"2",
							"3",
							"4"
						);
					case "destroy":
						List<String> portIds = new ArrayList<>(Port.getPorts().keySet());
						if(args.length == 1) return null;
						return NameUtil.filterByStart(portIds, args[1]);
					case "reload":
						return null;
					default:
						return NameUtil.filterByStart(portTabCompletes, args[0]);
				}
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player player) {
			if (args.length == 0) {
				return false;
			}

			switch (args[0].toLowerCase()) {
				case "travel":
					travelCommand(player, StringHelper.remFirst(args));
					return true;
				case "next":
					player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					TravelHandler.scheduleNext(player);
					return true;
				case "changesize":
					if (player.hasPermission("implodusports.admin.changesize")) {
						changeSizeCommand(player, StringHelper.remFirst(args));
					}
					return true;
				case "destroy":
					if (player.hasPermission("implodusports.admin.destroy")){
						System.out.println("Made it here!");
						deletePortCommand(player, StringHelper.remFirst(args));
					}
					return true;
				case "reload":
					if (player.hasPermission("implodusports.admin.reload")) {
						Settings.reloadConfig();
					}
					return true;
				default:
					return true;
			}
		}
		return false;
	}



}
