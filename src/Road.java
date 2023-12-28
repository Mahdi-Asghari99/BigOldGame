public class Road extends Tile{
    Boolean branch;
    Boolean cactus;
    public Road(Boolean branch, Boolean cactus) {
        super("Road");
        this.branch = branch;
        this.cactus = cactus;
    }

    public Boolean hasBranch() {
        return branch;
    }
    public Boolean hasCactus() {
        return cactus;
    }

    public void erase() {
        branch = false;
        cactus = false;
    }

    @Override
    public String toString() {
        String out =  "Tile{" + "type=" + getType();
        if (hasCactus()) {out += "perk=has a cactus";}
        if (hasBranch()) {out += "perk=has a branch";}
        out += "}";
        return out;
    }
}
