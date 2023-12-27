enum Direction {
    NORTH, SOUTH, WEST, EAST
}

public class Tile {
    private String type;
    private Boolean isMarked = false;
    private Tile north;
    private Tile south;
    private Tile west;
    private Tile east;

    public Tile(String type) {
        this.type = type;
    }

    // Getter and setter methods for category
    public String getType() {
        return type;
    }

    public Tile getNeighbor(Direction direction) {
        switch (direction) {
            case NORTH:
                return north;
            case SOUTH:
                return south;
            case WEST:
                return west;
            case EAST:
                return east;
            default:
                return null;
        }
    }

    public void setNeighbor(Direction direction, Tile neighbor) {
        switch (direction) {
            case NORTH:
                north = neighbor;
                break;
            case SOUTH:
                south = neighbor;
                break;
            case WEST:
                west = neighbor;
                break;
            case EAST:
                east = neighbor;
                break;
        }
    }


    @Override
    public String toString() {
        return "Tile{" +
                "type=" + type +
                '}';
    }
}
