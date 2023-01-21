package me.NinjaMandalorian.ImplodusPorts.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.handler.TravelHandler;
import me.NinjaMandalorian.ImplodusPorts.helper.PortHelper;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import me.NinjaMandalorian.ImplodusPorts.ui.BaseMenu.Builder;
import me.NinjaMandalorian.ImplodusPorts.ui.BaseMenu.PagedBuilder;
import me.NinjaMandalorian.ImplodusPorts.ui.tasks.InventoryTask;
import me.NinjaMandalorian.ImplodusPorts.ui.tasks.JourneyTask;
import net.md_5.bungee.api.ChatColor;

public class PortMenu {

    /**
     * Creates the base menu for a port, showing all travel-able ports & a button to open global menu. 
     * @param player - Player opening menu
     * @param port - Port to generate menu from
     * @return BaseMenu of port
     */
    public static BaseMenu createPortMenu(Player player, Port port) {
        
        PagedBuilder builder = BaseMenu.createPagedBuilder()
                .makePageButtons()
                .setButton(4, portToButton(port, port))
                .setButton(49, BaseButton.create(Material.OAK_CHEST_BOAT).glow()
                        .name("&o&aSee all ports")
                        .task(new InventoryTask(createPortGlobalMenu(player, port))))
                .title("&9Port - " + port.getDisplayName())
                .openMsg("&aOpening port &9" + port.getDisplayName() + "&a...")
                ;
        
        ArrayList<BaseButton> buttonList = new ArrayList<BaseButton>();
        for (Port availablePort : PortHelper.orderPorts(port, port.getNearby())) {
            buttonList.add(portToButton(port, availablePort));
        }
        builder = builder.setContents(buttonList);
        
        return builder.build();
            
    }
    
    /**
     * Creates the global menu for a port, where every public port is in view 
     * @param player - Player opening menu
     * @param port - Port to generate menu from
     * @return BaseMenu of port
     */
    public static BaseMenu createPortGlobalMenu(Player player, Port port) {

        BaseMenu builderMenu = BaseMenu.createBuilder()
                .setButton(4, portToButton(port, port))
                .title("&cGlobal Port Menu")
                .openMsg("&aOpening &cGlobal Port Menu&a...")
                .fillOutline()
                .build();
        
        return builderMenu;
    }
    
    public static BaseButton portToButton(Port currentPort, Port port) {
        BaseButton portButton = BaseButton.create();
        
        if (currentPort.equals(port)) {
            // Creating button for current port.
            portButton = portButton.itemStack(new ItemStack(Port.getIcon(port.getSize())));
            portButton = portButton.glow().name(port.getDisplayName());
            
            List<String> lore = Arrays.asList(
                    ChatColor.GOLD + "Size: " + portSizeString(port)
                    );
            
            portButton = portButton.lore(lore).task(null);
            
        } else {
            // Creating button for travel-able port.
            portButton = portButton.itemStack(new ItemStack(Port.getIcon(port.getSize())));
            portButton = portButton.name(port.getDisplayName());
            
            List<String> lore = Arrays.asList(
                    ChatColor.GOLD + "Size: " + portSizeString(port),
                    ChatColor.GOLD + "Travel Time: "+TravelHandler.getTravelTime(currentPort, port),
                    ChatColor.GOLD + "Cost: " + ImplodusPorts.econ.format(TravelHandler.getJourneyCost(Arrays.asList(currentPort, port))),
                    ChatColor.GREEN + "Click to Travel"
                    );
            
            portButton = portButton.lore(lore);
            portButton = portButton.task(new JourneyTask(currentPort, port));
        }
        
        return portButton;
    }
    
    /**
     * Returns string correlating to port size. Includes color.
     * @param port - Port size integer
     * @return Port size string
     */
    public static String portSizeString(Port port) {
        switch(port.getSize()) {
        case 1:
            return ChatColor.AQUA + "Jetty";
        case 2:
            return ChatColor.DARK_AQUA + "Dock";
        case 3:
            return ChatColor.YELLOW + "Harbour";
        case 4:
            return ChatColor.DARK_GREEN + "Megaport";
        }
        return "";
    }
    
}
