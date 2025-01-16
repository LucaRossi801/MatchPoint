package GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import dataBase.DataBase;
import dataBase.Sessione;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Classe che rappresenta il pannello per la gestione delle operazioni di un
 * gestore. Questo pannello include opzioni per: - Inserire un centro sportivo.
 * - Modificare un centro sportivo. - Visualizzare le prenotazioni. - Tornare
 * indietro al login.
 */
public class CreateGestorePanel extends JPanel {
	private Image clearImage; // Immagine di sfondo nitida per il pannello.

	/**
	 * Costruttore della classe CreateGestorePanel.
	 *
	 * @param cardLayout Gestore del layout a schede (CardLayout) per la navigazione
	 *                   tra pannelli.
	 * @param cardPanel  Pannello principale contenitore dei diversi pannelli.
	 */
	public CreateGestorePanel(CardLayout cardLayout, JPanel cardPanel) {
		// Carica l'immagine di sfondo.
		URL clearImageUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
		String er = "Errore";
		if (clearImageUrl != null) {
			clearImage = new ImageIcon(clearImageUrl).getImage();
		} else {
			String ImageNotFound = "Errore nel caricamento dell'immagine: GUI/immagini/sfondohome.png";
			CustomMessage.show(ImageNotFound, er, false);
		}

		// Imposta il layout.
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 0, 20, 0); // Maggiore spaziatura tra i pulsanti.
		gbc.gridx = 0; // Centra i pulsanti orizzontalmente.

		Dimension buttonSize = new Dimension(400, 120); // Dimensioni personalizzate per i pulsanti.

		// Crea il primo pulsante "Inserisci Centro".
		JButton inserisciCentroButton = BackgroundPanel.createFlatButton("Inserisci Centro",
				e -> cardLayout.show(cardPanel, "inserisciCentro"), buttonSize);
		inserisciCentroButton.setForeground(new Color(220, 250, 245));

		// Aggiungi l'icona al pulsante.
		ImageIcon addIcon = caricaIcona("/GUI/immagini/add_icon.png");
		if (addIcon != null) {
			inserisciCentroButton.setIcon(addIcon);
			inserisciCentroButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona.
			inserisciCentroButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo.
		}
		gbc.gridy = 0; // Prima riga.
		add(inserisciCentroButton, gbc);

		// Crea il secondo pulsante "Modifica Centro".
		JButton modificaCentroButton = BackgroundPanel.createFlatButton("Modifica Centro", e -> {
			if (!(DataBase.getCentriSportiviGestiti(Sessione.getId()).isEmpty())) {
				ModificaCentroPanel modificaCentroPanel = new ModificaCentroPanel();
				BackgroundPanel.cardPanel.add(modificaCentroPanel, "modificaCentro");
				cardLayout.show(cardPanel, "modificaCentro");
			} else {
				CustomMessage.show("Non esistono centri registrati!", er, false);
			}
		}, buttonSize);
		modificaCentroButton.setForeground(new Color(220, 250, 245));

		// Aggiungi l'icona al pulsante.
		ImageIcon editIcon = caricaIcona("/GUI/immagini/edit_icon.png");
		if (editIcon != null) {
			modificaCentroButton.setIcon(editIcon);
			modificaCentroButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona.
			modificaCentroButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo.
		}
		gbc.gridy = 1; // Seconda riga.
		add(modificaCentroButton, gbc);

		// Crea il terzo pulsante "Vedi Prenotazioni".
		JButton vediPrenotazioniButton = BackgroundPanel.createFlatButton("Vedi Prenotazioni", e -> {
			if (!(DataBase.getCentriSportiviGestiti(Sessione.getId()).isEmpty())) {
				VediPrenotazioniGestorePanel panel = new VediPrenotazioniGestorePanel(cardLayout, cardPanel);
				BackgroundPanel.cardPanel.add(panel, "vediPrenotazioniGestore");
				cardLayout.show(cardPanel, "vediPrenotazioniGestore");
			} else {
				CustomMessage.show("Non esistono centri registrati!", er, false);
			}
		}, buttonSize);
		vediPrenotazioniButton.setForeground(new Color(220, 250, 245));

		// Aggiungi l'icona al pulsante.
		ImageIcon listIcon = caricaIcona("/GUI/immagini/list_icon.png");
		if (listIcon != null) {
			vediPrenotazioniButton.setIcon(listIcon);
			vediPrenotazioniButton.setHorizontalTextPosition(SwingConstants.RIGHT); // Testo a destra dell'icona.
			vediPrenotazioniButton.setIconTextGap(15); // Spaziatura tra l'icona e il testo.
		}
		gbc.gridy = 2; // Terza riga.
		add(vediPrenotazioniButton, gbc);

		// Crea il pulsante "Back".
		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			Login.resetFields(); // Svuota i campi di testo.
			BackgroundPanel.showPanel("login"); // Torna al pannello di login.
		}, new Dimension(150, 50) // Dimensione personalizzata del bottone.
		);

		// Personalizza colore per il pulsante "Back".
		backButton.setForeground(Color.GRAY);
		backButton.setBackground(Color.DARK_GRAY);
		int ButtonFontDim = 18;
		backButton.setFont(new Font("Arial", Font.BOLD, ButtonFontDim)); // Font più piccolo per il pulsante "Back".
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
			String IconNotFound = "Errore nel caricamento dell'icona: ";
			String er = "Errore";
			CustomMessage.show(IconNotFound + percorso, er, false);
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
		if (clearImage != null) {
			g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
