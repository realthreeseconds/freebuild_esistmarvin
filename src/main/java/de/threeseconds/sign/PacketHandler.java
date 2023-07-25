package de.threeseconds.sign;

import de.threeseconds.FreeBuild;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.Packet;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public final class PacketHandler extends ChannelDuplexHandler {

    public static final Map<UUID, Predicate<Packet<?>>> PACKET_HANDLERS = new HashMap<>();

    private final Player p; // Store your target player

    public PacketHandler(Player p) {
        this.p = p;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packetO) throws Exception {
        if (!(packetO instanceof Packet<?> packet)) { // Utilize Java 17 features for pattern matching; Only intercept Packet Data
            super.channelRead(ctx, packetO);
            return;
        }

        Predicate<Packet<?>> handler = PACKET_HANDLERS.get(p.getUniqueId());
        if (handler != null) new BukkitRunnable() {
            public void run() {
                boolean success = handler.test(packet); // Check to make sure that the predicate works
                if (success) PACKET_HANDLERS.remove(p.getUniqueId()); // If successful, remove the packet handler
            }
        }.runTask(FreeBuild.getInstance().getPaperCore()); // Execute your Predicate Handler

        super.channelRead(ctx, packetO); // Perform default actions done by the duplex handler
    }

}
