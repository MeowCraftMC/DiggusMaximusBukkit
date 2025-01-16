package cx.rain.mc.diggus_maximus_bukkit.network;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
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

        if (PluginConstants.PACKET_ID.equals(payload.getId())) {
            var packet = PacketReader.read(payload.getData());
            var player = event.getPlayer();
            var excavate = new Excavator(packet, player);
            Bukkit.getScheduler().runTask(plugin, excavate::start);
        }
    }
}
