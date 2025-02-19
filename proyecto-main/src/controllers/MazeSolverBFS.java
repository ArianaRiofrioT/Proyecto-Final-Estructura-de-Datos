package controllers;

import controllers.interfaces.MazeSolver;
import java.util.*;
import models.Maze;

public class MazeSolverBFS implements MazeSolver {
    private int exploredPaths = 0;
    private long startTime, endTime;

    @Override
    public List<int[]> solve(Maze maze, int startRow, int startCol, int endRow, int endCol) {
        startTimer();  // Iniciar temporizador
        List<int[]> path = new ArrayList<>();
        boolean[][] visited = new boolean[maze.getRows()][maze.getCols()];
        Queue<int[]> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();

        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0], c = current[1];

            if (r == endRow && c == endCol) {
                // Si llegamos al destino, reconstruimos la ruta
                List<int[]> route = new ArrayList<>();
                String key = r + "," + c;
                while (parentMap.containsKey(key)) {
                    route.add(0, new int[]{r, c});
                    String parent = parentMap.get(key);
                    String[] parts = parent.split(",");
                    r = Integer.parseInt(parts[0]);
                    c = Integer.parseInt(parts[1]);
                    key = parent;
                }
                route.add(0, new int[]{startRow, startCol});
                stopTimer();  // Detener temporizador
                return route;
            }

            // Explorar vecinos
            for (int[] dir : new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}}) {
                int nr = r + dir[0], nc = c + dir[1];
                if (nr >= 0 && nr < maze.getRows() && nc >= 0 && nc < maze.getCols() &&
                    !visited[nr][nc] && maze.getValue(nr, nc) == 0) {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                    parentMap.put(nr + "," + nc, r + "," + c);
                }
            }
            exploredPaths++;  // Contabilizar el número de rutas exploradas
        }

        stopTimer();  // Detener temporizador
        return null;  // Si no hay solución
    }

    @Override
    public int getExploredPaths() {
        return exploredPaths;
    }

    @Override
    public long getExecutionTime() {
        return endTime - startTime;
    }

    private void startTimer() {
        startTime = System.nanoTime();
    }

    private void stopTimer() {
        endTime = System.nanoTime();
    }
}
