package me.NinjaMandalorian.ImplodusPorts.ui.tasks;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.ui.BaseMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryTask implements BaseTask {

	private final BaseMenu menu;

	public InventoryTask(BaseMenu menu) {
		this.menu = menu;
	}

	@Override
	public void run(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		menu.open(player);
		ImplodusPorts.getInstance().getLogger().info("InventoryTask: Inventory opened for " + player.getName());
	}

}
