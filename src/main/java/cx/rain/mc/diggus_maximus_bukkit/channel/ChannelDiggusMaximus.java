package cx.rain.mc.diggus_maximus_bukkit.channel;

import cx.rain.mc.diggus_maximus_bukkit.DiggusMaximusBukkit;
import cx.rain.mc.diggus_maximus_bukkit.ExcavatorConstants;
import cx.rain.mc.diggus_maximus_bukkit.excavator.Excavator;
import cx.rain.mc.diggus_maximus_bukkit.nms.ExcavatorFactory;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ChannelDiggusMaximus implements PluginMessageListener {
    public final static String CHANNEL_NAME = "diggusmaximus:start_excavate_packet";

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(CHANNEL_NAME)) {
            return;
        }

        if (!player.hasPermission(ExcavatorConstants.PERMISSION_ENABLED)) {
            player.sendMessage("You have no permission to use diggusmaximus in this server!");  // Todo: i18n support.
            return;
        }

        var excavator = ExcavatorFactory.getExcavator();
        var packet = excavator.read(message);

        var loc = packet.blockPos();
        loc.setWorld(player.getWorld());

        // Todo: config.
        if (player.getLocation().distance(loc) < 10) {
            var excavate = new Excavator(player, loc);
            excavate.start();
        }
    }

//    public static void sendPacket(Player player, Location loc) {
//        player.sendPluginMessage(DiggusMaximusBukkit.getInstance(), CHANNEL_NAME, ExcavatorFactory.getExcavator().makeSendBuffer(loc));
//    }
}
