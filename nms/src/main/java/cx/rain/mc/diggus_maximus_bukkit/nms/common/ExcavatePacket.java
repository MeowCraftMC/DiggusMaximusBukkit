package cx.rain.mc.diggus_maximus_bukkit.nms.common;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;

public record ExcavatePacket(Location blockPos, NamespacedKey blockId, BlockFace blockFace, int shape) {
}
