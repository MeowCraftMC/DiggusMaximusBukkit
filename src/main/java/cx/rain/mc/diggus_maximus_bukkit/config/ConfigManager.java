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
        return config.getBoolean("enableShape", true);
    }

    public boolean shouldShapeIgnoresBlockMismatch() {
        return config.getBoolean("shapeIgnoresBlockMismatch", true);
    }

    public boolean isDiagonallyMine() {
        return config.getBoolean("diagonallyMine", true);
    }

    public boolean isAutoPickup() {
        return config.getBoolean("autoPickup", true);
    }

    public boolean willDamageTool() {
        return config.getBoolean("damageTool", true);
    }

    public boolean shouldDontBreakTool() {
        return config.getBoolean("dontBreakTool", true);
    }

    public boolean requireToolMatches() {
        return config.getBoolean("requireToolMatches", false);
    }

    public boolean isCustomMatchedTool(Material tool, Material block) {
        var section = config.getConfigurationSection("customToolsMatch");

        if (section != null && section.contains(tool.name())) {
            return section.getStringList(tool.name()).contains(block.name());
        }

        return false;
    }

    public boolean isInSameGroup(Material block, Material other) {
        var section = config.getConfigurationSection("customGroup");
        if (section != null) {
            for (var n : section.getKeys(false)) {
                var group = section.getStringList(n);
                if (group.contains(block.name()) && group.contains(other.name())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInBlockList(Material block) {
        return config.getStringList("blocklist").contains(block.name());
    }

    public boolean shouldUseBlockAllowList() {
        return config.getBoolean("enableAllowList", false);
    }

    public boolean isInAllowList(Material block) {
        return shouldUseBlockAllowList() && config.getStringList("allowlist").contains(block.name());
    }
}
