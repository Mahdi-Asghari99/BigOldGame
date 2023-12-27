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
    // Adding gain function for extendability => In the future if we want to sharpen the knife
    public void gain() {
        gain(1);
    }



}
