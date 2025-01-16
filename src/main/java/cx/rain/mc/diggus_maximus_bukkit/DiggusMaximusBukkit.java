package cx.rain.mc.diggus_maximus_bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketListener;
import cx.rain.mc.diggus_maximus_bukkit.config.ConfigManager;
import cx.rain.mc.diggus_maximus_bukkit.network.ExcavatePacketListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiggusMaximusBukkit extends JavaPlugin {
    private static DiggusMaximusBukkit INSTANCE;

    private final ConfigManager configManager;

    private ProtocolManager protocolManager;
    private PacketListener listener;

    public DiggusMaximusBukkit() {
        INSTANCE = this;
        configManager = new ConfigManager(this);
    }

    public static DiggusMaximusBukkit getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        listener = new ExcavatePacketListener(this, PacketType.Play.Client.CUSTOM_PAYLOAD);
    }

    @Override
    public void onEnable() {
        protocolManager.addPacketListener(listener);
    }

    @Override
    public void onDisable() {
        protocolManager.removePacketListener(listener);
    }
}
