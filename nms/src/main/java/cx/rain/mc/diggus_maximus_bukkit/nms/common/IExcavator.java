package cx.rain.mc.diggus_maximus_bukkit.nms.common;

import org.bukkit.Location;

public interface IExcavator {
    public ExcavatePacket read(byte[] packet);

    public byte[] makeSendBuffer(Location location);
}
