package de.threeseconds.plot;

import de.threeseconds.plot.inventories.PlotSettings;

public enum PlotSetting {

    JOINABLE("Besuchen"),
    PVP("PvP aktiviert"),
    RECEIVE_REQUESTS("Akzeptiert Anfragen"),
    BROWSER_VISIBILITY("Browsersichtbarkeit");

    private String title;

    PlotSetting(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
