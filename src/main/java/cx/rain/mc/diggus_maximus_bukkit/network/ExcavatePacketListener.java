package cx.rain.mc.diggus_maximus_bukkit.network;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import cx.rain.mc.diggus_maximus_bukkit.PluginConstants;
import cx.rain.mc.diggus_maximus_bukkit.config.ConfigManager;
import cx.rain.mc.diggus_maximus_bukkit.excavator.Excavator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ExcavatePacketListener extends PacketAdapter {

    private final ConfigManager configManager;

    public ExcavatePacketListener(Plugin plugin, ConfigManager configManager, PacketType... types) {
        super(plugin, ListenerPriority.NORMAL, types);
        this.configManager = configManager;
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        var payload = new CustomPayloadWrapper.Modifier(event.getPacket().getModifier()).read(0);
        if (PluginConstants.PACKET_ID.equals(payload.getId())) {
            var packet = PacketReader.read(payload.getData());
            var player = event.getPlayer();
            var excavate = new Excavator(configManager, packet, player);
            Bukkit.getScheduler().runTask(plugin, excavate::start);
        }
    }
}
