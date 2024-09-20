package me.NinjaMandalorian.ImplodusPorts.settings;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class Settings {

	// Default config values. These are set to prevent potential null errors for
	// unimplemented variables.
	public static double smallDistance = 500.00;
	public static double smallCost = 50.00;
	public static double smallSpeed = 25.00;
	public static double smallWalkRadius = 50.00;
	public static double mediumDistance = 1000.00;
	public static double mediumCost = 40.00;
	public static double mediumSpeed = 30.00;
	public static double mediumWalkRadius = 50.00;
	public static double largeDistance = 2000.00;
	public static double largeCost = 30.00;
	public static double largeSpeed = 35.00;
	public static double largeWalkRadius = 50.00;
	public static double megaDistance = 5000.00;
	public static double megaCost = 15.00;
	public static double megaSpeed = 40.00;
	public static double megaWalkRadius = 50.00;
	public static Level loggerLevel;
	public static double walkWarningRadiusPercentage = 0.8;
	public static String DynmapIconName = "anchor";
	private static ImplodusPorts plugin;
	private static FileConfiguration config;

	public static void init() {

		plugin = ImplodusPorts.getInstance();

		plugin.saveDefaultConfig();
		config = plugin.getConfig();

		smallDistance = config.getDouble("sizes.small.distance");
		smallCost = config.getDouble("sizes.small.cost");
		smallSpeed = config.getDouble("sizes.small.speed");
		smallWalkRadius = config.getDouble("sizes.small.walk_radius");
		mediumDistance = config.getDouble("sizes.medium.distance");
		mediumCost = config.getDouble("sizes.medium.cost");
		mediumSpeed = config.getDouble("sizes.medium.speed");
		mediumWalkRadius = config.getDouble("sizes.medium.walk_radius");
		largeDistance = config.getDouble("sizes.large.distance");
		largeCost = config.getDouble("sizes.large.cost");
		largeSpeed = config.getDouble("sizes.large.speed");
		largeWalkRadius = config.getDouble("sizes.large.walk_radius");
		megaDistance = config.getDouble("sizes.mega.distance");
		megaCost = config.getDouble("sizes.mega.cost");
		megaSpeed = config.getDouble("sizes.mega.speed");
		megaWalkRadius = config.getDouble("sizes.mega.walk_radius");
		walkWarningRadiusPercentage = config.getDouble("walk_warning_radius_percentage");
		DynmapIconName = config.getString("dynmap.icon_name");

		loggerLevel = (config.getString("logger.level") == null) ? Level.INFO : Level.parse(config.getString("logger.level"));
	}

	public static Double getBaseCost() {
		return config.getDouble("basecost");
	}
	
	/**
	 * Reloads the config with the internal config.yml
	 */
	public static void reloadConfig() {
		ImplodusPorts.getInstance().reloadConfig();
		ImplodusPorts.getInstance().saveDefaultConfig();

		plugin = ImplodusPorts.getInstance();

		plugin.saveDefaultConfig();
		config = plugin.getConfig();

		smallDistance = config.getDouble("sizes.small.distance");
		smallCost = config.getDouble("sizes.small.cost");
		smallSpeed = config.getDouble("sizes.small.speed");
		smallWalkRadius = config.getDouble("sizes.small.walk_radius");
		mediumDistance = config.getDouble("sizes.medium.distance");
		mediumCost = config.getDouble("sizes.medium.cost");
		mediumSpeed = config.getDouble("sizes.medium.speed");
		mediumWalkRadius = config.getDouble("sizes.medium.walk_radius");
		largeDistance = config.getDouble("sizes.large.distance");
		largeCost = config.getDouble("sizes.large.cost");
		largeSpeed = config.getDouble("sizes.large.speed");
		largeWalkRadius = config.getDouble("sizes.large.walk_radius");
		megaDistance = config.getDouble("sizes.mega.distance");
		megaCost = config.getDouble("sizes.mega.cost");
		megaSpeed = config.getDouble("sizes.mega.speed");
		megaWalkRadius = config.getDouble("sizes.mega.walk_radius");
	}

}
