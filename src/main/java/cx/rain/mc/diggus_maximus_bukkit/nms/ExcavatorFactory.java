package cx.rain.mc.diggus_maximus_bukkit.nms;

import cx.rain.mc.diggus_maximus_bukkit.nms.common.IExcavator;
import cx.rain.mc.diggus_maximus_bukkit.nms.v1_19_4.ExcavatorImplV1_19_4;
import org.bukkit.Bukkit;

public class ExcavatorFactory {
    protected static IExcavator EXCAVATOR;

    static {
        var bukkitVersion = Bukkit.getBukkitVersion();
        switch (bukkitVersion) {
            case "1.19.4-R0.1-SNAPSHOT":
                EXCAVATOR = new ExcavatorImplV1_19_4();
                break;
            default:
                throw new RuntimeException("Not supported version!");
        }
    }

    public static IExcavator getExcavator() {
        return EXCAVATOR;
    }
}
