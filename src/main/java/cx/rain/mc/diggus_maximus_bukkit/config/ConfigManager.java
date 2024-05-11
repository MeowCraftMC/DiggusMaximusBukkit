package cx.rain.mc.diggus_maximus_bukkit.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
    }

    public int getMaxMineCount() {
        return config.getInt("maxMineCount", 40);
    }

    public int getMaxMineDistance() {
        return config.getInt("maxDistance", 11);
    }

    public boolean isEnableShape() {
        return config.getBoolean("enableShape", false);
    }

    public boolean isAutoPickup() {
        return config.getBoolean("autoPickup", true);
    }

    public boolean shouldPlayerExhaustion() {
        return config.getBoolean("playerExhaustion", true);
    }

    public double getExhaustionMultiplier() {
        return config.getDouble("exhaustionMultiplier", 1.0);
    }

    public boolean willDamageTool() {
        return config.getBoolean("damageTool", true);
    }

    public boolean shouldDontBreakTool() {
        return config.getBoolean("dontBreakTool", true);
    }

    public boolean hasCustomMatchedTool() {
        return config.getBoolean("requiresCustomMatchedTool", true);
    }

    public boolean shouldUseBlockAllowList() {
        return config.getBoolean("useBlockAllowList", true);
    }

    public boolean isCustomMatchedTool(Material tool, Material block) {
        var section = config.getConfigurationSection("customMatchedTools");

        if (section != null && section.contains(tool.name())) {
            return section.getStringList(tool.name()).contains(block.name());
        }

        return false;
    }

    public boolean isInBlockList(Material block) {
        return config.getStringList("blocklist").contains(block.name());
    }

    public boolean isInAllowList(Material block) {
        return config.getStringList("allowlist").contains(block.name());
    }
}
