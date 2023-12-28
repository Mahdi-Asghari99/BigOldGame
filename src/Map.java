import java.sql.Array;
import java.util.*;
import java.util.ArrayList;

public class Map {
    private static final int[][] testMapArray = { //0: road, 1: hill, 2: branch, 3: cactus
            {0, 3, 1, 0, 2, 0, 2, 1}, // row 0
            {2, 0, 1, 0, 1, 3, 0, 1},
            {0, 0, 1, 3, 0, 0, 0, 2},
            {1, 2, 1, 1, 3, 2, 1, 0},
            {1, 0, 1, 2, 0, 0, 3, 2},
            {2, 0, 1, 3, 1, 2, 0, 1},
            {3, 0, 2, 0, 1, 3, 1, 0},
            {1, 1, 1, 1, 1, 2, 0, 0}
    } ;

    private final Tile startTile;
    private Tile goalTile;

    public Map(int[][] mapArray) {
        startTile = createTileFromArray(mapArray, 0, 0, new Tile[mapArray.length][mapArray[0].length]);
    }

    public Map() {
        this(testMapArray);
    }

    private Tile createTileFromArray(int[][] mapArray, int row, int col, Tile[][] createdTiles) {
        if (row < 0 || row >= mapArray.length || col < 0 || col >= mapArray[0].length) {
            // Out of bounds, return null
            return null;
        }

        if (createdTiles[row][col] != null) {
            // Tile already created, return the existing tile
            return createdTiles[row][col];
        }

        int type = mapArray[row][col];
        Tile tile = null;
        switch (type) {
            case 1:
                tile = new Hill();
                break;
            case 0 :
                tile = new Road(false, false);
                break;
            case 2 :
                tile = new Road(true, false);
                break;
            case 3 :
                tile = new Road(false, true);
                break;
        };

        if (row == mapArray.length-1 && col == mapArray[0].length-1) {
            goalTile =  tile;
        }

        createdTiles[row][col] = tile;

        tile.setNeighbor(Direction.NORTH, createTileFromArray(mapArray, row - 1, col, createdTiles));
        tile.setNeighbor(Direction.SOUTH, createTileFromArray(mapArray, row + 1, col, createdTiles));
        tile.setNeighbor(Direction.WEST, createTileFromArray(mapArray, row, col - 1, createdTiles));
        tile.setNeighbor(Direction.EAST, createTileFromArray(mapArray, row, col + 1, createdTiles));

        return tile;
    }

    public Tile getStartTile() {
        return startTile;
    }

    public boolean isGoal(Tile tile) {
        return tile.equals(goalTile);
    }

}
