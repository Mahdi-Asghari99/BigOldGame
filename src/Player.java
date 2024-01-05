public class Player {

    private static final double BASE_TEMP_LOSS = 0.03;
    private static final double BODYTEMP_LOSS_UNIT = 0.02;
    private static final double TEMP_GAIN_UNIT = 0.1;
    private static final double BASE_WATER_LOSS = 0.04;
    private static final double WATER_LOSS_UNIT = 0.01;
    private static final int TEMP_THRESHOLD_OF_WATER_LOSS = 32;
    private static final int TEMP_THRESHOLD_OF_BODYTEMP_LOSS = 15;
    private double water;
    private double bodyTemp;

    private Bag bag;



    public Player() {
        water = 0;
        bodyTemp = 36.8;
        bag = new Bag();
        bag.addItem("Knife", new Knife());
        bag.addItem("Bottle", new Bottle());
        bag.addItem("Branch", new Branch()); // be more friendly to the player.
        // with 3 branches at start
    }

    public boolean isAlive() {
        return water > -0.5 && bodyTemp > 35.0;
    }

    private boolean isNight(int curTime) {
        return curTime >= 21 || curTime <= 8;
    }

    private void updateWater(int time, int temp) {
        if(!isNight(time) && temp >= TEMP_THRESHOLD_OF_WATER_LOSS) {
            water -= (temp - TEMP_THRESHOLD_OF_WATER_LOSS) * WATER_LOSS_UNIT + BASE_WATER_LOSS;
        }
    }

    private void updateBodyTemp(int time, int temp) {
        if(isNight(time) && temp <= TEMP_THRESHOLD_OF_BODYTEMP_LOSS) {
            bodyTemp -= (TEMP_THRESHOLD_OF_BODYTEMP_LOSS - temp) * BODYTEMP_LOSS_UNIT + BASE_TEMP_LOSS;
        }
    }

    public void updateState(int curTime, int temp) {
        updateWater(curTime, temp);
        updateBodyTemp(curTime, temp);
    }

    public boolean canMakeFire() {
        return bag.gotA("Branch") && ((Branch)(bag.getItem("Branch"))).availableForFire();
    }

    public void makeFire() {
        bodyTemp += TEMP_GAIN_UNIT;
        consume("Branch");
    }

    public boolean canPickupBranch() {
        return canPickup("Branch");
    }

    public void pickupBranch() {
        pickup("Branch");
    }

    public boolean canDrinkWater() {
        return canUse("Bottle");
    }

    public void drinkWater() {
        water += getDurability("Bottle");
        consume("Bottle");
    }

    public boolean canCutCactus() {
        return canUseKnife() && canPickup("Bottle");
    }

    public void cutCactus() {
        pickup("Bottle");
        useKnife();
    }

    public boolean canUseKnife() {
        return canUse("Knife");
    }

    public void useKnife() {
        consume("Knife");
    }

    public void pickup(String name) {
        Item item = getItem(name);
        item.gain();
    }

    public void pickup(String name, double amount) {
        Item item = getItem(name);
        item.gain(amount);
    }

    public void consume(String name) {
        getItem(name).use();
    }

    public double getDurability(String name) {
        Item item = getItem(name);
        return item.get();
    }

    public Item getItem(String name) {
        return bag.getItem(name);
    }

    public boolean canUse(String name) {
        return bag.gotA(name) && bag.getItem(name).available();
    }

    public boolean canPickup(String name) {
        return bag.gotA(name) && bag.getItem(name).notFull();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Player Status: \n");
        sb.append(String.format("Water: %3.3f\n", water));
        sb.append(String.format("Body Temperature: %3.3f \n", bodyTemp));
        sb.append(bag.toString());
        return sb.toString();
    }


    public boolean hasWaterBottle() {
        return bag.gotA("Bottle");
    }

    public void dropWaterBottle() {
        bag.removeItem("Bottle");
    }

    public boolean hasKnife() {
        return bag.gotA("Knife");
    }

    public void dropKnife() {
        bag.removeItem("Knife");
    }


    public boolean canDropBranch(int numBranches) {
        // several cases: no branches at all, not enough branches, enough branches, illegal input
        if (numBranches < 0) {
            System.out.println("Can't drop negative branches.");
            return false;
        }
        // no branches at all
        if (hasBranch() == false) {
            System.out.println("No branches at all.");
            return false;
        }
        // not enough branches
        if (numBranches > getDurability("Branch")) {
            System.out.println("Not enough branches.");
            return false;
        }
        // enough branches
        return true;
    }

    public void dropBranch(int numBranches) {
        if (!canDropBranch(numBranches)) {
            throw new RuntimeException("Can't drop " + numBranches + " branches.");
        }
        Item branch = getItem("Branch");
        if (branch.getClass() == Branch.class) {
            ((Branch)branch).discardBranch(numBranches);
        } else {
            throw new RuntimeException("This is not a branch.");
        }
    }

    public boolean hasBranch() {
        return bag.gotA("Branch");
    }


}
