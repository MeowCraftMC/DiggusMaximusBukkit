package cx.rain.mc.diggus_maximus_bukkit.network.converter;

import com.comphenix.protocol.reflect.EquivalentConverter;
import com.comphenix.protocol.reflect.FuzzyReflection;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.comphenix.protocol.reflect.accessors.MethodAccessor;
import com.comphenix.protocol.reflect.fuzzy.FuzzyMethodContract;
import com.comphenix.protocol.utility.MinecraftReflection;
import org.bukkit.NamespacedKey;

public class NamespacedKeyConverter implements EquivalentConverter<NamespacedKey> {
    public static final EquivalentConverter<NamespacedKey> INSTANCE = new NamespacedKeyConverter();

    private static final MethodAccessor CONSTRUCTOR;

    static {
        CONSTRUCTOR = Accessors.getMethodAccessor(FuzzyReflection.fromClass(MinecraftReflection.getMinecraftKeyClass())
                .getMethod(FuzzyMethodContract.newBuilder()
                        .parameterCount(2)
                        .parameterExactType(String.class, 0)
                        .parameterExactType(String.class, 1)
                        .returnTypeExact(MinecraftReflection.getMinecraftKeyClass())
                        .build()));

    }

    @Override
    public Object getGeneric(NamespacedKey specific) {
        return CONSTRUCTOR.invoke(null, specific.getNamespace(), specific.getKey());
    }

    @Override
    public NamespacedKey getSpecific(Object generic) {
        var modifier = new StructureModifier<String>(generic.getClass())
                .withTarget(generic)
                .withType(String.class);
        return NamespacedKey.fromString(modifier.read(0) + ":" + modifier.read(1));
    }

    @Override
    public Class<NamespacedKey> getSpecificType() {
        return NamespacedKey.class;
    }
}
