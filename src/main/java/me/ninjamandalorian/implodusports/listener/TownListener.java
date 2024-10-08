package me.ninjamandalorian.implodusports.listener;

import com.palmergames.bukkit.towny.event.town.TownRuinedEvent;
import com.palmergames.bukkit.towny.event.town.TownUnclaimEvent;
import com.palmergames.bukkit.towny.object.WorldCoord;
import me.ninjamandalorian.implodusports.Logger;
import me.ninjamandalorian.implodusports.object.Port;
import org.bukkit.Location;
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
		Logger.log("[IPorts] Town ruined event, deleting relevant ports.");
		Logger.log("[IPorts] Deleting " + e.getTown() + " Port");
		Port.portDestroy(Port.getPort(e.getTown()));
	}

	/**
	 * Unclaims a port in a newly unclaimed townblock
	 * @param e relevant unclaim event
	 */
	@EventHandler
	public void onTownUnclaimEvent(TownUnclaimEvent e) {
		Logger.log("[IPorts] Town unclaim event, deleting relevant ports.");

		if(Port.getPort(e.getTown()) == null) return;
		Location signLoc = Port.getPort(e.getTown()).getSignLocation();
		WorldCoord signWorldCoord = new WorldCoord(signLoc.getWorld().getName(), signLoc.getBlockX(), signLoc.getBlockZ());
		WorldCoord eventWorldCoord = e.getWorldCoord();
		//Logger.debug((signWorldCoord.getX()) + " " + (signWorldCoord.getZ()));
		//Logger.debug((eventWorldCoord.getX()) + " " + (eventWorldCoord.getZ()));
		if(Math.floor((double) signWorldCoord.getX() /16) == eventWorldCoord.getX() && Math.floor((double)signWorldCoord.getZ()/16) == eventWorldCoord.getZ())
		{
			Logger.log("[IPorts] Found Bad port.");
			Logger.log("[IPorts] Deleting " + e.getTown() + " Port");
			Port.portDestroy(Port.getPort(e.getTown()));
		}
	}




}
