package me.ninjamandalorian.implodusports.listener;

import me.ninjamandalorian.implodusports.Logger;
import me.ninjamandalorian.implodusports.helper.PortHelper;
import me.ninjamandalorian.implodusports.object.Port;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import java.util.List;

public class SignListener implements Listener {

	/**
	 * Checker for placing ports.
	 *
	 * @param e - Event
	 */
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		//Logger.debug("SignChangeEvent");
		Player player = e.getPlayer();
		Block block = e.getBlock();

		if (!e.getLine(0).equalsIgnoreCase("[Port]") || e.getLine(1).strip().equalsIgnoreCase("")) return;

		if (player.hasPermission("implodusports.admin.create")) {
			Port port = PortHelper.portFromSign(player, block, e.getLines());
			if(port == null) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[&6iPorts&9] &cPort creation failed due to error."));
				Logger.log("Port creation failed due to error.");
				e.setCancelled(true);
				return;
			}
			Port.portCreate(player, port);
			List<String> formattedSign = PortHelper.formatSign(port);
			for (int i = 0; i < 4; i++) {
				e.setLine(i, formattedSign.get(i));
			}

			return;
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[&6iPorts&9] &cYou do not have permission to create a port."));
			e.setCancelled(true);
			return;
		}
	}

}
