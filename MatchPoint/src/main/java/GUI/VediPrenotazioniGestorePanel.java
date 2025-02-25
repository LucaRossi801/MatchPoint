package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.Campo;
import components.CentroSportivo;
import dataBase.DataBase;
import dataBase.Sessione;
import components.Prenotazione;

/**
 * Classe VediPrenotazioniGestorePanel
 * 
 * Questa classe rappresenta un pannello per i gestori che consente di
 * visualizzare le prenotazioni associate ai centri e campi sportivi gestiti.
 * Include funzionalità per selezionare un centro sportivo, scegliere un campo,
 * e visualizzare una lista dettagliata delle prenotazioni raggruppate per
 * giorno.
 */
public class VediPrenotazioniGestorePanel extends JPanel {
	private JComboBox<String> centriComboBox;
	private JComboBox<Campo> campiComboBox;
	private JTextArea prenotazioniArea;
	private Map<String, CentroSportivo> centriSportivi;
	private Image clearImage;

	private CardLayout cardLayout;
	private JPanel cardPanel;

	/**
	 * Costruttore per il pannello delle prenotazioni del gestore.
	 *
	 * @param cardLayout Il CardLayout utilizzato per la navigazione tra i pannelli.
	 * @param cardPanel  Il contenitore dei pannelli (card panel).
	 */
	public VediPrenotazioniGestorePanel(CardLayout cardLayout, JPanel cardPanel) {
		this.cardLayout = cardLayout;
		this.cardPanel = cardPanel;

		// Caricamento dello sfondo
		URL clearImageUrl = getClass().getResource("/GUI/immagini/sfondohome.png");
		if (clearImageUrl != null) {
			clearImage = new ImageIcon(clearImageUrl).getImage();
		} else {
			String ImageNotFound = "Errore nel caricamento dell'immagine: " + "/GUI/immagini/sfondohome.png";
			String er = "Errore";
			CustomMessage.show(ImageNotFound, er,
					false);
		}

		// Configura layout e componenti
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Pannello superiore
		JPanel topPanel = new JPanel(new GridBagLayout());
		topPanel.setOpaque(false);

		// Caricamento delle icone ridimensionate
		ImageIcon centroIcon = loadScaledIcon("/GUI/immagini/centroIcon.png", 70, 70);
		ImageIcon campoIcon = loadScaledIcon("/GUI/immagini/campoIcon.png", 70, 70);

		// Etichetta per il centro sportivo
		JPanel centriPanel = new JPanel(new BorderLayout());
		centriPanel.setOpaque(false);
		JLabel centriLabel = new JLabel("Seleziona Centro Sportivo:", centroIcon, JLabel.LEFT);
		centriLabel.setFont(new Font("Arial", Font.BOLD, 16));
		centriPanel.add(centriLabel, BorderLayout.WEST);
		gbc.gridx = 0;
		gbc.gridy = 0;
		topPanel.add(centriPanel, gbc);

		comboBoxCentri(gbc, topPanel);

		// Etichetta per il campo sportivo
		JPanel campiPanel = new JPanel(new BorderLayout());
		campiPanel.setOpaque(false);
		JLabel campiLabel = new JLabel("Seleziona Campo:", campoIcon, JLabel.LEFT);
		campiLabel.setFont(new Font("Arial", Font.BOLD, 16));
		campiPanel.add(campiLabel, BorderLayout.WEST);
		gbc.gridx = 0;
		gbc.gridy = 1;
		topPanel.add(campiPanel, gbc);
		
		comboBoxCampi(gbc, topPanel);
		
		// Aggiungi il pannello superiore al layout principale
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(topPanel, gbc);

		// Area per visualizzare le prenotazioni
		prenotazioniArea = new JTextArea();
		prenotazioniArea.setOpaque(false); // Rendi completamente trasparente
		prenotazioniArea.setBackground(new Color(255, 255, 255, 180)); // Sfondo semitrasparente
		prenotazioniArea.setForeground(Color.BLACK); // Colore del testo
		prenotazioniArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Configurazione dello JScrollPane
		JScrollPane scrollPane = new JScrollPane(prenotazioniArea) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();
				// Colore per il contorno (più chiaro)
				g2d.setColor(new Color(180, 180, 180, 150)); // Grigio chiaro semitrasparente
				g2d.fillRect(0, 0, getWidth(), getHeight());
				g2d.dispose();
				super.paintComponent(g);
			}
		};

		// Imposta lo sfondo del viewport (interno)
		scrollPane.getViewport().setOpaque(true);
		scrollPane.getViewport().setBackground(new Color(230, 230, 230, 180)); // Grigio molto chiaro semitrasparente

		// Rendi il `JScrollPane` opaco
		scrollPane.setOpaque(false);

		// Configurazione dei bordi e della dimensione
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(900, 400));
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), // Colore
																												// del
																												// bordo
																												// grigio
																												// scuro
				"Prenotazioni Giorno per Giorno", 0, 0, new Font("Arial", Font.BOLD, 14), Color.GRAY));

		// Modifica la velocità dello scrolling
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Incremento per ogni "tick" della rotella
		scrollPane.getVerticalScrollBar().setBlockIncrement(60); // Incremento per clic nella barra

		// Aggiungi lo JScrollPane al layout
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		add(scrollPane, gbc);

		// Bottone Indietro
		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			// Resetta i campi del pannello
			centriComboBox.setSelectedIndex(-1);
			campiComboBox.removeAllItems();
			prenotazioniArea.setText("");

			// Cambia schermata al card layout
			cardLayout.show(cardPanel, "createGestore");
		}, new Dimension(120, 30));
		int ButtonFontDim = 18;
		backButton.setFont(new Font("Arial", Font.BOLD, ButtonFontDim));
		backButton.setForeground(Color.GRAY);
		backButton.setBackground(Color.DARK_GRAY);

		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		add(backButton, gbc);

		aggiornaCampi();
	}

	private void comboBoxCampi(GridBagConstraints gbc, JPanel topPanel) {
		// ComboBox per i campi sportivi
		campiComboBox = new JComboBox<>();
		
		campiComboBox.setPreferredSize(new Dimension(200, 30));
		campiComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		campiComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		gbc.gridx = 1;
		topPanel.add(campiComboBox, gbc);
		campiComboBox.addItemListener(e -> aggiornaPrenotazioni());
	}

	private void comboBoxCentri(GridBagConstraints gbc, JPanel topPanel) {
		// ComboBox per i centri sportivi
		centriComboBox = new JComboBox<>();
		caricaCentriSportivi();
		centriComboBox.addItemListener(e -> aggiornaCampi());
		centriComboBox.setPreferredSize(new Dimension(200, 30));
		centriComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		centriComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		gbc.gridx = 1;
		topPanel.add(centriComboBox, gbc);
		centriComboBox.addItemListener(e -> aggiornaPrenotazioni());
	}

	/**
	 * Metodo per caricare e ridimensionare un'icona.
	 *
	 * @param path   Il percorso dell'immagine.
	 * @param width  Larghezza desiderata.
	 * @param height Altezza desiderata.
	 * @return Un oggetto ImageIcon ridimensionato.
	 */
	private ImageIcon loadScaledIcon(String path, int width, int height) {
		URL imageUrl = getClass().getResource(path);
		if (imageUrl != null) {
			ImageIcon icon = new ImageIcon(imageUrl);
			Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);
		} else {
			String er = "Errore";
			CustomMessage.show("Errore nel caricamento dell'immagine: " + path, er, false);
			return null;
		}
	}

	/**
	 * Classe per personalizzare l'aspetto della barra di scorrimento.
	 */
	class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
		@Override
		protected void configureScrollBarColors() {
			this.thumbColor = new Color(200, 200, 200);
		}

		@Override
		protected JButton createDecreaseButton(int orientation) {
			return createInvisibleButton();
		}

		@Override
		protected JButton createIncreaseButton(int orientation) {
			return createInvisibleButton();
		}

		private JButton createInvisibleButton() {
			JButton button = new JButton();
			button.setPreferredSize(new Dimension(0, 0));
			button.setMinimumSize(new Dimension(0, 0));
			button.setMaximumSize(new Dimension(0, 0));
			return button;
		}
	}

	/**
	 * Carica i centri sportivi nel JComboBox.
	 */
	private void caricaCentriSportivi() {
		centriSportivi = DataBase.getCentriSportiviGestiti(Sessione.getId()); // Ottieni i dati dalla sessione
		centriComboBox.removeAllItems();
		for (CentroSportivo centro : centriSportivi.values()) {
			centriComboBox.addItem(centro.getNome());
		}
	}

	/**
	 * Aggiorna i campi sportivi in base al centro selezionato.
	 */
	private void aggiornaCampi() {
		String centroSelezionato = (String) centriComboBox.getSelectedItem();

		if (centroSelezionato != null) {
			CentroSportivo centro = centriSportivi.get(centroSelezionato);

			if (centro != null) {
				try {
					// Recupera i campi per il centro selezionato
					List<Campo> campi = DataBase.getCampiById(centro.getID());
					campiComboBox.removeAllItems();

					for (Campo campo : campi) {
						campiComboBox.addItem(campo);
					}
				} catch (Exception e) {
					e.printStackTrace();
					String er = "Errore";
					JOptionPane.showMessageDialog(this, "Errore durante il caricamento dei campi. Riprovare.", er,
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			campiComboBox.removeAllItems();
		}

		aggiornaPrenotazioni(); // Aggiorna le prenotazioni per il campo selezionato
	}

	/**
	 * Aggiorna l'area delle prenotazioni in base al campo selezionato.
	 */
	private void aggiornaPrenotazioni() {
	    // Recupera il centro selezionato e il campo selezionato
	    String centroSelezionato = (String) centriComboBox.getSelectedItem();
	    Campo campoSelezionato = (Campo) campiComboBox.getSelectedItem();

	    // Reset dell'area delle prenotazioni
	    prenotazioniArea.setText(""); // Reset area di testo per messaggio

	    // Verifica che un centro e un campo siano selezionati
	    if (centroSelezionato == null || campoSelezionato == null) {
	        prenotazioniArea.setText("Seleziona un centro sportivo e un campo per visualizzare le prenotazioni.");
	        return;
	    }

	    // Contenitore delle prenotazioni
	    JPanel contenitorePrenotazioni = new JPanel();
	    contenitorePrenotazioni.setLayout(new BoxLayout(contenitorePrenotazioni, BoxLayout.Y_AXIS));
	    contenitorePrenotazioni.setOpaque(false); // Assicurati che sia trasparente

	    try {
	        // Ottieni gli ID del centro e del campo
	        int centroId = centriSportivi.get(centroSelezionato).getID();
	        int campoId = campoSelezionato.getId();

	        // Recupera le prenotazioni dal database
	        List<Prenotazione> prenotazioni = DataBase.getPrenotazioniByCampo(centroId, campoId);

	        // Se la lista delle prenotazioni è vuota
	        if (prenotazioni.isEmpty()) {
	            // Mostra il messaggio nell'area delle prenotazioni
	            prenotazioniArea.setText("Nessuna prenotazione per questo campo.");
	            prenotazioniArea.setEditable(false); // Rende l'area non modificabile

	            // Rimuovi tutti i componenti precedenti dal contenitore
	            contenitorePrenotazioni.removeAll();  
	            contenitorePrenotazioni.revalidate(); // Ricalcola il layout
	            contenitorePrenotazioni.repaint();    // Ridisegna il contenitore

	            // Aggiorna il JScrollPane con il contenitore vuoto
	            JScrollPane scrollPane = (JScrollPane) getComponent(1);
	            scrollPane.setViewportView(contenitorePrenotazioni); // Imposta il contenitore vuoto

	            return; // Esci dalla funzione poiché non ci sono prenotazioni
	        }

	        // Raggruppa le prenotazioni per giorno
	        Map<String, List<Prenotazione>> prenotazioniPerGiorno = raggruppaPrenotazioniPerGiorno(prenotazioni);

	        // Ordina i giorni in ordine decrescente
	        List<String> giorniOrdinati = new ArrayList<>(prenotazioniPerGiorno.keySet());
	        Collections.sort(giorniOrdinati, Collections.reverseOrder());

	        // Aggiungi le prenotazioni per giorno
	        for (String giorno : giorniOrdinati) {
	            List<Prenotazione> prenotazioniDelGiorno = prenotazioniPerGiorno.get(giorno);

	            // Header per il giorno
	            JLabel headerGiorno = new JLabel("Prenotazioni per il giorno: " + giorno);
				
	            int ButtonFontDim = 18;
				headerGiorno.setFont(new Font("Arial", Font.BOLD, ButtonFontDim));
	            headerGiorno.setForeground(new Color(16, 139, 135));
	            headerGiorno.setAlignmentX(Component.CENTER_ALIGNMENT);
	            contenitorePrenotazioni.add(headerGiorno);

	            // Linea separatrice tra il giorno e le prenotazioni
	            contenitorePrenotazioni.add(creaLineaSeparatrice());
	            contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio tra prenotazioni
	            // Aggiungi prenotazioni per il giorno
	            for (Prenotazione prenotazione : prenotazioniDelGiorno) {
	                JPanel cardPrenotazione = creaCardPrenotazione(prenotazione);
	                cardPrenotazione.setAlignmentX(Component.CENTER_ALIGNMENT);
	                contenitorePrenotazioni.add(cardPrenotazione);
	                contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio tra prenotazioni
	            }

	            // Aggiungi una linea separatrice tra i giorni
	            contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 10)));
	            contenitorePrenotazioni.add(creaLineaSeparatriceSpessa());
	            contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 15)));
	        }

	        // Aggiungi il contenitore al JScrollPane
	        JScrollPane scrollPane = (JScrollPane) getComponent(1);
	        scrollPane.setViewportView(contenitorePrenotazioni); // Imposta il contenitore aggiornato

	        prenotazioniArea.setText("");  // Reset dell'area delle prenotazioni
	        prenotazioniArea.setEditable(false); // Rende l'area non modificabile

	    } catch (Exception e) {
	        e.printStackTrace();
	        prenotazioniArea.setText("Errore durante il caricamento delle prenotazioni.");
	    }
	}




	/**
	 * Crea una linea separatrice originale tra i giorni.
	 */
	private JSeparator creaLineaSeparatrice() {
		JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
		separatore.setForeground(new Color(16, 139, 135));
		separatore.setMaximumSize(new Dimension(800, 2)); // Limita la larghezza
		separatore.setPreferredSize(new Dimension(800, 2)); // Imposta una dimensione preferita
		separatore.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra il separatore
		return separatore;
	}

	/**
	 * Crea una linea separatrice più spessa tra i gruppi di prenotazioni.
	 */
	private JPanel creaLineaSeparatriceSpessa() {
		JPanel separatore = new JPanel();
		separatore.setMaximumSize(new Dimension(800, 4)); // Limita la larghezza
		separatore.setPreferredSize(new Dimension(800, 4)); // Altezza maggiore per una linea più evidente
		separatore.setBackground(new Color(16, 139, 135)); // Colore della linea
		return separatore;
	}

	/**
	 * Crea un pannello per rappresentare una prenotazione.
	 */
	private JPanel creaCardPrenotazione(Prenotazione prenotazione) {
		JPanel card = new JPanel(new GridBagLayout());

		// Recupera la data e l'orario di fine della prenotazione
		Date dataPrenotazione = prenotazione.getData();
		Time oraInizio = prenotazione.getOraInizio();
		Time oraFine = prenotazione.getOraFine();

		// Converte java.sql.Date in LocalDate
		LocalDate localDate = dataPrenotazione.toLocalDate();

		// Converte Time in LocalTime
		LocalTime localTimeInizio = oraInizio.toLocalTime();
		LocalTime localTimeFine = oraFine.toLocalTime();

		// Combina LocalDate e LocalTime per ottenere un LocalDateTime
		LocalDateTime inizioPrenotazione = localDate.atTime(localTimeInizio);
		LocalDateTime finePrenotazione = localDate.atTime(localTimeFine);

		// Controlla se la prenotazione è passata
		LocalDateTime oraCorrente = LocalDateTime.now();
		boolean prenotazionePassata = finePrenotazione.isBefore(oraCorrente);

		// Crea la card con un colore di sfondo a seconda dello stato della prenotazione
		card.setBackground(prenotazionePassata ? new Color(200, 200, 200) : new Color(230, 240, 250));
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(16, 139, 135), 2),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		// Ridotto la larghezza massima del rettangolo
		card.setMaximumSize(new Dimension(750, 100));

		// Configurazione layout della card
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		creazioneEtichette(prenotazione, card, gbc);

		return card;
	}

	private void creazioneEtichette(Prenotazione prenotazione, JPanel card, GridBagConstraints gbc) {
		// Ora inizio - Ora fine
		gbc.gridx = 0;
		gbc.gridy = 0;
		card.add(new JLabel("⏰ Orario: " + prenotazione.getOraInizio() + " - " + prenotazione.getOraFine()), gbc);

		// Durata
		gbc.gridx = 1;
		card.add(new JLabel("🕒 Durata: " + prenotazione.getDurataInFormatoOreMinuti() + " h"), gbc);

		// Costo
		gbc.gridx = 2;
		card.add(new JLabel("💶 Costo: €" + DettagliPrenotazioneDialog.calcolaCosto(prenotazione)), gbc);
	}

	/**
	 * Raggruppa le prenotazioni per giorno e le ordina per orario (più presto in basso).
	 */
	private Map<String, List<Prenotazione>> raggruppaPrenotazioniPerGiorno(List<Prenotazione> prenotazioni) {
	    Map<String, List<Prenotazione>> prenotazioniPerGiorno = new HashMap<>();

	    for (Prenotazione prenotazione : prenotazioni) {
	        // Ottieni la data in formato stringa
	        String giorno = prenotazione.getData().toLocalDate().toString();

	        // Aggiungi la prenotazione al giorno corrispondente
	        prenotazioniPerGiorno.computeIfAbsent(giorno, k -> new ArrayList<>()).add(prenotazione);
	    }

	    // Ordina le prenotazioni per giorno
	    for (List<Prenotazione> prenotazioniDelGiorno : prenotazioniPerGiorno.values()) {
	        // Ordina in base all'orario di inizio (più presto in fondo)
	        prenotazioniDelGiorno.sort((p1, p2) -> {
	            LocalTime orario1 = p1.getOraInizio().toLocalTime();
	            LocalTime orario2 = p2.getOraInizio().toLocalTime();
	            return orario2.compareTo(orario1); // Invertito per avere le più tardi in alto
	        });
	    }

	    return prenotazioniPerGiorno;
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();

		// Colore verde acqua semitrasparente
		Color semiTransparentAqua = new Color(16, 139, 135, 128); // RGB (16, 139, 135) con trasparenza (128)

		// Disegna un rettangolo trasparente sopra lo sfondo
		g2d.setColor(semiTransparentAqua);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// Disegna l'immagine di sfondo
		if (clearImage != null) {
			g2d.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);

		}

		g2d.dispose();
	}

}
