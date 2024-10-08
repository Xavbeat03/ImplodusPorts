package me.ninjamandalorian.implodusports.listener;

import me.ninjamandalorian.implodusports.ImplodusPorts;
import me.ninjamandalorian.implodusports.handler.TravelHandler;
import me.ninjamandalorian.implodusports.object.Port;
import me.ninjamandalorian.implodusports.settings.Settings;
import me.ninjamandalorian.implodusports.ui.PortMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {

	ArrayList<Player> warnedPlayers = new ArrayList<Player>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		if (block == null) return;

		if (block.getType().toString().contains("SIGN")) {
			Sign sign = (Sign) block.getState();
			if (!ChatColor.stripColor(sign.getLine(0)).equals("[Port]")) return;
			Port port = Port.getPort(block.getLocation());
			if (port == null) return;

			Action action = e.getAction();
			if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
				e.setCancelled(true);
				PortMenu.createPortMenu(player, port).open(player);
				return;
			} else if (action.equals(Action.LEFT_CLICK_BLOCK)) {
				if (!player.hasPermission("implodusports.admin.destroy")) {
					// Preserve sign text
					e.setCancelled(true);
				}
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Port port = TravelHandler.getCurrentPort(player);
		if (port == null) return;
		Double distance = port.getSignLocation().distance(e.getTo());
		Double maxDistance = 0.0;
		switch (port.getSize()) {
			case 1:
				maxDistance = Settings.smallWalkRadius;
				break;
			case 2:
				maxDistance = Settings.mediumWalkRadius;
				break;
			case 3:
				maxDistance = Settings.largeWalkRadius;
				break;
			case 4:
				maxDistance = Settings.megaWalkRadius;
				break;
		}

		if (distance < maxDistance * .8) return;

		if (distance > maxDistance) {
			TravelHandler.cancelJourney(player);
		} else {
			if (!warnedPlayers.contains(player)) {
				player.sendMessage(ChatColor.RED + "You are nearing the edge of the port. Leaving will cancel your journey.");
				warnedPlayers.add(player);
				Bukkit.getScheduler().scheduleSyncDelayedTask(ImplodusPorts.getInstance(), () -> {
					warnedPlayers.remove(player);
				}, 100L);
			}
		}
	}

	/**
	 * Cancels port journey if player is damaged.
	 * @param e - entity damage event
	 */
	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		Port port = TravelHandler.getCurrentPort(player);
		if (port == null) return;
		TravelHandler.cancelJourney(player);
	}
}
