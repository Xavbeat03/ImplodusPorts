package me.NinjaMandalorian.ImplodusPorts.ui.tasks;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.Logger;
import me.NinjaMandalorian.ImplodusPorts.ui.BaseMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PageTask implements BaseTask {

	private int direction;

	public PageTask(int dir) {
		this.direction = dir;
	}

	@Override
	public void run(InventoryClickEvent event) {
		BaseMenu menu = (BaseMenu) event.getInventory().getHolder();
		if (menu == null) {
			return;
		}
		menu.changePage(direction);
		ImplodusPorts.getInstance().getLogger().info("PageTask: Page changed by " + direction + " for " + event.getWhoClicked().getName());
	}
}
