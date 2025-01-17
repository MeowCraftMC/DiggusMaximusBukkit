package cx.rain.mc.diggus_maximus_bukkit.excavator;

import cx.rain.mc.diggus_maximus_bukkit.DiggusMaximusBukkit;
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

public class Excavator {
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

    public Excavator(ExcavatePacket packet, Player player) {
        this.player = player;
        this.world = player.getWorld();

        this.startPos = packet.pos();
        this.startPos.setWorld(world);

        this.facing = packet.facing();
        this.shape = packet.shape();

        this.startId = Material.matchMaterial(packet.id().toString());

        this.tool = player.getInventory().getItem(EquipmentSlot.HAND);
        this.toolBefore = tool.clone();
    }

    public void start() {
        var block = startPos.getBlock();
        if (block.isEmpty() || player.breakBlock(block)) {
            points.add(startPos);
            mined++;
        }

        while (!points.isEmpty()) {
            spread(points.remove());
        }

        if (!getConfig().willDamageTool()) {
            player.getInventory().setItemInMainHand(toolBefore);
        }
    }

    private void spread(Location pos) {
        for (var p : ExcavateSpreadHelper.getOffsets(shape, facing, startPos, pos)) {
            if (isValidPos(p)) {
                tryToExcavate(pos.clone().add(p));
            }
        }
    }

    private void tryToExcavate(Location pos) {
        if (mined >= getConfig().getMaxMineCount()
                || (getConfig().willDamageTool() && getConfig().shouldDontBreakTool() && isToolAlmostBreak())) {
            return;
        }

        var block = pos.getBlock();
        if (!block.getType().isAir()
                && isSame(block)
                && checkDistance(pos)
                && isMatchedTool(block)
                && isBreakable(block)
                && player.breakBlock(block)) {
            excavate(pos);
        }
    }

    private void excavate(Location pos) {
        points.add(pos);
        mined++;

        if (getConfig().isAutoPickup()) {
            world.getNearbyEntities(BoundingBox.of(pos.getBlock()), e -> e instanceof Item && e.isValid())
                    .stream()
                    .map(e -> (Item) e)
                    .forEach(e -> {
                        var result = player.getInventory().addItem(e.getItemStack());
                        for (var i : result.values()) {
                            world.dropItem(pos, i);
                        }
                        e.remove();
                    });
        }
    }

    private boolean isToolAlmostBreak() {
        if (tool != null && tool.hasItemMeta() && tool.getItemMeta() instanceof Damageable damageable) {
            return damageable.getDamage() >= tool.getType().getMaxDurability() - 1;
        }

        return false;
    }

    private boolean isSame(Block block) {
        if (shape != ExcavateShape.NONE) {
            return getConfig().shouldShapeIgnoresBlockMismatch();
        }

        return getConfig().isInSameGroup(startId, block.getType())
                || startId.equals(block.getType());
    }

    private boolean isValidPos(Vector offset) {
        return (Math.abs(offset.getBlockX()) + Math.abs(offset.getBlockY()) + Math.abs(offset.getBlockZ())) != 0;
    }

    private boolean checkDistance(Location pos) {
        return pos.distance(startPos) < getConfig().getMaxMineDistance();
    }

    private boolean isMatchedTool(Block block) {
        if (getConfig().requireToolMatches()) {
            return getConfig().isCustomMatchedTool(tool.getType(), block.getType())
                    || block.isPreferredTool(tool);
        } else {
            return true;
        }
    }

    private boolean isBreakable(Block block) {
        if (getConfig().isInBlockList(block.getType()) || getConfig().isInAllowList(block.getType())) {
            return false;
        }

        return block.getType().getHardness() >= 0;
    }

    private ConfigManager getConfig() {
        return DiggusMaximusBukkit.getInstance().getConfigManager();
    }
}
