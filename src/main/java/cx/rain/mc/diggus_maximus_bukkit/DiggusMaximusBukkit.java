package cx.rain.mc.diggus_maximus_bukkit;

import cx.rain.mc.diggus_maximus_bukkit.channel.ChannelDiggusMaximus;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiggusMaximusBukkit extends JavaPlugin {
    private static DiggusMaximusBukkit INSTANCE;

    public DiggusMaximusBukkit() {
        INSTANCE = this;
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

    public static DiggusMaximusBukkit getInstance() {
        return INSTANCE;
    }
}
