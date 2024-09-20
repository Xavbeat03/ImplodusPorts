package me.NinjaMandalorian.ImplodusPorts.handler;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MessageHandler {

	/**
	 * Sends message to the player to for going to next port.
	 *
	 * @param player - Player to send message to
	 */
	public static void sendJourneyNext(Player player) {
		BukkitAudiences adventure = ImplodusPorts.getInstance().getAdventure();
		Audience audience = adventure.player(player);

		Component component = Component.text(ChatColor.translateAlternateColorCodes('&', "&f[&aNext Port&f]"))
			.hoverEvent(HoverEvent.showText(Component.text("Click to begin travelling...")))
			.clickEvent(ClickEvent.runCommand("/implodusports:iports next"));

		audience.sendMessage(component);
	}

	public static void sendJourneyEnd(Player player, Port finalPort) {
		BukkitAudiences adventure = ImplodusPorts.getInstance().getAdventure();
		Audience audience = adventure.player(player);

		Location portLoc = finalPort.getTeleportLocation();

		Component component = Component.text(ChatColor.translateAlternateColorCodes('&', "&aYou have arrived at "));

		component = component.append(Component.text(ChatColor.translateAlternateColorCodes('&', "&d" + finalPort.getDisplayName())))
			.hoverEvent(HoverEvent.showText(Component.text("Location: " + portLoc.getBlockX() + ", " + portLoc.getBlockZ())));


		component = component.append(Component.text(ChatColor.GREEN + "."));

		audience.sendMessage(component);
	}

}
