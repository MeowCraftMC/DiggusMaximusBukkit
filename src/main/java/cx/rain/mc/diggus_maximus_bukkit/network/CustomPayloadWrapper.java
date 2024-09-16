package cx.rain.mc.diggus_maximus_bukkit.network;

import com.comphenix.protocol.reflect.EquivalentConverter;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.AutoWrapper;
import cx.rain.mc.diggus_maximus_bukkit.network.converter.NamespacedKeyConverter;
import io.netty.buffer.ByteBuf;
import org.bukkit.NamespacedKey;

public class CustomPayloadWrapper {
    public static final Class<?> NMS_CLASS = MinecraftReflection.getMinecraftClass("network.protocol.common.custom.DiscardedPayload");

    public static final EquivalentConverter<CustomPayloadWrapper> CONVERTER = AutoWrapper.wrap(CustomPayloadWrapper.class, NMS_CLASS)
            .field(0, NamespacedKeyConverter.INSTANCE);

    private NamespacedKey id;
    private ByteBuf data;

    public CustomPayloadWrapper() {
    }

    public NamespacedKey getId() {
        return id;
    }

    public ByteBuf getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public static class Modifier extends StructureModifier<CustomPayloadWrapper> {
        public Modifier(StructureModifier<?> original) {
            super(NMS_CLASS);
            initialize((StructureModifier<CustomPayloadWrapper>) original);
            target = original.getTarget();
            setConverter(CONVERTER);
        }
    }
}
