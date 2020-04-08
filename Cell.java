package maze;

public class Cell {
    private int i, j;
    private boolean walls[] = {true, true, true, true};   //walls of the cell in order from the top, clockwise

    protected Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    protected int getI() {     //returns X coordinate
        return this.i;
    }

    protected int getJ() {     //returns Y coordinate
        return this.j;
    }

    protected boolean getWall(int x) {     //returns true if cell has a wall in the x direction
        return this.walls[x];
    }

    protected void wallRemoved(int x){
        this.walls[x] = false;
    }

}