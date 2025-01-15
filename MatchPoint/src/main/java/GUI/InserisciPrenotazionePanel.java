package GUI;

import org.jdesktop.swingx.JXDatePicker;

import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;
import dataBase.DataBase;
import dataBase.Sessione;
import localizzazione.FileReaderUtils;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.Timer;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class InserisciPrenotazionePanel extends JPanel {
	private Image clearImage;

	private JLabel centroSelezionatoLabel;
	private JLabel campoSelezionatoLabel;
	private Integer campoSelezionatoID;
	private Integer centroSelezionatoID;
	private JSpinner oraInizioSpinner;
	private JSpinner oraFineSpinner;
	JXDatePicker datePicker = new JXDatePicker();

	/**
	 * Costruttore della classe InserisciPrenotazionePanel. Inizializza il pannello
	 * per l'inserimento di una nuova prenotazione, con selezione di centro, campo,
	 * data e orari.
	 *
	 * @param cardLayout Il layout per la gestione della navigazione tra pannelli.
	 * @param cardPanel  Il pannello principale contenente i sottopannelli.
	 */
	public InserisciPrenotazionePanel(CardLayout cardLayout, JPanel cardPanel) {
		// Carica l'immagine di sfondo
		URL clearImageUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
		if (clearImageUrl != null) {
			clearImage = new ImageIcon(clearImageUrl).getImage();
		} else {
			CustomMessage.show("Errore nel caricamento dell'immagine: GUI/immagini/sfondohome.png", "Errore", false);
		}

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 15, 15, 15);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Titolo
		JLabel titolo = new JLabel("Inserisci Prenotazione", JLabel.CENTER);
		titolo.setFont(new Font("Arial", Font.BOLD, 28));
		titolo.setForeground(Color.WHITE);
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(titolo, gbc);

		gbc.gridwidth = 1;

		// Bottone per selezionare il centro
		JButton selezionaCentroButton = BackgroundPanel.createFlatButton("Seleziona Centro",
				e -> apriSelezionaCentroDialog(), new Dimension(300, 50));
		gbc.gridx = 0;
		gbc.gridy = 1;
		add(selezionaCentroButton, gbc);

		centroSelezionatoLabel = new JLabel("Non selezionato", JLabel.LEFT);
		centroSelezionatoLabel.setFont(new Font("Arial", Font.BOLD, 18));
		centroSelezionatoLabel.setForeground(Color.WHITE);
		gbc.gridx = 1;
		add(centroSelezionatoLabel, gbc);

		// Bottone per selezionare il campo
		JButton selezionaCampoButton = BackgroundPanel.createFlatButton("Seleziona Campo",
				e -> apriSelezionaCampoDialog(), new Dimension(300, 50));
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(selezionaCampoButton, gbc);

		campoSelezionatoLabel = new JLabel("Non selezionato", JLabel.LEFT);
		campoSelezionatoLabel.setFont(new Font("Arial", Font.BOLD, 18));
		campoSelezionatoLabel.setForeground(Color.WHITE);
		gbc.gridx = 1;
		add(campoSelezionatoLabel, gbc);

		// Date Picker per la selezione della data
		datePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));
		datePicker.setDate(new Date());
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new OutlinedLabel("Data:", Color.BLACK), gbc);
		gbc.gridx = 1;
		LocalDateTime oraCorrente = LocalDateTime.now().plusHours(24);
		datePicker.getMonthView()
				.setLowerBound(Date.from(oraCorrente.plusHours(24).atZone(ZoneId.systemDefault()).toInstant()));
		add(datePicker, gbc);

		// Spinner per orario di inizio e fine
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(new OutlinedLabel("Ora Inizio:", Color.BLACK), gbc);
		gbc.gridx = 1;
		oraInizioSpinner = createCustomTimeSpinner();
		add(oraInizioSpinner, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		add(new OutlinedLabel("Ora Fine:", Color.BLACK), gbc);
		gbc.gridx = 1;
		oraFineSpinner = createCustomTimeSpinner();
		add(oraFineSpinner, gbc);

		// Bottone "Riepilogo"
		JButton riepilogoButton = BackgroundPanel.createFlatButton("Mostra Riepilogo", e -> mostraRiepilogo(datePicker),
				new Dimension(150, 50));
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		add(riepilogoButton, gbc);

		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			resetFields(); // Pulisce i campi
			BackgroundPanel.showPanel("createGiocatore");
		}, new Dimension(150, 30));
		backButton.setForeground(Color.GRAY);
		backButton.setBackground(Color.DARK_GRAY);
		backButton.setFont(new Font("Arial", Font.BOLD, 18));
		gbc.gridy = 7;
		add(backButton, gbc);
	}

	/**
	 * Apre il dialog per selezionare il centro sportivo. Carica i dati dei centri
	 * disponibili dalle province e consente di scegliere un centro.
	 */
	private void apriSelezionaCentroDialog() {
		String filePathProvince = "src/main/java/localizzazione/comuni.csv";
		Map<String, List<String>> provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePathProvince);
		Map<String, Map<String, Integer>> centriByProvince = new HashMap<>();
		Map<String, String> comuniByCentro = new HashMap<>();

		// Verifica e popola la mappa dei centri
		for (String provincia : provinceComuni.keySet()) {
			Map<String, CentroSportivo> centri = DataBase.getCentriSportiviPerProvincia(provincia);
			if (centri != null) { // Verifica che i centri siano disponibili
				Map<String, Integer> centriMap = new HashMap<>();
				for (CentroSportivo centro : centri.values()) {
					centriMap.put(centro.getNome(), centro.getID());
					comuniByCentro.put(centro.getNome(), centro.getComune());
				}
				centriByProvince.put(provincia, centriMap);
			}
		}

		SelezionaDialog selezionaDialog = new SelezionaDialog("Seleziona Centro", filePathProvince, centriByProvince,
				comuniByCentro);
		selezionaDialog.setVisible(true);

		String centroSelezionato = selezionaDialog.getSelezione();
		centroSelezionatoID = (Integer) selezionaDialog.getSelezioneID();

		// Aggiorna la label in base alla selezione
		if (centroSelezionato != null && centroSelezionatoID != null) {
			centroSelezionatoLabel.setText(centroSelezionato);
		} else {
			centroSelezionatoLabel.setText("Non selezionato");
		}
	}

	/**
	 * Apre il dialog per selezionare un campo sportivo associato al centro
	 * selezionato. Verifica che un centro sia stato selezionato prima di procedere.
	 */
	private void apriSelezionaCampoDialog() {
		String centroCorrente = centroSelezionatoLabel.getText().trim();

		if (centroCorrente.equals("Non selezionato")) {
			JOptionPane.showMessageDialog(this, "Seleziona prima un centro.", "Errore", JOptionPane.ERROR_MESSAGE);
			return;
		}

		CentroSportivo centro = DataBase.getCentroByName(centroCorrente);
		if (centro == null) {
			JOptionPane.showMessageDialog(this, "Errore nel recupero del centro selezionato.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Map<String, Integer> campi = DataBase.getCampiCentroMappa(centro.getID());
		if (campi == null || campi.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Nessun campo disponibile per il centro selezionato.", "Errore",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		SelezionaCampoDialog selezionaCampoDialog = new SelezionaCampoDialog("Seleziona Campo", campi);
		selezionaCampoDialog.setVisible(true);

		String campoSelezionato = selezionaCampoDialog.getSelezione();
		campoSelezionatoID = selezionaCampoDialog.getSelezioneID();

		if (campoSelezionato != null && campoSelezionatoID != null) {
			campoSelezionatoLabel.setText(campoSelezionato);
		} else {
			campoSelezionatoLabel.setText("Non selezionato");
		}
	}

	/**
	 * Mostra un riepilogo dei dettagli della prenotazione selezionata. Include
	 * centro, campo, data, orari e costo, con opzione per confermare la
	 * prenotazione.
	 *
	 * @param datePicker Il selettore della data utilizzato per la prenotazione.
	 */
	private void mostraRiepilogo(JXDatePicker datePicker) {

		// Controlli preliminari
		if (campoSelezionatoID == null) {
			CustomMessage.show("Seleziona un campo prima di procedere.", "Errore", false);
			return;
		}

		if (datePicker.getDate() == null) {
			CustomMessage.show("Seleziona una data valida.", "Errore", false);
			return;
		}

		try {
			// Ottieni i valori degli spinner e convertili in Time
			String oraInizioString = (String) oraInizioSpinner.getValue() + ":00";
			String oraFineString = (String) oraFineSpinner.getValue() + ":00";
			java.sql.Time oraInizio = java.sql.Time.valueOf(oraInizioString);
			java.sql.Time oraFine = java.sql.Time.valueOf(oraFineString);

			// Converte la data dal JXDatePicker in java.sql.Date
			java.sql.Date dataPrenotazione = new java.sql.Date(datePicker.getDate().getTime());

			// Ottieni gli ID necessari
			int idSessione = Sessione.getId();

			// Crea l'oggetto Prenotazione
			Prenotazione prenotazione = new Prenotazione(dataPrenotazione, oraInizio, oraFine, idSessione,
					campoSelezionatoID);

			// Calcola il costo della prenotazione
			double costo = DettagliPrenotazioneDialog.calcolaCosto(prenotazione);

			// Formattazione dei dettagli per il riepilogo
			String riepilogoCentro = "Centro: " + centroSelezionatoLabel.getText();
			String riepilogoCampo = "Campo: " + campoSelezionatoLabel.getText();
			String riepilogoData = "Data: " + new SimpleDateFormat("dd-MM-yyyy").format(dataPrenotazione);
			String riepilogoOrario = "Orario: " + oraInizio.toString() + " - " + oraFine.toString();
			String riepilogoCosto = "Costo: €" + costo;

			// Pannello principale con GridBagLayout
			JPanel mainPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.insets = new Insets(10, 10, 10, 10); // Margini generali

			// Aggiungi le etichette al pannello
			JLabel labelCentro = new JLabel(riepilogoCentro);
			JLabel labelCampo = new JLabel(riepilogoCampo);
			JLabel labelData = new JLabel(riepilogoData);
			JLabel labelOrario = new JLabel(riepilogoOrario);
			JLabel labelCosto = new JLabel(riepilogoCosto);

			// Imposta font e allineamento per le etichette
			for (JLabel label : new JLabel[] { labelCentro, labelCampo, labelData, labelOrario, labelCosto }) {
				label.setFont(new Font("Segoe UI", Font.PLAIN, 18));
				label.setAlignmentX(Component.CENTER_ALIGNMENT);
			}

			// Posizionamento etichette
			gbc.gridy = 0;
			mainPanel.add(labelCentro, gbc);
			gbc.gridy = 1;
			mainPanel.add(labelCampo, gbc);
			gbc.gridy = 2;
			mainPanel.add(labelData, gbc);
			gbc.gridy = 3;
			mainPanel.add(labelOrario, gbc);
			gbc.gridy = 4;
			mainPanel.add(labelCosto, gbc);

			// Aggiungi uno spazio
			gbc.gridy = 5;
			mainPanel.add(Box.createVerticalStrut(5), gbc);

			// Creazione della finestra di dialogo
			JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Riepilogo Prenotazione",
					true);
			dialog.getContentPane().add(mainPanel);
			dialog.setSize(700, 500);
			dialog.setLocationRelativeTo(this);

			// Verifica disponibilità e gestione pulsanti
			if (!verificaDisponibilita(prenotazione)) {
				dialog.dispose();
				return;
			}

			JButton confermaButton = BackgroundPanel.createFlatButton("Conferma Prenotazione", e -> {
				try {
					GestorePagamenti gestorePagamenti = new GestorePagamenti();

					// Mostra la schermata di pagamento
					gestorePagamenti.mostraSchermataPagamento(prenotazione, 0);

					// Pausa di 3 secondi
					Thread.sleep(3000); // Pausa di 3 secondi (3000 millisecondi)

					// Verifica se il pagamento è stato effettuato
					if (gestorePagamenti.isPagamentoEffettuato()) {
						// Inserimento prenotazione solo se il pagamento è confermato
						DataBase.inserisciPrenotazione(prenotazione);
						CustomMessage.show("Prenotazione confermata!", "Conferma", true);
						dialog.dispose(); // Chiude il dialogo
					} else {
						CustomMessage.show("Pagamento non riuscito. Riprova.", "Errore", false);
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
					CustomMessage.show("Errore nel database: " + ex.getMessage(), "Errore", false);
				} catch (Exception ex) {
					ex.printStackTrace();
					CustomMessage.show("Errore imprevisto: " + ex.getMessage(), "Errore", false);
				}
				resetFields();
			}, new Dimension(400, 50));

			confermaButton.setFont(new Font("Arial", Font.BOLD, 18));

			JButton chiudiButton = BackgroundPanel.createFlatButton("Chiudi", e -> dialog.dispose(),
					new Dimension(400, 30));
			chiudiButton.setFont(new Font("Arial", Font.BOLD, 18));
			chiudiButton.setBackground(Color.DARK_GRAY);
			chiudiButton.setForeground(Color.GRAY);

			gbc.gridy = 6;
			gbc.gridwidth = 2;
			mainPanel.add(confermaButton, gbc);
			gbc.gridy = 7;
			mainPanel.add(chiudiButton, gbc);

			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifica la disponibilità del campo per la prenotazione richiesta. Controlla
	 * sovrapposizioni con altre prenotazioni e validità degli orari.
	 *
	 * @param p La prenotazione da verificare.
	 * @return true se il campo è disponibile, false altrimenti.
	 */
	public static boolean verificaDisponibilita(Prenotazione p) {

		// Controllo preliminare: verifica che oraInizio non sia successiva a oraFine
		if (p.getOraInizio() == null || p.getOraFine() == null || p.getOraInizio().after(p.getOraFine())) {
			CustomMessage.show("L'ora di inizio deve essere precedente a quella di fine.", "Errore", false);
			return false;
		}

		// Definisci gli orari limite (apertura alle 8:00 e chiusura a mezzanotte)
		Time oraApertura = Time.valueOf("08:00:00");
		Time oraChiusura = Time.valueOf("23:00:00");

		// Controlla che gli orari di inizio e fine siano all'interno dell'intervallo
		if (p.getOraInizio().before(oraApertura) || p.getOraFine().after(oraChiusura)) {
			if (p.getOraInizio().before(oraApertura)) {
				CustomMessage.show("Il campo apre alle 8:00. Seleziona un orario valido.", "Errore", false);
			} else if (p.getOraFine().after(oraChiusura)) {
				CustomMessage.show("Il campo chiude alle 23. Seleziona un orario valido.", "Errore", false);
			}
			return false;
		}

		// Recupera il campo associato
		Campo campo = DataBase.getCampoById(p.getCampoId());
		if (campo == null) {
			CustomMessage.show("Campo non trovato.", "Errore", false);
			return false;
		}

		// Recupera tutte le prenotazioni esistenti per lo stesso campo e data
		List<Prenotazione> prenotazioniEsistenti = DataBase.getPrenotazioniByCampo(campo.getCentroId(), p.getCampoId());

		// Controlla sovrapposizioni con le prenotazioni esistenti
		for (Prenotazione prenotazione : prenotazioniEsistenti) {

			// Escludi la prenotazione corrente dal controllo
			if (prenotazione.getId() == p.getId()) {
				continue; // Salta la prenotazione corrente
			}

			// Verifica che la data sia la stessa
			if (!prenotazione.getData().equals(p.getData())) {
				continue; // Salta le prenotazioni di date diverse
			}

			// Recupera gli orari della prenotazione esistente
			Time oraInizioEsistente = prenotazione.getOraInizio();
			Time oraFineEsistente = prenotazione.getOraFine();

			// Controlla sovrapposizione
			if (isOverlapping(p.getOraInizio(), p.getOraFine(), oraInizioEsistente, oraFineEsistente)) {
				CustomMessage.show("Orario non disponibile", "Errore", false);
				return false;
			}
		}

		// Nessuna sovrapposizione trovata, campo disponibile
		return true;
	}

	/**
	 * Controlla se due intervalli di tempo si sovrappongono.
	 *
	 * @param start1 L'ora di inizio del primo intervallo.
	 * @param end1   L'ora di fine del primo intervallo.
	 * @param start2 L'ora di inizio del secondo intervallo.
	 * @param end2   L'ora di fine del secondo intervallo.
	 * @return true se gli intervalli si sovrappongono, false altrimenti.
	 */
	private static boolean isOverlapping(Time start1, Time end1, Time start2, Time end2) {
		// Verifica se l'intervallo [start1, end1] si sovrappone a [start2, end2]
		return !end1.before(start2) && !start1.after(end2);
	}

	/**
	 * Crea uno spinner personalizzato per la selezione di orari. Gli orari
	 * disponibili includono incrementi di 30 minuti.
	 *
	 * @return Un oggetto JSpinner configurato.
	 */
	private JSpinner createCustomTimeSpinner() {
		List<String> times = generateTimeValues();
		JSpinner timeSpinner = new JSpinner(new SpinnerListModel(times));
		timeSpinner.setValue("08:00");
		((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
		return timeSpinner;
	}

	/**
	 * Genera una lista di orari incrementati di 30 minuti.
	 *
	 * @return Una lista di stringhe che rappresentano gli orari.
	 */
	private List<String> generateTimeValues() {
		List<String> times = new ArrayList<>();
		for (int h = 0; h < 24; h++) {
			times.add(String.format("%02d:00", h));
			times.add(String.format("%02d:30", h));
		}
		return times;
	}
	
	/**
	 * Resetta tutti i campi del pannello.
	 */
	private void resetFields() {
	    centroSelezionatoLabel.setText("Non selezionato");
	    campoSelezionatoLabel.setText("Non selezionato");
	    datePicker.setDate(null); // Imposta la data a null per pulirla
	    oraInizioSpinner.setValue("08:00");
	    oraFineSpinner.setValue("09:00");
	}


	/**
	 * Override del metodo paintComponent per disegnare l'immagine di sfondo.
	 *
	 * @param g L'oggetto Graphics utilizzato per il rendering.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (clearImage != null) {
			g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
		}
	}

}
