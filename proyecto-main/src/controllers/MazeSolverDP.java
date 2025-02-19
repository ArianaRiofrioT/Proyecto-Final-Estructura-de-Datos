package controllers;

import controllers.interfaces.MazeSolver;
import java.util.ArrayList;
import java.util.List;
import models.Maze;

public class MazeSolverDP implements MazeSolver {

    private int exploredPaths = 0;  // Variable que cuenta las rutas exploradas
    private long startTime, endTime;  // Variables para medir el tiempo de ejecución

    @Override
    public List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        startTimer();  // Inicia el temporizador

        int rows = maze.getRows();  // Obtiene el número de filas del laberinto
        int cols = maze.getCols();  // Obtiene el número de columnas del laberinto
        int[][] memo = new int[rows][cols];  // Matriz que almacena los resultados parciales (memoización)
        boolean[][] visited = new boolean[rows][cols];  // Matriz para marcar las celdas visitadas

        // Inicializa la matriz memo con -1 (indica que no se ha resuelto)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                memo[i][j] = -1;  // -1 significa que la celda no ha sido visitada ni procesada
            }
        }

        List<int[]> path = new ArrayList<>();  // Lista que almacena las coordenadas de cada celda en el camino

        if (dp(maze, startRow, startCol, endRow, endCol, memo, visited, path)) {
            stopTimer();  // Detiene el temporizador si se encuentra una solución
            return path;  // Devuelve el camino encontrado
        }
        stopTimer();  // Detiene el temporizador si no se encuentra solución
        return null;  // Si no hay solución, devuelve null
    }

    // Método dp que utiliza programación dinámica para resolver el laberinto
    private boolean dp(Maze maze, int row, int col, int endRow, int endCol, int[][] memo, boolean[][] visited, List<int[]> path) {
        // Verifica si la celda es válida (dentro de límites, no es un muro, no ha sido visitada)
        if (row < 0 || col < 0 || row >= maze.getRows() || col >= maze.getCols() || 
            maze.getValue(row, col) == 1 || visited[row][col]) {  // 1 significa muro
            return false;  // Si la celda es inválida, retorna falso
        }

        visited[row][col] = true;  // Marca la celda como visitada
        path.add(new int[]{row, col});  // Agrega la celda al camino (path)
        exploredPaths++;  // Incrementa el contador de rutas exploradas

        // Si hemos llegado al destino
        if (row == endRow && col == endCol) {
            return true;  // Si llegamos al destino, retornamos true
        }

        // Si ya se ha resuelto esta celda, no la volvemos a explorar
        if (memo[row][col] != -1) {
            return memo[row][col] == 1;  // Retorna el valor almacenado en memo (1 si es alcanzable, 0 si no)
        }

        // Intenta explorar las 4 direcciones posibles (abajo, arriba, derecha, izquierda)
        boolean found = dp(maze, row + 1, col, endRow, endCol, memo, visited, path) ||  // Abajo
                        dp(maze, row - 1, col, endRow, endCol, memo, visited, path) ||  // Arriba
                        dp(maze, row, col + 1, endRow, endCol, memo, visited, path) ||  // Derecha
                        dp(maze, row, col - 1, endRow, endCol, memo, visited, path);    // Izquierda

        // Si no encontramos un camino, deshacemos el paso (backtracking)
        if (!found) {
            path.remove(path.size() - 1);  // Elimina la última celda añadida al camino (backtracking)
        }

        memo[row][col] = found ? 1 : 0;  // Almacena el resultado en memo (1 si alcanzable, 0 si no)
        return found;  // Retorna si se encontró un camino desde esta celda
    }

    @Override
    public int getExploredPaths() {
        return exploredPaths;  // Devuelve el número de rutas exploradas
    }

    @Override
    public long getExecutionTime() {
        return endTime - startTime;  // Devuelve el tiempo de ejecución en nanosegundos
    }

    // Métodos para medir el tiempo de ejecución
    private void startTimer() {
        startTime = System.nanoTime();  // Registra el tiempo de inicio
    }

    private void stopTimer() {
        endTime = System.nanoTime();  // Registra el tiempo de fin
    }
}
