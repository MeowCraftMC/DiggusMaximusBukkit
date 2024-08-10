package cx.rain.mc.diggus_maximus_bukkit.nms.v1_21;

import cx.rain.mc.diggus_maximus_bukkit.nms.common.ExcavatePacket;
import cx.rain.mc.diggus_maximus_bukkit.nms.common.IExcavator;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;

public class ExcavatorImplV1_21 implements IExcavator {
    @Override
    public ExcavatePacket read(byte[] packet) {
        var buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(packet));
        var pos = buf.readBlockPos();
        var id = buf.readResourceLocation();
        var facing = buf.readInt();
        var shape = buf.readInt();

        var face = switch (facing) {
            case -1 -> BlockFace.SELF;
            case 0 -> BlockFace.DOWN;
            case 1 -> BlockFace.UP;
            case 2 -> BlockFace.NORTH;
            case 3 -> BlockFace.SOUTH;
            case 4 -> BlockFace.WEST;
            case 5 -> BlockFace.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + facing);
        };
        return new ExcavatePacket(new Location(null, pos.getX(), pos.getY(), pos.getZ(), 0, 0),
                NamespacedKey.fromString(id.toString()), face, shape);
    }

    @Override
    public byte[] makeSendBuffer(Location location) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeBlockPos(new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
        return buf.array();
    }
}
