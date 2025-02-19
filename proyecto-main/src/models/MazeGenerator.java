package models;

import java.util.Random;  // Importa la clase Random para generar números aleatorios

public class MazeGenerator {

    // Método estático que genera un laberinto aleatorio
    public static Maze generateMaze(int rows, int cols) {
        Maze maze = new Maze(rows, cols);  // Crea un objeto 'Maze' con las dimensiones dadas (filas x columnas)
        Random rand = new Random();  // Crea un objeto Random para generar números aleatorios

        // Llenamos la matriz 'grid' del laberinto con valores aleatorios
        for (int i = 0; i < rows; i++) {  // Recorre las filas del laberinto
            for (int j = 0; j < cols; j++) {  // Recorre las columnas del laberinto
                // Asigna a cada celda un valor aleatorio: 0 (camino) o 1 (pared)
                maze.setValue(i, j, rand.nextInt(2));  // rand.nextInt(2) genera un número aleatorio entre 0 y 1
            }
        }

        return maze;  // Devuelve el laberinto generado
    }
}

