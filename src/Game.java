import java.util.HashMap;
import java.util.Scanner;

public class Game {
    private Map map;
    private Player player;

    private static final int[] temperatures = {
            15, 14, 13, 12, 11, 10,
            11, 12, 15, 16, 18, 26,
            32, 34, 36, 38, 36, 32,
            28, 24, 18, 18, 17, 16,
    }; // 0h to 23h

    private static final String YES = "YES";
    private static final String NO = "NO";

    private static final int[][] ONE_STEP = {
            {-1, 0}, // N
            {1, 0}, // S
            {0, 1}, // E
            {0, -1} // W
    };
    private static final HashMap<String, Integer> CMD2STEP;
    static {
        CMD2STEP = new HashMap<>();
        CMD2STEP.put("N", 0);
        CMD2STEP.put("S", 1);
        CMD2STEP.put("E", 2);
        CMD2STEP.put("W", 3);
    }
    private static final int START_TIME = 9; // 9AM clock
    private static final int TOTAL_TIME = 72; // max lasting time of game.
    private int elapse = 0;
    private int y, x;
    private int prevY, prevX;

    private Scanner scanner;

    public Game() {
        scanner = new Scanner(System.in);
        map = new Map();
        player = new Player();
        updateCurLocation(map.startTile[0], map.startTile[1]);
        updatePrevLocation();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            scanner.close();
        } finally {
            super.finalize();
        }
    }

    private void updateCurLocation(int y, int x) {
        this.y = y;
        this.x = x;
    }

    private void updatePrevLocation() {
        this.prevY = y;
        this.prevX = x;
    }

    private void updateLocation(int y, int x) {
        markOnMap();
        updatePrevLocation();
        updateCurLocation(y, x);
    }

    private void updateLocation() {
        updateLocation(y, x);
    }

    private void updatePlayer() {
        player.updateState(currentTime(), (int)currentTemp());
    }

    private void updateTime() {
        updatePlayer();
        ++elapse;
    }

    private boolean justMoved() {
        return y != prevY || x != prevX;
    }

    public int timeLeft() {
        return TOTAL_TIME - elapse;
    }

    public int currentTime() {
        return (START_TIME + elapse) % 24;
    }// module 24

    public double currentTemp() {
        return getTemp(currentTime());
    }

    public int getTemp(int time) {
        return temperatures[time];
    }

    public boolean move(String cmd) {
        int direction = CMD2STEP.getOrDefault(cmd, -1);
        if(direction == -1) return false;
        int newY = y + ONE_STEP[direction][0];
        int newX = x + ONE_STEP[direction][1];
        if(map.isValid(newY, newX) && map.isAccessible(newY, newX)) {
            if(familiarRoad(newY, newX)) {
                System.out.println("Seems you have been there, you sure about this move? type \"yes\" to confirm >");
                String input = scanner.nextLine().toUpperCase().trim();
                if(!input.equals(YES)) {
                    return false;
                }
            }
            updateTime();
            updateLocation(newY, newX);
            return true;
        }
        return false;
    }

    public void dead() {
        System.out.println("you are dead");
    }
    public void runOutOfTime() {
        System.out.println("You failed to get out within 72 steps");
    }

    public void invalidCommand() {
        System.out.println("Invalid Command");
    }

    public void waitingForCommand() {
        System.out.print("Input a Command here: ");
    }

    public void printTime() {
        System.out.printf("Current Time: %d\n", currentTime());
        System.out.printf("Time Remain: %d\n", timeLeft());
    }

    public void printTemp() {
        System.out.printf("Curent Temprature: %3.2f\n", currentTemp());
    }

    public void printPlayer() {
        System.out.println(player);
    }

    public boolean reachedTheGoal() {
        return map.isGoal(y, x);
    }

    public boolean familiarRoad(int y, int x) {
        return map.isMarked(y, x);
    }

    public void markOnMap() {
        map.mark(y, x);
    }

    public void pickup() {
        map.erase(y, x);
    }

    public boolean encounteredCactus() {
        return map.isCactus(y, x);
    }

    public boolean encounteredBranch() {
        return map.isBranch(y, x);
    }

    public boolean cutCactus() {
        if(player.canCutCactus()) {
            player.cutCactus();
            pickup();
            updateTime();
            return true;
        }
        return false;
    }

    public boolean pickupBranch() {
        if(player.canPickupBranch()) {
            player.pickupBranch();
            pickup();
            return true;
        }
        return false;
    }

    public boolean makeFire() {
        if(player.canMakeFire()) {
            player.makeFire();
            updateTime();
            return true;
        }
        return false;
    }

    public boolean drinkWater() {
        if(player.canDrinkWater()) {
            player.drinkWater();
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = game.scanner;
        boolean succeed;

        while(true) {
            if(!game.player.isAlive()) {
                game.dead();
                break;
            }
            if(game.timeLeft() == 0) {
                game.runOutOfTime();
                break;
            }

            if(game.reachedTheGoal()) {
                System.out.println("You WIN");
                break;
            }

            if(game.encounteredBranch() && game.justMoved()) {
                System.out.println("Branch found");
                System.out.println("Pickup(yes) or not(no)?");
                String input = scanner.nextLine().toUpperCase().trim();
                if(input.equals(YES)) {
                    if(game.pickupBranch()) {
                        System.out.println("Branch picked");
                    } else {
                        System.out.println("Branch reached upperbound");
                    }
                } else if(!input.equals(NO)) {
                    game.invalidCommand();
                }
                game.updateLocation();
                continue;
            }
            if(game.encounteredCactus() && game.justMoved()) {
                System.out.println("Cactus found");
                System.out.println("Cut(yes) or not(no)?");
                String input = scanner.nextLine().toUpperCase().trim();
                if(input.equals(YES)) {
                    if(game.cutCactus()) {
                        System.out.println("Water gained");
                    } else {
                        System.out.println("Cannot cut, because knife is damaged or bottle is full");
                    }
                } else if(!input.equals(NO)) {
                    game.invalidCommand();
                }
                game.updateLocation();
                continue;
            }

            game.waitingForCommand();
            String command = scanner.nextLine().toUpperCase().trim();

            if(command.equals("EXIT")) {
                System.out.println("Bye!");
                break;
            }

            succeed = false;
            switch (command) {
                case "TIME":
                    game.printTime();
                    break;
                case "TEMP":
                    game.printTemp();
                    break;
                case "PLAYER":
                    game.printPlayer();
                    break;
                case "STATUS":
                    game.printTime();
                    game.printTemp();
                    game.printPlayer();
                    break;
                case "DRINK":
                    succeed = game.drinkWater();
                    if(!succeed) {
                        System.out.println("Bottle is empty.");
                    } else {
                        System.out.println("Cool, Crisp, Refreshing!");
                    }
                    break;
                case "FIRE":
                    succeed = game.makeFire();
                    if(!succeed) {
                        System.out.println("Get some branches before you can make a fire.");
                    } else {
                        System.out.println("Feel warm.");
                    }
                    break;
                case "N": case "S": case "E": case "W":
                    succeed = game.move(command);
                    if(!succeed) {
                        System.out.println("Move didn't happen.");
                    }
                    break;
                default:
                    game.invalidCommand();
            }
        }
        scanner.close();
    }


}
