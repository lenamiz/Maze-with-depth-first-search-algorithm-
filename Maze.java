package maze;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;
import java.util.Random;

public class Maze extends JPanel {

    private static int w = 20; //scaling factor
    private static int mazeSize = 800;
    private static int rows = mazeSize / (2 * w);
    private static int cols = mazeSize / (2 * w);

    private Cell grid[][] = new Cell[rows][cols];       // maze grid
    private boolean cellsVisited[][] = new boolean[rows][cols];

    private Cell entranceCell;
    private Cell exitCell;

    private Stack stack;
    private Stack searchStack;

    public Maze() {
        super();
        this.stack = new Stack();
    }

    /*random number generator*/
    private int getRandomNumber(int bound) {
        Random rnd = new Random();
        int random = rnd.nextInt(bound + 1);    //number from 0 to bound
        return random;
    }

    // ////////////////////////////////////// CREATING THE MAZE //////////////////////////////////////////////////////

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        createMaze(g, getRandomNumber(cols - 1), getRandomNumber(rows - 1));            //choosing a starting point
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.black);
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                Cell cell = new Cell(i, j);
                int x = i * w;
                int y = j * w;
                grid[i][j] = cell;

                if (cell.getWall(0)) {
                    g.drawLine(x, y, x + w, y);
                }
                if (cell.getWall(1)) {
                    g.drawLine(x + w, y, x + w, y + w);
                }
                if (cell.getWall(2)) {
                    g.drawLine(x + w, y + w, x, y + w);
                }
                if (cell.getWall(3)) {
                    g.drawLine(x, y, x, y + w);
                }
            }
        }
    }

    /*going through the maze, starting from the cell with coordinates (a,b) */
    private void createMaze(Graphics g, int a, int b) {
        Cell nextCell;
        Cell initCell;
        Cell currentCell;
        int i = a;
        int j = b;
        initCell = grid[i][j];
        markCellAsVisited(g, i, j);

        stack.push(initCell);
        while (!(stack.isEmpty())) {
            currentCell = (Cell) stack.pop();
            if (doesCellHaveUnvisitedNeighbours(currentCell)) {
                stack.push(currentCell);
                nextCell = chooseNextCell(currentCell);
                if (nextCell != null) {
                    removeWall(g, currentCell, nextCell);
                    markCellAsVisited(g, nextCell.getI(), nextCell.getJ());
                    stack.push(nextCell);
                }
            }
        }
        createExits(g);
    }

    private void markCellAsVisited(Graphics g, int i, int j) {
        int x = (i * w) + 1;
        int y = (j * w) + 1;
        cellsVisited[i][j] = true;
    }

    private boolean doesCellHaveUnvisitedNeighbours(Cell cell) {
        int i = cell.getI();
        int j = cell.getJ();
        boolean hasNeighbours[] = new boolean[4];
        boolean hasUnvisitedNeighbours = false;

        /*neighbour up*/
        if ((j - 1) > 0 && (!cellsVisited[i][j - 1])) {
            hasNeighbours[0] = true;
        }
        /*neighbour to the right*/
        if ((i + 1) < cols && !cellsVisited[i + 1][j]) {
            hasNeighbours[1] = true;
        }
        /*neighbour down*/
        if ((j + 1) < rows && !cellsVisited[i][j + 1]) {
            hasNeighbours[2] = true;
        }
        /*neighbour to the left*/
        if ((i - 1) > 0 && (!cellsVisited[i - 1][j])) {
            hasNeighbours[3] = true;
        }

        /*check for any neighbour*/
        for (int k = 0; k < hasNeighbours.length; k++) {
            if (hasNeighbours[k]) {
                hasUnvisitedNeighbours = true;
            }
        }
        return hasUnvisitedNeighbours;
    }

    private Cell chooseNextCell(Cell cell) {
        Cell nextCell = cell;
        int i = cell.getI();
        int j = cell.getJ();

        int random = getRandomNumber(3);
        switch (random) {
            case 0: //going up
                j = j - 1;
                break;
            case 1: //going right
                i = i + 1;
                break;
            case 2: //going down
                j = j + 1;
                break;
            case 3: //going left
                i = i - 1;
                break;
            default:
                break;
        }

        if (i >= 0 && i < cols && j >= 0 && j < rows) {
            nextCell = grid[i][j];
        }
        return nextCell;
    }

    private void createExits(Graphics g) {
        g.setColor(Color.lightGray);

        /*creating entrance in the northern wall of the maze*/

        this.entranceCell = grid[getRandomNumber(cols - 1)][0];
        int entrX = this.entranceCell.getI() * w;
        int entrY = this.entranceCell.getJ() * w;
        g.drawLine(entrX, entrY, entrX + w, entrY);
        this.entranceCell.wallRemoved(0);

        /*creating exit in the eastern wall of the maze*/

        this.exitCell = grid[cols - 1][getRandomNumber(rows - 1)];
        int exitX = this.exitCell.getI() * w;
        int exitY = this.exitCell.getJ() * w;
        g.drawLine(exitX + w, exitY, exitX + w, exitY + w);
        this.exitCell.wallRemoved(1);
    }

    private void removeWall(Graphics g, Cell currentCell, Cell nextCell) {
        g.setColor(Color.lightGray);
        int i = currentCell.getI();
        int j = currentCell.getJ();

        /*wall's coordinates*/
        int currentX = i * w;
        int currentY = j * w;

        int random = getRandomNumber(3);
        switch (random) {
            case 0: //removing northern wall of the current cell
                if (j == 0) {
                    break;
                }
                g.drawLine(currentX, currentY, currentX + w, currentY);
                currentCell.wallRemoved(0);
                nextCell.wallRemoved(2);
                break;
            case 1: //removing eastern wall of the current cell
                if (i == cols - 1) {
                    break;
                }
                g.drawLine(currentX + w, currentY, currentX + w, currentY + w);
                currentCell.wallRemoved(1);
                nextCell.wallRemoved(3);
                break;
            case 2: //removing southern wall of the current cell
                if (j == rows - 1) {
                    break;
                }
                g.drawLine(currentX + w, currentY + w, currentX, currentY + w);
                currentCell.wallRemoved(2);
                nextCell.wallRemoved(0);
                break;
            case 3: //removing western wall of the current cell
                if (i == 0) {
                    break;
                }
                g.drawLine(currentX, currentY, currentX, currentY + w);
                currentCell.wallRemoved(3);
                nextCell.wallRemoved(1);
                break;
            default:
                break;
        }
    }



// //////////////////////////////////// DISPLAYING THE MAZE ///////////////////////////////////////////////////////

    private static void display(){
        Maze mainPanel = new Maze();

        JFrame jf = new JFrame("The Maze");

        jf.setSize(mazeSize,mazeSize);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().add(mainPanel);
        jf.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                display();
            }
        });
    }
}