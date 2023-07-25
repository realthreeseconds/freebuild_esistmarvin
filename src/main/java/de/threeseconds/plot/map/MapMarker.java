package de.threeseconds.plot.map;

import de.threeseconds.plot.Plot;
import org.jetbrains.annotations.NotNull;

public interface MapMarker {

    void update(@NotNull Plot plot);

    void deleteMarker();
}
