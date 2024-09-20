package me.NinjaMandalorian.ImplodusPorts.data;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class PortDataManager {

	private static final String filePath = "ports.csv";
	private static String categories = ""
		+ "id,"
		+ "signLocation,"
		+ "teleportLocation,"
		+ "size,"
		+ "displayName";

	/**
	 * Loads in port data from file, returns as a map
	 *
	 * @return Map of ports
	 */
	public static Map<String, Port> loadPortData() {
		HashMap<String, Port> returnData = new HashMap<String, Port>();

		String rawData = DataManager.getRawData(filePath);

		// Splits with newline then remove categories
		String[] portsData = rawData.split("\n");

		for (String portData : portsData) {
			String[] splitData = splitWithQuotes(portData);
			if (splitData.length < 5 || splitData[0].equalsIgnoreCase("id")) continue;
			try {
				Port port = new Port(
					splitData[0],
					stringToLocation(splitData[1]),
					stringToLocation(splitData[2]),
					Integer.parseInt(splitData[3]),
					splitData[4]
				);

				returnData.put(splitData[0], port);
			}
			catch (NotRegisteredException e){
				Bukkit.getLogger().info("Port " + splitData[0] + " had an error while loading.");
			}
		}

		return returnData;
	}

	/**
	 * Saves a port to file
	 *
	 * @param port - Port to save
	 */
	public static void savePort(Port port) {
		HashMap<String, Port> data = (HashMap<String, Port>) loadPortData();
		data.put(port.getId(), port);

		savePortData(data);
	}

	public static void deletePort(Port port) {
		HashMap<String, Port> data = (HashMap<String, Port>) loadPortData();
		data.remove(port.getId());
	}

	/**
	 * Saves port data to file
	 *
	 * @param data - Port data
	 */
	public static void savePortData(Map<String, Port> data) {
		StringBuilder rawData = new StringBuilder();
		rawData.append(categories).append("\n");
		for (Port port : data.values()) {
			rawData.append(portToString(port));
			rawData.append("\n");
		}
		DataManager.saveRawData(filePath, rawData.toString());
	}

	/**
	 * Converts port to string for saving
	 *
	 * @param port - Port to convert
	 * @return Port string
	 */
	private static String portToString(Port port) {
		return port.getId() + ","
			+ locationToString(port.getSignLocation()) + ","
			+ locationToString(port.getTeleportLocation()) + ","
			+ port.getSize() + ","
			+ port.getDisplayName()
			;
	}

	/**
	 * Splits string for .csv format, ignoring commas within brackets.
	 *
	 * @param string - Input string
	 * @return Split array excluding quoted
	 */
	
	private static String[] splitWithQuotes(String string) {
		String[] split = string.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		if (string.contains("\"")) {
			for (int i = 0; i < split.length; i++) {
				split[i] = split[i].replace("\"", "");
			}
		}
		return split;
	}

	/**
	 * Converts a saved string to a Location object.
	 *
	 * @param string - Saved string
	 * @return Bukkit location or null
	 */
	private static Location stringToLocation(String string) {
		String[] array = string.split(",");
		if (array.length == 4) {
			return new Location(
				Bukkit.getWorld(array[0]),
				Double.parseDouble(array[1]),
				Double.parseDouble(array[2]),
				Double.parseDouble(array[3])
			);
		} else if (array.length == 6) {
			return new Location(
				Bukkit.getWorld(array[0]),
				Double.parseDouble(array[1]),
				Double.parseDouble(array[2]),
				Double.parseDouble(array[3]),
				Float.parseFloat(array[4]),
				Float.parseFloat(array[5])
			);
		}
		return null;
	}

	/**
	 * Converts a location object to a string.
	 *
	 * @param location - Location
	 * @return String
	 */
	private static String locationToString(Location location) {
		String returnString = "\"";

		returnString += location.getWorld().getName() + ",";
		returnString += location.getX() + ",";
		returnString += location.getY() + ",";
		returnString += location.getZ() + ",";
		returnString += location.getYaw() + ",";
		returnString += location.getPitch() + ",";

		returnString += "\"";
		return returnString;
	}

}
