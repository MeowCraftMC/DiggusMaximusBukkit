package cx.rain.mc.diggus_maximus_bukkit.excavator;

import cx.rain.mc.diggus_maximus_bukkit.DiggusMaximusBukkit;
import cx.rain.mc.diggus_maximus_bukkit.config.ConfigManager;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ExcavateSpreadHelper {

    public static List<Vector> getOffsets(ExcavateShape shape, BlockFacing facing,
                                          Location startPos, Location curPos) {
        if (shape == ExcavateShape.NONE) {
            return getConfig().isDiagonallyMine() ? ExcavateSpreadHelper.standardDiag : ExcavateSpreadHelper.standard;
        }

        if (!getConfig().isEnableShape()) {
            return List.of();
        }

        return switch (shape) {
            case HOLE -> ExcavateSpreadHelper.hole(facing);
            case HORIZONTAL_LAYER -> ExcavateSpreadHelper.horizontalLayer();
            case LAYER -> ExcavateSpreadHelper.layers(facing);
            case ONE_TWO -> ExcavateSpreadHelper.oneByTwo(startPos, curPos);
            case ONE_TWO_TUNNEL -> ExcavateSpreadHelper.oneByTwoTunnel(startPos, curPos, facing);
            case THREE_THREE -> ExcavateSpreadHelper.threeByThree(startPos, curPos, facing);
            case THREE_THREE_TUNNEL -> ExcavateSpreadHelper.threeByThreeTunnel(startPos, curPos, facing);
            default -> List.of();
        };
    }

    private static List<Vector> horizontalLayer() {
        var offsets = new ArrayList<Vector>();
        offsets.add(new Vector(1, 0, 0));
        offsets.add(new Vector(0, 0, 1));
        offsets.add(new Vector(-1, 0, 0));
        offsets.add(new Vector(0, 0, -1));
        return offsets;
    }

    private static List<Vector> layers(BlockFacing facing) {
        if (facing.getAxis() == Axis.Y) {
            return horizontalLayer();
        }

        var offsets = new ArrayList<Vector>();
        offsets.add(new Vector(0, 1, 0));
        offsets.add(new Vector(0, -1, 0));
        if (facing.getAxis() == Axis.Z) {
            offsets.add(new Vector(1, 0, 0));
            offsets.add(new Vector(-1, 0, 0));
        } else {
            offsets.add(new Vector(0, 0, 1));
            offsets.add(new Vector(0, 0, -1));
        }
        return offsets;
    }

    private static List<Vector> hole(BlockFacing facing) {
        var offsets = new ArrayList<Vector>();
        offsets.add(new Vector(0, 0, 0).add(facing.getFacing().getOppositeFace().getDirection()));
        return offsets;
    }

    private static List<Vector> oneByTwo(Location startPos, Location curPos) {
        var offsets = new ArrayList<Vector>();
        if (startPos.getY() == curPos.getY()) {
            offsets.add(new Vector(0, -1, 0));
        }
        return offsets;
    }

    private static List<Vector> oneByTwoTunnel(Location startPos, Location curPos, BlockFacing facing) {
        var offsets = hole(facing);
        if (startPos.getY() == curPos.getY()) {
            offsets.add(new Vector(0, -1, 0));
        }
        return offsets;
    }

    public static List<Vector> threeByThree(Location startPos, Location curPos, BlockFacing facing) {
        var offsets = new ArrayList<Vector>();
        if (startPos.equals(curPos)) {
            if (facing.getAxis() == Axis.Z) {
                offsets.add(new Vector(0, 0, 1));
                offsets.add(new Vector(0, 0, -1));
                offsets.add(new Vector(0, 1, 1));
                offsets.add(new Vector(0, 1, 0));
                offsets.add(new Vector(0, 1, -1));
                offsets.add(new Vector(0, -1, 1));
                offsets.add(new Vector(0, -1, 0));
                offsets.add(new Vector(0, -1, -1));
            }

            if (facing.getAxis() == Axis.Z) {
                offsets.add(new Vector(1, 0, 0));
                offsets.add(new Vector(-1, 0, 0));
                offsets.add(new Vector(1, 1, 0));
                offsets.add(new Vector(0, 1, 0));
                offsets.add(new Vector(-1, 1, 0));
                offsets.add(new Vector(1, -1, 0));
                offsets.add(new Vector(0, -1, 0));
                offsets.add(new Vector(-1, -1, 0));
            }

            if (facing.getAxis() == Axis.Y) {
                offsets.add(new Vector(1, 0, 0));
                offsets.add(new Vector(-1, 0, 0));
                offsets.add(new Vector(1, 0, 1));
                offsets.add(new Vector(-1, 0, 1));
                offsets.add(new Vector(0, 0, 1));
                offsets.add(new Vector(0, 0, -1));
                offsets.add(new Vector(1, 0, -1));
                offsets.add(new Vector(-1, 0, -1));
            }
        }
        return offsets;
    }

    private static List<Vector> threeByThreeTunnel(Location startPos, Location curPos, BlockFacing facing) {
        var offsets = hole(facing);
        offsets.addAll(threeByThree(startPos, curPos, facing));
        return offsets;
    }

    private final static List<Vector> standard = new ArrayList<>();
    private final static List<Vector> standardDiag = new ArrayList<>();

    static {
        standard.add(new Vector(0, 1, 0));
        standard.add(new Vector(0, 0, 1));
        standard.add(new Vector(0, -1, 0));
        standard.add(new Vector(1, 0, 0));
        standard.add(new Vector(0, 0, -1));
        standard.add(new Vector(-1, 0, 0));

        standardDiag.add(new Vector(-1, -1, -1));
        standardDiag.add(new Vector(0, -1, -1));
        standardDiag.add(new Vector(1, -1, -1));
        standardDiag.add(new Vector(-1, 0, -1));
        standardDiag.add(new Vector(0, 0, -1));
        standardDiag.add(new Vector(1, 0, -1));
        standardDiag.add(new Vector(-1, 1, -1));
        standardDiag.add(new Vector(0, 1, -1));
        standardDiag.add(new Vector(1, 1, -1));
        standardDiag.add(new Vector(-1, -1, 0));
        standardDiag.add(new Vector(0, -1, 0));
        standardDiag.add(new Vector(1, -1, 0));
        standardDiag.add(new Vector(-1, 0, 0));
        standardDiag.add(new Vector(0, 0, 0));
        standardDiag.add(new Vector(1, 0, 0));
        standardDiag.add(new Vector(-1, 1, 0));
        standardDiag.add(new Vector(0, 1, 0));
        standardDiag.add(new Vector(1, 1, 0));
        standardDiag.add(new Vector(-1, -1, 1));
        standardDiag.add(new Vector(0, -1, 1));
        standardDiag.add(new Vector(1, -1, 1));
        standardDiag.add(new Vector(-1, 0, 1));
        standardDiag.add(new Vector(0, 0, 1));
        standardDiag.add(new Vector(1, 0, 1));
        standardDiag.add(new Vector(-1, 1, 1));
    }

    private static ConfigManager getConfig() {
        return DiggusMaximusBukkit.getInstance().getConfigManager();
    }
}