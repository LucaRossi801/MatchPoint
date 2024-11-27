package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
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
    private JComboBox<Campo> campiComboBox;  // ComboBox per i campi sportivi
    private JTextArea prenotazioniArea;      // Area per mostrare le prenotazioni
    private Map<String, CentroSportivo> centriSportivi; // Mappa per memorizzare i centri sportivi
    private Image clearImage;
    
    public VediPrenotazioniGestorePanel() {
    
        URL clearImageUrl = getClass().getResource("/GUI/immagini/sfondohome.png");
        if (clearImageUrl != null) {
            clearImage = new ImageIcon(clearImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine: " + "/GUI/immagini/sfondohome.png");
        }
        
        // Configura layout e componenti
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etichetta e ComboBox per i centri sportivi
        JLabel centriLabel = new JLabel("Seleziona Centro Sportivo:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;  // Allinea a sinistra
        add(centriLabel, gbc);

        centriComboBox = new JComboBox<>();
        caricaCentriSportivi();
        centriComboBox.addActionListener(e -> aggiornaCampi());
        centriComboBox.setPreferredSize(new Dimension(120, 25)); // Ridotto in larghezza
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.0; // Non espandere orizzontalmente
        gbc.fill = GridBagConstraints.NONE; // Non espandere
        add(centriComboBox, gbc);

        // Etichetta e ComboBox per i campi sportivi
        JLabel campiLabel = new JLabel("Seleziona Campo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST; // Allinea a sinistra
        add(campiLabel, gbc);

        campiComboBox = new JComboBox<>();
        campiComboBox.addActionListener(e -> aggiornaPrenotazioni());
        campiComboBox.setPreferredSize(new Dimension(120, 25)); // Ridotto in larghezza
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.0; // Non espandere orizzontalmente
        gbc.fill = GridBagConstraints.NONE; // Non espandere
        add(campiComboBox, gbc);

        // Area per visualizzare le prenotazioni
        prenotazioniArea = new JTextArea();
        prenotazioniArea.setEditable(false); // Rendi il JTextArea di sola lettura
        prenotazioniArea.setWrapStyleWord(true); // Parola intera su nuova riga
        prenotazioniArea.setLineWrap(true); // Abilita l'andata a capo automatica
        JScrollPane scrollPane = new JScrollPane(prenotazioniArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Prenotazioni Giorno per Giorno"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0; // Lo scrollpane si espande orizzontalmente
        gbc.weighty = 1.0; // Lo scrollpane si espande verticalmente
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

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
                    JOptionPane.showMessageDialog(this, 
                        "Errore durante il caricamento dei campi. Riprovare.", 
                        "Errore", 
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
        prenotazioniArea.setText(""); // Pulisce l'area di testo

        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        Campo campoSelezionato = (Campo) campiComboBox.getSelectedItem(); // Assumendo che campiComboBox contenga oggetti `Campo`

        if (centroSelezionato != null && campoSelezionato != null) {
            try {
                // Recupera l'ID del centro selezionato
                CentroSportivo centro = centriSportivi.get(centroSelezionato);
                int centroId = centro.getID();

                // Recupera l'ID del campo
                int campoId = campoSelezionato.getId(); // Assumendo che `Campo` abbia un metodo `getId()`

                // Chiamata al metodo aggiornato
                List<Prenotazione> prenotazioni = DataBase.getPrenotazioniByCampo(centroId, campoId);

                if (prenotazioni.isEmpty()) {
                    prenotazioniArea.setText("Nessuna prenotazione per questo campo.");
                } else {
                    for (Prenotazione prenotazione : prenotazioni) {
                        prenotazioniArea.append(prenotazione.toString() + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Errore durante il caricamento delle prenotazioni.", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
