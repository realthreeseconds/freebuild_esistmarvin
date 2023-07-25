package de.threeseconds.sign;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketInjector {

    public static void addPacketInjector(Player p) {
        ServerPlayer sp = ((CraftPlayer) p).getHandle();

        try {
            Field connection = ServerGamePacketListenerImpl.class.getDeclaredField("h");
            connection.setAccessible(true);
            Channel ch = ((Connection) connection.get(sp.connection)).channel;

            if (ch.pipeline().get("PacketInjector") != null) return;
            ch.pipeline().addAfter("decoder", "PacketInjector", new PacketHandler(p));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    public static void removePacketInjector(Player p) {
        ServerPlayer sp = ((CraftPlayer) p).getHandle();

        try {
            Field connection = ServerGamePacketListenerImpl.class.getDeclaredField("h");
            connection.setAccessible(true);
            Channel ch = ((Connection) connection.get(sp.connection)).channel;

            if (ch.pipeline().get("PacketInjector") == null) return;
            ch.pipeline().remove("PacketInjector");
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

}

