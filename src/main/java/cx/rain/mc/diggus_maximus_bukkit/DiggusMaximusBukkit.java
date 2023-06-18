package cx.rain.mc.diggus_maximus_bukkit;

import cx.rain.mc.diggus_maximus_bukkit.channel.ChannelDiggusMaximus;
import cx.rain.mc.diggus_maximus_bukkit.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiggusMaximusBukkit extends JavaPlugin {
    private static DiggusMaximusBukkit INSTANCE;

    private final ConfigManager configManager;

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
    public void onEnable() {
        // Plugin startup logic
        getServer().getMessenger().registerIncomingPluginChannel(this, ChannelDiggusMaximus.CHANNEL_NAME, new ChannelDiggusMaximus());
        getServer().getMessenger().registerOutgoingPluginChannel(this, ChannelDiggusMaximus.CHANNEL_NAME);

        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
