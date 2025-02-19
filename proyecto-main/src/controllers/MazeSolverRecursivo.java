package controllers;

import controllers.interfaces.MazeSolver;
import java.util.ArrayList;
import java.util.List;
import models.Maze;

public class MazeSolverRecursivo implements MazeSolver {

    private int exploredPaths = 0;  // Contador de rutas exploradas
    private long startTime, endTime;

    @Override
    public List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        startTimer();  // Iniciar el temporizador
        List<int[]> path = new ArrayList<>();
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()];

        if (recursivo(maze, startRow, startCol, endRow, endCol, path, visited)) {
            stopTimer();  // Detener el temporizador
            return path;
        }

        stopTimer();  // Detener el temporizador en caso de no encontrar el camino
        return null;  // Si no se encuentra un camino
    }

    private boolean recursivo(Maze maze, int row, int col, int endRow, int endCol, List<int[]> path, boolean[][] visited) {
        if (row < 0 || col < 0 || row >= maze.getRows() || col >= maze.getCols() || 
            maze.getValue(row, col) == 1 || visited[row][col]) {
            return false;
        }

        visited[row][col] = true;
        path.add(new int[]{row, col});
        exploredPaths++;  // Aumentar el contador de rutas exploradas

        if (row == endRow && col == endCol) {
            return true;
        }

        // Intentar explorar las 4 direcciones
        if (recursivo(maze, row + 1, col, endRow, endCol, path, visited) ||
            recursivo(maze, row - 1, col, endRow, endCol, path, visited) ||
            recursivo(maze, row, col + 1, endRow, endCol, path, visited) ||
            recursivo(maze, row, col - 1, endRow, endCol, path, visited)) {
            return true;
        }

        // Si no se encuentra la solución, eliminar el último paso
        path.remove(path.size() - 1);
        return false;
    }

    @Override
    public int getExploredPaths() {
        return exploredPaths;  // Devuelve el número de rutas exploradas
    }

    @Override
    public long getExecutionTime() {
        return endTime - startTime;  // Devuelve el tiempo de ejecución
    }

    // Métodos para medir el tiempo de ejecución
    private void startTimer() {
        startTime = System.nanoTime();
    }

    private void stopTimer() {
        endTime = System.nanoTime();
    }
}
