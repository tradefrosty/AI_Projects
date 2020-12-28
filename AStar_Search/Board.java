
import java.util.ArrayList;

/**
 *
 * @author Jacob Woodhouse
 * @version 10/29/2020
 */
public class Board {

    private int size = 15;
    private Node[][] board;

    public Node goal;

    public Node start;

    /**
     * A basic constructor initializing the size of the board
     */
    public Board() {

        board = new Node[size][size];
    }

    /**
     * This method  creates the board and generates each node.
     * There's a 10% chance for each node to be unpathable.
     */
    public void createBoard() {
        int index = 0;
        for (int k = 0; k < size; k++) {
            for (int j = 0; j < size; j++) {

                double rng = Math.random();
                if (rng <= 0.10) {
                    this.board[k][j] = new Node(k, j, false, index);
                } else {
                    this.board[k][j] = new Node(k, j, true, index);
                }
                index++;

            }
        }
    }

    /**
     * This returns the location of a node
     * @return Board 
     */
    public Node[][] getBoard() {
        return this.board;
    }

    /**
     * setting goal
     * @param r row
     * @param c column
     */
    public void setGoal(int r, int c) {
        this.board[r - 1][c - 1].setGoal(true);
        this.goal = this.board[r - 1][c - 1];
    }

    /**
     * setting starting position
     * @param r row
     * @param c column
     */
    public void setStart(int r, int c) {
        this.board[r - 1][c - 1].setStart(true);
        this.start = this.board[r - 1][c - 1];
    }

    /**
     *  A standard toString method
     * @return 
     */
    public String toString() {
        String print = "\t";
        System.out.println("\t   1  2  3  4  5  6  7  8  9 10 11 12 13 14 15");
        for (int i = 0; i < size; i++) {
            if (i >= 9) {
                print += (i + 1) + " ";
            } else {
                print += (i + 1) + "  ";
            }
            for (int j = 0; j < size; j++) {

                if (board[i][j].getGoal() == true) {
                    print += "G  ";
                } else if (board[i][j].getStart() == true) {
                    print += "S  ";
                } else if (board[i][j].getIsPathable() == false) {
                    print += "B  ";
                } else {
                    print += "-  ";
                }

                if (board[i][j].getGoalParent() == true) {
                    print += "P ";
                }
            }
            print += "\n\t";
        }
        return print;
    }

    /**
     * This method checks if the position is valid. It's used in main.
     * @param n
     * @return
     */
    public boolean checkPosition(Node n) {
        int r = n.getRow();
        int c = n.getCol();
        boolean boundary = false;
        if (r >= 0 && r < 15 && c >= 0 && c < 15) {
            boundary = true;
        } else {
            boundary = false;
        }

        if (n.getIsPathable() == false || boundary == false) {
            return false;
        } else {
            return true;
        }
    }

}
