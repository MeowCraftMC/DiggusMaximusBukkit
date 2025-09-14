package cx.rain.mc.diggus_maximus_bukkit.excavator;

import lombok.Getter;

@Getter
public enum Shape {
    NONE(-1),
    HORIZONTAL_LAYER(0),
    LAYER(1),
    HOLE(2),
    ONE_BY_TWO(3),
    ONE_BY_TWO_TUNNEL(4),
    THREE_BY_THREE(5),
    THREE_BY_THREE_TUNNEL(6),
    ;

    private final int id;

    Shape(int id) {
        this.id = id;
    }

    public Shape prev() {
        return prev(this);
    }

    public Shape next() {
        return next(this);
    }

    public static Shape prev(Shape shape) {
        return switch (shape) {
            case HORIZONTAL_LAYER -> THREE_BY_THREE_TUNNEL;
            case NONE, LAYER -> HORIZONTAL_LAYER;
            case HOLE -> LAYER;
            case ONE_BY_TWO -> HOLE;
            case ONE_BY_TWO_TUNNEL -> ONE_BY_TWO;
            case THREE_BY_THREE -> ONE_BY_TWO_TUNNEL;
            case THREE_BY_THREE_TUNNEL -> THREE_BY_THREE;
        };
    }

    public static Shape next(Shape shape) {
        return switch (shape) {
            case HORIZONTAL_LAYER -> LAYER;
            case LAYER -> HOLE;
            case HOLE -> ONE_BY_TWO;
            case ONE_BY_TWO -> ONE_BY_TWO_TUNNEL;
            case ONE_BY_TWO_TUNNEL -> THREE_BY_THREE;
            case THREE_BY_THREE -> THREE_BY_THREE_TUNNEL;
            case NONE, THREE_BY_THREE_TUNNEL -> HORIZONTAL_LAYER;
        };
    }

    public static Shape from(int i) {
        return switch (i) {
            case 0 -> HORIZONTAL_LAYER;
            case 1 -> LAYER;
            case 2 -> HOLE;
            case 3 -> ONE_BY_TWO;
            case 4 -> ONE_BY_TWO_TUNNEL;
            case 5 -> THREE_BY_THREE;
            case 6 -> THREE_BY_THREE_TUNNEL;
            default -> NONE;
        };
    }

    public static Shape fromIndex(int i) {
        return switch (i) {
            case 0 -> NONE;
            case 1 -> HORIZONTAL_LAYER;
            case 2 -> LAYER;
            case 3 -> HOLE;
            case 4 -> ONE_BY_TWO;
            case 5 -> ONE_BY_TWO_TUNNEL;
            case 6 -> THREE_BY_THREE;
            case 7 -> THREE_BY_THREE_TUNNEL;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}
