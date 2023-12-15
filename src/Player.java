public class Player {

    private static final double BASE_TEMP_LOSS = 0.03;
    private static final double HOURLY_TEMP_LOSS_INCREASE = 0.02;
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
        bag.addItem("Branch", new Branch());
    }

    public boolean isAlive() {
        return water > -0.5 && bodyTemp > 35.0;
    }

    private static boolean isNight(int curTime) {
        return curTime >= 21 || curTime <= 8;
    }

    private void updateWater(int time, int temp) {
        if(!isNight(time) && temp >= TEMP_THRESHOLD_OF_WATER_LOSS) {
            water -= (temp - TEMP_THRESHOLD_OF_WATER_LOSS) * WATER_LOSS_UNIT + BASE_WATER_LOSS;
        }
    }

    private void updateBodyTemp(int time, int temp) {
        if(isNight(time) && temp <= TEMP_THRESHOLD_OF_BODYTEMP_LOSS) {
            bodyTemp -= (TEMP_THRESHOLD_OF_BODYTEMP_LOSS - temp) * HOURLY_TEMP_LOSS_INCREASE + BASE_TEMP_LOSS;
        }
    }

    public boolean canUse(String name) {
        return bag.gotA(name) && bag.getItem(name).available();
    }

    public boolean canPickup(String name) {
        return bag.gotA(name) && bag.getItem(name).notFull();
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
        bag.getItem(name).use();
    }


    public Item getItem(String name) {
        return bag.getItem(name);
    }

    public double getDurability(String name) {
        Item item = getItem(name);
        return item.get();
    }

    public boolean canMakeFire() {
        return canUse("Branch");
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
        consume("Knife");
    }

    public boolean canUseKnife() {
        return canUse("Knife");
    }

    public void useKnife() {
        consume("Knife");
    }

    public void updateState(int curTime, int temp) {
        updateWater(curTime, temp);
        updateBodyTemp(curTime, temp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Player Status: \n");
        sb.append(String.format("Water: %3.3f\n", water));
        sb.append(String.format("Body Temperature: %3.3f \n", bodyTemp));
        sb.append(bag.toString());
        return sb.toString();
    }
}
