package GUI;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

/**
 * Classe utility per mostrare messaggi personalizzati in finestre di dialogo.
 * Consente di visualizzare messaggi con titolo e design distinti per successo o
 * errore.
 */
public class CustomMessage {

	/**
	 * Mostra un messaggio personalizzato in una finestra di dialogo.
	 *
	 * @param message il messaggio da visualizzare
	 * @param title   il titolo della finestra di dialogo
	 * @param success {@code true} per un messaggio di successo (colore verde),
	 *                {@code false} per un messaggio di errore (colore rosso)
	 */
	public static void show(String message, String title, boolean success) {
		// Percorso dell'icona del logo
		String logoPath = "/GUI/immagini/icona.png";

		// Creazione della finestra di dialogo modale
		JDialog dialog = new JDialog((Frame) null, title, true);
		dialog.setTitle(title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setSize(400, 250); // Dimensioni della finestra di dialogo
		dialog.setLocationRelativeTo(null); // Centrare la finestra sullo schermo
		dialog.setLayout(new BorderLayout());
		dialog.setAlwaysOnTop(true); // Forza la finestra sopra tutte le altre
		dialog.toFront(); // Porta la finestra in primo piano
		dialog.repaint(); // Aggiorna graficamente la finestra

		// Imposta l'icona della finestra (se disponibile)
		if (logoPath != null) {
			URL iconURL = CustomMessage.class.getResource(logoPath);
			if (iconURL != null) {
				ImageIcon icon = new ImageIcon(iconURL);
				dialog.setIconImage(icon.getImage());
			} else {
				CustomMessage.show("Icona non trovata: " + logoPath, "Errore", false);
			}
		}

		// Pannello principale con il colore di sfondo basato sul tipo di messaggio
		JPanel panel = new JPanel();
		panel.setBackground(success ? new Color(34, 139, 34) : Color.RED); // Verde per successo, rosso per errore
		panel.setLayout(new BorderLayout());
		dialog.add(panel);

		// Etichetta per il messaggio
		JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
		messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
		messageLabel.setForeground(Color.WHITE); // Testo bianco
		messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margini
		panel.add(messageLabel, BorderLayout.CENTER);

		// Pulsante "OK" per chiudere la finestra di dialogo
		JButton okButton = new JButton("OK");
		okButton.setFont(new Font("Arial", Font.BOLD, 14));
		okButton.setPreferredSize(new Dimension(80, 30)); // Dimensioni del pulsante
		okButton.setBackground(Color.WHITE); // Sfondo bianco per il pulsante
		okButton.setForeground(success ? new Color(32, 178, 170) : Color.RED); // Testo verde o rosso
		okButton.setFocusPainted(false); // Rimuove il bordo al focus
		okButton.addActionListener(e -> dialog.dispose()); // Chiude la finestra al clic

		// Pannello contenente il pulsante "OK"
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false); // Sfondo trasparente
		buttonPanel.add(okButton); // Aggiunge il pulsante al centro
		panel.add(buttonPanel, BorderLayout.SOUTH);

		// Mostra la finestra di dialogo
		dialog.setVisible(true);
	}
}
