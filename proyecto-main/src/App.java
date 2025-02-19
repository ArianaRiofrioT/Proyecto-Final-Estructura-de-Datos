// Importamos la clase SwingUtilities, que proporciona un método para ejecutar código en el hilo de eventos de Swing
import javax.swing.SwingUtilities;
// Importamos la clase MazeGUI desde el paquete views (puedes cambiarlo a MazeUI según sea necesario)
import views.MazeGUI; // Cambiar a MazeUI

// Clase principal de la aplicación
public class App {

    // Método principal que se ejecuta al iniciar la aplicación
    public static void main(String[] args) {

        // Ejecutamos la interfaz gráfica de usuario en el hilo de eventos de Swing para evitar bloqueos
        SwingUtilities.invokeLater(() -> {
            // Creamos una nueva instancia de MazeGUI y la hacemos visible
            new MazeGUI().setVisible(true); // Cambiar a MazeUI
        });
    }
}
