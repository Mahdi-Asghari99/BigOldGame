public class Branch extends Item {
    public Branch() {
        super("Branch", 3, 4);

    }

    @Override
    public boolean available() {
        return durability >= 4;
    }

    @Override
    public void use() {
        durability -= 4;
    }

    @Override
    public void gain() {
        gain(1);
    }

    @Override
    public String toString() {
        return String.format("Item: %S, Remaining: %d", name, (int)durability);
    }
}
