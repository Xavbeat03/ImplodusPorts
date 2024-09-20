package me.NinjaMandalorian.ImplodusPorts.object;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.Logger;
import me.NinjaMandalorian.ImplodusPorts.data.PortDataManager;
import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Port object, handles all interactions with individual ports.
 *
 * @author NinjaMandalorian
 */
public class Port {

	private static Map<String, Port> activePorts = new HashMap<>();

	private final String id;
	private final Location signLocation;
	private final Location teleportLocation;
	private int size;
	private final String displayName;

	private Town town;


	/**
	 * Constructor for individual ports.
	 *
	 * @param size - Size of the port
	 * @param displayName - Display name of the port
	 */
	public Port(String id, Location signLocation, Location tLocation, int size, String displayName) throws NotRegisteredException {
		this.id = id;
		this.signLocation = signLocation;
		this.teleportLocation = tLocation;
		this.size = size;
		this.displayName = displayName;
		if(ImplodusPorts.getInstance().getTownyAPI().getTownBlock(signLocation) != null) {
			this.town = ImplodusPorts.getInstance().getTownyAPI().getTownBlock(signLocation).getTown();
		}
		else {
			Logger.log("Town not found for port " + id);
		}
		

	}
	
	public Port(String id, Location sLocation, Location tLocation, int size) throws NotRegisteredException {
		this(id, sLocation, tLocation, size, id);
	}

	/**
	 * Gets all the icons for each type of port.
	 *
	 * @return List of Materials
	 */
	public static List<Material> getIcons() {
		return Arrays.asList(Material.GLASS, Material.BIRCH_BOAT, Material.OAK_BOAT, Material.DARK_OAK_BOAT,
			Material.MANGROVE_BOAT);
	}

	/**
	 * Gives icon for port size
	 *
	 * @param size - Port size
	 * @return Material
	 */
	public static Material getIcon(int size) {
		if (size >= getIcons().size())
			return Material.GLASS;
		return getIcons().get(size);
	}

	/**
	 * Get all Ports
	 *
	 * @return Map of a Ports
	 */
	public static Map<String, Port> getPorts() {
		return activePorts;
	}

	public static Port getPort(String id) {
		return activePorts.getOrDefault(id, null);
	}

	public static Port getPort(Location location) {
		return getPort(location.getBlock());
	}

	/**
	 * Gets a port by block
	 * @param block - Block to get port for
	 * @return Port or null
	 */
	public static Port getPort(Block block) {
		return activePorts.values().stream()
			.filter(port -> port.getSignLocation().getBlock().equals(block))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Gets a port by town
	 * @param town - Town to get port for
	 * @return Port or null
	 */
	public static Port getPort(Town town) {
		return activePorts.values().stream()
			.filter(port -> port.getTown() != null && port.getTown().equals(town))
			.findFirst()
			.orElse(null);
	}

	public static void initPorts() {
		activePorts = PortDataManager.loadPortData();
	}

	public static void portCreate(Player player, Port port) {
		player.sendMessage("%s[IPorts]%s CREATED PORT".formatted(ChatColor.RED, ChatColor.WHITE));
		Logger.log(player.getName() + " created port " + port.getId());
		activePorts.put(port.getId(), port);
		PortDataManager.savePort(port);
	}

	public static void portDestroy(Player player, Port port) {
		player.sendMessage("%s[IPorts]%s DESTROYED PORT".formatted(ChatColor.RED, ChatColor.WHITE));
		Logger.log(player.getName());
		portDestroy(port);
	}

	/**
	 * Destroys a port
	 * @param port - Port to destroy
	 */
	public static synchronized void portDestroy(Port port) {
		if(port == null) return;
		Logger.log("[IPorts] Destroyed port " + port.getId());
		activePorts.remove(port.getId());
		if(port.getSignLocation().getBlock().getType() != Material.AIR) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ImplodusPorts.getInstance(), 
				() -> port.getSignLocation().getBlock().setType(Material.AIR, false));

		}
		PortDataManager.deletePort(port);
	}

	public String getId() {
		return this.id;
	}

	public Location getSignLocation() {
		return this.signLocation;
	}

	public Location getTeleportLocation() {
		return this.teleportLocation;
	}

	public int getSize() {
		return this.size;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Town getTown() {
		return this.town;
	}

	public List<Port> getNearby() {
		ArrayList<Port> returnList = new ArrayList<>();
		Logger.debug("GETTING NEARBY FOR " + this.id);
		for (Port port : activePorts.values()) {
			Double distance = this.distanceTo(port);
			Double port1Range = switch (size) {
				case 1 -> Settings.smallDistance;
				case 2 -> Settings.mediumDistance;
				case 3 -> Settings.largeDistance;
				case 4 -> Settings.megaDistance;
				default -> 0.0;
			};
			Double port2Range = switch (port.getSize()) {
				case 1 -> Settings.smallDistance;
				case 2 -> Settings.mediumDistance;
				case 3 -> Settings.largeDistance;
				case 4 -> Settings.megaDistance;
				default -> 0.0;
			};
			if ((port.equals(this) && port.getTeleportLocation().getWorld() != this.signLocation.getWorld()) || (distance > port1Range && distance > port2Range)) {
				continue;
			}
			returnList.add(port);
		}

		if (returnList.isEmpty()) {
			Port closestPort = null;
			for (Port port : activePorts.values()) {
				if (closestPort == null || this.distanceTo(closestPort) > this.distanceTo(port)) {
					closestPort = port;
				}
			}
			returnList.add(closestPort);
		}

		return returnList;
	}

	/**
	 * Gets the distance between two ports
	 * @param port - Port to get distance to
	 * @return Distance 
	 */
	public Double distanceTo(Port port) {
		// If the worlds are different, return 0
		if (this.signLocation.getWorld() == null 
			|| port.getSignLocation().getWorld() == null 
			|| !this.signLocation.getWorld().equals(port.getSignLocation().getWorld()))
			return 0.0;

		return (this.getSignLocation().distance(port.getSignLocation()));
	}

	/**
	 * Changes the size of the port
	 * @param newSize - New size, 1-4
	 */
	public void changeSize(int newSize) {
		this.size = Math.min(4, Math.max(1, newSize));
		PortDataManager.savePort(this);
	}

}
