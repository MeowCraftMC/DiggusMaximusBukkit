package cx.rain.mc.diggus_maximus_bukkit.excavator;

import cx.rain.mc.diggus_maximus_bukkit.DiggusMaximusBukkit;
import cx.rain.mc.diggus_maximus_bukkit.utility.BlockFacing;
import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ExcavateSpreadHelper {
    public static List<Vector> getSpreadShape(Shape shape, BlockFacing hitFace, BlockFacing playerHorizontalFacing,
                                              Location startPos, Location curPos) {
        return switch (shape) {
            case HOLE -> ExcavateSpreadHelper.hole(hitFace);
            case HORIZONTAL_LAYER -> ExcavateSpreadHelper.horizontalLayer();
            case LAYER -> ExcavateSpreadHelper.layers(hitFace);
            case ONE_BY_TWO -> ExcavateSpreadHelper.oneByTwo(startPos, curPos, hitFace);
            case ONE_BY_TWO_TUNNEL -> ExcavateSpreadHelper.oneByTwoTunnel(startPos, curPos, hitFace, playerHorizontalFacing);
            case THREE_BY_THREE -> ExcavateSpreadHelper.threeByThree(startPos, curPos, hitFace);
            case THREE_BY_THREE_TUNNEL -> ExcavateSpreadHelper.threeByThreeTunnel(startPos, curPos, hitFace);
            case NONE ->
                    DiggusMaximusBukkit.getInstance().getConfigManager().isDiagonallyMine() ? ExcavateSpreadHelper.standardDiagonally : ExcavateSpreadHelper.standard;
        };
    }

    private static List<Vector> horizontalLayer() {
        List<Vector> cube = new ArrayList<>();
        cube.add(new Vector(1, 0, 0));
        cube.add(new Vector(0, 0, 1));
        cube.add(new Vector(-1, 0, 0));
        cube.add(new Vector(0, 0, -1));
        return cube;
    }

    private static List<Vector> layers(BlockFacing facing) {
        if (facing.isVertical()) {
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

    private static List<Vector> hole(BlockFacing facing) {
        List<Vector> cube = new ArrayList<>();
        cube.add(facing.getOpposite().getFacing().getDirection());
        return cube;
    }

    private static List<Vector> threeByThree(Location startPos, Location curPos, BlockFacing facing) {
        List<Vector> cube = new ArrayList<>();
        if (startPos.equals(curPos)) {
            if (facing.isHorizontal()) {
                cube.add(new Vector(0, 1, 0));
                cube.add(new Vector(0, -1, 0));
                if (facing.getAxis() == Axis.Z) {
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

    private static List<Vector> threeByThreeTunnel(Location startPos, Location curPos, BlockFacing facing) {
        List<Vector> cube = threeByThree(startPos, curPos, facing);
        cube.add(facing.getOpposite().getFacing().getDirection());
        cube.addAll(cube.stream().map(p -> p.clone().add(facing.getOpposite().getFacing().getDirection())).toList());
        return cube;
    }

    private static List<Vector> oneByTwo(Location startPos, Location curPos, BlockFacing hitFace) {
        List<Vector> cube = new ArrayList<>();
        if (startPos.getY() == curPos.getY()) {
            if (hitFace == BlockFacing.DOWN) {
                cube.add(new Vector(0, 1, 0));
            } else {
                cube.add(new Vector(0, -1, 0));
            }
        }
        return cube;
    }

    private static List<Vector> oneByTwoTunnel(Location startPos, Location curPos, BlockFacing hitFace, BlockFacing playerFacing) {
        var direction = hitFace;
        if (hitFace.isVertical()) {
            direction = playerFacing.getOpposite();
        }
        List<Vector> cube = hole(direction);
        cube.addAll(oneByTwo(startPos, curPos, hitFace));
        return cube;
    }

    private final static List<Vector> standard = new ArrayList<>();
    private final static List<Vector> standardDiagonally = new ArrayList<>();

    static {
        standard.add(new Vector(0, 1, 0));
        standard.add(new Vector(0, 0, 1));
        standard.add(new Vector(0, -1, 0));
        standard.add(new Vector(1, 0, 0));
        standard.add(new Vector(0, 0, -1));
        standard.add(new Vector(-1, 0, 0));

        standardDiagonally.add(new Vector(-1, -1, -1));
        standardDiagonally.add(new Vector(0, -1, -1));
        standardDiagonally.add(new Vector(1, -1, -1));
        standardDiagonally.add(new Vector(-1, 0, -1));
        standardDiagonally.add(new Vector(0, 0, -1));
        standardDiagonally.add(new Vector(1, 0, -1));
        standardDiagonally.add(new Vector(-1, 1, -1));
        standardDiagonally.add(new Vector(0, 1, -1));
        standardDiagonally.add(new Vector(1, 1, -1));
        standardDiagonally.add(new Vector(-1, -1, 0));
        standardDiagonally.add(new Vector(0, -1, 0));
        standardDiagonally.add(new Vector(1, -1, 0));
        standardDiagonally.add(new Vector(-1, 0, 0));
        standardDiagonally.add(new Vector(0, 0, 0));
        standardDiagonally.add(new Vector(1, 0, 0));
        standardDiagonally.add(new Vector(-1, 1, 0));
        standardDiagonally.add(new Vector(0, 1, 0));
        standardDiagonally.add(new Vector(1, 1, 0));
        standardDiagonally.add(new Vector(-1, -1, 1));
        standardDiagonally.add(new Vector(0, -1, 1));
        standardDiagonally.add(new Vector(1, -1, 1));
        standardDiagonally.add(new Vector(-1, 0, 1));
        standardDiagonally.add(new Vector(0, 0, 1));
        standardDiagonally.add(new Vector(1, 0, 1));
        standardDiagonally.add(new Vector(-1, 1, 1));
        standardDiagonally.add(new Vector(0, 1, 1));
        standardDiagonally.add(new Vector(1, 1, 1));
    }
}