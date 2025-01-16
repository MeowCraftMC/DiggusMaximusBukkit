package cx.rain.mc.diggus_maximus_bukkit.excavator;

public enum ExcavateShape {
    NONE(-1),
    HORIZONTAL_LAYER(0),
    LAYER(1),
    HOLE(2),
    ONE_TWO(3),
    ONE_TWO_TUNNEL(4),
    THREE_THREE(5),
    THREE_THREE_TUNNEL(6),
    ;

    private final int id;

    ExcavateShape(int id) {
        this.id = id;
    }

    public static ExcavateShape fromId(int id) {
        return switch (id) {
            case 0 -> HORIZONTAL_LAYER;
            case 1 -> LAYER;
            case 2 -> HOLE;
            case 3 -> ONE_TWO;
            case 4 -> ONE_TWO_TUNNEL;
            case 5 -> THREE_THREE;
            case 6 -> THREE_THREE_TUNNEL;
            default -> NONE;
        };
    }
}
