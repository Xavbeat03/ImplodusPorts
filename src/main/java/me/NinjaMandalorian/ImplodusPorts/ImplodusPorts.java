package me.NinjaMandalorian.ImplodusPorts;

import com.palmergames.bukkit.towny.TownyAPI;
import me.NinjaMandalorian.ImplodusPorts.command.ImplodusPortsCommands;
import me.NinjaMandalorian.ImplodusPorts.data.DataManager;
import me.NinjaMandalorian.ImplodusPorts.data.PortDataManager;
import me.NinjaMandalorian.ImplodusPorts.dymap.DynmapHandler;
import me.NinjaMandalorian.ImplodusPorts.listener.*;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class ImplodusPorts extends JavaPlugin {

	// Plugin Instance
	private static ImplodusPorts instance;
	public static ImplodusPorts getInstance() {
		return instance;
	}

	// Interfaces/APIs
	private Economy econ;
	private TownyAPI townyAPI;
	private BukkitAudiences adventure;
	private DynmapAPI dynmapAPI;
	private MarkerAPI markerAPI;
	public Economy getEconomy() {
		return econ;
	}
	public TownyAPI getTownyAPI() { return townyAPI; }
	public BukkitAudiences getAdventure() { return adventure; }
	public DynmapAPI getDynmapAPI() { return dynmapAPI; }
	public MarkerAPI getMarkerAPI() { return markerAPI; }
	public boolean dynmapIsEnabled = false;

	// Private class variables
	private PluginManager pm;

	public void onEnable() {

		// Initialize API components and check for required dependencies
		instance = this;
		adventure = BukkitAudiences.create(instance);
		pm = getServer().getPluginManager();
		setupEconomy();
		setupTowny();
		setupDynmap();

		// Initialise parts of the plugin
		DataManager.init();
		Settings.init();
		Port.initPorts();

		// Register commands
		new ImplodusPortsCommands();

		// Register listeners
		pm.registerEvents(new InventoryListener(), instance);
		pm.registerEvents(new PlayerListener(), instance);
		pm.registerEvents(new SignListener(), instance);
		pm.registerEvents(new BlockListener(), instance);
		if (townyAPI != null) {
			pm.registerEvents(new TownListener(), instance);
		}

		if(dynmapIsEnabled) {
			// Dynmap hook
			DynmapHandler dynmapHandler = new DynmapHandler();
			dynmapHandler.resetPortMarkers();
		}
	}

	public void onDisable() {
		Bukkit.getLogger().info("Disabling ImplodusPorts");
		PortDataManager.savePortData(Port.getPorts());
	}

	/**
	 *  Checks if Vault is installed and sets up Economy
	 */
	private void setupEconomy() {
		if (pm.getPlugin("Vault") == null) {
			Bukkit.getLogger().severe("Vault was not found. Plugin disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			Bukkit.getLogger().severe("Economy could not be loaded. Plugin disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		econ = rsp.getProvider();
	}

	/**
	 *  Checks if Towny is installed and sets up TownyAPI
	 */
	private void setupTowny() {
		if ((pm.getPlugin("Towny") == null)) {
			Bukkit.getLogger().severe("Towny was not found. Plugin disabling...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		townyAPI = TownyAPI.getInstance();
	}

	/**
	 *  Checks if Towny is installed and sets up TownyAPI
	 */
	private void setupDynmap() {
		if ((pm.getPlugin("dynmap") == null)) {
			Bukkit.getLogger().severe("Dynmap was not found.");
			return;
		}
		dynmapAPI = (DynmapAPI) pm.getPlugin("dynmap");
		markerAPI = dynmapAPI.getMarkerAPI();
		if (markerAPI == null) {
			Bukkit.getLogger().severe("MarkerAPI could not be loaded.");
			return;
		}
		dynmapIsEnabled = true;
	}

}
