package GUI;

import javax.swing.*;
import components.CentroSportivo;
import components.Sessione;
import dataBase.DataBase;

import java.awt.*;
import java.net.URL;
import java.util.Map;

public class ModificaCentroPanel extends JPanel {
	private JComboBox<String> centriComboBox; // ComboBox per selezione centri
	private JTextField nomeField, provinciaField, comuneField;
	private JButton salvaButton, modificaCampiButton;
	private Map<String, CentroSportivo> centriGestiti; // Mappa nome-centro
	private Integer utenteId; // ID dell'utente loggato
	private DataBase dataBase; // Database di riferimento
	private Image clearImage;

	public ModificaCentroPanel() {
		// Carica l'immagine di sfondo
		try {
			URL clearImageUrl = getClass().getResource("/GUI/immagini/sfondohome.png");
			if (clearImageUrl != null) {
				clearImage = new ImageIcon(clearImageUrl).getImage();
			} else {
				System.err.println("Errore nel caricamento dell'immagine: /GUI/immagini/sfondohome.png");
			}
		} catch (Exception e) {
			System.err.println("Eccezione durante il caricamento dell'immagine: " + e.getMessage());
		}

		setLayout(new GridBagLayout()); // Imposta il layout principale come GridBagLayout
		setBackground(new Color(240, 248, 255));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL; // Riempie orizzontalmente

		// Inizializza ID utente dalla sessione
		utenteId = Sessione.getId();

		// Pannello superiore per selezione del centro
		JPanel selezionePanel = new JPanel(new FlowLayout());
		selezionePanel.setBackground(new Color(32, 178, 170)); // Sfondo verde
		JLabel selezionaLabel = new OutlinedLabel("Seleziona Centro:", Color.WHITE);
		selezionaLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
		selezionaLabel.setForeground(Color.WHITE);
		selezionePanel.add(selezionaLabel);

		centriComboBox = new JComboBox<>();
		centriComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
		centriComboBox.addActionListener(e -> aggiornaDettagliCentro());
		selezionePanel.add(centriComboBox);

		// Posiziona il pannello superiore
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // Occupa due colonne
		add(selezionePanel, gbc);

		// Campo Nome
		JLabel nomeLabel = new OutlinedLabel("Nome:", Color.BLACK);
		nomeLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(nomeLabel, gbc);

		nomeField = new JTextField(20);
		nomeField.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		add(nomeField, gbc);

		// Campo Provincia
		JLabel provinciaLabel = new OutlinedLabel("Provincia:", Color.BLACK);
		provinciaLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(provinciaLabel, gbc);

		provinciaField = new JTextField(20);
		provinciaField.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		add(provinciaField, gbc);

		// Campo Comune
		JLabel comuneLabel = new OutlinedLabel("Comune:", Color.BLACK);
		comuneLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(comuneLabel, gbc);

		comuneField = new JTextField(20);
		comuneField.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.EAST;
		add(comuneField, gbc);

		// Pulsante Salva Modifiche
		salvaButton = BackgroundPanel.createFlatButton("Salva Modifiche", e -> salvaModifiche(),
				new Dimension(200, 50));
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		salvaButton.setBackground(new Color(0, 128, 128));
		salvaButton.setForeground(Color.WHITE);
		add(salvaButton, gbc);

		// Pulsante Modifica Campi
		modificaCampiButton = BackgroundPanel.createFlatButton("Modifica Campi", e -> modificaCampi(),
				new Dimension(200, 50));
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		add(modificaCampiButton, gbc);

		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			BackgroundPanel.showPanel("createGestore"); // Torna al pannello di login
		}, new Dimension(150, 30) // Dimensione personalizzata del bottone
		);
		// Personalizza colore per il pulsante "Back"
		backButton.setForeground(Color.GRAY); // Sfondo grigio
		backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
		backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back"
		gbc.gridy = 6; // Quarta riga
		add(backButton, gbc);

		// Carica i centri sportivi gestiti
		caricaCentriGestiti();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (clearImage != null) {
			g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
		}
	}

	private void caricaCentriGestiti() {
		// Controlla se l'utente è loggato
		utenteId = Sessione.getId();
		if (utenteId != null && utenteId > 0) {
			centriGestiti = dataBase.getCentriSportiviGestiti(utenteId); // Ottieni centri gestiti
			centriComboBox.removeAllItems(); // Svuota ComboBox

			if (centriGestiti != null && !centriGestiti.isEmpty()) {
				for (String nomeCentro : centriGestiti.keySet()) {
					centriComboBox.addItem(nomeCentro);
				}
				aggiornaDettagliCentro(); // Aggiorna dettagli del primo centro
			}
		}
	}

	private void aggiornaDettagliCentro() {
		// Aggiorna i dettagli del centro selezionato
		String centroSelezionato = (String) centriComboBox.getSelectedItem();
		if (centroSelezionato != null && centriGestiti != null) {
			CentroSportivo centro = centriGestiti.get(centroSelezionato);
			if (centro != null) {
				nomeField.setText(centro.nome);
				provinciaField.setText(centro.provincia);
				comuneField.setText(centro.comune);
			}
		}
	}

	private void salvaModifiche() {
		String centroSelezionato = (String) centriComboBox.getSelectedItem();
		if (centroSelezionato != null && centriGestiti != null) {
			CentroSportivo centro = centriGestiti.get(centroSelezionato);

			String nuovoNome = nomeField.getText();
			String nuovaProvincia = provinciaField.getText();
			String nuovoComune = comuneField.getText();

			boolean success = dataBase.updateCentroSportivo(centro.ID, nuovoNome, nuovaProvincia, nuovoComune);

			if (success) {
				JOptionPane.showMessageDialog(this, "Modifiche salvate con successo!", "Successo",
						JOptionPane.INFORMATION_MESSAGE);
				centro.nome = nuovoNome;
				centro.provincia = nuovaProvincia;
				centro.comune = nuovoComune;
			} else {
				JOptionPane.showMessageDialog(this, "Errore nel salvataggio delle modifiche.", "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void modificaCampi() {
		String centroSelezionato = (String) centriComboBox.getSelectedItem();
		if (centroSelezionato != null && centriGestiti != null) {
			CentroSportivo centro = centriGestiti.get(centroSelezionato);

			ModificaCampiDialog modificaCampiDialog = new ModificaCampiDialog(SwingUtilities.getWindowAncestor(this),
					centro, dataBase);
			modificaCampiDialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this, "Seleziona un centro sportivo per modificarne i campi.", "Attenzione",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}