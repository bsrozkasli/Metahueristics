package BasarOzkasli;

import representation.Representation;
import java.util.Arrays;

public class KnapsackRepresentation implements Representation {
    private final boolean[] items;

    public KnapsackRepresentation(boolean[] items) {
        this.items = items;
    }

    public boolean[] getItems() {
        return items;
    }

    public boolean isSelected(int index) {
        return items[index];
    }

    public int size() {
        return items.length;
    }

    @Override
    public Representation copy() {
        return new KnapsackRepresentation(Arrays.copyOf(items, items.length));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(items);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof KnapsackRepresentation)) return false;
        KnapsackRepresentation other = (KnapsackRepresentation) obj;
        return Arrays.equals(this.items, other.items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < items.length; i++) {
            sb.append(items[i] ? "1" : "0");
            if (i < items.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}