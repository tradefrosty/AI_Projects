
import java.util.*;

/**
 * The 8-Queens problem requires that 8 queens be placed on a board with 8 rows
 * and columns so that no queen occupies the same row, column or diagonal as
 * another queen. To solve this problem using the Hill-Climbing with random
 * restart algorithm, we must first generate a random starting state which
 * places a queen in a random row of each column. From there, we first check to
 * see if the state is a goal state (no queens are in conflict). If not, we
 * evaluate all of the possible neighbor states by moving each column’s queen
 * through the rows of its column and generating a heuristic value for each of
 * those states. When all of the neighbor states have been generated, we check
 * to see if any states were generated that have a lower heuristic value than
 * the current state. If a better state was not found, then we have reached the
 * local minima and must perform a random restart. If a better (lower heuristic)
 * state was found, then that state becomes the current state and the above
 * process is repeated on that state.
 *
 *
 *
 * @author Jacob Woodhouse
 * @version 09/25/2020
 */
public class Board {

    private int heuristic = 0;
    private int restarts = 0;
    private int moves = 0;
    private int neighbors = 8;
    private int[][] graph = new int[8][8];
    private int[][] mockGraph = new int[8][8];
    private int[][] saveGraph = new int[8][8];

    /**
     * This is the constructor, initializing the grid to all 0s.
     */
    public Board() {
        for (int x = 0; x < this.graph.length; x++) {
            for (int y = 0; y < this.graph.length; y++) {
                this.graph[x][y] = 0;
            }
        }
    }

    /**
     * This method places a queen into each column as a 1
     */
    public void placeQueens() {
        Random rng = new Random();
        for (int y = 0; y < this.graph.length; y++) {
            int num = rng.nextInt(7);
            this.graph[num][y] = 1;
        }
        heuristic = heuristic(this.graph);
    }

    /**
     * This checks for a conflict in each row.
     *
     * @param mock this is a mock version of the real graph
     * @param y represents the column
     * @return
     */
    public boolean checkRow(int[][] mock, int y) {
        boolean conflict = false;
        int count = 0;

        for (int x = 0; x < this.graph.length; x++) {
            if (mock[x][y] == 1) {
                count++;
            }
        }
        if (count > 1) {
            conflict = true;
        }
        return conflict;
    }

    /**
     * This method checks to see if the column has a conflict.
     *
     * @param mock this is a mock version of the real graph
     * @param x
     * @return
     */
    public boolean checkCol(int[][] mock, int x) {
        boolean conflict = false;
        int count = 0;
        for (int y = 0; y < this.graph.length; y++) {
            if (mock[x][y] == 1) {
                count++;
            }
        }
        if (count > 1) {
            conflict = true;
        }
        return conflict;
    }

    /**
     * This method checks to see if any of the diagonals have a conflict.
     *
     * @param mock this is a mock version of the real graph
     * @param x
     * @param y
     * @return
     */
    public boolean checkDia(int[][] mock, int x, int y) {
        boolean conflict = false;

        for (int i = 1; i < this.graph.length; i++) {

            if ((x + i < this.graph.length) && (y + i < this.graph.length)) {
                if (mock[x + i][y + i] == 1) {
                    conflict = true;
                }
            }
            if ((x + i < 8) && (y - i >= 0)) {
                if (mock[x + i][y - i] == 1) {
                    conflict = true;
                }
            }
            if ((x - i >= 0) && (y - i >= 0)) {
                if (mock[x - i][y - i] == 1) {
                    conflict = true;
                }
            }
            if ((x - i >= 0) && (y + i < 8)) {
                if (mock[x - i][y + i] == 1) {
                    conflict = true;
                }
            }
        }
        return conflict;
    }

    /**
     * This method sorts through each queen and counts all of the conflicts.
     *
     * @param test
     * @return
     */
    public int heuristic(int[][] test) {
        int conflicts = 0;
        boolean row;
        boolean col;
        boolean dia;

        for (int x = 0; x < this.graph.length; x++) {
            for (int y = 0; y < 8; y++) {
                if (test[x][y] == 1) {
                    row = checkRow(test, y);
                    col = checkCol(test, x);
                    dia = checkDia(test, x, y);

                    if (row || col || dia) {
                        conflicts++;
                    }
                }
            }
        }
        return conflicts;
    }

    /**
     * This is a basic function that copies an array. I thought I was going to
     * use it more.
     */
    public void copyArray() {
        for (int x = 0; x < this.graph.length; x++) {
            System.arraycopy(this.graph[x], 0, this.mockGraph[x], 0, 8);
        }
    }

    /**
     * This is the start method. Most of the program is run through here. The
     * method places the queens, searches each row and column, resets the board,
     * prints values, and determines the success.
     */
    public void start() {
        int colCount;
        int minCol;
        int minRow;
        int prevColQueen = 0;
        boolean success = false;
        placeQueens();

        while (success == false) {
            colCount = 0;

            copyArray(); //array copy
            movement(); //moving pieces
            boolean reset = false;
            reset = refresh(this.saveGraph);
            resetGraph(reset);

            minCol = searchCol(this.saveGraph); //searching rows and columns
            minRow = searchRow(this.saveGraph);
            for (int i = 0; i < this.graph.length; i++) {
                this.graph[i][minCol] = 0;
            }

            this.graph[minRow][minCol] = 1;
            this.moves++; //counting moves
            this.heuristic = heuristic(this.graph);

            if (heuristic(this.graph) == 0) { //finding the solution
                System.out.println("\nCurrent State");
                printGraph();
                System.out.println("Solution Found!");
                System.out.println("State changes: " + this.moves);
                System.out.println("Restarts: " + this.restarts);
                success = true;
            } else { //random restarts
                System.out.println("\n");
                System.out.println("Current h: " + this.heuristic);
                System.out.println("Current State");
                printGraph();
                System.out.println("Neighbors found with lower h: " + this.neighbors);
                System.out.println("Setting new current State");
            }

        }
    }

    /**
     * This finds the minimum neighbor state in the column.
     *
     * @param test
     * @return
     */
    public int searchCol(int[][] test) {
        int minCol = this.graph.length;
        int minVal = this.graph.length;
        int count = 0;

        for (int x = 0; x < this.graph.length; x++) {
            for (int y = 0; y < this.graph.length; y++) {
                if (test[x][y] < minVal) {
                    minVal = test[x][y];
                    minCol = y;
                }
                if (test[x][y] < this.heuristic) {
                    count++;
                }
            }
        }
        this.neighbors = count;
        return minCol;
    }

    /**
     * This method finds the minimum neighbor state in the row
     *
     * @param mock this is a mock version of the real graph
     * @return
     */
    public int searchRow(int[][] mock) {
        int minRow = this.graph.length;
        int minVal = this.graph.length;

        for (int x = 0; x < this.graph.length; x++) {
            for (int y = 0; y < this.graph.length; y++) {
                if (mock[x][y] < minVal) {
                    minVal = mock[x][y];
                    minRow = x;
                }
            }
        }
        return minRow;
    }

    /**
     * This method determines whether or not to restart the board
     *
     * @param mock this is a mock version of the real graph
     * @return
     */
    /**
     * This is a method to print the graph
     */
    public void printGraph() {
        for (int x = 0; x < this.graph.length; x++) {
            for (int y = 0; y < this.graph.length; y++) {
                System.out.print(this.graph[x][y] + " ");
            }
            System.out.println("");
        }
    }

    public boolean refresh(int[][] mock) {
        int min = 8;
        boolean restart = false;

        for (int x = 0; x < this.graph.length; x++) {
            for (int y = 0; y < this.graph.length; y++) {
                if (mock[x][y] < min) {
                    min = mock[x][y];
                }
            }
        }
        if (this.neighbors == 0) {
            restart = true;
        }
        return restart;
    }

    /**
     * If the board needs to restart, this method does the job and resets each
     * queen.
     *
     * @param refresh
     */
    public void resetGraph(boolean refresh) {
        if (refresh == true) {
            for (int x = 0; x < this.graph.length; x++) {
                for (int y = 0; y < this.graph.length; y++) {
                    this.graph[x][y] = 0;
                }
            }
            placeQueens();
            System.out.println("RESTART!!!!");
            this.restarts++;
        }
    }

    /**
     * This method moves through each column
     */
    public void movement() {
        int queen = 0;
        for (int col = 0; col < this.graph.length; col++) {
            for (int x = 0; x < this.graph.length; x++) {
                this.mockGraph[x][col] = 0;
            }
            for (int x = 0; x < this.graph.length; x++) {
                if (this.graph[x][col] == 1) {
                    queen = x;
                }
                this.mockGraph[x][col] = 1;
                this.saveGraph[x][col] = heuristic(this.mockGraph);
                this.mockGraph[x][col] = 0;
            }
            this.mockGraph[queen][col] = 1;

        }
    }
}
