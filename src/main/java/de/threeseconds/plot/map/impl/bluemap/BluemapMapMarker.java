package de.threeseconds.plot.map.impl.bluemap;


import com.flowpowered.math.vector.Vector2d;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.markers.ExtrudeMarker;
import de.bluecolored.bluemap.api.markers.Marker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Shape;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.map.MapMarker;
import de.threeseconds.plot.map.util.ChunkBitmap;
import de.threeseconds.plot.map.util.ClaimUtil;
import de.threeseconds.plot.map.util.Point;
import de.threeseconds.util.BoundingBox;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class BluemapMapMarker implements MapMarker {

    private final MarkerSet set;
    private final ExtrudeMarker marker;

    public BluemapMapMarker(MarkerSet set, ExtrudeMarker marker) {
        this.set = set;
        this.marker = marker;
    }

    public ExtrudeMarker get() {
        return marker;
    }

    private Shape buildShapeFromPoints(List<Point> points) {
        Shape.Builder shapeBuilder = Shape.builder();
        for (Point p : points) {
            shapeBuilder.addPoint(Vector2d.from(p.x(), p.y()));
        }
        return shapeBuilder.build();
    }

    @Override
    public void update(@NotNull Plot plot) {
        ChunkBitmap bmp = new ChunkBitmap(plot.getPlotChunks());
        List<List<Point>> edges = bmp.traceBlocks(true);

        if (edges.size() < 1) return;

        this.marker.setShape(
                this.buildShapeFromPoints(edges.get(0)),
                this.marker.getShapeMinY(),
                this.marker.getShapeMaxY()
        );

        Collection<Shape> holes = this.marker.getHoles();
        holes.clear();
        for (int i=1; i < edges.size(); i++) {
            holes.add(this.buildShapeFromPoints(edges.get(i)));
        }
    }

    @Override
    public void deleteMarker() {
        (new HashSet<>(set.getMarkers().entrySet())).stream()
                .filter((Map.Entry<String, Marker> entry) -> Objects.equals(entry.getValue(), this.marker))
                .map(Map.Entry::getKey)
                .forEach(set::remove);
    }

    // Package Private
    private static final String markerSetId = "freebuild_marker_set";
    private static final Map<UUID, MarkerSet> markerSetMap = new HashMap<>();
    private static @Nullable MarkerSet getMarkerSet(BlueMapAPI api, Plot plot) {
        World world = Bukkit.getWorld("freebuildWorld");
        if (world == null) return null;
        UUID uuid = world.getUID();
        if (markerSetMap.containsKey(uuid)) return markerSetMap.get(uuid);

        Optional<BlueMapWorld> opt = api.getWorld(world);
        if (opt.isEmpty()) return null;
        BlueMapWorld bmw = opt.get();

        MarkerSet ms = MarkerSet.builder()
                .label("Grundstücke")
                .build();
        markerSetMap.put(uuid, ms);

        for (BlueMapMap map : bmw.getMaps()) {
            map.getMarkerSets().put(markerSetId, ms);
        }
        return ms;
    }

    static @Nullable BluemapMapMarker getMarker(Object apiInstance, Plot plot) {
        BlueMapAPI api = (BlueMapAPI) apiInstance;
        MarkerSet ms = getMarkerSet(api, plot);
        if (ms == null) return null;

        String token = String.valueOf(plot.getPlotId());
        Marker existing = ms.get(token);
        if (existing != null) {
            if (existing instanceof ExtrudeMarker) {
                return new BluemapMapMarker(ms, (ExtrudeMarker) existing);
            } else {
                ms.remove(token);
            }
        }

        BoundingBox bounds = plot.getOuterBounds();
        org.bukkit.util.Vector mins = bounds.getMins();
        org.bukkit.util.Vector maxs = bounds.getMaxs();

        World world = Bukkit.getWorld("freebuildWorld");
        if (world == null) {
            world = Bukkit.getWorlds().stream().findFirst().orElseThrow(null);
        }

        java.awt.Color col = ClaimUtil.getClaimColor(plot);

        ExtrudeMarker marker = ExtrudeMarker.builder()
                .shape(
                        Shape.createRect(
                                mins.getX(), mins.getZ(),
                                maxs.getX(), maxs.getZ()
                        ),
                        world.getMinHeight(),
                        world.getMaxHeight()
                )
                .label(plot.getPlotName())
                .fillColor(new Color(
                        col.getRed(),
                        col.getGreen(),
                        col.getBlue(),
                        0.2f
                ))
                .lineColor(new Color(
                        col.getRed(),
                        col.getGreen(),
                        col.getBlue(),
                        0.4f
                ))
                .build();

        ms.put(token, marker);

        return new BluemapMapMarker(ms, marker);
    }

    static void cleanup(Object apiInstance) {
        BlueMapAPI api = (BlueMapAPI) apiInstance;
        for (BlueMapMap map : api.getMaps()) {
            map.getMarkerSets().remove(markerSetId);
        }
    }
}
