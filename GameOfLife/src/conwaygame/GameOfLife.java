package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        //Reading files
        StdIn.setFile(file);

        //Reading number of rows and columns
        int r = StdIn.readInt();
        int c = StdIn.readInt();

        //Allocating space for 2D array
        grid = new boolean[r][c];
        
        //Nested for loop fills in values of true or false based on input file
        for(int row = 0; row < grid.length; row++){
            for(int col = 0; col < grid[0].length; col++){
                grid[row][col] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        //Returns true if the value at row and column variable is true
        if(grid[row][col]){
            return ALIVE;
        }

        //Default return false if the grid value is not true
        return DEAD; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        //Nested for loop iterating through 2D array to find if any cells are alive
        for(int c = 0; c < grid.length; c++){
            for(int r = 0; r < grid[0].length; r++){
                //Calls the getCellState method to confirm status of current grid value
                if(getCellState(r, c) == true){
                    return true;
                }
            }
        }

        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors(int row, int col) {

        // Creates a counting variable to hold the possible living cells nearby
        int alive = 0;
    
        // Nest for loops checking a 3x3 area, excluding the location of the cell, and accounting for wrapping neighbors
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                // Skip the center cell
                if (i != row || j != col) {  // Use || instead of &&
                    // Calculate the wrapped indices for neighbors
                    int newRow = (i + grid.length) % grid.length;
                    int newCol = (j + grid[0].length) % grid[0].length;
    
                    // Check if the current cell is alive
                    if (grid[newRow][newCol]) {
                        alive++;
                    }
                }
            }
        }
    
        return alive;
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        //Creates a 2D array with the same dimensions as the one created by the file, prepopulated with false
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];

        //Nested for loops to verify and enforce game rules
        for(int c = 0; c < grid.length; c++){
            for(int r = 0; r < grid[0].length; r++){

                //Overwrite default false values in the new grid with corresponding values from grid
                newGrid[r][c] = grid[r][c];

                //If a cell has one or less neighbor, the cell dies
                if(numOfAliveNeighbors(r, c) <= 1){
                    newGrid[r][c] = DEAD;
                }

                //If there are exactly three living cells nearby and the current grid value is false, grid value set to true
                if(numOfAliveNeighbors(r,c) == 3 && grid[r][c] == DEAD){
                    newGrid[r][c] = ALIVE;
                }

                //If four or more neighbors are nearby, the current grid value is set to false
                if(numOfAliveNeighbors(r,c) >= 4){
                    newGrid[r][c] = DEAD;
                }
            }
        }

        //Returns new grid with rules applied
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        //Updates the grid array with a new grid of the next generation after rules are applied
        grid = computeNewGrid();

        //Counter variable to hold total number of alive cells in the new generation
        totalAliveCells = 0;

        //Nest for loops to iterate through the grid variable
        for(int r = 0; r < grid.length; r++){
            for(int c = 0; c < grid[0].length; c++){
                //Calls the getCellState method to confirm status of current grid value, counting the number of alive cells
                if(getCellState(r, c) == true){
                    //Updates the number of alive cells
                    totalAliveCells += 1;
                }
            }
        }

    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // Runs the nextGeneration method n number of times, updating the grid and the total number of alive cells along the way
        for(int i = 0; i < n; i++){
            nextGeneration();
        }

    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {
        // Initialize the count of communities
        int numOfCommunities = 0;
    
        // Create a Union-Find data structure to track connected components
        WeightedQuickUnionUF connectedGrid = new WeightedQuickUnionUF(grid.length, grid[0].length);
    
        // Step 1: Union adjacent alive cells
        for (int i = 0; i < grid.length; i++) {
            for (int g = 0; g < grid[i].length; g++) {
                // Check if the current cell is alive
                if (grid[i][g] == ALIVE) {
                    // Explore neighbors of the alive cell
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = g - 1; y <= g + 1; y++) {
                            // Check if the neighbor is a valid, alive cell
                            if (x >= 0 && x < grid.length && y >= 0 && y < grid[x].length && grid[x][y] == ALIVE) {
                                // Union the alive cell with its alive neighbors
                                connectedGrid.union(i, g, x, y);
                            }
                        }
                    }
                }
            }
        }
    
        // Array to keep track of unique root nodes in the connected grid
        boolean[] newRoots = new boolean[grid.length * grid[0].length];
    
        // Step 2: Iterate through the grid again to count the number of distinct communities
        for (int s = 0; s < grid.length; s++) {
            for (int k = 0; k < grid[s].length; k++) {
                // Check if the current cell is alive
                if (grid[s][k] == ALIVE) {
                    // Find the root of the connected component for the alive cell
                    int newRoot = connectedGrid.find(s, k);
                    // If the root is not marked, mark it and increment the community count
                    if (!newRoots[newRoot]) {
                        newRoots[newRoot] = true;
                        numOfCommunities++;
                    }
                }
            }
        }
    
        // Return the total number of distinct connected communities
        return numOfCommunities;
    }
    

}
