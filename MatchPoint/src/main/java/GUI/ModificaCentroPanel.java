package GUI;

import javax.swing.*;

import components.Campo;
import components.CentroSportivo;
import dataBase.DataBase;
import dataBase.Sessione;
import localizzazione.FileReaderUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModificaCentroPanel extends JPanel {
	private JComboBox<String> centriComboBox; // ComboBox per selezione centri
	private JComboBox<String> provinciaComboBox; // ComboBox per selezione centri
	private JComboBox<String> comuneComboBox; // ComboBox per selezione centri
	private JTextField nomeField, provinciaField, comuneField;
	private JButton salvaButton, modificaCampiButton;
	private Map<String, CentroSportivo> centriGestiti; // Mappa nome-centro
	private Integer utenteId; // ID dell'utente loggato
	private DataBase dataBase; // Database di riferimento
	private Image clearImage;
	private JPanel riepilogoPanel; // Rendi riepilogoPanel una variabile di istanza
	private Map<String, List<String>> provinceComuni; // Mappa province -> comuni

	public ModificaCentroPanel() {
		// Carica i dati delle province e comuni
		String filePath = "src/main/java/localizzazione/comuni.csv"; 
        provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePath);
        if (provinceComuni == null || provinceComuni.isEmpty()) {
            System.err.println("Errore: la mappa provinceComuni è vuota o null.");
            provinceComuni = new HashMap<>(); // Prevenire errori futuri
        }
		
		// Carica l'immagine di sfondo
		riepilogoPanel = new JPanel();
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
				String ImageNotFound = "Errore nel caricamento dell'immagine: /GUI/immagini/sfondohome.png";
				System.err.println(ImageNotFound);
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
        add(provinciaLabel, gbc);

        provinciaComboBox = new JComboBox<>(provinceComuni.keySet().toArray(new String[0]));
        provinciaComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        provinciaComboBox.addActionListener(e -> aggiornaComuni());
        gbc.gridx = 1;
        add(provinciaComboBox, gbc);

        // Campo Comune
        JLabel comuneLabel = new OutlinedLabel("Comune:", Color.BLACK);
        comuneLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(comuneLabel, gbc);

        comuneComboBox = new JComboBox<>();
        comuneComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        add(comuneComboBox, gbc);

		// Pulsante Modifica Campi
		modificaCampiButton = BackgroundPanel.createFlatButton("Modifica campi", e -> modificaCampi(),
				new Dimension(200, 40));
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.EAST;
		add(modificaCampiButton, gbc);
		
		gbc.gridy = 6;
		gbc.gridwidth = 2;
		add(scrollPane, gbc);
		AggiungiCampoDialog.clear();
		JButton aggiungiCampoButton = BackgroundPanel.createFlatButton("Aggiungi campo", e-> {// Passa riepilogoPanel invece di riepilogoArea
			new AggiungiCampoDialog(SwingUtilities.getWindowAncestor(this), riepilogoPanel);
		}, new Dimension(200, 40));
       gbc.gridx = 0;
       gbc.gridy = 5;
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
       
		
       buttonSalvaModifiche(gbc);
		
	buttonBack(gbc);

		// Carica i centri sportivi gestiti
		caricaCentriGestiti();
	}

	private void buttonBack(GridBagConstraints gbc) {
		//pulsante Back
		JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			BackgroundPanel.showPanel("createGestore"); // Torna al pannello di login
			resetForm(riepilogoPanel); // Resetta i campi e il pannello riepilogo
		}, new Dimension(150, 30) // Dimensione personalizzata del bottone
		);
		// Personalizza colore per il pulsante "Back"
		backButton.setForeground(Color.GRAY); // Sfondo grigio
		backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
		backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back"
		gbc.gridy = 9; // Quarta riga
		add(backButton, gbc);
	}

	private void buttonSalvaModifiche(GridBagConstraints gbc) {
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
	            // Ordina i nomi dei centri in ordine alfabetico
	            List<String> nomiCentri = new ArrayList<>(centriGestiti.keySet());
	            nomiCentri.sort(String::compareToIgnoreCase);

	            // Aggiungi i nomi ordinati alla ComboBox
	            for (String nomeCentro : nomiCentri) {
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
				nomeField.setText(centro.getNome());
				provinciaComboBox.setSelectedItem(centro.getProvincia());
                comuneComboBox.setSelectedItem(centro.getComune());
			}
		}
	}

	private void salvaModifiche() {
		String centroSelezionato = (String) centriComboBox.getSelectedItem();
		if (centroSelezionato != null && centriGestiti != null) {
			CentroSportivo centro = centriGestiti.get(centroSelezionato);

			String nuovoNome = nomeField.getText(); // Questo è corretto, poiché il nome è un JTextField
			String nuovaProvincia = (String) provinciaComboBox.getSelectedItem(); // Ottieni l'elemento selezionato dalla JComboBox
			String nuovoComune = (String) comuneComboBox.getSelectedItem(); // Ottieni l'elemento selezionato dalla JComboBox

			boolean success = DataBase.updateCentroSportivo(centro.getID(), nuovoNome, nuovaProvincia, nuovoComune);
			
			// aggiungi anche i campi al DB (se ci sono)
			
			ArrayList<Campo> campiSelezionati = AggiungiCampoDialog.getCampi();
			for (Campo c : campiSelezionati) {
				DataBase.insert(c.getTipologiaCampo(), c.getCostoOraNotturna(), c.costoOraDiurna, c.lunghezza,
						c.larghezza, c.isCoperto(), centro.getID());
			}
			
			if (success) {
				CustomMessage.show("Modifiche salvate con successo!", "Successo", true);
				BackgroundPanel.showPanel("createGestore"); // Torna al pannello di login
				nuovoNome = centro.getNome();
				nuovaProvincia = centro.getProvincia();
				nuovoComune = centro.getComune();
				aggiornaDettagliCentro();
				caricaCentriGestiti();
				resetForm(riepilogoPanel); // Resetta i campi e il pannello riepilogo
				BackgroundPanel.showPanel("createGestore");
			} else {
				String Moderror = "Errore nel salvataggio delle modifiche.";
				String er = "Errore";
				CustomMessage.show( Moderror, er, false);
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
	            String er = "Errore";
				CustomMessage.show("Nessun campo disponibile per il centro selezionato.", er, false);
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

	    String er = "Errore";
		if (centroSelezionato != null && centriGestiti.containsKey(centroSelezionato)) {
	        CentroSportivo centro = centriGestiti.get(centroSelezionato);

	        // Chiedi conferma all'utente
	        boolean conferma = CustomMessageWithChoice.show(
                    "Sei sicuro di voler eliminare il centro?",
                    "Conferma Cancellazione",
                    false // Utilizza il colore rosso per il messaggio di errore
                );
	        if (conferma) {
	            // Elimina il centro dal database
	            boolean success = DataBase.eliminaCentro(centro.getID());

	            if (success) {
	                CustomMessage.show("Centro eliminato con successo!", "Successo", true);
	                nomeField.setText("");
	                // Rimuovi il centro dalla mappa e aggiorna la ComboBox
	                centriGestiti.remove(centroSelezionato);
	                centriComboBox.removeItem(centroSelezionato);
	                if (centriGestiti.isEmpty()) {
	                	CustomMessage.show("Nessun centro trovato.", "Informazione", false);
	        			BackgroundPanel.showPanel("createGestore");
	        			return; // Interrompi il caricamento se non ci sono campi
	                }
	                // Eventuali ulteriori aggiornamenti dell'interfaccia
	                
	            } else {
	                CustomMessage.show("Errore durante l'eliminazione del centro.", er, false);
	            }
	        }
	        else {
	        	CustomMessage.show("Cancellazione annullata", "Attenzione", false);
	        }
	    } else {
	        CustomMessage.show("Nessun centro selezionato.", er, false);
	    }
	}
	    private Map<String, List<String>> caricaProvinceEComuni(String filePath) {
	        Map<String, List<String>> provinceComuni = new HashMap<>();
	        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] parts = line.split(";");
	                if (parts.length >= 2) {
	                    String provincia = parts[0].trim();
	                    String comune = parts[1].trim();
	                    provinceComuni.putIfAbsent(provincia, new ArrayList<>());
	                    provinceComuni.get(provincia).add(comune);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return provinceComuni;
	    }

	    private void aggiornaComuni() {
	        if (provinceComuni == null) {
	            System.err.println("La mappa provinceComuni è null.");
	            return; // Prevenire eccezioni
	        }

	        String provinciaSelezionata = (String) provinciaComboBox.getSelectedItem();
	        comuneComboBox.removeAllItems();

	        if (provinciaSelezionata != null && provinceComuni.containsKey(provinciaSelezionata)) {
	            for (String comune : provinceComuni.get(provinciaSelezionata)) {
	                comuneComboBox.addItem(comune);
	            }
	        }
	    }

	
	private void resetForm(JPanel riepilogoPanel) {
	    // Svuota i campi di input
	    aggiornaDettagliCentro();

	    // Rimuovi tutti i componenti dal riepilogoPanel
	    riepilogoPanel.removeAll();
	    riepilogoPanel.revalidate();
	    riepilogoPanel.repaint();

	    // Resetta i campi aggiunti tramite AggiungiCampoDialog
	    AggiungiCampoDialog.getCampi().clear();
	}
	

}
