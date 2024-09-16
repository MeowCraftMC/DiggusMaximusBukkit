package cx.rain.mc.diggus_maximus_bukkit.excavator;

import cx.rain.mc.diggus_maximus_bukkit.PluginConstants;
import cx.rain.mc.diggus_maximus_bukkit.config.ConfigManager;
import cx.rain.mc.diggus_maximus_bukkit.network.ExcavatePacket;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class Excavator {
    private final ConfigManager configManager;
    private final Player player;
    private final World world;
    private final Location startPos;
    private final BlockFacing facing;
    private final ExcavateShape shape;
    private final Material startId;
    private final ItemStack tool;
    private final ItemStack toolBefore;

    private final Deque<Location> points = new ArrayDeque<>();
    private int mined = 0;

    public Excavator(ConfigManager configManager, ExcavatePacket packet, Player player) {
        this.configManager = configManager;

        this.player = player;
        this.world = player.getWorld();

        this.startPos = packet.pos();
        this.startPos.setWorld(world);

        this.facing = packet.facing();
        this.shape = packet.shape();

        this.startId = Material.matchMaterial(packet.id().toString());

        this.tool = player.getInventory().getItem(EquipmentSlot.HAND);
        this.toolBefore = tool != null ? tool.clone() : null;
    }

    public void start() {
        if (!player.hasPermission(PluginConstants.PERMISSION)) {
            return;
        }

        var block = startPos.getBlock();
        if (block.isEmpty() || player.breakBlock(block)) {
            points.add(startPos);
            mined++;
        }

        while (!points.isEmpty()) {
            spread(points.remove());
        }
    }

    private void spread(Location pos) {
        for (var p : ExcavateSpreadHelper.getSpreadType(configManager, shape, facing, startPos, pos)) {
            if (isValidPos(p)) {
                excavate(pos.clone().add(p));
            }
        }
    }

    private void excavate(Location pos) {
        if (mined >= configManager.getMaxMineCount() || (configManager.willDamageTool() && configManager.shouldDontBreakTool() && isToolAlmostBreak())) {
            return;
        }

        var block = pos.getBlock();
        if (!block.getType().isAir()
                && isSame(block)
                && checkDistance(pos)
                && isMatchedTool(block)
                && isBreakable(block)
                && player.breakBlock(block)) {
            points.add(pos);
            mined++;

            if (!configManager.willDamageTool()) {
                player.getInventory().setItemInMainHand(toolBefore);
            }

            if (configManager.isAutoPickup()) {
                world.getNearbyEntities(BoundingBox.of(block), e -> e instanceof Item && e.isValid())
                        .stream()
                        .map(e -> (Item) e)
                        .forEach(e -> {
                            player.getInventory().addItem(e.getItemStack());
                            e.remove();
                        });
            }
        }
    }

    private boolean isToolAlmostBreak() {
        if (tool != null && tool.hasItemMeta() && tool.getItemMeta() instanceof Damageable damageable) {
            return damageable.getDamage() >= tool.getType().getMaxDurability() - 1;
        }

        return false;
    }

    private boolean isSame(Block block) {
        return configManager.isInSameGroup(startId, block.getType())
                || startId.equals(block.getType());
    }

    private boolean isValidPos(Vector offset) {
        return (Math.abs(offset.getBlockX()) + Math.abs(offset.getBlockY()) + Math.abs(offset.getBlockZ())) != 0;
    }

    private boolean checkDistance(Location pos) {
        return pos.distance(startPos) < configManager.getMaxMineDistance();
    }

    private boolean isMatchedTool(Block block) {
        if (configManager.requireToolMatches()) {
            return configManager.isCustomMatchedTool(tool.getType(), block.getType())
                    || block.isPreferredTool(tool);
        } else {
            return true;
        }
    }

    private boolean isBreakable(Block block) {
        if (configManager.isInBlockList(block.getType()) || configManager.isInAllowList(block.getType())) {
            return false;
        }

        return block.getType().getHardness() >= 0;
    }
}
