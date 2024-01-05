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

    private static final HashMap<String, Direction> CMD2STEP;
    static {
        CMD2STEP = new HashMap<>();
        CMD2STEP.put("N", Direction.NORTH);
        CMD2STEP.put("S", Direction.SOUTH);
        CMD2STEP.put("E", Direction.EAST);
        CMD2STEP.put("W", Direction.WEST);
    }
    private static final int START_TIME = 9; // 9AM clock
    private static final int TOTAL_TIME = 72; // max lasting time of game.
    private int elapse = 0;
    private Tile currentLocation;
    private Tile previousLocation;

    private Scanner scanner;

    public Game() {
        scanner = new Scanner(System.in);
        map = new Map();
        player = new Player();
        updateCurLocation(map.getStartTile());
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

    private void updateCurLocation(Tile tile) {
        currentLocation = tile;
    }

    private void updatePrevLocation() {
        previousLocation = currentLocation;
    }

    private void updateLocation(Tile tile) {
        markOnMap();
        updatePrevLocation();
        updateCurLocation(tile);
    }

    private void updateLocation() {
        updateLocation(currentLocation);
    }

    private void updatePlayer() {
        player.updateState(currentTime(), (int)currentTemp());
    }

    private void updateTime() {
        updatePlayer();
        ++elapse;
    }

    private boolean justMoved() {
        return !currentLocation.equals(previousLocation);
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
        Direction direction = CMD2STEP.getOrDefault(cmd, null);
        if(direction == null) return false;
        Tile newTile = currentLocation.getNeighbor(direction);
        if(newTile == null) return false;
        if(newTile.isAccessible()) {
            if(familiarRoad(newTile)) {
                System.out.println("Seems you have been there, you sure about this move? type \"yes\" to confirm >");
                String input = scanner.nextLine().toUpperCase().trim();
                if(!input.equals(YES)) {
                    return false;
                }
            }
            updateTime();
            updateLocation(newTile);
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
        return map.isGoal(currentLocation);
    }

    public boolean familiarRoad(Tile tile) {
        return tile.isMarked();
    }

    public void markOnMap() {
        currentLocation.mark();
    }

    public void pickup() {
        ((Road) currentLocation).erase();
    }

    public boolean encounteredCactus() {
        return ((Road) currentLocation).hasCactus();
    }

    public boolean encounteredBranch() {
        return ((Road) currentLocation).hasBranch();
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

    public void drop() {
        System.out.print("What to drop? (water bottle, knife, branch: ");
        String itemToDrop = scanner.nextLine().toUpperCase().trim();

        if (itemToDrop.equals("WATER BOTTLE")) {
            if (player.hasWaterBottle()) {
                player.dropWaterBottle();
                System.out.println("Water bottle dropped.");
            } else {
                System.out.println("You don't have a water bottle.");
            }
        } else if (itemToDrop.equals("KNIFE")) {
            if (player.hasKnife()) {
                player.dropKnife();
                System.out.println("Knife dropped.");
            } else {
                System.out.println("You don't have a knife.");
            }
        } else if (itemToDrop.equals("BRANCH")) {
            System.out.print("How many branches to drop? ");
            int numBranches = Integer.parseInt(scanner.nextLine());

            if (player.hasBranch() && player.canDropBranch(numBranches)) {
                player.dropBranch(numBranches);
                System.out.println(numBranches + " branches dropped.");
            } else {
                System.out.println("You don't have enough branches or cannot drop that many branches.");
            }
        } else {
            System.out.println("Invalid item.");
        }
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
                        System.out.println("Cannot cut, because knife is damaged or you do not have a knife or bottle is full or you do not have a bottle.");
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

                case "DROP":
                    game.drop();
                    break;

                default:
                    game.invalidCommand();
            }
        }
        scanner.close();
    }


}
