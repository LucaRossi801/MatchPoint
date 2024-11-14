package GUI;

import javax.swing.*;
import java.net.URL;

public class HomePage {
	private JFrame frame;

	public static void main(String[] args) {
		// Avvio dell'interfaccia utente
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HomePage window = new HomePage();
			}

		});
	}

	public HomePage() {
		initialize();
	}

	public void initialize() {
		// Verifica e carica l'icona dell'applicazione
		JFrame frame = new JFrame("MatchPoint");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		URL iconUrl = getClass().getResource("/GUI/immagini/icona.png"); // Percorso dell'icona
		if (iconUrl != null) {
			frame.setIconImage(new ImageIcon(iconUrl).getImage()); // Imposta l'icona
		} else {
			System.out.println("Immagine dell'icona non trovata!"); // Aggiungi un messaggio di errore nel caso l'icona
																	// non venga trovata
		}
		BackgroundPanel backgroundPanel = new BackgroundPanel("/GUI/immagini/sfondohomesfocato.png",
				"/GUI/immagini/sfondohome.png");
		frame.add(backgroundPanel);
		frame.pack();
		frame.setLocationRelativeTo(null); // Centra la finestra
		frame.setVisible(true);
	}
}