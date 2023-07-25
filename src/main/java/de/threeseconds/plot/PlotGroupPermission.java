package de.threeseconds.plot;

import org.bukkit.Material;

public enum PlotGroupPermission {

    KICK(Material.PISTON, "GS Kick"),
    BAN(Material.LEATHER_BOOTS, "GS Ban"),
    PLAYER_GROUP(Material.NAME_TAG, "Spielergruppe ändern"),
    NAME(Material.OAK_SIGN, "GS Namen ändern"),
    GROUP_PERMS(Material.NAME_TAG, "Permissions ändern"),
    BROWSER_VISIBILITY(Material.BOOKSHELF, "GS Browser Sichtbarkeit"),
    JOINABLE(Material.LEATHER_BOOTS, "GS Betreten erlauben"),
    ICON(Material.GRASS_BLOCK, "GS Icon anpassen"),
    TOGGLE_REQUESTS(Material.WRITABLE_BOOK, "Anfragen erlauben"),
    HANDLE_REQUESTS(Material.BOOK, "Anfragen bearbeiten"),
    TOGGLE_PVP(Material.IRON_SWORD, "PvP aktivieren"),
    SEND_REQUESTS(Material.WRITABLE_BOOK, "Anfragen versenden");

    private Material permissionMaterial;
    private String permissionName;

    PlotGroupPermission(Material permissionMaterial, String permissionName) {
        this.permissionMaterial = permissionMaterial;
        this.permissionName = permissionName;
    }

    public Material getPermissionMaterial() {
        return permissionMaterial;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
