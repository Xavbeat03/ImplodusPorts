package me.NinjaMandalorian.ImplodusPorts.listener;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.handler.TravelHandler;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import me.NinjaMandalorian.ImplodusPorts.ui.PortMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {

	ArrayList<Player> warnedPlayers = new ArrayList<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		if (block == null) return;

		if (block.getType().toString().contains("SIGN")) {
			Sign sign = (Sign) block.getState();
			SignSide front = sign.getSide(Side.FRONT);
			SignSide back = sign.getSide(Side.BACK);
			if (!ChatColor.stripColor(front.getLine(0)).equals("[Port]") || !ChatColor.stripColor(back.getLine(0)).equals("[Port]")) return;
			Port port = Port.getPort(block.getLocation());
			if (port == null) return;

			Action action = e.getAction();
			if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
				e.setCancelled(true);
				PortMenu.createPortMenu(player, port).open(player);
			} else if (action.equals(Action.LEFT_CLICK_BLOCK) 
				&& !player.hasPermission("implodusports.admin.destroy")) {
					// Preserve sign text
					e.setCancelled(true);
				}

		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		Port port = TravelHandler.getCurrentPort(player);
		if (port == null || e.getTo() == null) return;
		double distance = port.getSignLocation().distance(e.getTo());
		double maxDistance = switch (port.getSize()) {
			case 1 -> Settings.smallWalkRadius;
			case 2 -> Settings.mediumWalkRadius;
			case 3 -> Settings.largeWalkRadius;
			case 4 -> Settings.megaWalkRadius;
			default -> 0.0;
		};

		if (distance < maxDistance * Settings.walkWarningRadiusPercentage) return;

		if (distance > maxDistance) {
			TravelHandler.cancelJourney(player);
		} else {
			if (!warnedPlayers.contains(player)) {
				player.sendMessage(ChatColor.RED + "You are nearing the edge of the port. Leaving will cancel your journey.");
				warnedPlayers.add(player);
				Bukkit.getScheduler().scheduleSyncDelayedTask(ImplodusPorts.getInstance(), 
					() -> warnedPlayers.remove(player), 100L);
			}
		}
	}

	/**
	 * Cancels port journey if player is damaged.
	 * @param e - entity damage event
	 */
	@EventHandler
	public void onPlayerDamaged(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player player)) return;
		Port port = TravelHandler.getCurrentPort(player);
		if (port == null) return;
		TravelHandler.cancelJourney(player);
	}
}
