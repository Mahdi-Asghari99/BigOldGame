public class Road extends Tile{
    Boolean hasBranch = false;
    Boolean hasCactus = false;
    public Road(Boolean hasBranch, Boolean hasCactus) {
        super("Road");
        this.hasBranch = hasBranch;
        this.hasCactus = hasCactus;
    }

    @Override
    public String toString() {
        String out =  "Tile{" + "type=" + getType();
        if (hasCactus) {out += "perk=has a cactus";}
        if (hasBranch) {out += "perk=has a branch";}
        out += "}";
        return out;
    }
}
