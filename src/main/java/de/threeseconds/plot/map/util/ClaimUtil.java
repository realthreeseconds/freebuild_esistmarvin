package de.threeseconds.plot.map.util;

import de.threeseconds.plot.Plot;
import de.threeseconds.util.ColorUtil;
import de.threeseconds.util.FreeBuildPlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClaimUtil {

    private static final Map<UUID, Color> colorMap = new HashMap<>();
    public static @NotNull Color getClaimColor(@NotNull Plot plot) {
        FreeBuildPlayer freeBuildPlayer = plot.getPlotOwner();
        UUID uuid = UUID.fromString(freeBuildPlayer.getUuid());
        Color color = colorMap.get(uuid);
        if (color == null) {
            color = ColorUtil.uuidToColor(uuid);
            colorMap.put(uuid, color);
        }
        return color;
    }
}
