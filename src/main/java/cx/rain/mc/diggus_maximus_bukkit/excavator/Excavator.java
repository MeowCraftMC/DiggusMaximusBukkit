package cx.rain.mc.diggus_maximus_bukkit.excavator;

import cx.rain.mc.diggus_maximus_bukkit.channel.ChannelDiggusMaximus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Excavator {
    private final Player player;
    private final Block block;
    private final Material blockMaterial;
    private final Location startPos;

    private final Deque<Location> points = new ArrayDeque<>();

    private int mined = 0;

    public Excavator(Player player, Location startPos) {
        this.player = player;
        this.block = startPos.getBlock();
        this.blockMaterial = this.block.getType();
        this.startPos = startPos;
    }

    public void start() {
        forceExcavate(startPos);
        while (!points.isEmpty()) {
            spread(points.remove());
        }
    }

    private void forceExcavate(Location pos) {
        if (player.breakBlock(pos.getBlock())) {
            points.add(pos);
            mined++;
        }
    }

    private void spread(Location pos) {
        for (var p : getPositions(pos.getWorld())) {
            var t = pos.clone().add(p);
            if (isValidPos(t)) {
                excavate(t);
            }
        }
    }

    private void excavate(Location pos) {
        if (mined >= 40) {  // Todo: config.
            return;
        }

        var blockToBreak = pos.getBlock();
        if (isSame(blockToBreak)
                && canMine(pos)
                && player.breakBlock(blockToBreak)) {
            points.add(pos);
            mined++;
        }
    }

    private boolean isSame(Block blockNew) {
        return blockMaterial.equals(blockNew.getType());
    }

    private static boolean isValidPos(Location pos) {
        return (Math.abs(pos.getBlockX()) + Math.abs(pos.getBlockY()) + Math.abs(pos.getBlockZ())) != 0;
    }

    private boolean canMine(Location pos) {
        return pos.distance(startPos) < 11;
    }

    private static List<Location> getPositions(World world) {
        var positions = new ArrayList<Location>();

        for (var i = -1; i <= 1; i++) {
            for (var j = -1; j <= 1; j++) {
                for (var k = -1; k <= 1; k++) {
                    positions.add(new Location(world, i, j, k, 0, 0));
                }
            }
        }

        return positions;
    }
}
