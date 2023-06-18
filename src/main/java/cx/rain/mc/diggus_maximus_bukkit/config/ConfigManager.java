package cx.rain.mc.diggus_maximus_bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
    }

    public int getMaxMineCount() {
        return config.getInt("miner.maxMineBlocks", 40);
    }

    public int getMaxMineDistance() {
        return config.getInt("miner.maxMineDistance", 11);
    }
}
