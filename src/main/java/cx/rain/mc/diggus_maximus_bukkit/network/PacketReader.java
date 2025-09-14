package cx.rain.mc.diggus_maximus_bukkit.network;

import com.comphenix.protocol.utility.MinecraftMethods;
import cx.rain.mc.diggus_maximus_bukkit.excavator.Shape;
import cx.rain.mc.diggus_maximus_bukkit.network.packet.ExcavatePacketV1;
import cx.rain.mc.diggus_maximus_bukkit.network.packet.ExcavatePacketV2;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import cx.rain.mc.diggus_maximus_bukkit.utility.FriendlyByteBufHelper;
import cx.rain.mc.diggus_maximus_bukkit.utility.NamespacedKeyHelper;
import io.netty.buffer.Unpooled;

public class PacketReader {
    public static ExcavatePacketV1 read(byte[] bytes) {
        var buf = Unpooled.wrappedBuffer(bytes);
        var friendlyBuf = MinecraftMethods.getFriendlyBufBufConstructor().apply(buf);
        var pos = FriendlyByteBufHelper.readBlockPos(friendlyBuf);
        var id = NamespacedKeyHelper.fromMinecraftKey(FriendlyByteBufHelper.readResourceLocation(friendlyBuf));
        var facing = BlockFacing.from(buf.readInt());
        var shape = Shape.from(buf.readInt());
        return new ExcavatePacketV1(pos, id, facing, shape);
    }

    public static ExcavatePacketV2 readV2(byte[] bytes) {
        var buf = Unpooled.wrappedBuffer(bytes);
        var friendlyBuf = MinecraftMethods.getFriendlyBufBufConstructor().apply(buf);
        var pos = FriendlyByteBufHelper.readBlockPos(friendlyBuf);
        var id = NamespacedKeyHelper.fromMinecraftKey(FriendlyByteBufHelper.readResourceLocation(friendlyBuf));
        var shape = Shape.fromIndex(FriendlyByteBufHelper.readVarInt(friendlyBuf));
        var hitFace = BlockFacing.fromIndex(FriendlyByteBufHelper.readVarInt(friendlyBuf));
        var stopBeforeToolBroken = buf.readBoolean();
        var stopAfterToolBroken = buf.readBoolean();
        return new ExcavatePacketV2(pos, id, shape, hitFace, stopBeforeToolBroken, stopAfterToolBroken);
    }
}
