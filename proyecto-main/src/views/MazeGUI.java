package views;

import controllers.*;
import controllers.interfaces.MazeSolver;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import models.Maze;

public class MazeGUI extends JFrame {
    private Maze maze;
    private JTextArea summaryArea;
    private JPanel mazePanel;
    private int startRow = -1, startCol = -1, endRow = -1, endCol = -1;
    private List<int[]> path;
    private JLabel titleLabel;
    private long startTime, endTime;
    private int exploredPaths = 0;
    private int bestPathSteps = Integer.MAX_VALUE;

    public MazeGUI() {
        setTitle("Laberinto");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Título en cursiva
        titleLabel = new JLabel("LABERINTO");
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);
        titleLabel.setVisible(false);

        // Panel de botones
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton createButton = new JButton("Crear Laberinto");
        JButton bfsButton = new JButton("BFS");
        JButton dfsButton = new JButton("DFS");
        JButton dinamicaButton = new JButton("Dinamica Pro");
        JButton recursivoButton = new JButton("Recursivo Simple");
        JButton randomButton = new JButton("Crear Laberinto Aleatorio");
        JButton clearButton = new JButton("Limpiar Laberinto");
        JButton directViewButton = new JButton("Ver Camino Directo");

        // Estilo de los botones
        for (JButton button : new JButton[]{createButton, bfsButton, dfsButton, dinamicaButton, recursivoButton, randomButton, clearButton, directViewButton}) {
            button.setBackground(new Color(76, 175, 80));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(56, 142, 60));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(76, 175, 80));
                }
            });
        }

        inputPanel.add(createButton);
        inputPanel.add(bfsButton);
        inputPanel.add(dfsButton);
        inputPanel.add(dinamicaButton);
        inputPanel.add(recursivoButton);
        inputPanel.add(randomButton);
        inputPanel.add(clearButton);
        inputPanel.add(directViewButton);

        // Área de resumen
        summaryArea = new JTextArea(5, 40);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Panel para el laberinto
        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (maze == null) return;
                int cellSize = 30;
                int mazeWidth = maze.getCols() * cellSize;
                int mazeHeight = maze.getRows() * cellSize;
                // Calcular las coordenadas de inicio para centrar el laberinto
                int startX = (getWidth() - mazeWidth) / 2;
                int startY = (getHeight() - mazeHeight) / 2;
                
                for (int r = 0; r < maze.getRows(); r++) {
                    for (int c = 0; c < maze.getCols(); c++) {
                        int x = startX + c * cellSize;
                        int y = startY + r * cellSize;

                        if (maze.getValue(r, c) == 1) {
                            g.setColor(Color.BLACK);
                             g.fillRect(x, y, cellSize, cellSize);
                        } else if (r == startRow && c == startCol) {
                            g.setColor(Color.GREEN);
                            g.fillRect(x, y, cellSize, cellSize);
                        } else if (r == endRow && c == endCol) {
                            g.setColor(Color.RED);
                            g.fillRect(x, y, cellSize, cellSize);
                        } else {
                            g.setColor(Color.WHITE);
                            g.fillRect(x, y, cellSize, cellSize);
                            g.setColor(Color.BLACK);
                            g.drawRect(x, y, cellSize, cellSize);
                        }
                    }
                }

                // Mostrar la mejor ruta
                if (path != null) {
                    g.setColor(Color.CYAN);
                    for (int[] p : path) {
                        int x = startX + p[1] * cellSize;
                        int y = startY + p[0] * cellSize;
                        g.fillRect(x, y, cellSize, cellSize);
                    }
                }
            }
        };

        mazePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int cellSize = 30;
                int mazeWidth = maze.getCols() * cellSize;
                int mazeHeight = maze.getRows() * cellSize;
                int startX = (mazePanel.getWidth() - mazeWidth) / 2;
                int startY = (mazePanel.getHeight() - mazeHeight) / 2;

                int col = (e.getX() - startX) / cellSize;
                int row = (e.getY() - startY) / cellSize;

                if (maze == null || row < 0 || col < 0 || row >= maze.getRows() || col >= maze.getCols()) return;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    maze.setValue(row, col, maze.getValue(row, col) == 0 ? 1 : 0);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (startRow == -1) {
                        startRow = row;
                        startCol = col;
                    } else if (endRow == -1) {
                        endRow = row;
                        endCol = col;
                    }
                }
                repaint();
            }

        });

        // Botones de acción
        createButton.addActionListener(e -> crearLaberinto());
        randomButton.addActionListener(e -> crearLaberintoAleatorio());
        clearButton.addActionListener(e -> limpiarLaberinto());
        directViewButton.addActionListener(e -> verCaminoDirecto());

        bfsButton.addActionListener(e -> resolverLaberinto(new MazeSolverBFS()));
        dfsButton.addActionListener(e -> resolverLaberinto(new MazeSolverDFS()));
        dinamicaButton.addActionListener(e -> resolverLaberinto(new MazeSolverDP()));
        recursivoButton.addActionListener(e -> resolverLaberinto(new MazeSolverRecursivo()));

        // Usar BorderLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(summaryArea), BorderLayout.SOUTH);
        mainPanel.add(mazePanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void crearLaberinto() {
        String rowsStr = JOptionPane.showInputDialog(this, "Ingrese el número de filas:");
        String colsStr = JOptionPane.showInputDialog(this, "Ingrese el número de columnas:");
        try {
            int rows = Integer.parseInt(rowsStr);
            int cols = Integer.parseInt(colsStr);
            maze = new Maze(rows, cols);
            startRow = startCol = endRow = endCol = -1;
            path = null;
            exploredPaths = 0;
            bestPathSteps = Integer.MAX_VALUE;
            titleLabel.setVisible(true);
            repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.");
        }
    }

    private void crearLaberintoAleatorio() {
        int rows = 20 + (int)(Math.random() * 11);
        int cols = 30 + (int)(Math.random() * 21);
        maze = new Maze(rows, cols);
        startRow = startCol = endRow = endCol = -1;
        path = null;
        exploredPaths = 0;
        bestPathSteps = Integer.MAX_VALUE;
        titleLabel.setVisible(true);
        repaint();
    }

    private void limpiarLaberinto() {
        maze = null;
        startRow = startCol = endRow = endCol = -1;
        path = null;
        exploredPaths = 0;
        bestPathSteps = Integer.MAX_VALUE;
        titleLabel.setVisible(false);
        repaint();
    }

    private void verCaminoDirecto() {
        // Mostrar la ruta más corta directamente sin paso a paso
        if (path != null) {
            summaryArea.setText("Camino encontrado directamente:\n" +
                    "Pasos: " + path.size() + "\n" +
                    "Tiempo de ejecución: " + (endTime - startTime) / 1e9 + " s\n" +
                    "Rutas exploradas: " + exploredPaths + "\n" +
                    "Mejor ruta: " + bestPathSteps + " pasos.");
        } else {
            summaryArea.setText("No se encontró solución.");
        }
    }

    private void resolverLaberinto(MazeSolver solver) {
        if (maze == null || startRow == -1 || endRow == -1) {
            summaryArea.setText("Por favor, crea el laberinto y selecciona inicio y fin.");
            return;
        }

        startTime = System.nanoTime();
        path = solver.solve(maze, startRow, startCol, endRow, endCol);
        endTime = System.nanoTime();

        if (path != null) {
            exploredPaths = solver.getExploredPaths();  // Obtener el número de rutas exploradas
            bestPathSteps = path.size();
            summaryArea.setText("Método: " + solver.getClass().getSimpleName() + "\n" +
                    "Tiempo: " + (endTime - startTime) / 1e9 + " s\n" +
                    "Pasos de recorrido: " + path.size() + "\n" +
                    "Pasos hasta respuesta: " + path.size() + "\n" +
                    "Rutas exploradas: " + exploredPaths);
        } else {
            summaryArea.setText("No se encontró solución.");
        }
        repaint();
    }
} 
