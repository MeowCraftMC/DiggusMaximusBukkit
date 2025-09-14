package cx.rain.mc.diggus_maximus_bukkit.utility;

import lombok.Getter;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;

@Getter
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

    public boolean isVertical() {
        return axis == Axis.Y;
    }

    public boolean isHorizontal() {
        return axis == Axis.X || axis == Axis.Z;
    }

    public BlockFacing getOpposite() {
        return switch (this) {
            case NONE -> BlockFacing.NONE;
            case DOWN -> BlockFacing.UP;
            case UP -> BlockFacing.DOWN;
            case NORTH -> BlockFacing.SOUTH;
            case SOUTH -> BlockFacing.NORTH;
            case WEST -> BlockFacing.EAST;
            case EAST -> BlockFacing.WEST;
        };
    }

    public static BlockFacing from(int id) {
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

    public static BlockFacing from(BlockFace face) {
        return switch (face) {
            case SELF -> BlockFacing.NONE;
            case DOWN -> BlockFacing.DOWN;
            case UP -> BlockFacing.UP;
            case NORTH -> BlockFacing.NORTH;
            case SOUTH -> BlockFacing.SOUTH;
            case WEST -> BlockFacing.WEST;
            case EAST -> BlockFacing.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + face);
        };
    }

    public static BlockFacing fromIndex(int i) {
        return switch (i) {
            case 0 -> BlockFacing.DOWN;
            case 1 -> BlockFacing.UP;
            case 2 -> BlockFacing.NORTH;
            case 3 -> BlockFacing.SOUTH;
            case 4 -> BlockFacing.WEST;
            case 5 -> BlockFacing.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}
