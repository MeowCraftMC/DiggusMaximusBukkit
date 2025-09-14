package cx.rain.mc.diggus_maximus_bukkit.network;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import cx.rain.mc.diggus_maximus_bukkit.DiggusMaximusBukkit;
import cx.rain.mc.diggus_maximus_bukkit.PluginConstants;
import cx.rain.mc.diggus_maximus_bukkit.excavator.Excavator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ExcavatePacketListener extends PacketAdapter {
    public ExcavatePacketListener(Plugin plugin, PacketType... types) {
        super(plugin, ListenerPriority.NORMAL, types);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        var payload = new CustomPayloadWrapper.Modifier(event.getPacket().getModifier()).read(0);
        if (payload == null) {
            return;
        }

        var player = event.getPlayer();
        if (!player.hasPermission(PluginConstants.PERMISSION)) {
            return;
        }

        if (PluginConstants.PACKET_ID_V1.equals(payload.getId())) {
            var packet = PacketReader.read(payload.getData());

            var config = DiggusMaximusBukkit.getInstance().getConfigManager();
            var stopBeforeToolBroken = config.shouldDontBreakTool();
            var stopAfterToolBroken = config.shouldStopAfterToolBreak();
            var excavator = new Excavator(packet.pos(), packet.id(), packet.facing(), packet.shape(),
                    stopBeforeToolBroken, stopAfterToolBroken, player);
            Bukkit.getScheduler().runTask(plugin, excavator::start);
            return;
        }

        if (PluginConstants.PACKET_ID_V2.equals(payload.getId())) {
            var packet = PacketReader.readV2(payload.getData());

            var excavator = new Excavator(packet.pos(), packet.id(), packet.hitFace(), packet.shape(),
                    packet.stopBeforeToolBroken(), packet.stopAfterToolBroken(), player);
            Bukkit.getScheduler().runTask(plugin, excavator::start);
        }
    }
}
