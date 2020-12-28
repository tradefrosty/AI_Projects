/**
 * 
 * @author Jacob Woodhouse
 * 
 * I made several changes to this code. I wanted to be able to access boolean values.
 * I also made it comparable and created a compareTo method based on a index value.
 */
public class Node implements Comparable<Node> {

    private int type, row, col, f, g = 0, h;
    private Node parent;
    public static final int PATHABLE = 0, UNPATHABLE = 1;
    private boolean isPathable;
    private boolean start;
    private boolean current;
    private boolean goal;
    private boolean goalParent;
    private int index;

    public Node(int r, int c, boolean value, int i) {
        row = r;
        col = c;
        isPathable = value;
        parent = null;
        index = i;

    }

    public void setIndex(int value) {
        index = value;
    }

    public void setGoalParent(boolean value) {
        goalParent = value;
    }

    public boolean getGoalParent() {
        return goalParent;
    }

    public int getIndex() {
        return index;
    }

    public void setGoal(boolean value) {
        goal = value;
    }

    public void setStart(boolean value) {
        start = value;
    }

    public void setCurrent(boolean value) {
        current = value;
    }

    public boolean getStart() {
        return start;
    }

    public boolean getCurrent() {
        return current;
    }

    public boolean getGoal() {
        return goal;
    }

    public void setIsPathable(boolean value) {
        this.isPathable = value;
    }

    public boolean getIsPathable() {
        return this.isPathable;
    }

    public void setF() {
        f = g + h;
    }

    public void setG(int value) {
        g = value;
    }

    public void setH(int value) {
        h = value;
    }

    public void setParent(Node n) {
        parent = n;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public Node getParent() {
        return parent;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getType() {
        return type;
    }

    public boolean equals(Object in) {
        Node n = (Node) in;
        return row == n.getRow() && col == n.getCol();
    }

    public int compareTo(Node other) {
        if (this.f == other.getF()) {
            return 0;
        } else if (this.f > other.getF()) {
            return 1;
        } else {
            return -1;
        }
    }

    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
