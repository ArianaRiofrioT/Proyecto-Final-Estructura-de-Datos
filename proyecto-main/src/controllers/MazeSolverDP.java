package controllers;

import controllers.interfaces.MazeSolver;
import java.util.ArrayList;
import java.util.List;
import models.Maze;

public class MazeSolverDP implements MazeSolver {
    private int exploredPaths = 0;
    private long startTime, endTime;

    @Override
    public List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        startTimer();  // Iniciar temporizador

        int rows = maze.getRows();
        int cols = maze.getCols();
        int[][] memo = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols]; // Para evitar ciclos

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                memo[i][j] = -1;
            }
        }

        List<int[]> path = new ArrayList<>();

        if (dp(maze, startRow, startCol, endRow, endCol, memo, visited, path)) {
            stopTimer();  // Detener temporizador
            return path;
        }
        stopTimer();  // Detener temporizador
        return null;  // Si no hay solución
    }

    private boolean dp(Maze maze, int row, int col, int endRow, int endCol, int[][] memo, boolean[][] visited, List<int[]> path) {
        // Comprobamos si la posición es válida
        if (row < 0 || col < 0 || row >= maze.getRows() || col >= maze.getCols() || 
            maze.getValue(row, col) == 1 || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;  // Marcamos como visitado
        path.add(new int[]{row, col});
        exploredPaths++;  // Aumentamos el contador de rutas exploradas

        // Si llegamos al destino
        if (row == endRow && col == endCol) {
            return true;
        }

        // Si ya hemos visitado esta celda, no continuar
        if (memo[row][col] != -1) {
            return memo[row][col] == 1;
        }

        // Intentamos explorar las cuatro direcciones
        boolean found = dp(maze, row + 1, col, endRow, endCol, memo, visited, path) ||
                        dp(maze, row - 1, col, endRow, endCol, memo, visited, path) ||
                        dp(maze, row, col + 1, endRow, endCol, memo, visited, path) ||
                        dp(maze, row, col - 1, endRow, endCol, memo, visited, path);

        // Si no encontramos la solución, deshacemos el paso
        if (!found) {
            path.remove(path.size() - 1);
        }

        memo[row][col] = found ? 1 : 0;
        return found;
    }

    @Override
    public int getExploredPaths() {
        return exploredPaths;  // Devuelve el número de rutas exploradas
    }

    @Override
    public long getExecutionTime() {
        return endTime - startTime;  // Devuelve el tiempo que tardó el algoritmo
    }

    // Métodos para medir el tiempo de ejecución
    private void startTimer() {
        startTime = System.nanoTime();
    }

    private void stopTimer() {
        endTime = System.nanoTime();
    }
}
