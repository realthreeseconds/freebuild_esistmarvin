package de.threeseconds.plot;

import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PlotGroup {

    OWNER(
            0,
            "<dark_red>Owner",
            null,
            PlotGroupPermission.values()
    ),
    ADMIN(
            1,
            "<red>Administrator",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTFkMzgzNDAxZjc3YmVmZmNiOTk4YzJjZjc5YjdhZmVlMjNmMThjNDFkOGE1NmFmZmVkNzliYjU2ZTIyNjdhMyJ9fX0=",
            PlotGroupPermission.values()
    ),
    MODERATOR(
            2,
            "<aqua>Moderator",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdhZDQ4ZjE4Y2FkOTllMDZkZmE0ZTU5ZjdlNDg2ODY0Zjk3NGRhYTk4NWY2ZDZlNjU3MDMyYjIzZWFmY2E3In19fQ==",
            PlotGroupPermission.KICK,
            PlotGroupPermission.BAN,
            PlotGroupPermission.HANDLE_REQUESTS,
            PlotGroupPermission.SEND_REQUESTS,
            PlotGroupPermission.JOINABLE
    ),
    MEMBER(
            3,
            "<yellow>Mitglied",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM5MTdmMmY2NGFhY2Q4OTUzOTVkYzRlMjg3N2JlN2NjYjNkYzFlOWQyYzRjMWM3ZmFjNzYzNGFjOGFkIn19fQ==",
            null
    ),
    GUEST(
            4,
            "<gray>Gast",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU5M2RhNzRlOTY4ODQxM2MyMzdmM2NlMzI0ZDcwODVhY2E4OGRmYTRiNzI1N2MyZGEwYmRmYzM0NTYzMDc3In19fQ==",
            null
    );

    private Integer groupId;
    private String groupName;
    private String skullTexture;
    private List<PlotGroupPermission> groupPermissions;

    PlotGroup(Integer groupId, String groupName, String skullTexture, PlotGroupPermission... groupPermissions) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.skullTexture = skullTexture;
        this.groupPermissions = (groupPermissions != null ? new ArrayList<>(Arrays.asList(groupPermissions)) : new ArrayList<>());
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public Integer getGroupPermissionSize() {
        return (!groupPermissions.isEmpty() ? groupPermissions.size() : 0);
    }
    public List<PlotGroupPermission> getGroupPermissions() {
        return groupPermissions;
    }
}
