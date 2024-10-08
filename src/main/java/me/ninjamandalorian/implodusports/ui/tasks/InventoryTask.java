package me.ninjamandalorian.implodusports.ui.tasks;

import me.ninjamandalorian.implodusports.ui.BaseMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryTask implements BaseTask {

	private BaseMenu menu;

	public InventoryTask(BaseMenu menu) {
		this.menu = menu;
	}

	@Override
	public void run(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		menu.open(player);
	}

}
