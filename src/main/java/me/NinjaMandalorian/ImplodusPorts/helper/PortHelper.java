package me.NinjaMandalorian.ImplodusPorts.helper;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import me.NinjaMandalorian.ImplodusPorts.Logger;
import me.NinjaMandalorian.ImplodusPorts.handler.TravelHandler;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import me.NinjaMandalorian.ImplodusPorts.ui.PortMenu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PortHelper {


	/**
	 * Orders ports for display. <br>
	 * Megaports, then rest. Closest to farthest.
	 *
	 * @param currentPort - Port ordering from
	 * @param toSort      - List of ports to sort
	 * @return Sorted Port List.
	 */
	public static List<Port> orderPorts(Port currentPort, List<Port> toSort) {
		List<Port> returnList = new ArrayList<Port>();
		TreeMap<Double, Port> sortedMap = new TreeMap<Double, Port>();

		// Removes self
		toSort.remove(currentPort);

		// Adds all megaports to sortedMap & removes from toSort
		for (Port port : toSort) {
			if (port.getSize() == 4) {
				Double distance = currentPort.distanceTo(port);
				while (sortedMap.containsKey(distance)) distance += 0.01;
				sortedMap.put(distance, port);
			}
		}

		// Adds all megaports to final list & clears sortedMap
		returnList.addAll(sortedMap.values());
		sortedMap.clear();

		// Re-uses sortedMap for rest of ports
		for (Port port : toSort) {
			if (port.getSize() != 4) {
				Double distance = currentPort.distanceTo(port);
				while (sortedMap.containsKey(distance)) distance += 0.01;
				sortedMap.put(distance, port);
			}
		}
		// Adds rest and returns.
		returnList.addAll(sortedMap.values());

		return returnList;
	}

	public static Port portFromSign(Player player, Block block, String[] lines) {
		try {
			lines[1] = lines[1].replace(':', ' ');
			String id = lines[1].toLowerCase().strip().replace(' ', '_');
			String displayName = StringHelper.capitalize(lines[1].strip().replace('_', ' '));
			Location signLocation = block.getLocation();
			Location teleportLocation = squareLocation(player.getLocation());

			Port port = new Port(id, signLocation, teleportLocation, 1, displayName);
			return port;
		}
		catch (NotRegisteredException e){
			Logger.debug("Port creation failed.");
			return null;
		}
	}

	public static List<String> formatSign(Port port) {
		//Logger.debug("FORMAT SIGN FOR : " + port.getDisplayName());

		ArrayList<String> returnList = new ArrayList<String>();

		returnList.add(0, ChatColor.translateAlternateColorCodes('&', "&5&l[Port]"));
		returnList.add(1, ChatColor.GOLD + port.getDisplayName());
		returnList.add(2, PortMenu.portSizeString(port).substring(0, 2) + ChatColor.ITALIC + PortMenu.portSizeString(port).substring(2));
		returnList.add(3, "");

		return returnList;
	}

	public static Location squareLocation(Location location) {
		return new Location(location.getWorld(),
			midway(location.getX()),
			midway(location.getY()),
			midway(location.getZ()),
			Math.round(location.getYaw() / 45) * 45,
			0);
	}

	private static Double midway(Double number) {
		return Math.floor(number) + 0.5;
	}

	public static ArrayList<Port> getAvailablePorts(Player player, World world) {
		ArrayList<Port> portList = (ArrayList<Port>) Port.getPorts().values();

		for (Port port : portList) {
			if (!port.getTeleportLocation().getWorld().equals(world)) {
				portList.remove(port);
				continue;
			}

			if (!TravelHandler.canTravelTo(port, player)) {
				portList.remove(port);
			}

		}

		return portList;
	}

	public static List<Port> delFront(List<Port> inList) {
		Port[] inArray = inList.toArray(new Port[inList.size()]);
		ArrayList<Port> returnList = new ArrayList<Port>();
		for (int i = 1; i < inArray.length; i++) {
			returnList.add(inArray[i]);
		}

		return returnList;
	}
}
