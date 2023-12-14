public class Knife extends Item{

    public Knife() {
        super("Knife", 100, 100);
    }

    public Knife(int durability) {
        super("Knife", durability, 100);
    }
    @Override
    public void use() {
        --durability;
    }

    @Override
    public void gain() {
        return;
    }



}
