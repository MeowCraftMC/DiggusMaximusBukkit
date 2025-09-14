package cx.rain.mc.diggus_maximus_bukkit;

import org.bukkit.NamespacedKey;

import java.util.Objects;

public class PluginConstants {
    public static final NamespacedKey PACKET_ID_V1 = Objects.requireNonNull(NamespacedKey.fromString("diggusmaximus:start_excavate_packet"));
    public static final NamespacedKey PACKET_ID_V2 = Objects.requireNonNull(NamespacedKey.fromString("diggusmaximus:excavate"));

    public static final String PERMISSION = "diggusmaximus.use";
}
