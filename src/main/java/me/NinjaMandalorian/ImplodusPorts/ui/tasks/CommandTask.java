package me.NinjaMandalorian.ImplodusPorts.ui.tasks;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class CommandTask implements BaseTask {

	private final String commandString;
	private boolean closeMenu = false;

	public CommandTask(String command) {
		this.commandString = command;
	}

	public CommandTask autoClose() {
		closeMenu = true;
		return this;
	}

	@Override
	public void run(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		player.chat(commandString);
		if (closeMenu) player.closeInventory();
		ImplodusPorts.getInstance().getLogger().info("CommandTask: Command executed by " + player.getName() + ": " + commandString);
	}

}
