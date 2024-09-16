package cx.rain.mc.diggus_maximus_bukkit.utility;

import com.comphenix.protocol.wrappers.MinecraftKey;
import org.bukkit.NamespacedKey;

public class NamespacedKeyHelper {
    public static NamespacedKey fromMinecraftKey(MinecraftKey key) {
        return NamespacedKey.fromString(key.getFullKey());
    }
}
