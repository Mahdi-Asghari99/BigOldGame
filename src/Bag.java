import java.util.HashMap;
import java.util.Map;
public class Bag {
    HashMap<String, Item> items;

    public Bag() {
        items = new HashMap<>();
    }

    public boolean addItem(String name, Item item) {
        if(!items.containsKey(name)) {
            items.put(name, item);
            return true;
        }
        return false;
    }

    public boolean gotA(String itemName) {
        return items.containsKey(itemName);
    }

    public Item getItem(String itemName) {
        return items.get(itemName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bag: \n");
        for(Map.Entry<String, Item> e : items.entrySet()) {
            sb.append(e.getValue().toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public void removeItem(String itemName) {
        Item item = items.get(itemName);
        if (item != null && item.available()) {
            items.remove(itemName);
        }
    }
}
