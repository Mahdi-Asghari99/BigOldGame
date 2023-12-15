import java.sql.Array;
import java.util.*;
import java.util.ArrayList;

public class Map {
    private ArrayList<ArrayList<Integer>> map;
    private ArrayList<ArrayList<Boolean>> footmark;
    private static final int[] [] mapArray = { //0: road, 1: hill, 2: branch, 3: cactus
            {0, 3, 1, 0, 2, 0, 2, 1}, // row 0
            {2, 0, 1, 0, 1, 3, 0, 1},
            {0, 0, 1, 3, 0, 0, 0, 2},
            {1, 2, 1, 1, 3, 2, 1, 0},
            {1, 0, 1, 2, 0, 0, 3, 2},
            {2, 0, 1, 3, 1, 2, 0, 1},
            {3, 0, 2, 0, 1, 3, 1, 0},
            {1, 1, 1, 1, 1, 2, 0, 0}
    } ;
    private static final int[] temperatures = {
            15, 14, 13, 12, 11, 10,
            11, 12, 15, 16, 18, 26,
            32, 34, 36, 38, 36, 32,
            28, 24, 18, 18, 17, 16,
    }; // 0h to 23h
    public int[] startTile = new int[]{0,0};
    public int[] goalTile = new int[]{7,7};

    private int height;
    private int width;

    /**
     * constructor:
     * create a 2D ArrayList
     */
    public Map() {
//        tiles = new ArrayList<>();
        height = mapArray.length;
        width = mapArray[0].length;
        map = new ArrayList<>();
        footmark = new ArrayList<>();
        createMap();

    }

    public int get(int y, int x) {
        return map.get(y).get(x);
    }

    public void erase(int y, int x) {
        map.get(y).set(x, 0);
    }

    public boolean isValid(int y, int x) {
        return y >= 0 && y < height && x >= 0 && x < width;
    }

    public boolean isHill(int y, int x) {
        return get(y, x) == 1;
    }

    public boolean isAccessible(int y, int x) {
        return !isHill(y, x);
    }

    public boolean isBranch(int y, int x) {
        return get(y, x) == 2;
    }

    public boolean isCactus(int y, int x) {
        return get(y, x) == 3;
    }

    public boolean isGoal(int y, int x) {
        return y == goalTile[0] && x == goalTile[1];
    }

    public void mark(int y, int x) { //  arraylist-footmark's setter
        footmark.get(y).set(x, true); // Y is row, X is column.
    }

    public boolean isMarked(int y, int x) {
        return footmark.get(y).get(x);
    } // arraylist-footmark's getter

    public int getTemp(int time) {
        return temperatures[time];
    }


    private void createMap() {
        for(int i = 0; i < mapArray.length; i++) {  // i- row - y, j- column - x
            ArrayList<Integer> row = new ArrayList<>(); // Initialize map from the array.
            ArrayList<Boolean> fmRow = new ArrayList<>(); //road have been to.
            for(int j = 0; j < mapArray[0].length; j++) {
                row.add(mapArray[i][j]);
                fmRow.add(false);
            }
            map.add(row);
            footmark.add(fmRow);
        }
    }
}
