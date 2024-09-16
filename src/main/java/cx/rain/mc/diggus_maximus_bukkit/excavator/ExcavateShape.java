package cx.rain.mc.diggus_maximus_bukkit.excavator;

public enum ExcavateShape {
    NONE(-1),
    HORIZONTAL_LAYER(0),
    LAYER(1),
    HOLE(2),
    ONExTWO(3),
    ONExTWO_TUNNEL(4),
    THREExTHREE(5),
    THREExTHREE_TUNNEL(6),
    ;

    private final int id;

    ExcavateShape(int id) {
        this.id = id;
    }

    public static ExcavateShape fromId(int id) {
        return switch (id) {
            case -1 -> NONE;
            case 0 -> HORIZONTAL_LAYER;
            case 1 -> LAYER;
            case 2 -> HOLE;
            case 3 -> ONExTWO;
            case 4 -> ONExTWO_TUNNEL;
            case 5 -> THREExTHREE;
            case 6 -> THREExTHREE_TUNNEL;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }
}
