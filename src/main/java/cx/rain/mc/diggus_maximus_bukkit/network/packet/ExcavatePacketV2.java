package cx.rain.mc.diggus_maximus_bukkit.network.packet;

import cx.rain.mc.diggus_maximus_bukkit.excavator.Shape;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public record ExcavatePacketV2(Location pos, NamespacedKey id, Shape shape, BlockFacing hitFace,
                               boolean stopBeforeToolBroken, boolean stopAfterToolBroken) {
}
