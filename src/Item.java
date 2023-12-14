abstract class Item {
    protected double durability;
    protected double upperbound;

    protected String name;

    public Item(String name, double durability, double upperbound) {
        this.name = name;
        this.durability = durability;
        this.upperbound = upperbound;
    }

    public abstract void use();
    public abstract void gain();

    public double get() {
        return durability;
    }
    public void gain(double amount) {
        durability += amount;
        durability = Math.min(durability, upperbound);
    }
    public boolean available() {
        return durability > 0;
    }

    public boolean notFull() {
        return durability < upperbound;
    }

    @Override
    public String toString() {
        return String.format("Item: %S, Remaining: %3.2f", name, durability);
    }
}
