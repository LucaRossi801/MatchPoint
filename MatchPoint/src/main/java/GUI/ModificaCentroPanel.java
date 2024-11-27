package GUI;

import javax.swing.*;

import components.Campo;
import components.CentroSportivo;
import components.Sessione;
import dataBase.DataBase;

import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

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
		JPanel riepilogoPanel = new JPanel();
		riepilogoPanel.setLayout(new BoxLayout(riepilogoPanel, BoxLayout.Y_AXIS));
		riepilogoPanel.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(riepilogoPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(350, 200));

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

		// Pulsante Modifica Campi
		modificaCampiButton = BackgroundPanel.createFlatButton("Modifica Campi", e -> modificaCampi(),
				new Dimension(200, 40));
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		add(modificaCampiButton, gbc);
		
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		add(scrollPane, gbc);
		JButton aggiungiCampoButton = BackgroundPanel.createFlatButton("Aggiungi campo", e-> {// Passa riepilogoPanel invece di riepilogoArea
			new AggiungiCampoDialog(SwingUtilities.getWindowAncestor(this), riepilogoPanel);
		}, new Dimension(300, 50));
       gbc.gridx = 0;
       gbc.gridy = 6;
       gbc.gridwidth = 2;
       add(aggiungiCampoButton, gbc);
       
       JButton eliminaCampoButton = BackgroundPanel.createFlatButton("Elimina centro selezionato", e-> {
       	eliminaCentro();
      	 
      }, new Dimension(150, 40));
      gbc.gridx = 0;
      gbc.gridy = 7;
      gbc.gridwidth = 2;
      eliminaCampoButton.setBackground(Color.RED); // Sfondo al passaggio del mouse
      add(eliminaCampoButton, gbc);
       
		
// Pulsante Salva Modifiche
		salvaButton = BackgroundPanel.createFlatButton("Salva Modifiche", e -> salvaModifiche(),
				new Dimension(200, 50));
		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		salvaButton.setBackground(new Color(0, 128, 128));
		salvaButton.setForeground(Color.WHITE);
		add(salvaButton, gbc);
		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			BackgroundPanel.showPanel("createGestore"); // Torna al pannello di login
		}, new Dimension(150, 30) // Dimensione personalizzata del bottone
		);
		// Personalizza colore per il pulsante "Back"
		backButton.setForeground(Color.GRAY); // Sfondo grigio
		backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
		backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back"
		gbc.gridy = 9; // Quarta riga
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

			boolean success = DataBase.updateCentroSportivo(centro.ID, nuovoNome, nuovaProvincia, nuovoComune);
			
			// aggiungi anche i campi al DB (se ci sono)
			
			ArrayList<Campo> campiSelezionati = AggiungiCampoDialog.getCampi();
			for (Campo c : campiSelezionati) {
				Campo.inserisci(c.getTipologiaCampo(), c.getCostoOraNotturna(), c.costoOraDiurna, c.lunghezza,
						c.larghezza, c.isCoperto(), centro.getID());
			}
			CustomMessage.show("Campo inserito con successo!", "Successo", true);
			BackgroundPanel.showPanel("createGestore"); // Torna al pannello di login
			if (success) {
				CustomMessage.show("Modifiche salvate con successo!", "Successo", true);
				centro.nome = nuovoNome;
				centro.provincia = nuovaProvincia;
				centro.comune = nuovoComune;
				aggiornaDettagliCentro();
				caricaCentriGestiti();
				BackgroundPanel.showPanel("createGestore");
			} else {
				CustomMessage.show( "Errore nel salvataggio delle modifiche.", "Errore", false);
			}
		}
	}

	private void modificaCampi() {
	    String centroSelezionato = (String) centriComboBox.getSelectedItem();
	    if (centroSelezionato != null && centriGestiti != null) {
	        CentroSportivo centro = centriGestiti.get(centroSelezionato);

	        // Verifica se il centro ha campi associati
	        List<Campo> campi = DataBase.getCampiById(centro.getID());
	        if (campi == null || campi.isEmpty()) {
	            // Mostra messaggio di errore e resta sul pannello corrente
	            CustomMessage.show("Nessun campo disponibile per il centro selezionato.", "Errore", false);
	            BackgroundPanel.showPanel("modificaCentro");
	            return;
	        }

	        // Carica il pannello di modifica campi
	        ModificaCampoPanel modificaCampoPanel = new ModificaCampoPanel(centro.getID());
	        BackgroundPanel.cardPanel.add(modificaCampoPanel, "modificaCampi");
	        BackgroundPanel.showPanel("modificaCampi");
	    } else {
	        // Torna al pannello di modifica centro
	        BackgroundPanel.showPanel("modificaCentro");
	    }
	}
	private void eliminaCentro() {
	    // Recupera il nome del centro selezionato dalla ComboBox
	    String centroSelezionato = (String) centriComboBox.getSelectedItem();

	    if (centroSelezionato != null && centriGestiti.containsKey(centroSelezionato)) {
	        CentroSportivo centro = centriGestiti.get(centroSelezionato);

	        // Chiedi conferma all'utente
	        int conferma = JOptionPane.showConfirmDialog(this,
	                "Sei sicuro di voler eliminare il centro \"" + centro.nome + "\"?",
	                "Conferma eliminazione",
	                JOptionPane.YES_NO_OPTION);

	        if (conferma == JOptionPane.YES_OPTION) {
	            // Elimina il centro dal database
	            boolean success = DataBase.eliminaCentro(centro.getID());

	            if (success) {
	                CustomMessage.show("Centro eliminato con successo!", "Successo", true);

	                // Rimuovi il centro dalla mappa e aggiorna la ComboBox
	                centriGestiti.remove(centroSelezionato);
	                centriComboBox.removeItem(centroSelezionato);

	                // Eventuali ulteriori aggiornamenti dell'interfaccia
	                nomeField.setText("");
	                provinciaField.setText("");
	                comuneField.setText("");
	            } else {
	                CustomMessage.show("Errore durante l'eliminazione del centro.", "Errore", false);
	            }
	        }
	    } else {
	        CustomMessage.show("Nessun centro selezionato.", "Errore", false);
	    }
	}

}
