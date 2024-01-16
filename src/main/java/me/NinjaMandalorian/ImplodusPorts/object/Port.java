package me.NinjaMandalorian.ImplodusPorts.object;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.Logger;
import me.NinjaMandalorian.ImplodusPorts.data.PortDataManager;
import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Port object, handles all interactions with individual ports.
 *
 * @author NinjaMandalorian
 */
public class Port {

	private static HashMap<String, Port> activePorts = new HashMap<String, Port>();

	private String id;
	private Location signLocation;
	private Location teleportLocation;
	private int size;
	private String displayName;

	private Town town;


	/**
	 * Constructor for individual ports.
	 *
	 * @param size
	 * @param displayName
	 */
	public Port(String id, Location sLocation, Location tLocation, int size, String displayName) throws NotRegisteredException {
		this.id = id;
		this.signLocation = sLocation;
		this.teleportLocation = tLocation;
		this.size = size;
		this.displayName = displayName;
		this.town = null;
		if(ImplodusPorts.getInstance().isTownyEnabled() &&  ImplodusPorts.getInstance().getTownyAPI().getTownBlock(sLocation) != null) {
			try {
				this.town = ImplodusPorts.getInstance().getTownyAPI().getTownBlock(sLocation).getTown();
			}
			catch(NullPointerException e) {
				this.town = null;
			}
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
	 * @return List of a Ports
	 */
	public static HashMap<String, Port> getPorts() {
		return activePorts;
	}

	public static Port getPort(String id) {
		return activePorts.get(id);
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
		for (Port port : activePorts.values()) {
			if (port.getSignLocation().getBlock().equals(block)) {
				return port;
			}
		}
		return null;
	}

	/**
	 * Gets a port by town
	 * @param town - Town to get port for
	 * @return Port or null
	 */
	public static Port getPort(Town town) {
		for (Port port : activePorts.values()) {
			if (port.getTown() != null && port.getTown().equals(town)) {
				return port;
			}
		}
		return null;
	}

	public static void initPorts() {
		activePorts = PortDataManager.loadPortData();
	}

	public static void portCreate(Player player, Port port) {
		player.sendMessage("[IPorts] CREATED PORT");
		Logger.log(("Player " + player != null ? player.getName() : "CONSOLE") + " created port " + port.getId());
		activePorts.put(port.getId(), port);
		PortDataManager.savePort(port);
	}

	public static void portDestroy(Player player, Port port) {
		player.sendMessage("[IPorts]  DESTROYED PORT");
		Logger.log("Player " + player != null ? player.getName() : "CONSOLE" + " destroyed port " + port.getId());
		portDestroy(port);
	}

	/**
	 * Destroys a port
	 * @param port - Port to destroy
	 */
	public static synchronized void portDestroy(Port port) {
		if(port== null) return;
		Logger.log("[IPorts] Destroyed port " + port.getId());
		activePorts.remove(port.getId());
		if(port.getSignLocation().getBlock().getType() != Material.AIR) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ImplodusPorts.getInstance(), () -> {
				port.getSignLocation().getBlock().setType(Material.AIR, false);
			});

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
		ArrayList<Port> returnList = new ArrayList<Port>();
		//Logger.debug("GETTING NEARBY FOR " + this.id);
		for (Port port : activePorts.values()) {
			if (port.equals(this)) {
				if (port.getTeleportLocation().getWorld() != this.signLocation.getWorld()) {
					continue;
				}
			}
			Double distance = this.distanceTo(port);
			Double port1Range = 0.0;
			switch (size) {
				case 1:
					port1Range = Settings.smallDistance;
					break;
				case 2:
					port1Range = Settings.mediumDistance;
					break;
				case 3:
					port1Range = Settings.largeDistance;
					break;
				case 4:
					port1Range = Settings.megaDistance;
					break;
			}
			Double port2Range = 0.0;
			switch (port.getSize()) {
				case 1:
					port2Range = Settings.smallDistance;
					break;
				case 2:
					port2Range = Settings.mediumDistance;
					break;
				case 3:
					port2Range = Settings.largeDistance;
					break;
				case 4:
					port2Range = Settings.megaDistance;
					break;
			}
			if (distance > port1Range && distance > port2Range) {
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

	public Double distanceTo(Port port) {
		if (!this.signLocation.getWorld().equals(port.getSignLocation().getWorld()))
			return 0.0;
		return (this.getSignLocation().distance(port.getSignLocation()));
	}

	public void changeSize(int newSize) {
		this.size = Math.min(4, Math.max(1, newSize));
		PortDataManager.savePort(this);
	}

}
