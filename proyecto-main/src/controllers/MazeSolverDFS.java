package controllers;

import controllers.interfaces.MazeSolver; // Importa la interfaz que define la solución de laberintos
import java.util.*; // Importa todas las clases de la biblioteca estándar necesarias para estructuras de datos
import models.Maze; // Importa la clase Maze que representa el laberinto

public class MazeSolverDFS implements MazeSolver { // Clase que implementa MazeSolver usando búsqueda en profundidad (DFS)
    private int exploredPaths = 0; // Contador para rastrear la cantidad de caminos explorados en la búsqueda // Contador del número de caminos explorados durante la búsqueda
    private long startTime, endTime; // Variables para almacenar el tiempo de inicio y fin de la ejecución // Variables para medir el tiempo de ejecución del algoritmo

    @Override // Indica que este método sobrescribe uno definido en la interfaz
    public List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol) { // Método público que implementa la solución del laberinto usando DFS
        startTimer();  // Iniciar la medición del tiempo de ejecución
        List<int[]> path = new ArrayList<>(); // Lista para almacenar la ruta final desde el inicio hasta el destino // Lista para almacenar la ruta final desde el inicio hasta el destino
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()]; // Matriz de booleanos para rastrear qué celdas ya fueron visitadas // Matriz para registrar las celdas visitadas
        Stack<int[]> stack = new Stack<>(); // Pila utilizada para gestionar la exploración de nodos en DFS // Pila para manejar los nodos (propio del algoritmo DFS)
        Map<String, String> parentMap = new HashMap<>(); // Mapa para rastrear el nodo padre de cada celda visitada // Mapa para rastrear la relación padre-hijo en la ruta

        stack.push(new int[]{startRow, startCol}); // Apilar la celda inicial
        visited[startRow][startCol] = true; // Marcar la celda de inicio como visitada

        while (!stack.isEmpty()) { // Mientras haya nodos en la pila
            int[] current = stack.pop(); // Extraer el nodo en la cima de la pila
            int r = current[0], c = current[1]; // Obtener las coordenadas actuales

            if (r == endRow && c == endCol) { // Si llegamos al destino
                List<int[]> route = new ArrayList<>(); // Lista para reconstruir el camino
                String key = r + "," + c; // Clave que representa la celda actual
                while (parentMap.containsKey(key)) { // Mientras la celda tenga un padre registrado
                    route.add(0, new int[]{r, c}); // Insertar la celda al inicio de la lista
                    String parent = parentMap.get(key); // Obtener la celda padre
                    String[] parts = parent.split(","); // Separar la clave en coordenadas
                    r = Integer.parseInt(parts[0]); // Extraer la fila del padre
                    c = Integer.parseInt(parts[1]); // Extraer la columna del padre
                    key = parent; // Actualizar la clave
                }
                route.add(0, new int[]{startRow, startCol}); // Agregar la celda inicial al camino
                stopTimer();  // Detener la medición del tiempo
                return route; // Retornar la ruta encontrada
            }

            // Definir las direcciones posibles de movimiento (derecha, izquierda, abajo, arriba)
            for (int[] dir : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
                int nr = r + dir[0], nc = c + dir[1]; // Calcular nuevas coordenadas
                if (nr >= 0 && nr < maze.getRows() && nc >= 0 && nc < maze.getCols() && // Verificar límites del laberinto
                    !visited[nr][nc] && maze.getValue(nr, nc) == 0) { // Verificar que no esté visitada y sea transitable
                    visited[nr][nc] = true; // Marcar la celda como visitada
                    stack.push(new int[]{nr, nc}); // Apilar la celda para su exploración
                    parentMap.put(nr + "," + nc, r + "," + c); // Registrar la relación padre-hijo
                }
            }
            exploredPaths++;  // Incrementar el contador de caminos explorados
        }

        stopTimer();  // Detener la medición del tiempo si no se encontró solución
        return null;  // Retornar null si no se encontró camino
    }

    @Override
    public int getExploredPaths() { // Método público que retorna el número total de caminos explorados
        return exploredPaths;
    }

    @Override
    public long getExecutionTime() { // Método público que retorna el tiempo total de ejecución en nanosegundos
        return endTime - startTime;
    }

    private void startTimer() { // Método privado que inicia el temporizador registrando el tiempo actual en nanosegundos // Método privado que registra el tiempo de inicio en nanosegundos
        startTime = System.nanoTime(); // Captura el tiempo de inicio de la ejecución en nanosegundos
    }

    private void stopTimer() { // Método privado que registra el tiempo de finalización en nanosegundos // Método privado que registra el tiempo de finalización en nanosegundos
        endTime = System.nanoTime(); // Captura el tiempo actual en nanosegundos para calcular la duración total
    }
}
