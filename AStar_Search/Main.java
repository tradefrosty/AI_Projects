/**
 * @author Jacob Woodhouse
 * @version 10/29/2020
 */
import java.util.Scanner;
import java.util.PriorityQueue;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        Node map[][];
        
        boolean play = true;

        while (play) {
            Board board = new Board();
            board.createBoard();
            System.out.println("Randomly Generated Board. Select your start and goal positions.");
            System.out.println(board.toString());

            System.out.println("Starting Row: ");
            int startRow = scan.nextInt();
            System.out.println("Starting Column: ");
            int startColumn = scan.nextInt();

            System.out.println("Goal Row: ");
            int goalRow = scan.nextInt();
            System.out.println("Goal Column: ");
            int goalColumn = scan.nextInt();
            System.out.println(board.toString());

            board.setGoal(goalRow, goalColumn);
            board.setStart(startRow, startColumn);

            Node checkGoal = board.getBoard()[goalRow - 1][goalColumn - 1];
            Node checkStart = board.getBoard()[startRow - 1][startColumn - 1];

            if (board.checkPosition(checkGoal) && board.checkPosition(checkStart)) {
                AStar genPath = new AStar(board);
                genPath.start();
                System.out.println("\n" + genPath.toString());
                System.out.println("Path has been generated");
                System.out.println("Do you want to continue? y or n");
                char decision = scan.next().charAt(0);
                if (decision == 'n'){
                    play = false;
                }else{
                    continue;
                }
            } else {
                System.out.println("Restart game and input a valid position");
                play = false;
            }

            System.out.println("Do you want to continue? y or n");
        }

    }
}
