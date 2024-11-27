package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.CentroSportivo;
import dataBase.DataBase;
import components.Prenotazione;
import components.Sessione;

public class VediPrenotazioniGestorePanel extends JPanel {
    private JComboBox<String> centriComboBox; // ComboBox per i centri sportivi
    private JComboBox<String> campiComboBox;  // ComboBox per i campi sportivi
    private JTextArea prenotazioniArea;      // Area per mostrare le prenotazioni

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
    	 Map<String, CentroSportivo> centriSportivi = new HashMap<>();
    		centriSportivi = DataBase.getCentriSportiviGestiti(Sessione.getId()); // Recupera i dati in modo statico
        centriComboBox.removeAllItems();
        for (CentroSportivo centro : centriSportivi.values()) {
            centriComboBox.addItem(centro.getNome()); // Supponendo che "nome" sia accessibile tramite un getter
        }

    }

    /**
     * Aggiorna i campi sportivi in base al centro selezionato.
     */
    private void aggiornaCampi() {
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        if (centroSelezionato != null) {
<<<<<<< Updated upstream
            CentroSportivo centro = DataBase.getCampiByCentro(); // Ottieni il centro sportivo dal nome
=======
            CentroSportivo centro = DataBase.getCentroByName(centroSelezionato); // Ottieni il centro sportivo dal nome
>>>>>>> Stashed changes
            if (centro != null) {
                List<String> campi = null;
				try {
					campi = DataBase.getCampiByCentro(centro.ID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                campiComboBox.removeAllItems();
                for (String campo : campi) {
                    campiComboBox.addItem(campo);
                }
            }
        }
        aggiornaPrenotazioni();
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
