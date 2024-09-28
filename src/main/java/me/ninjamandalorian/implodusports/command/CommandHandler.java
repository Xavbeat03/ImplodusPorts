package me.ninjamandalorian.implodusports.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import me.ninjamandalorian.implodusports.ImplodusPorts;
import me.ninjamandalorian.implodusports.utility.Reloadable;

/**
 * Handles the command registration and execution of the plugin.
 */
public class CommandHandler implements Reloadable {
	private final ImplodusPorts plugin;

	public CommandHandler(ImplodusPorts plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(new CommandAPIBukkitConfig(plugin).shouldHookPaperReload(true).silentLogs(true));
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable();
		
		// Register commands
		CommandIports.registerCommandIports();
		
	}

	@Override
	public void onDisable() {
		CommandAPI.onDisable();
	}
}
