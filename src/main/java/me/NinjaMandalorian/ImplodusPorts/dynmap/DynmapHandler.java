package me.NinjaMandalorian.ImplodusPorts.dynmap;

import me.NinjaMandalorian.ImplodusPorts.ImplodusPorts;
import me.NinjaMandalorian.ImplodusPorts.object.Port;
import me.NinjaMandalorian.ImplodusPorts.settings.Settings;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import static me.NinjaMandalorian.ImplodusPorts.ui.PortMenu.portSizeString;

public class DynmapHandler {

    public static final String DYNMAP_ICON_NAME = Settings.DynmapIconName;
    private final MarkerAPI markerAPI;
    private final MarkerIcon markerIcon;

    private MarkerSet markerSet;

    public DynmapHandler() {
		ImplodusPorts plugin = ImplodusPorts.getInstance();
		markerAPI = plugin.getMarkerAPI();
        markerIcon = markerAPI.getMarkerIcon(DYNMAP_ICON_NAME);
    }

    public void resetPortMarkers() {
        if (markerSet != null) {
            markerSet.deleteMarkerSet();
            markerSet = null;
        }
        initMarkerSet();
        clearAllPortMarkers();
        placeAllPortMarkers();
    }

    private void initMarkerSet() {
        markerSet = markerAPI.getMarkerSet("ports.markerset");
        if (markerSet == null) {
            markerSet = markerAPI.createMarkerSet("ports.markerset", "Ports", null, false);
        } else {
            markerSet.setMarkerSetLabel("Ports");
        }
        markerSet.setMinZoom(0);
        markerSet.setLayerPriority(3);
        markerSet.setHideByDefault(false);
        markerSet.addAllowedMarkerIcon(markerIcon);
    }

    private void clearAllPortMarkers() {
        for (Marker marker : markerSet.getMarkers()) {
            marker.deleteMarker();
        }
    }

    private void placeAllPortMarkers() {
        for (Port port : Port.getPorts().values()) {
            String portSize = portSizeString(port);
            String townName = port.getTown().getName();
            String markerID = townName + "__port";
            Marker marker = markerSet.createMarker
                    (
                        markerID,
                        "Port of " + townName,
                        port.getTown().getWorld().getName(),
                        port.getSignLocation().getX(),
                        port.getSignLocation().getY(),
                        port.getSignLocation().getZ(),
                        markerIcon,
                        false
                    );
            String infobox = "<div class=\\\"regioninfo\\\"><div class=\"\\infowindow\\\"><span style=\"0font-size: 120%;\">Port of @townname</span><br />Type: <span style=\"0font-weight: bold;\">@portsize</span></div></div>";
            infobox = infobox.replace("@townname", townName);
            infobox = infobox.replace("@portsize", portSize.substring(2)); // Remove color code (Ex: §bJetty -> Jetty)
            marker.setDescription(infobox);
        }
    }
}
