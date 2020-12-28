
import java.util.*;

/**
 *
 * @author Jacob Woodhouse
 * @version 10/29/2020
 */
public class AStar {

    private PriorityQueue<Node> open;
    private boolean goalFound;
    private Board board;
    private Node start;
    private Node goal;
    private ArrayList<Node> path;
    private HashMap<Integer, Node> closed;

    /**
     * My main constructor. It sets the goal and start positions, and also
     * initializes several lists.
     * @param b
     */
    public AStar(Board b) {
        board = b;
        start = board.start; //setting start position
        start.setParent(null); // setting start parent to 0
        goal = board.goal; // setting goal position
        open = new PriorityQueue<Node>(); // open list
        closed = new HashMap<Integer, Node>(); // closed list
        open.add(start); // adding start position to open list

    }

    /**
     * This is the heart of the algorithm. It loops through the 
     * Astar process and removes the minimum value from the heap to be able to
     * generate neighbors and determine the goal.
     */
    public void start() {
        Node remove; // initializing node

        while (!open.isEmpty()) { // run when list is not empty
            remove = open.remove();

            if (remove.equals(goal)) { //if the current node equals the goal node
                // start generating a path
                goalFound = true;
                genPath();

            }
            discover(remove);
            closed.put(remove.getIndex(), remove);

        }

        if (open.isEmpty() && goalFound == false) {
            System.out.println("Path not found! Try Again!");
        } else {
            System.out.println("Path was found!");
        }
    }

    /**
     * This is the discover method. It helps find neighbors around the current
     * node.
     * @param n
     */
    public void discover(Node n) {
        int r = n.getRow(); // grabbing the current nodes row and column
        int c = n.getCol();

        for (int i = 0; i < 8; i++) { //I'm using a switch to calculate cost of each move
            Node mock;
            try {
                switch (i) {
                    case 0:
                        mock = board.getBoard()[r + 1][c];
                        break;
                    case 1:
                        mock = board.getBoard()[r - 1][c];
                        break;
                    case 2:
                        mock = board.getBoard()[r][c + 1];
                        break;
                    case 3:
                        mock = board.getBoard()[r][c - 1];
                        break;
                    case 4:
                        mock = board.getBoard()[r - 1][c + 1];
                        break;
                    case 5:
                        mock = board.getBoard()[r - 1][c - 1];
                        break;
                    case 6:
                        mock = board.getBoard()[r + 1][c + 1];
                        break;
                    default:
                        mock = board.getBoard()[r + 1][c - 1];
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
            // after the break from the switch, I check to see if it's a valid position
            if (validPosition(mock)) {
                starProcess(i, mock, n);
            }

        }
    }

    /**
     * This methods checks whether or not the position is valid
     * @param n
     * @return
     */
    public boolean validPosition(Node n) {
        if (checkBoundary(n) == false) { //checking boundary
            return false;
        } else if (n.getIsPathable() == false) { // checking to see if it's pathable
            return false;
        } else if (closed.containsKey(n.getIndex())) { //if it's in the closed list
            return false;
        } else if (open.contains(n)) { //if it's in the openlist
            return false;
        } else {
            return true; // return true if it's none of these things
        }
    }

    /**
     * This method whether or not the position is out of bounds
     * @param n
     * @return
     */
    public boolean checkBoundary(Node n) {
        int r = n.getRow(); //protects the boundary outside of the box
        int c = n.getCol();

        if (r >= 0 && r < 15 && c >= 0 && c < 15) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method discovers the path by working backwards from the 
     * goal node.
     */
    public void genPath() {
        path = new ArrayList<>();

        Node postion = goal;
        while (postion.getParent() != null) {
            postion.setGoalParent(true); //setting true for two string method
            path.add(postion); //adding to path
            postion = postion.getParent(); //
        }
        path.add(postion);

    }

    
    /**
     * This method finds the heuristic value. 
     * @param n
     * @return
     */
    public int findH(Node n) { //calculating heuristic

        int row = n.getRow(); //grabbing rows and columns
        int column = n.getCol();
        int heuristic = 0;
        while (row < board.goal.getRow()) { //calculating H up towards the goal
            row++;
            heuristic += 10;
        }
        while (row > board.goal.getRow()) { // calculating H down towards the goal
            row--;
            heuristic += 10;
        }
        while (column < board.goal.getCol()) { //calculating H from the left of the goal
            column++;
            heuristic += 10;
        }
        while (column > board.goal.getCol()) { //calculating H from the right of the goal
            column--;
            heuristic += 10;
        }

        return heuristic;
    }

    /**
     * This method returns a boolean based on if the algorithm found the goal
     * @return
     */
    public boolean getGoalFound() {
        return this.goalFound;
    }

    /**
     * This is the main toString method. It prints the board. 
     * @return 
     */
    public String toString() {
        String print = "\t";
        System.out.print("\t   1  2  3  4  5  6  7  8  9 10 11 12 13 14 15");

        if (getGoalFound() == false) {
            for (int k = 0; k < 15; k++) {
                if (k >= 9) {
                    print += (k + 1) + " ";
                } else {
                    print += (k + 1) + "  ";
                }
                for (int j = 0; j < 15; j++) {

                    if (board.getBoard()[k][j].getGoal() == true) {
                        print += "G  ";
                    } else if (board.getBoard()[k][j].getStart() == true) {
                        print += "S  ";
                    } else if (board.getBoard()[k][j].getIsPathable() == false) {
                        print += "B  ";
                    } else {
                        print += "-  ";
                    }

                }
                print += "\n\t";
            }
        } else {

            for (int k = 0; k < 15; k++) {
                if (k >= 9) {
                    print += (k + 1) + " ";
                } else {
                    print += (k + 1) + "  ";
                }
                for (int j = 0; j < 15; j++) {

                    if (board.getBoard()[k][j].getGoal() == true) {
                        print += "G  ";
                    } else if (board.getBoard()[k][j].getStart() == true) {
                        print += "S  ";
                    } else if (board.getBoard()[k][j].getIsPathable() == false) {
                        print += "B  ";
                    } else if (board.getBoard()[k][j].getGoalParent()) {
                        print += "X  ";
                    } else {
                        print += "-  ";
                    }
                 
                }
              print += "\n\t";  
            }
        }
        return print;
    }

    /**
     * This is the star process that calculates F, sets the parent value, and 
     * adds the node to the open list
     * @param k
     * @param n
     * @param p
     */
    public void starProcess(int k, Node n, Node p) {
        int g = n.getG();
        if (k < 4) {
            g += 10;
        } else {
            g += 14;
        }
        n.setG(g); // setting g
        n.setH(findH(n)); // calculating H
        n.setF(); // calculating F
        n.setParent(p); // storing the parent node
        open.add(n); // adding to the openlist
    }
}
