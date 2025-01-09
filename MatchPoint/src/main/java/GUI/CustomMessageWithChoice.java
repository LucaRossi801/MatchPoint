package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CustomMessageWithChoice {

	/**
	 * Mostra un messaggio personalizzato con opzioni di scelta (Sì/No).
	 * 
	 * @param message il messaggio da visualizzare
	 * @param title   il titolo della finestra di dialogo
	 * @param success true per un messaggio di successo (verde), false per errore (errore)
	 * @return true se l'utente sceglie "Sì", false altrimenti
	 */
	public static boolean show(String message, String title, boolean success) {
		// Percorso del logo
		String logoPath = "/GUI/immagini/icona.png";

		JDialog dialog = new JDialog((Frame) null, title, true); // Modale
		dialog.setTitle(title);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setSize(400, 250);
		dialog.setLocationRelativeTo(null);
		dialog.setLayout(new BorderLayout());
		dialog.setAlwaysOnTop(true);

		// Imposta l'icona della finestra
		if (logoPath != null) {
			URL iconURL = CustomMessage.class.getResource(logoPath);
			if (iconURL != null) {
				ImageIcon icon = new ImageIcon(iconURL);
				dialog.setIconImage(icon.getImage());
			} else {
				CustomMessage.show("Icona non trovata: " + logoPath, "Errore", false);
			}
		}

		// Imposta il colore di sfondo
		JPanel panel = new JPanel();
		panel.setBackground(success ? new Color(34, 139, 34) : Color.RED);
		panel.setLayout(new BorderLayout());
		dialog.add(panel);

		// Testo del messaggio
		JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
		messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(messageLabel, BorderLayout.CENTER);

		// Pulsanti Sì e No
		JButton yesButton = new JButton("Sì");
		yesButton.setFont(new Font("Arial", Font.BOLD, 14));
		yesButton.setBackground(Color.WHITE);
		yesButton.setForeground(success ? new Color(32, 178, 170) : Color.RED);

		JButton noButton = new JButton("No");
		noButton.setFont(new Font("Arial", Font.BOLD, 14));
		noButton.setBackground(Color.WHITE);
		noButton.setForeground(success ? new Color(32, 178, 170) : Color.RED);

		// Pannello pulsanti
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.add(yesButton);
		buttonPanel.add(noButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		// Variabile di stato
		final boolean[] userChoice = { false };

		// Aggiungi action listener
		yesButton.addActionListener(e -> {
			userChoice[0] = true;
			dialog.dispose();
		});

		noButton.addActionListener(e -> dialog.dispose());

		dialog.setVisible(true);
		return userChoice[0];
	}
}
