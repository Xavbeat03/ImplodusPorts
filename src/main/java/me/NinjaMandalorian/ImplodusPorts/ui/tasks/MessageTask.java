package me.NinjaMandalorian.ImplodusPorts.ui.tasks;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.logging.Logger;

public class MessageTask implements BaseTask {

	private final String message;

	public MessageTask(String msg) {
		this.message = msg;
	}

	@Override
	public void run(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		player.sendMessage(message);
		ImplodusPorts.getInstance().getLogger().info("Message sent to " + player.getName() + ": " + message);
	}

}
