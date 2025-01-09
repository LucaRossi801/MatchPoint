package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Classe che rappresenta il pannello per le operazioni disponibili a un
 * giocatore. Questo pannello permette di: - Inserire una prenotazione. -
 * Visualizzare le prenotazioni effettuate. - Tornare al pannello di login.
 */
public class CreateGiocatorePanel extends JPanel {
	private Image clearImage; // Immagine di sfondo nitida per il pannello.

	/**
	 * Costruttore della classe CreateGiocatorePanel.
	 *
	 * @param cardLayout Gestore del layout a schede (CardLayout) per la navigazione
	 *                   tra pannelli.
	 * @param cardPanel  Pannello principale contenitore dei diversi pannelli.
	 */
	public CreateGiocatorePanel(CardLayout cardLayout, JPanel cardPanel) {
		// Carica l'immagine di sfondo.
		URL clearImageUrl = getClass().getResource("/GUI/immagini/sfondohome.png");
		if (clearImageUrl != null) {
			clearImage = new ImageIcon(clearImageUrl).getImage();
		} else {
			CustomMessage.show("Errore nel caricamento dell'immagine: " + "/GUI/immagini/sfondohome.png", "Errore",
					false);
		}

		// Imposta il layout.
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 0, 20, 0); // Maggiore spaziatura tra i pulsanti.
		gbc.gridx = 0; // Centra i pulsanti orizzontalmente.

		Dimension buttonSize = new Dimension(400, 120); // Dimensioni personalizzate per i pulsanti.

		// Crea il pulsante "Inserisci Prenotazione".
		JButton prenotaButton = BackgroundPanel.createFlatButton("Inserisci Prenotazione",
				e -> cardLayout.show(cardPanel, "inserisciPrenotazione"), buttonSize);
		prenotaButton.setForeground(new Color(220, 250, 245));

		// Aggiungi l'icona al pulsante.
		ImageIcon addIcon = caricaIcona("/GUI/immagini/add_icon.png");
		if (addIcon != null) {
			prenotaButton.setIcon(addIcon);
			prenotaButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona.
			prenotaButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo.
		}
		gbc.gridy = 0; // Prima riga.
		add(prenotaButton, gbc);

		// Crea il pulsante "Vedi Prenotazioni".
		JButton vediPrenotazioniButton = BackgroundPanel.createFlatButton("Vedi Prenotazioni", e -> {
			VediPrenotazioniGiocatorePanel panel = new VediPrenotazioniGiocatorePanel(cardLayout, cardPanel);
			BackgroundPanel.cardPanel.add(panel, "vediPrenotazioniGiocatore");
			cardLayout.show(cardPanel, "vediPrenotazioniGiocatore");
		}, buttonSize);
		vediPrenotazioniButton.setForeground(new Color(220, 250, 245));

		// Aggiungi l'icona al pulsante.
		ImageIcon listIcon = caricaIcona("/GUI/immagini/list_icon.png");
		if (listIcon != null) {
			vediPrenotazioniButton.setIcon(listIcon);
			vediPrenotazioniButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona.
			vediPrenotazioniButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo.
		}
		gbc.gridy = 1; // Seconda riga.
		add(vediPrenotazioniButton, gbc);

		// Crea il pulsante "Back".
		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> cardLayout.show(cardPanel, "login"),
				new Dimension(150, 50));

		// Personalizza colore per il pulsante "Back".
		backButton.setForeground(Color.GRAY);
		backButton.setBackground(Color.DARK_GRAY);
		backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back".
		gbc.gridy = 3; // Quarta riga.
		add(backButton, gbc);
	}

	/**
	 * Carica un'icona dal percorso specificato e la ridimensiona.
	 *
	 * @param percorso Il percorso dell'icona.
	 * @return L'icona ridimensionata o null se non trovata.
	 */
	private ImageIcon caricaIcona(String percorso) {
		URL iconUrl = getClass().getResource(percorso);
		if (iconUrl != null) {
			ImageIcon icon = new ImageIcon(iconUrl);
			// Ridimensiona l'immagine.
			Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);
		} else {
			CustomMessage.show("Errore nel caricamento dell'icona: " + percorso, "Errore", false);
			return null; // Restituisci null se l'icona non viene trovata.
		}
	}

	/**
	 * Disegna l'immagine di sfondo nel pannello.
	 *
	 * @param g Il contesto grafico.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Disegna l'immagine di sfondo.
		if (clearImage != null) {
			g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
