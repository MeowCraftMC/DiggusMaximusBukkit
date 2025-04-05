package cx.rain.mc.diggus_maximus_bukkit.network;

import com.comphenix.protocol.utility.MinecraftMethods;
import cx.rain.mc.diggus_maximus_bukkit.excavator.ExcavateShape;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import cx.rain.mc.diggus_maximus_bukkit.utility.FriendlyByteBufHelper;
import cx.rain.mc.diggus_maximus_bukkit.utility.NamespacedKeyHelper;
import io.netty.buffer.Unpooled;

public class PacketReader {
    public static ExcavatePacket read(byte[] bytes) {
        var buf = Unpooled.wrappedBuffer(bytes);
        var friendlyBuf = MinecraftMethods.getFriendlyBufBufConstructor().apply(buf);
        var pos = FriendlyByteBufHelper.readBlockPos(friendlyBuf);
        var id = NamespacedKeyHelper.fromMinecraftKey(FriendlyByteBufHelper.readResourceLocation(friendlyBuf));
        var facing = BlockFacing.fromId(buf.readInt());
        var shape = ExcavateShape.fromId(buf.readInt());
        return new ExcavatePacket(pos, id, facing, shape);
    }
}
