package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class HomePage {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HomePage window = new HomePage();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public HomePage() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Inizializza il pannello con la versione sfocata dell'immagine
        BackgroundPanel backgroundPanel = new BackgroundPanel("/GUI/immagini/sfondohomesfocato.png", "/GUI/immagini/sfondohome.png");
        frame.setContentPane(backgroundPanel);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
    }
}
