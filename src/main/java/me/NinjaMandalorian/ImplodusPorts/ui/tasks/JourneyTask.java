package me.NinjaMandalorian.ImplodusPorts.ui.tasks;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.handler.TravelHandler;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;

public class JourneyTask implements BaseTask {

	private final List<Port> ports;

	public JourneyTask(Port origin, Port destination) {
		ports = Arrays.asList(origin, destination);
	}

	@Override
	public void run(InventoryClickEvent event) {
		TravelHandler.startJourney((Player) event.getWhoClicked(), ports.get(0), ports.get(1));
		event.getWhoClicked().closeInventory();
		ImplodusPorts.getInstance().getLogger().info("JourneyTask: Journey started from " + ports.get(0).getDisplayName() + " to " + ports.get(1).getDisplayName() + " by " + event.getWhoClicked().getName());
	}

}
