package cx.rain.mc.diggus_maximus_bukkit.network;

import cx.rain.mc.diggus_maximus_bukkit.excavator.ExcavateShape;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public record ExcavatePacket(Location pos, NamespacedKey id, BlockFacing facing, ExcavateShape shape) {
}
