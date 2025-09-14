package cx.rain.mc.diggus_maximus_bukkit.utility;

import com.comphenix.protocol.reflect.FuzzyReflection;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.MethodAccessor;
import com.comphenix.protocol.reflect.fuzzy.FuzzyMethodContract;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.MinecraftKey;
import org.bukkit.Location;

public class FriendlyByteBufHelper {

    public static final Class<?> FRIENDLY_BYTE_BUF = MinecraftReflection.getPacketDataSerializerClass();
    public static final MethodAccessor READ_BLOCK_POS;
    public static final MethodAccessor READ_VAR_INT;
    public static final MethodAccessor READ_RESOURCE_LOCATION;

    static {
        READ_BLOCK_POS = Accessors.getMethodAccessor(FuzzyReflection.fromClass(FRIENDLY_BYTE_BUF)
                .getMethod(FuzzyMethodContract.newBuilder()
                        .parameterCount(0)
                        .returnTypeExact(MinecraftReflection.getBlockPositionClass())
                        .build()));
        READ_VAR_INT = Accessors.getMethodAccessor(FuzzyReflection.fromClass(FRIENDLY_BYTE_BUF)
                .getMethod(FuzzyMethodContract.newBuilder()
                        .nameExact("readVarInt")
                        .build()));
        READ_RESOURCE_LOCATION = Accessors.getMethodAccessor(FuzzyReflection.fromClass(FRIENDLY_BYTE_BUF)
                .getMethod(FuzzyMethodContract.newBuilder()
                        .parameterCount(0)
                        .returnTypeExact(MinecraftReflection.getMinecraftKeyClass())
                        .build()));
    }

    public static Location readBlockPos(Object buf) {
        var obj = READ_BLOCK_POS.invoke(buf);
        var wrapped = BukkitConverters.getSectionPositionConverter().getSpecific(obj);
        return wrapped.toLocation(null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Enum<T>> T readEnum(Object buf, Class<T> enumClass) {
        return (T)((Enum[])enumClass.getEnumConstants())[readVarInt(buf)];
    }

    public static int readVarInt(Object buf) {
        var obj = READ_VAR_INT.invoke(buf);
        return (int) obj;
    }

    public static MinecraftKey readResourceLocation(Object buf) {
        var obj = READ_RESOURCE_LOCATION.invoke(buf);
        return MinecraftKey.fromHandle(obj);
    }
}
