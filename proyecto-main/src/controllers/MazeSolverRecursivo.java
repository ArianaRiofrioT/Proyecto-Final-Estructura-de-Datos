package controllers;

import controllers.interfaces.MazeSolver;
import java.util.ArrayList;
import java.util.List;
import models.Maze;

public class MazeSolverRecursivo implements MazeSolver {

    private int exploredPaths = 0;  // Contador de rutas exploradas, cada vez que se explora un camino se incrementa.
    private long startTime, endTime;  // Variables para medir el tiempo de ejecución del algoritmo.

    @Override
    public List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        startTimer();  // Inicia el temporizador para medir el tiempo de ejecución

        // 'path' es una lista que almacenará las celdas visitadas en el recorrido del laberinto
        List<int[]> path = new ArrayList<>();

        // 'visited' es una matriz booleana que marca las celdas que ya han sido visitadas.
        // Si una celda ya fue visitada, no se vuelve a explorar para evitar ciclos.
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()];

        // Llamamos al método recursivo que intentará encontrar el camino desde (startRow, startCol) hasta (endRow, endCol)
        if (recursivo(maze, startRow, startCol, endRow, endCol, path, visited)) {
            stopTimer();  // Si encontramos una solución, detenemos el temporizador
            return path;  // Devuelve el camino encontrado
        }

        stopTimer();  // Si no se encuentra el camino, también detenemos el temporizador
        return null;  // Devuelve null si no se pudo encontrar un camino
    }

    // Método recursivo que explora el laberinto
    private boolean recursivo(Maze maze, int row, int col, int endRow, int endCol, List<int[]> path, boolean[][] visited) {
        // Verificamos si la celda actual es válida:
        // - Está dentro de los límites del laberinto
        // - No es una pared (los muros están representados con '1')
        // - No ha sido visitada previamente
        if (row < 0 || col < 0 || row >= maze.getRows() || col >= maze.getCols() || 
            maze.getValue(row, col) == 1 || visited[row][col]) {
            return false;  // Si la celda es inválida, retornamos false
        }

        // Marcamos la celda como visitada para no volver a visitarla
        visited[row][col] = true;

        // Añadimos la celda actual al 'path' para registrar el camino recorrido
        path.add(new int[]{row, col});
        exploredPaths++;  // Incrementamos el contador de rutas exploradas

        // Si hemos llegado al destino, retornamos true
        if (row == endRow && col == endCol) {
            return true;
        }

        // Intentamos explorar las 4 direcciones posibles: abajo, arriba, derecha, izquierda
        // Llamamos recursivamente a cada una de las direcciones
        if (recursivo(maze, row + 1, col, endRow, endCol, path, visited) ||  // Abajo
            recursivo(maze, row - 1, col, endRow, endCol, path, visited) ||  // Arriba
            recursivo(maze, row, col + 1, endRow, endCol, path, visited) ||  // Derecha
            recursivo(maze, row, col - 1, endRow, endCol, path, visited)) {   // Izquierda
            return true;  // Si alguna dirección retorna true, significa que encontramos el camino
        }

        // Si no se encuentra una solución, deshacemos el último paso (backtracking)
        path.remove(path.size() - 1);  // Elimina la última celda añadida al 'path'
        return false;  // Retorna false si no encontramos una solución en las 4 direcciones
    }

    @Override
    public int getExploredPaths() {
        return exploredPaths;  // Devuelve el número de rutas exploradas
    }

    @Override
    public long getExecutionTime() {
        return endTime - startTime;  // Devuelve el tiempo de ejecución en nanosegundos
    }

    // Métodos para medir el tiempo de ejecución del algoritmo
    private void startTimer() {
        startTime = System.nanoTime();  // Registra el tiempo de inicio
    }

    private void stopTimer() {
        endTime = System.nanoTime();  // Registra el tiempo de fin
    }
}
