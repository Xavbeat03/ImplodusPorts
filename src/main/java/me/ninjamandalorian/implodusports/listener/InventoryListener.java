package me.ninjamandalorian.implodusports.listener;

import me.ninjamandalorian.implodusports.ui.BaseButton;
import me.ninjamandalorian.implodusports.ui.BaseMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

	/**
	 * Handles inventories being clicked --> If inventory held by BaseMenu, corresponding task is run.
	 *
	 * @param e - Event
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getInventory().getHolder() instanceof BaseMenu menu) {
			e.setCancelled(true); // Immediately cancels any movement of items.

			// If outside of custom inventory, returns.
			int slotNum = e.getRawSlot();
			if (slotNum + 1 > e.getInventory().getSize()) return;

			BaseButton button = menu.getButton(slotNum);
			if (button != null) button.run(e);
		} else return;
	}
}
