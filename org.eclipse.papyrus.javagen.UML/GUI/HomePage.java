package GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.net.URL;
import javax.swing.ImageIcon;

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

        // Verifica e carica l'icona dell'applicazione
        URL iconUrl = getClass().getResource("/GUI/immagini/icona.png"); // Percorso dell'icona
        if (iconUrl != null) {
            frame.setIconImage(new ImageIcon(iconUrl).getImage()); // Imposta l'icona
        } else {
            System.out.println("Immagine dell'icona non trovata!");  // Aggiungi un messaggio di errore nel caso l'icona non venga trovata
        }

        // Percorso relativo all'immagine (ad esempio "GUI/immagini/sfondohome.jpg")
        BackgroundPanel backgroundPanel = new BackgroundPanel("immagini/sfondohomesfocato.png", "immagini/sfondohome.png");
        frame.setContentPane(backgroundPanel);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
    }
}
