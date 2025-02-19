package controllers.interfaces;

import java.util.List;
import models.Maze;

public interface MazeSolver {
    List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol);

    // Métodos adicionales para obtener información sobre el proceso de resolución
    int getExploredPaths();  // Número de rutas exploradas
    long getExecutionTime();  // Tiempo de ejecución
}
