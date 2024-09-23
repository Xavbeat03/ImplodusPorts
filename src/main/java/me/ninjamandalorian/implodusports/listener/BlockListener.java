package me.ninjamandalorian.implodusports.listener;

import me.ninjamandalorian.implodusports.object.Port;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlock();

		if (block.getType().toString().contains("SIGN")) {
			Port port = Port.getPort(block.getLocation());
			if (port == null) return;

			if (player.hasPermission("implodusports.admin.destroy")) {
				Port.portDestroy(player, port);
				return;
			} else {
				e.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9[&6iPorts&9] &cYou do not have permission to destroy this."));
				return;
			}
		}
	}

}
