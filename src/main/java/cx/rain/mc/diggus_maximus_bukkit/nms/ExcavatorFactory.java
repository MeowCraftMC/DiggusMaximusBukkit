package cx.rain.mc.diggus_maximus_bukkit.nms;

import cx.rain.mc.diggus_maximus_bukkit.DiggusMaximusBukkit;
import cx.rain.mc.diggus_maximus_bukkit.nms.common.IExcavator;
import cx.rain.mc.diggus_maximus_bukkit.nms.v1_19_4.ExcavatorImplV1_19_4;
import org.bukkit.Bukkit;

public class ExcavatorFactory {
    protected static IExcavator EXCAVATOR;

    static {
        var bukkitVersion = Bukkit.getBukkitVersion();
        switch (bukkitVersion) {
            case "1.19.4-R0.1-SNAPSHOT",
                    "1.20-R0.1-SNAPSHOT", "1.20.1-R0.1-SNAPSHOT", "1.20.2-R0.1-SNAPSHOT", "1.20.3-R0.1-SNAPSHOT", "1.20.4-R0.1-SNAPSHOT" ->
                    EXCAVATOR = new ExcavatorImplV1_19_4();
            default -> {
                DiggusMaximusBukkit.getInstance().getLogger().warning("Not supported version: " + bukkitVersion
                        + ", use it at our own risk.");
                EXCAVATOR = new ExcavatorImplV1_19_4();
            }
        }
    }

    public static IExcavator getExcavator() {
        return EXCAVATOR;
    }
}
