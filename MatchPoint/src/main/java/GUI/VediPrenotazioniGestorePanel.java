package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.Campo;
import components.CentroSportivo;
import dataBase.DataBase;
import components.Prenotazione;
import components.Sessione;

public class VediPrenotazioniGestorePanel extends JPanel {
    private JComboBox<String> centriComboBox; // ComboBox per i centri sportivi
    private JComboBox<String> campiComboBox;  // ComboBox per i campi sportivi
    private JTextArea prenotazioniArea;      // Area per mostrare le prenotazioni
    private Map<String, CentroSportivo> centriSportivi; // Mappa per memorizzare i centri sportivi


    public VediPrenotazioniGestorePanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#F0F0F0")); // Colore di base se sfondoHome non è specificato

        // Disegna sfondo personalizzato
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image sfondoHome = new ImageIcon("GUI/imsfondohome.png").getImage();
                g.drawImage(sfondoHome, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setOpaque(false); // Mantieni trasparenza

        // Configura i componenti
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Etichetta e ComboBox per i centri sportivi
        JLabel centriLabel = new JLabel("Seleziona Centro Sportivo:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.weighty = 0.1;
        backgroundPanel.add(centriLabel, gbc);

        centriComboBox = new JComboBox<>();
        caricaCentriSportivi();
        centriComboBox.addActionListener(e -> aggiornaCampi());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        backgroundPanel.add(centriComboBox, gbc);

        // Etichetta e ComboBox per i campi sportivi
        JLabel campiLabel = new JLabel("Seleziona Campo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        backgroundPanel.add(campiLabel, gbc);

        campiComboBox = new JComboBox<>();
        campiComboBox.addActionListener(e -> aggiornaPrenotazioni());
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.8;
        backgroundPanel.add(campiComboBox, gbc);

        // Area per visualizzare le prenotazioni
        prenotazioniArea = new JTextArea();
        prenotazioniArea.setEditable(false); // L'area di testo è solo di lettura
        JScrollPane scrollPane = new JScrollPane(prenotazioniArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Prenotazioni Giorno per Giorno"));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        backgroundPanel.add(scrollPane, gbc);

        // Aggiungi il pannello principale
        add(backgroundPanel, new GridBagConstraints());
        aggiornaCampi();
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
    	        CentroSportivo centro = centriSportivi.values().stream()
    	            .filter(c -> c.getNome().equals(centroSelezionato))
    	            .findFirst()
    	            .orElse(null); // Trova il centro corrispondente al nome selezionato

    	        if (centro != null) {
    	            try {
    	                // Recupera i campi per il centro selezionato
    	                List<Campo> campi = DataBase.getCampiById(centro.getID());
    	                campiComboBox.removeAllItems();

    	                // Aggiunge i campi al ComboBox
    	                for (Campo campo : campi) {
    	                    // Formattazione: "TipologiaCampo (LunghezzaxLarghezza)"
    	                    String item = String.format("%s (%dx%d)",
    	                                                campo.getTipologiaCampo().toString(),
    	                                                campo.getLunghezza(),
    	                                                campo.getLarghezza());
    	                    campiComboBox.addItem(item);
    	                }
    	            } catch (Exception e) {
    	                e.printStackTrace(); // Log dell'errore per il debug
    	                JOptionPane.showMessageDialog(this, 
    	                    "Errore durante il caricamento dei campi. Riprovare.", 
    	                    "Errore", 
    	                    JOptionPane.ERROR_MESSAGE);
    	            }
    	        }
    	    } else {
    	        campiComboBox.removeAllItems(); // Resetta il ComboBox se non è selezionato alcun centro
    	    }

    	    aggiornaPrenotazioni(); // Aggiorna le prenotazioni per il campo selezionato
    	}

 


    /**
     * Aggiorna l'area delle prenotazioni in base al campo selezionato.
     */
    private void aggiornaPrenotazioni() {
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        String campoSelezionato = (String) campiComboBox.getSelectedItem();
        prenotazioniArea.setText(""); // Pulisce l'area di testo

        if (centroSelezionato != null && campoSelezionato != null) {
            CentroSportivo centro = DataBase.getCentroByName(centroSelezionato);
            if (centro != null) {
                List<Prenotazione> prenotazioni = DataBase.getPrenotazioniByCampo(centro.ID, campoSelezionato);
                if (prenotazioni.isEmpty()) {
                    prenotazioniArea.setText("Nessuna prenotazione per questo campo.");
                } else {
                    for (Prenotazione prenotazione : prenotazioni) {
                        prenotazioniArea.append(prenotazione.toString() + "\n");
                    }
                }
            }
        }
    }
}
