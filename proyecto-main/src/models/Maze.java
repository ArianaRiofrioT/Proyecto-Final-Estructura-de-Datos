package models;

public class Maze {
    private int[][] grid;

    public Maze(int rows, int cols) {
        grid = new int[rows][cols];
    }

    public int getRows() { return grid.length; }
    public int getCols() { return grid[0].length; }
    public int getValue(int row, int col) { return grid[row][col]; }
    public void setValue(int row, int col, int value) { grid[row][col] = value; }
    public int[][] getGrid() { return grid; }
}