package cx.rain.mc.diggus_maximus_bukkit.excavator;

import cx.rain.mc.diggus_maximus_bukkit.config.ConfigManager;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ExcavateSpreadHelper {

    public static List<Vector> getSpreadType(ConfigManager configManager, ExcavateShape shape, BlockFacing facing,
                                             Location startPos, Location curPos) {
        if (!configManager.isEnableShape()) {
            return configManager.isEnableDiagonally() ? ExcavateSpreadHelper.standardDiag : ExcavateSpreadHelper.standard;
        }

        return switch (shape) {
            case NONE -> configManager.isEnableDiagonally() ? ExcavateSpreadHelper.standardDiag : ExcavateSpreadHelper.standard;
            case HOLE -> ExcavateSpreadHelper.hole(facing);
            case HORIZONTAL_LAYER -> ExcavateSpreadHelper.horizontalLayer();
            case LAYER -> ExcavateSpreadHelper.layers(facing);
            case ONExTWO -> ExcavateSpreadHelper.oneByTwo(startPos, curPos);
            case ONExTWO_TUNNEL -> ExcavateSpreadHelper.oneByTwoTunnel(startPos, curPos, facing);
            case THREExTHREE -> ExcavateSpreadHelper.threeByThree(startPos, curPos, facing);
            case THREExTHREE_TUNNEL -> ExcavateSpreadHelper.threeByThreeTunnel(startPos, curPos, facing);
        };
    }

    public static List<Vector> horizontalLayer() {
        List<Vector> cube = new ArrayList<>();
        cube.add(new Vector(1, 0, 0));
        cube.add(new Vector(0, 0, 1));
        cube.add(new Vector(-1, 0, 0));
        cube.add(new Vector(0, 0, -1));
        return cube;
    }

    public static List<Vector> layers(BlockFacing facing) {
        if (facing.getAxis() == Axis.Y) {
            return horizontalLayer();
        }

        List<Vector> cube = new ArrayList<>();
        cube.add(new Vector(0, 1, 0));
        cube.add(new Vector(0, -1, 0));
        if (facing.getAxis() == Axis.Z) {
            cube.add(new Vector(1, 0, 0));
            cube.add(new Vector(-1, 0, 0));
        } else {
            cube.add(new Vector(0, 0, 1));
            cube.add(new Vector(0, 0, -1));
        }
        return cube;
    }

    public static List<Vector> hole(BlockFacing facing) {
        List<Vector> cube = new ArrayList<>();
        cube.add(new Vector(0, 0, 0).add(facing.getFacing().getOppositeFace().getDirection()));
        return cube;
    }

    public static List<Vector> threeByThree(Location startPos, Location curPos, BlockFacing facing) {
        List<Vector> cube = new ArrayList<>();
        if (startPos.equals(curPos)) {
            if (facing.isHorizontal()) {
                cube.add(new Vector(0, 1, 0));
                cube.add(new Vector(0, -1, 0));
                if (facing == BlockFacing.NORTH || facing == BlockFacing.SOUTH) {
                    cube.add(new Vector(1, 0, 0));
                    cube.add(new Vector(-1, 0, 0));
                    cube.add(new Vector(1, 1, 0));
                    cube.add(new Vector(-1, 1, 0));
                    cube.add(new Vector(1, -1, 0));
                    cube.add(new Vector(-1, -1, 0));
                } else {
                    cube.add(new Vector(0, 0, 1));
                    cube.add(new Vector(0, 0, -1));
                    cube.add(new Vector(0, 1, 1));
                    cube.add(new Vector(0, 1, -1));
                    cube.add(new Vector(0, -1, 1));
                    cube.add(new Vector(0, -1, -1));
                }
            } else {
                cube.add(new Vector(1, 0, 0));
                cube.add(new Vector(-1, 0, 0));
                cube.add(new Vector(1, 0, 1));
                cube.add(new Vector(-1, 0, 1));
                cube.add(new Vector(0, 0, 1));
                cube.add(new Vector(0, 0, -1));
                cube.add(new Vector(1, 0, -1));
                cube.add(new Vector(-1, 0, -1));
            }
        }
        return cube;
    }

    public static List<Vector> threeByThreeTunnel(Location startPos, Location curPos, BlockFacing facing) {
        List<Vector> cube = threeByThree(startPos, curPos, facing);
        cube.add(new Vector(0, 0, 0).add(facing.getFacing().getOppositeFace().getDirection()));
        cube.addAll(cube.stream().map(loc -> loc.add(facing.getFacing().getOppositeFace().getDirection())).toList());
        return cube;
    }

    public static List<Vector> oneByTwo(Location startPos, Location curPos) {
        List<Vector> cube = new ArrayList<>();
        if (startPos.getY() == curPos.getY()) {
            cube.add(new Vector(0, -1, 0));
        }
        return cube;
    }

    public static List<Vector> oneByTwoTunnel(Location startPos, Location curPos, BlockFacing facing) {
        List<Vector> cube = hole(facing);
        if (startPos.getY() == curPos.getY()) {
            cube.add(new Vector(0, -1, 0));
        }
        return cube;
    }

    public final static List<Vector> standard = new ArrayList<>();
    public final static List<Vector> standardDiag = new ArrayList<>();

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

    private static Location mutate(Location origin, int x, int y, int z) {
        return origin.clone().add(x, y, z);
    }
}