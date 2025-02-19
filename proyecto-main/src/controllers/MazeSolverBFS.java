package controllers;

import controllers.interfaces.MazeSolver; // Importa la interfaz que define el contrato para los solucionadores de laberintos
import java.util.*; // Importa todas las clases de la biblioteca estándar de Java necesarias para estructuras de datos
import models.Maze; // Importa la clase Maze que representa el laberinto

public class MazeSolverBFS implements MazeSolver { // Clase que implementa la interfaz MazeSolver utilizando el algoritmo de búsqueda en anchura (BFS)
    private int exploredPaths = 0; // Contador del número de caminos explorados durante la búsqueda
    private long startTime, endTime; // Variables para medir el tiempo de ejecución del algoritmo

    @Override
    public List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        startTimer();  // Iniciar la medición del tiempo de ejecución
        List<int[]> path = new ArrayList<>(); // Lista para almacenar la ruta final desde el inicio hasta el final, si se encuentra
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()]; // Matriz booleana para registrar qué celdas han sido visitadas
        Queue<int[]> queue = new LinkedList<>(); // Cola para manejar los nodos en orden FIFO (propio del algoritmo BFS)
        Map<String, String> parentMap = new HashMap<>(); // Mapa que almacena la relación padre-hijo entre celdas para reconstruir la ruta

        queue.add(new int[]{startRow, startCol}); // Se encola la posición de inicio
        visited[startRow][startCol] = true; // Se marca la celda de inicio como visitada para evitar ciclos

        while (!queue.isEmpty()) { // Mientras haya nodos por explorar en la cola
            int[] current = queue.poll(); // Extraer el nodo en la parte frontal de la cola
            int r = current[0], c = current[1]; // Obtener las coordenadas de la celda actual

            if (r == endRow && c == endCol) { // Si llegamos a la celda de destino
                List<int[]> route = new ArrayList<>(); // Lista para almacenar el camino reconstruido desde el final hasta el inicio
                String key = r + "," + c; // Clave que representa la celda actual en el mapa de padres
                while (parentMap.containsKey(key)) { // Mientras la celda tenga un padre registrado en el mapa
                    route.add(0, new int[]{r, c}); // Insertar la celda al inicio de la lista (para obtener el orden correcto)
                    String parent = parentMap.get(key); // Obtener la celda padre de la actual
                    String[] parts = parent.split(","); // Separar la clave en coordenadas
                    r = Integer.parseInt(parts[0]); // Extraer la fila del padre
                    c = Integer.parseInt(parts[1]); // Extraer la columna del padre
                    key = parent; // Actualizar la clave con la del padre
                }
                route.add(0, new int[]{startRow, startCol}); // Agregar la celda inicial al comienzo del camino
                stopTimer();  // Detener la medición del tiempo de ejecución
                return route; // Retornar el camino encontrado
            }

            // Definimos las cuatro direcciones posibles de movimiento: derecha, izquierda, abajo y arriba
            for (int[] dir : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
                int nr = r + dir[0], nc = c + dir[1]; // Calcular las nuevas coordenadas
                if (nr >= 0 && nr < maze.getRows() && nc >= 0 && nc < maze.getCols() && // Verificar que no se salga del rango del laberinto
                    !visited[nr][nc] && maze.getValue(nr, nc) == 0) { // Verificar que la celda no esté visitada y sea transitable
                    visited[nr][nc] = true; // Marcar la celda como visitada
                    queue.add(new int[]{nr, nc}); // Encolar la nueva celda para su exploración
                    parentMap.put(nr + "," + nc, r + "," + c); // Registrar la relación padre-hijo en el mapa de rutas
                }
            }
            exploredPaths++;  // Incrementar el contador de caminos explorados
        }

        stopTimer();  // Detener la medición del tiempo de ejecución si no se encontró solución
        return null;  // Retornar null si no se encontró un camino desde el inicio hasta el destino
    }

    @Override
    public int getExploredPaths() {
        return exploredPaths; // Retornar el número total de caminos explorados durante la ejecución del algoritmo
    }

    @Override
    public long getExecutionTime() {
        return endTime - startTime; // Calcular y retornar el tiempo total de ejecución en nanosegundos
    }

    private void startTimer() {
        startTime = System.nanoTime(); // Registrar el tiempo de inicio del algoritmo en nanosegundos
    }

    private void stopTimer() {
        endTime = System.nanoTime(); // Registrar el tiempo de finalización del algoritmo en nanosegundos
    }
}
