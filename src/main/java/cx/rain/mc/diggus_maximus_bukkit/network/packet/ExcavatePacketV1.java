package cx.rain.mc.diggus_maximus_bukkit.network.packet;

import cx.rain.mc.diggus_maximus_bukkit.excavator.Shape;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public record ExcavatePacketV1(Location pos, NamespacedKey id, BlockFacing facing, Shape shape) {
}
