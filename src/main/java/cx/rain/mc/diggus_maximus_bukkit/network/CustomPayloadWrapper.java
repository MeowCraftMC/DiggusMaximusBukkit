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

    public static final EquivalentConverter<CustomPayloadWrapper> CONVERTER = new EquivalentConverter<>() {
        private final EquivalentConverter<CustomPayloadWrapper> internal = AutoWrapper.wrap(CustomPayloadWrapper.class, NMS_CLASS)
                .field(0, NamespacedKeyConverter.INSTANCE);

        @Override
        public Object getGeneric(CustomPayloadWrapper specific) {
            return internal.getGeneric(specific);
        }

        @Override
        public CustomPayloadWrapper getSpecific(Object generic) {
            if (MinecraftReflection.is(NMS_CLASS, generic)) {
                return internal.getSpecific(generic);
            }

            return null;
        }

        @Override
        public Class<CustomPayloadWrapper> getSpecificType() {
            return CustomPayloadWrapper.class;
        }
    };

    private NamespacedKey id;
    private byte[] data;

    public CustomPayloadWrapper() {
    }

    public NamespacedKey getId() {
        return id;
    }

    public byte[] getData() {
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
