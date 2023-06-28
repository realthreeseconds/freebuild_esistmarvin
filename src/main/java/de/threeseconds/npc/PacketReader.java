package de.threeseconds.npc;

import de.threeseconds.FreeBuild;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;

public class PacketReader {

    private final Player player;

    public PacketReader(Player player) {
        this.player = player;
    }

    public boolean inject() {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;

        if (channel.pipeline().get("PacketInjector") != null) return false;


        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<ServerboundInteractPacket>() {

            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, ServerboundInteractPacket interactPacket, List<Object> out) throws Exception {
                out.add(interactPacket);

                if (interactPacket.getActionType() == ServerboundInteractPacket.ActionType.ATTACK || interactPacket.getActionType() == ServerboundInteractPacket.ActionType.INTERACT && getValue(getValue(interactPacket, "b"), "a").toString().equalsIgnoreCase("MAIN_HAND")) {
                    NPC npc = FreeBuild.getInstance().getNPCManager().getNPC(interactPacket.getEntityId());
                    if (npc == null) return;

                    Bukkit.getScheduler().runTask(FreeBuild.getInstance().getPaperCore(), () -> {
                        new NonPlayerCharacterInteractEvent(npc, player, interactPacket.getActionType() == ServerboundInteractPacket.ActionType.ATTACK ?
                                NonPlayerCharacterInteractEvent.ActionType.ATTACK :
                                NonPlayerCharacterInteractEvent.ActionType.INTERACT, interactPacket.isUsingSecondaryAction()).callEvent();
                    });

                }

            }

        });

        return true;
    }

    private Object getValue(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);

            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}




