package GUI;

import javax.swing.*;

import components.Campo;
import dataBase.DataBase;
import dataBase.Sessione;
import localizzazione.FileReaderUtils;

import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Classe responsabile della creazione del pannello per l'inserimento di un
 * nuovo centro sportivo. Consente di selezionare nome, provincia, comune e
 * campi sportivi associati.
 */
public class InserisciCentroPanel extends JPanel {
	private Image background; // Immagine di sfondo
	private JComboBox<String> provinciaComboBox; // Dropdown per la selezione della provincia
	private JComboBox<String> comuneComboBox; // Dropdown per la selezione del comune

	/**
	 * Costruttore della classe. Inizializza il pannello con i componenti necessari
	 * per l'inserimento del centro.
	 *
	 * @param cardLayout Gestore di layout per la navigazione tra pannelli.
	 * @param cardPanel  Pannello principale contenente i vari componenti.
	 */
	public InserisciCentroPanel(CardLayout cardLayout, JPanel cardPanel) {
		// Carica dati dal file CSV
		String filePath = "src/main/java/localizzazione/comuni.csv"; // Sostituisci con il percorso corretto
		Map<String, List<String>> provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePath);

		// Inizializza ComboBox
		provinciaComboBox = new JComboBox<>(provinceComuni.keySet().toArray(new String[0]));
		int ButtonFontDim = 18;
		provinciaComboBox.setFont(new Font("Montserrat", Font.PLAIN, ButtonFontDim));
		comuneComboBox = new JComboBox<>();
		comuneComboBox.setFont(new Font("Montserrat", Font.PLAIN, 18));
		// Resetta combobox
		provinciaComboBox.setSelectedIndex(-1); // Deseleziona la provincia
		provinciaComboBox.addActionListener(e -> {
			String provinciaSelezionata = (String) provinciaComboBox.getSelectedItem();
			comuneComboBox.removeAllItems();
			if (provinciaSelezionata != null) {
				List<String> comuni = provinceComuni.get(provinciaSelezionata);
				if (comuni != null) {
					for (String comune : comuni) {
						comuneComboBox.addItem(comune);
						comuneComboBox.setSelectedIndex(-1); // Deseleziona i comuni

					}
				}
			}
		});

		JPanel riepilogoPanel = new JPanel();
		riepilogoPanel.setLayout(new BoxLayout(riepilogoPanel, BoxLayout.Y_AXIS));
		riepilogoPanel.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(riepilogoPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(350, 200));

		// Carica immagine di sfondo
		URL backgroundUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
		if (backgroundUrl != null) {
			background = new ImageIcon(backgroundUrl).getImage();
		} else {
			String ImageNotFound = "Errore nel caricamento dell'immagine di sfondo.";
			System.out.println(ImageNotFound);
		}

		// Layout e configurazione
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Titolo
		JLabel titleLabel = new OutlinedLabel("Inserisci Centro", Color.WHITE);
		titleLabel.setFont(new Font("Montserrat", Font.BOLD, 30));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // Il titolo occupa due colonne
		gbc.anchor = GridBagConstraints.CENTER;
		add(titleLabel, gbc);

		// Mappa per gestire i campi
		Map<String, JTextField> fields = new HashMap<>();

		// Campi di testo
		String[] campi = { "NomeCentro" };
		int row = 1; // Riga di partenza

		int LabeFontDim = 24;
		for (String campo : campi) {
			// Etichetta
			JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
			label.setFont(new Font("Montserrat", Font.BOLD, LabeFontDim));
			gbc.gridx = 0; // Colonna sinistra
			gbc.gridy = row;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			add(label, gbc);

			// Campo di input
			JTextField inputField = new JTextField(20);
			inputField.setFont(new Font("Arial", Font.PLAIN, ButtonFontDim));
			gbc.gridx = 1; // Colonna destra
			add(inputField, gbc);

			// Salva il campo nella mappa
			fields.put(campo, inputField);

			row++;
		}
		// Provincia
		JLabel provinciaLabel = new OutlinedLabel("Provincia:", Color.BLACK);
		provinciaLabel.setFont(new Font("Montserrat", Font.BOLD, LabeFontDim));
		gbc.gridx = 0;
		gbc.gridy = row;
		add(provinciaLabel, gbc);

		gbc.gridx = 1;
		add(provinciaComboBox, gbc);
		row++;

		// Comune
		JLabel comuneLabel = new OutlinedLabel("Comune:", Color.BLACK);
		comuneLabel.setFont(new Font("Montserrat", Font.BOLD, LabeFontDim));
		gbc.gridx = 0;
		gbc.gridy = row;
		add(comuneLabel, gbc);

		gbc.gridx = 1;
		add(comuneComboBox, gbc);
		row++;

		// Bottone per aggiungere campo
		JButton aggiungiCampoButton = BackgroundPanel.createFlatButton("Aggiungi Campo", e -> {
			// Passa riepilogoPanel invece di riepilogoArea
			new AggiungiCampoDialog(SwingUtilities.getWindowAncestor(this), riepilogoPanel);
		}, new Dimension(300, 50));

		aggiungiCampoButton.setFont(new Font("Arial", Font.BOLD, ButtonFontDim));
		aggiungiCampoButton.setBackground(new Color(32, 178, 170));
		aggiungiCampoButton.setForeground(new Color(220, 250, 245));
		aggiungiCampoButton.setFocusPainted(false);

		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		add(aggiungiCampoButton, gbc);
		row++;

		// Aggiungi il JTextArea per il riepilogo
		gbc.gridy = row;
		gbc.gridwidth = 2;
		add(scrollPane, gbc);

		// Bottone Inserisci Centro
		JButton inserisciCentroButton = BackgroundPanel.createFlatButton("Inserisci Centro", e -> {
			// Recupera i dati inseriti e fai l'inserimento
			String nomeCentro = fields.get("NomeCentro").getText();
			String provincia = (String) provinciaComboBox.getSelectedItem();
			String comune = (String) comuneComboBox.getSelectedItem();

			// Controlla se tutti i campi sono stati compilati
			String er = "Errore";
			if (nomeCentro == null || provincia == null || comune == null || nomeCentro.isEmpty() || provincia.isEmpty()
					|| comune.isEmpty()) {
				String CompAll = "Compila tutti i campi!";
				CustomMessage.show(CompAll, er, false);
				return;
			}
			// aggiunge il centro sportivo al DB
			DataBase.insert(nomeCentro, provincia, comune, Sessione.getId());
			String S = "Successo";
			CustomMessage.show("Centro inserito con successo!", S, true);
			// Cambia schermata
			cardLayout.show(cardPanel, "createGestore");
			// Resetta tutti i campi
		    resetFields(fields, riepilogoPanel);
		    
			// Seleziona ID del centro sportivo creato
			String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
			String sql = "SELECT ID FROM CentroSportivo WHERE Nome ='" + nomeCentro + "' AND Comune = '" + comune + "'";
			String ris = "";
			try (Connection conn = DriverManager.getConnection(url)) {
				ris = DataBase.eseguiSelect(conn, sql);
			} catch (SQLException ex) {
				ex.printStackTrace();
				String DBerror = "Errore di connessione al DataBase";
				CustomMessage.show(DBerror, er, false);
				return;
			}
			int idGestore = Integer.parseInt(ris);
			// aggiungi anche i campi al DB (se ci sono)
			ArrayList<Campo> campiSelezionati = AggiungiCampoDialog.getCampi();
			for (Campo c : campiSelezionati) {
				DataBase.insert(c.getTipologiaCampo(), c.getCostoOraNotturna(), c.costoOraDiurna, c.lunghezza,
						c.larghezza, c.isCoperto(), idGestore);
			}

		}, new Dimension(300, 50));

		inserisciCentroButton.setFont(new Font("Arial", Font.BOLD, ButtonFontDim));
		inserisciCentroButton.setBackground(new Color(0, 128, 128));
		inserisciCentroButton.setForeground(Color.WHITE);

		gbc.gridy = row + 2;
		add(inserisciCentroButton, gbc);

		// Bottone Indietro
		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			
			// Resetta tutti i campi
		    resetFields(fields, riepilogoPanel);
			// Svuota tutti i campi di input
			fields.values().forEach(field -> field.setText(""));

			// Svuota i campi sportivi creati
			AggiungiCampoDialog.getCampi().clear();

			// Rimuovi i pannelli dei campi dal riepilogo
			riepilogoPanel.removeAll();
			riepilogoPanel.revalidate();
			riepilogoPanel.repaint();

			// Cambia schermata
			cardLayout.show(cardPanel, "createGestore");
		}, new Dimension(120, 30));

		backButton.setFont(new Font("Arial", Font.BOLD, ButtonFontDim));
		backButton.setForeground(Color.GRAY);
		backButton.setBackground(Color.DARK_GRAY);

		gbc.gridy = row + 3;
		gbc.anchor = GridBagConstraints.CENTER;
		add(backButton, gbc);

	}

	/**
	 * Resetta tutti i campi del pannello, inclusi JTextField, JComboBox e il
	 * JTextArea.
	 */
	public void resetFields(Map<String, JTextField> fields, JPanel riepilogoPanel) {
		// Pulisci i JTextField
		fields.values().forEach(field -> field.setText(""));

		// Reset delle JComboBox
		provinciaComboBox.setSelectedIndex(-1); // Deseleziona la provincia
		comuneComboBox.removeAllItems(); // Svuota i comuni

		// Rimuovi i pannelli dal riepilogo
		riepilogoPanel.removeAll();
		riepilogoPanel.revalidate();
		riepilogoPanel.repaint();
	}

	/**
	 * Override del metodo paintComponent per disegnare l'immagine di sfondo.
	 *
	 * @param g Oggetto Graphics utilizzato per disegnare il pannello.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (background != null) {
			g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		}
	}
}