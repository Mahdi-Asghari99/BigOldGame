public class Bottle extends Item{
    public Bottle() {
        super("Bottle", 0.4, 0.5);
    }

    @Override
    public void use() {
        durability = 0;
    }

    @Override
    public void gain() {
        gain(0.05);
    }

}
