package cx.rain.mc.diggus_maximus_bukkit.utility;

import org.bukkit.Axis;
import org.bukkit.block.BlockFace;

public enum BlockFacing {
    NONE(-1, BlockFace.SELF, null),

    DOWN(0, BlockFace.DOWN, Axis.Y),
    UP(1, BlockFace.UP, Axis.Y),

    NORTH(2, BlockFace.NORTH, Axis.Z),
    SOUTH(3, BlockFace.SOUTH, Axis.Z),

    WEST(4, BlockFace.WEST, Axis.X),
    EAST(5, BlockFace.EAST, Axis.X),
    ;

    private final int id;
    private final BlockFace facing;
    private final Axis axis;

    BlockFacing(int id, BlockFace facing, Axis axis) {
        this.id = id;
        this.facing = facing;
        this.axis = axis;
    }

    public int getId() {
        return id;
    }

    public Axis getAxis() {
        return axis;
    }

    public BlockFace getFacing() {
        return facing;
    }

    public boolean isVertical() {
        return axis == Axis.Y;
    }

    public boolean isHorizontal() {
        return axis == Axis.X || axis == Axis.Z;
    }

    public static BlockFacing fromId(int id) {
        return switch (id) {
            case -1 -> BlockFacing.NONE;
            case 0 -> BlockFacing.DOWN;
            case 1 -> BlockFacing.UP;
            case 2 -> BlockFacing.NORTH;
            case 3 -> BlockFacing.SOUTH;
            case 4 -> BlockFacing.WEST;
            case 5 -> BlockFacing.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }
}
