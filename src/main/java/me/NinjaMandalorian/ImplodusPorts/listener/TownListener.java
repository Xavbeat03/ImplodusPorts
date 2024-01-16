package me.NinjaMandalorian.ImplodusPorts.listener;

import com.palmergames.bukkit.towny.event.town.TownRuinedEvent;
import com.palmergames.bukkit.towny.event.town.TownUnclaimEvent;
import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.Logger;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownListener implements Listener{

	/**
	 * Listener for when a town is ruined.
	 * Deletes the town's port from the database and the active ports list.
	 * @param e - TownRuinedEvent
	 */
	@EventHandler
	public void onTownRuinedEvent(TownRuinedEvent e) {
		if(ImplodusPorts.getInstance().isTownyEnabled()){
			Logger.log("[IPorts] Town ruined event, deleting relevant ports.");
			Port.portDestroy(Port.getPort(e.getTown()));
		}
	}

	@EventHandler
	public void onTownUnclaimEvent(TownUnclaimEvent e) {
		if(ImplodusPorts.getInstance().isTownyEnabled()){
			Logger.log("[IPorts] Town unclaim event, deleting relevant ports.");
			Port.portDestroy(Port.getPort(e.getTown()));
		}
	}




}
