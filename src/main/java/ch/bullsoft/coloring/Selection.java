package ch.bullsoft.coloring;

public final class Selection {

    private final Integer node;
    private final Integer color;

    public Selection(Integer n, Integer c) {
        this.node = n;
        this.color = c;
    }

    public Integer getNode() {
        return node;
    }

    public Integer getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "(" + node + ", " + color + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Selection selection = (Selection) o;

        if (!getNode().equals(selection.getNode())) return false;
        return getColor().equals(selection.getColor());
    }

    @Override
    public int hashCode() {
        int result = getNode().hashCode();
        result = 31 * result + getColor().hashCode();
        return result;
    }
}