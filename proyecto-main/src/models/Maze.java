package models;

public class Maze {
    private int[][] grid;  // 'grid' es una matriz bidimensional que representa el laberinto. Cada celda es un número entero (0 o 1), donde:
                           // 0 representa una celda libre y 1 representa una pared.

    // Constructor de la clase Maze
    // Recibe el número de filas (rows) y columnas (cols) y crea una nueva matriz de enteros para representar el laberinto.
    public Maze(int rows, int cols) {
        grid = new int[rows][cols];  // Inicializa la matriz 'grid' con el tamaño especificado (rows x cols).
    }

    // Método para obtener el número de filas en el laberinto
    public int getRows() { 
        return grid.length;  // 'grid.length' devuelve la cantidad de filas en la matriz 'grid'.
    }

    // Método para obtener el número de columnas en el laberinto
    public int getCols() { 
        return grid[0].length;  // 'grid[0].length' devuelve la cantidad de columnas de la primera fila (todas las filas tienen la misma cantidad de columnas).
    }

    // Método para obtener el valor de una celda específica en el laberinto
    // Los valores pueden ser 0 (celda libre) o 1 (pared).
    public int getValue(int row, int col) { 
        return grid[row][col];  // Devuelve el valor de la celda ubicada en la fila 'row' y la columna 'col'.
    }

    // Método para establecer un valor en una celda específica en el laberinto
    public void setValue(int row, int col, int value) { 
        grid[row][col] = value;  // Establece el valor de la celda ubicada en 'row' y 'col' al valor proporcionado ('value').
    }

    // Método para obtener la matriz completa del laberinto
    public int[][] getGrid() { 
        return grid;  // Devuelve la matriz completa 'grid' que representa el laberinto.
    }
}
