package GUI;

import org.jdesktop.swingx.JXDatePicker;

import components.CentroSportivo;
import components.Prenotazione;
import components.Sessione;
import dataBase.DataBase;
import localizzazione.FileReaderUtils;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
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

	public InserisciPrenotazionePanel(CardLayout cardLayout, JPanel cardPanel) {
		// Carica l'immagine di sfondo
		URL clearImageUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
		if (clearImageUrl != null) {
			clearImage = new ImageIcon(clearImageUrl).getImage();
		} else {
			System.out.println("Errore nel caricamento dell'immagine: GUI/immagini/sfondohome.png");
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
		JXDatePicker datePicker = new JXDatePicker();
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
			Login.resetFields();
			BackgroundPanel.showPanel("createGiocatore");
		}, new Dimension(150, 30));
		backButton.setForeground(Color.GRAY);
		backButton.setBackground(Color.DARK_GRAY);
		backButton.setFont(new Font("Arial", Font.BOLD, 18));
		gbc.gridy = 7;
		add(backButton, gbc);
	}

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

	    SelezionaDialog selezionaDialog = new SelezionaDialog("Seleziona Centro", filePathProvince, centriByProvince, comuniByCentro);
	    selezionaDialog.setVisible(true);

	    String centroSelezionato = selezionaDialog.getSelezione();
	     centroSelezionatoID = (Integer) selezionaDialog.getSelezioneID();

	    // Aggiorna la label in base alla selezione
	    if (centroSelezionato != null && centroSelezionatoID != null) {
	        centroSelezionatoLabel.setText(centroSelezionato);
	        System.out.println("Centro ID selezionato: " + centroSelezionatoID);
	    } else {
	        centroSelezionatoLabel.setText("Non selezionato");
	    }
	}


	private void apriSelezionaCampoDialog() {
	    String centroCorrente = centroSelezionatoLabel.getText().trim();

	    if (centroCorrente.equals("Non selezionato")) {
	        JOptionPane.showMessageDialog(this, "Seleziona prima un centro.", "Errore", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    CentroSportivo centro = DataBase.getCentroByName(centroCorrente);
	    if (centro == null) {
	        JOptionPane.showMessageDialog(this, "Errore nel recupero del centro selezionato.", "Errore", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    Map<String, Integer> campi = DataBase.getCampiCentroMappa(centro.getID());
	    if (campi == null || campi.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Nessun campo disponibile per il centro selezionato.", "Errore", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    SelezionaCampoDialog selezionaCampoDialog = new SelezionaCampoDialog("Seleziona Campo", campi);
	    selezionaCampoDialog.setVisible(true);

	    String campoSelezionato = selezionaCampoDialog.getSelezione();
	     campoSelezionatoID = selezionaCampoDialog.getSelezioneID();

	    if (campoSelezionato != null && campoSelezionatoID != null) {
	        campoSelezionatoLabel.setText(campoSelezionato);
	        System.out.println("Campo ID selezionato: " + campoSelezionatoID);
	    } else {
	        campoSelezionatoLabel.setText("Non selezionato");
	    }
	}


	private void mostraRiepilogo(JXDatePicker datePicker) {
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
	        Prenotazione prenotazione = new Prenotazione(dataPrenotazione, oraInizio, oraFine, idSessione, campoSelezionatoID);

	        // Calcola il costo della prenotazione
	        double costo = prenotazione.calcolaCosto();

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
	        for (JLabel label : new JLabel[]{labelCentro, labelCampo, labelData, labelOrario, labelCosto}) {
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
	        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Riepilogo Prenotazione", true);
	        dialog.getContentPane().add(mainPanel);
	        dialog.setSize(700, 500);
	        dialog.setLocationRelativeTo(this);

	        // Verifica disponibilità e gestione pulsanti
	        if (!prenotazione.verificaDisponibilita()) {
	            dialog.dispose();
	            return;
	        }

	        JButton confermaButton = BackgroundPanel.createFlatButton("Conferma Prenotazione", e -> {
	            JOptionPane.showMessageDialog(dialog, "Prenotazione confermata!", "Conferma", JOptionPane.INFORMATION_MESSAGE);
	            dialog.dispose();
	            try {
	                DataBase.inserisciPrenotazione(prenotazione);
	            } catch (SQLException e1) {
	                e1.printStackTrace();
	            }
	        }, new Dimension(400, 50));
	        confermaButton.setFont(new Font("Arial", Font.BOLD, 18));

	        JButton chiudiButton = BackgroundPanel.createFlatButton("Chiudi", e -> dialog.dispose(), new Dimension(400, 30));
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
	        e.printStackTrace();;
	    }
	}


	private JSpinner createCustomTimeSpinner() {
		List<String> times = generateTimeValues();
		JSpinner timeSpinner = new JSpinner(new SpinnerListModel(times));
		timeSpinner.setValue("08:00");
		((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
		return timeSpinner;
	}

	private List<String> generateTimeValues() {
		List<String> times = new ArrayList<>();
		for (int h = 0; h < 24; h++) {
			times.add(String.format("%02d:00", h));
			times.add(String.format("%02d:30", h));
		}
		return times;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (clearImage != null) {
			g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
	
	
}
