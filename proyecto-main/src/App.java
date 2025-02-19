import javax.swing.SwingUtilities;
import views.MazeGUI; // Cambiar a MazeUI

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MazeGUI().setVisible(true); // Cambiar a MazeUI
        });
    }
}
