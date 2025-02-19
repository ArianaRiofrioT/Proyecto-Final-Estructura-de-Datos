package views; 

import controllers.*; // Importa todos los controladores necesarios
import controllers.interfaces.MazeSolver; // Importa la interfaz MazeSolver
import java.awt.*; // Importa las clases para trabajar con gráficos y componentes gráficos
import java.awt.event.MouseAdapter; // Para gestionar eventos de ratón
import java.awt.event.MouseEvent; // Para gestionar eventos de ratón
import java.util.List; // Importa la clase List
import javax.swing.*; // Importa clases para crear interfaces gráficas
import models.Maze; // Importa la clase Maze del paquete models

public class MazeGUI extends JFrame { // Clase principal de la interfaz gráfica que extiende JFrame
    private Maze maze; // Instancia del laberinto
    private JTextArea summaryArea; // Área de texto para mostrar los resultados y el resumen
    private JPanel mazePanel; // Panel donde se dibuja el laberinto
    private int startRow = -1, startCol = -1, endRow = -1, endCol = -1; // Coordenadas de inicio y fin del laberinto
    private List<int[]> path; // Ruta encontrada para resolver el laberinto
    private JLabel titleLabel; // Etiqueta de título
    private long startTime, endTime; // Tiempos para medir la duración de la resolución
    private int exploredPaths = 0; // Rutas exploradas durante la búsqueda
    private int bestPathSteps = Integer.MAX_VALUE; // Mejor cantidad de pasos encontrada en la solución

    public MazeGUI() { // Constructor principal de la clase
        setTitle("Laberinto"); // Título de la ventana
        setSize(900, 700); // Tamaño de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cerrar la aplicación cuando se cierre la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Título en cursiva
        titleLabel = new JLabel("LABERINTO"); // Crea el JLabel para el título
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 36)); // Establece la fuente y tamaño
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centra el texto horizontalmente
        titleLabel.setVerticalAlignment(SwingConstants.CENTER); // Centra el texto verticalmente
        titleLabel.setVisible(false); // Inicialmente oculto

        // Panel de botones
        JPanel inputPanel = new JPanel(); // Crea el panel para los botones
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Establece el layout de los botones
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Añade un borde vacío

        // Creación de los botones
        JButton createButton = new JButton("Crear Laberinto"); // Botón para crear laberinto
        JButton bfsButton = new JButton("BFS"); // Botón para resolver con BFS
        JButton dfsButton = new JButton("DFS"); // Botón para resolver con DFS
        JButton dinamicaButton = new JButton("Dinamica Pro"); // Botón para resolver con dinámica
        JButton recursivoButton = new JButton("Recursivo Simple"); // Botón para resolver recursivamente
        JButton randomButton = new JButton("Crear Laberinto Aleatorio"); // Botón para crear un laberinto aleatorio
        JButton clearButton = new JButton("Limpiar Laberinto"); // Botón para limpiar el laberinto
        JButton directViewButton = new JButton("Ver Camino Directo"); // Botón para ver el camino directo

        // Estilo de los botones
        for (JButton button : new JButton[]{createButton, bfsButton, dfsButton, dinamicaButton, recursivoButton, randomButton, clearButton, directViewButton}) {
            button.setBackground(new Color(76, 175, 80)); // Color de fondo
            button.setForeground(Color.WHITE); // Color del texto
            button.setFocusPainted(false); // Desactiva el contorno cuando el botón está seleccionado
            button.setFont(new Font("Arial", Font.BOLD, 14)); // Establece la fuente
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Establece el cursor como mano cuando pasa por encima
            button.addMouseListener(new MouseAdapter() { // Añade un mouse listener para eventos de ratón
                @Override
                public void mouseEntered(MouseEvent e) { // Cambia el color cuando el ratón entra en el botón
                    button.setBackground(new Color(56, 142, 60));
                }

                @Override
                public void mouseExited(MouseEvent e) { // Vuelve al color original cuando el ratón sale
                    button.setBackground(new Color(76, 175, 80));
                }
            });
        }

        // Añade los botones al panel de entrada
        inputPanel.add(createButton);
        inputPanel.add(bfsButton);
        inputPanel.add(dfsButton);
        inputPanel.add(dinamicaButton);
        inputPanel.add(recursivoButton);
        inputPanel.add(randomButton);
        inputPanel.add(clearButton);
        inputPanel.add(directViewButton);

        // Área de resumen
        summaryArea = new JTextArea(5, 40); // Crea un área de texto de 5 filas y 40 columnas
        summaryArea.setEditable(false); // No permite editar el área de texto
        summaryArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Establece la fuente

        // Panel para el laberinto
        mazePanel = new JPanel() { // Panel personalizado para pintar el laberinto
            @Override
            protected void paintComponent(Graphics g) { // Sobrescribe el método paintComponent para dibujar el laberinto
                super.paintComponent(g); // Llama al método paintComponent de la clase base
                if (maze == null) return; // Si el laberinto es nulo, no hacer nada
                int cellSize = 30; // Tamaño de cada celda del laberinto
                int mazeWidth = maze.getCols() * cellSize; // Ancho total del laberinto
                int mazeHeight = maze.getRows() * cellSize; // Altura total del laberinto
                // Calcular las coordenadas de inicio para centrar el laberinto
                int startX = (getWidth() - mazeWidth) / 2;
                int startY = (getHeight() - mazeHeight) / 2;
                
                // Dibujar las celdas del laberinto
                for (int r = 0; r < maze.getRows(); r++) {
                    for (int c = 0; c < maze.getCols(); c++) {
                        int x = startX + c * cellSize; // Coordenada x para la celda
                        int y = startY + r * cellSize; // Coordenada y para la celda

                        // Si la celda es un muro (1)
                        if (maze.getValue(r, c) == 1) {
                            g.setColor(Color.BLACK); // Muro negro
                            g.fillRect(x, y, cellSize, cellSize); // Rellena la celda con color negro
                        } else if (r == startRow && c == startCol) { // Si es la celda de inicio
                            g.setColor(Color.GREEN); // Celda de inicio en verde
                            g.fillRect(x, y, cellSize, cellSize);
                        } else if (r == endRow && c == endCol) { // Si es la celda de fin
                            g.setColor(Color.RED); // Celda de fin en rojo
                            g.fillRect(x, y, cellSize, cellSize);
                        } else { // Celdas vacías
                            g.setColor(Color.WHITE); // Celdas blancas
                            g.fillRect(x, y, cellSize, cellSize);
                            g.setColor(Color.BLACK); // Borde negro
                            g.drawRect(x, y, cellSize, cellSize); // Dibuja el borde de la celda
                        }
                    }
                }

                // Mostrar la mejor ruta (si existe)
                if (path != null) {
                    g.setColor(Color.CYAN); // Color de la ruta (cian)
                    for (int[] p : path) {
                        int x = startX + p[1] * cellSize; // Coordenada x de la ruta
                        int y = startY + p[0] * cellSize; // Coordenada y de la ruta
                        g.fillRect(x, y, cellSize, cellSize); // Rellena las celdas de la ruta con cian
                    }
                }
            }
        };

        // Añadir mouse listener para la interacción con el panel del laberinto
        mazePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellSize = 30; // Tamaño de cada celda
                int mazeWidth = maze.getCols() * cellSize; // Ancho total del laberinto
                int mazeHeight = maze.getRows() * cellSize; // Altura total del laberinto
                int startX = (mazePanel.getWidth() - mazeWidth) / 2; // Coordenada x inicial
                int startY = (mazePanel.getHeight() - mazeHeight) / 2; // Coordenada y inicial

                int col = (e.getX() - startX) / cellSize; // Columna donde se hace clic
                int row = (e.getY() - startY) / cellSize; // Fila donde se hace clic

                // Verifica si el clic está dentro del laberinto
                if (maze == null || row < 0 || col < 0 || row >= maze.getRows() || col >= maze.getCols()) return;

                // Si el clic es con el botón izquierdo, cambia el valor de la celda entre 0 y 1 (muro o vacío)
                if (SwingUtilities.isLeftMouseButton(e)) {
                    maze.setValue(row, col, maze.getValue(row, col) == 0 ? 1 : 0);
                } else if (SwingUtilities.isRightMouseButton(e)) { // Si es clic derecho, selecciona inicio o fin
                    if (startRow == -1) {
                        startRow = row;
                        startCol = col;
                    } else if (endRow == -1) {
                        endRow = row;
                        endCol = col;
                    }
                }
                repaint(); // Vuelve a dibujar el panel
            }
        });

        // Asignar acciones a los botones
        createButton.addActionListener(e -> crearLaberinto()); // Acción para crear un laberinto manualmente
        randomButton.addActionListener(e -> crearLaberintoAleatorio()); // Acción para crear un laberinto aleatorio
        clearButton.addActionListener(e -> limpiarLaberinto()); // Acción para limpiar el laberinto
        directViewButton.addActionListener(e -> verCaminoDirecto()); // Acción para ver el camino directo

        bfsButton.addActionListener(e -> resolverLaberinto(new MazeSolverBFS())); // Acción para resolver con BFS
        dfsButton.addActionListener(e -> resolverLaberinto(new MazeSolverDFS())); // Acción para resolver con DFS
        dinamicaButton.addActionListener(e -> resolverLaberinto(new MazeSolverDP())); // Acción para resolver con Dinámica Pro
        recursivoButton.addActionListener(e -> resolverLaberinto(new MazeSolverRecursivo())); // Acción para resolver recursivamente

        // Usar BorderLayout para organizar los componentes
        JPanel mainPanel = new JPanel(); // Panel principal que contiene todos los componentes
        mainPanel.setLayout(new BorderLayout()); // Establece el layout como BorderLayout
        mainPanel.add(titleLabel, BorderLayout.CENTER); // Añade el título al centro
        mainPanel.add(inputPanel, BorderLayout.NORTH); // Añade el panel de botones al norte
        mainPanel.add(new JScrollPane(summaryArea), BorderLayout.SOUTH); // Añade el área de resumen al sur
        mainPanel.add(mazePanel, BorderLayout.CENTER); // Añade el panel del laberinto al centro

        add(mainPanel); // Añade el panel principal a la ventana
    }

    private void crearLaberinto() { // Método para crear un laberinto a partir de las filas y columnas ingresadas
        String rowsStr = JOptionPane.showInputDialog(this, "Ingrese el número de filas:"); // Pide el número de filas
        String colsStr = JOptionPane.showInputDialog(this, "Ingrese el número de columnas:"); // Pide el número de columnas
        try {
            int rows = Integer.parseInt(rowsStr); // Convierte las filas a entero
            int cols = Integer.parseInt(colsStr); // Convierte las columnas a entero
            maze = new Maze(rows, cols); // Crea un nuevo objeto Maze con el número de filas y columnas
            startRow = startCol = endRow = endCol = -1; // Reinicia las coordenadas de inicio y fin
            path = null; // Reinicia la ruta
            exploredPaths = 0; // Reinicia el número de rutas exploradas
            bestPathSteps = Integer.MAX_VALUE; // Reinicia el número de pasos de la mejor ruta
            titleLabel.setVisible(true); // Muestra el título
            repaint(); // Vuelve a dibujar el panel
        } catch (NumberFormatException ex) { // Si ocurre un error en la conversión de números
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos."); // Muestra un mensaje de error
        }
    }

    private void crearLaberintoAleatorio() { // Método para crear un laberinto aleatorio
        int rows = 20 + (int)(Math.random() * 11); // Filas aleatorias entre 20 y 30
        int cols = 30 + (int)(Math.random() * 21); // Columnas aleatorias entre 30 y 50
        maze = new Maze(rows, cols); // Crea el laberinto aleatorio
        startRow = startCol = endRow = endCol = -1; // Reinicia las coordenadas de inicio y fin
        path = null; // Reinicia la ruta
        exploredPaths = 0; // Reinicia el número de rutas exploradas
        bestPathSteps = Integer.MAX_VALUE; // Reinicia el número de pasos de la mejor ruta
        titleLabel.setVisible(true); // Muestra el título
        repaint(); // Vuelve a dibujar el panel
    }

    private void limpiarLaberinto() { // Método para limpiar el laberinto
        maze = null; // Elimina el laberinto
        startRow = startCol = endRow = endCol = -1; // Reinicia las coordenadas
        path = null; // Reinicia la ruta
        exploredPaths = 0; // Reinicia el número de rutas exploradas
        bestPathSteps = Integer.MAX_VALUE; // Reinicia el número de pasos de la mejor ruta
        titleLabel.setVisible(false); // Oculta el título
        repaint(); // Vuelve a dibujar el panel
    }

    private void verCaminoDirecto() { // Método para ver el camino directo sin pasos intermedios
        if (path != null) { // Si hay una ruta encontrada
            summaryArea.setText("Camino encontrado directamente:\n" + // Muestra el resumen de la ruta
                    "Pasos: " + path.size() + "\n" +
                    "Tiempo de ejecución: " + (endTime - startTime) / 1e9 + " s\n" +
                    "Rutas exploradas: " + exploredPaths + "\n" +
                    "Mejor ruta: " + bestPathSteps + " pasos.");
        } else { // Si no se encuentra una ruta
            summaryArea.setText("No se encontró solución."); // Muestra mensaje de no solución
        }
    }

    private void resolverLaberinto(MazeSolver solver) { // Método para resolver el laberinto usando un solver específico
        if (maze == null || startRow == -1 || endRow == -1) { // Si no hay laberinto o no se han seleccionado las coordenadas de inicio o fin
            summaryArea.setText("Por favor, crea el laberinto y selecciona inicio y fin."); // Muestra mensaje de error
            return;
        }

        startTime = System.nanoTime(); // Registra el tiempo de inicio
        path = solver.solve(maze, startRow, startCol, endRow, endCol); // Resuelve el laberinto
        endTime = System.nanoTime(); // Registra el tiempo de fin

        if (path != null) { // Si se encuentra una ruta
            exploredPaths = solver.getExploredPaths();  // Obtiene el número de rutas exploradas
            bestPathSteps = path.size(); // Obtiene la cantidad de pasos de la mejor ruta
            summaryArea.setText("Método: " + solver.getClass().getSimpleName() + "\n" + // Muestra el resumen de la solución
                    "Tiempo: " + (endTime - startTime) / 1e9 + " s\n" +
                    "Pasos de recorrido: " + path.size() + "\n" +
                    "Pasos hasta respuesta: " + path.size() + "\n" +
                    "Rutas exploradas: " + exploredPaths);
        } else { // Si no se encuentra una ruta
            summaryArea.setText("No se encontró solución."); // Muestra mensaje de no solución
        }
        repaint(); // Vuelve a dibujar el panel
    }
}
