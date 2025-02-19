package models;

import java.util.Random;

public class MazeGenerator {

    public static Maze generateMaze(int rows, int cols) {
        Maze maze = new Maze(rows, cols);
        Random rand = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze.setValue(i, j, rand.nextInt(2)); // 0 camino, 1 pared
            }
        }

        return maze;
    }
}
