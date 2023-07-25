package de.threeseconds.plot;

public enum PlotFilter {

    ALL(true, "Jeder"),
    MEMBER(false, "Mitglied"),
    OWNER(false, "Eigentümer"),
    JOINABLE(false, "Joinable"),
    PVP(false, "PvP");

    private Boolean defaultFilter;
    private String name;

    PlotFilter(Boolean defaultFilter, String name) {
        this.defaultFilter = defaultFilter;
        this.name = name;
    }

    public Boolean getDefaultFilter() {
        return defaultFilter;
    }

    public String getName() {
        return name;
    }
}
