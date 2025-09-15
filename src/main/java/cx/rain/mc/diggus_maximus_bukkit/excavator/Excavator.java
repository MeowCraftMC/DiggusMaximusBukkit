package cx.rain.mc.diggus_maximus_bukkit.excavator;

import cx.rain.mc.diggus_maximus_bukkit.DiggusMaximusBukkit;
import cx.rain.mc.diggus_maximus_bukkit.config.ConfigManager;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
    private final Material startId;
    private final BlockFacing hitFace;
    private final BlockFacing playerHorizontalFace;
    private final Shape shape;
    private final boolean stopBeforeToolBroken;
    private final boolean stopAfterToolBroken;
    private final ItemStack toolBefore;
    private ItemStack tool;
//    private boolean wasBrokenOnNextBreak = false;

    private final Deque<Location> points = new ArrayDeque<>();
    private int mined = 0;

    public Excavator(Location startPos, NamespacedKey blockId, BlockFacing hitFace, Shape shape,
                     boolean stopBeforeToolBroken, boolean stopAfterToolBroken, Player player) {
        this.player = player;
        this.world = player.getWorld();

        this.startPos = startPos;
        this.startPos.setWorld(world);

        this.hitFace = hitFace;
        this.playerHorizontalFace = BlockFacing.from(player.getFacing());
        this.shape = shape;

        this.stopBeforeToolBroken = stopBeforeToolBroken;
        this.stopAfterToolBroken = stopAfterToolBroken; // Todo: stopAfterToolBroken

        this.startId = Material.matchMaterial(blockId.toString());

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
        for (var p : ExcavateSpreadHelper.getSpreadShape(shape, hitFace, playerHorizontalFace, startPos, pos)) {
            if (isValidPos(p)) {
                tryToExcavate(pos.clone().add(p));
            }
        }
    }

    private void tryToExcavate(Location pos) {
        if (mined >= getConfig().getMaxMineCount()
//                || (wasBrokenOnNextBreak && stopAfterToolBroken)
                || (getConfig().willDamageTool() && stopBeforeToolBroken && isToolAlmostBreak())) {
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
//            wasBrokenOnNextBreak = true;
            return damageable.getDamage() >= tool.getType().getMaxDurability() - 1;
        }

        return false;
    }

    private boolean isSame(Block block) {
        if (shape != Shape.NONE) {
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
