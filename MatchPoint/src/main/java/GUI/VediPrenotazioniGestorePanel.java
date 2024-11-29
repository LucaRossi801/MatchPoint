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
    private JComboBox<String> centriComboBox;
    private JComboBox<Campo> campiComboBox;
    private JTextArea prenotazioniArea;
    private Map<String, CentroSportivo> centriSportivi;
    private Image clearImage;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public VediPrenotazioniGestorePanel(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        // Caricamento dello sfondo
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

        // ComboBox per i centri sportivi
        centriComboBox = new JComboBox<>();
        caricaCentriSportivi();
        centriComboBox.addActionListener(e -> aggiornaCampi());
        centriComboBox.setPreferredSize(new Dimension(200, 30));
        centriComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        centriComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        topPanel.add(centriComboBox, gbc);

        // Etichetta per il campo sportivo
        JPanel campiPanel = new JPanel(new BorderLayout());
        campiPanel.setOpaque(false);
        JLabel campiLabel = new JLabel("Seleziona Campo:", campoIcon, JLabel.LEFT);
        campiLabel.setFont(new Font("Arial", Font.BOLD, 16));
        campiPanel.add(campiLabel, BorderLayout.WEST);
        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(campiPanel, gbc);

        // ComboBox per i campi sportivi
        campiComboBox = new JComboBox<>();
        campiComboBox.addActionListener(e -> aggiornaPrenotazioni());
        campiComboBox.setPreferredSize(new Dimension(200, 30));
        campiComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        campiComboBox.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        gbc.gridx = 1;
        topPanel.add(campiComboBox, gbc);

        // Aggiungi il pannello superiore al layout principale
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(topPanel, gbc);

        // Area per visualizzare le prenotazioni
        prenotazioniArea = new JTextArea();
        prenotazioniArea.setEditable(false);
        prenotazioniArea.setWrapStyleWord(true);
        prenotazioniArea.setLineWrap(true);
        prenotazioniArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        prenotazioniArea.setBackground(new Color(245, 245, 245));
        prenotazioniArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(prenotazioniArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setMinimumSize(new Dimension(150, 80));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2),
            "Prenotazioni Giorno per Giorno",
            0,
            0,
            new Font("Arial", Font.BOLD, 14),
            Color.GRAY
        ));
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
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.GRAY);
        backButton.setBackground(Color.DARK_GRAY);

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        aggiornaCampi();
    }


    /**
     * Metodo per caricare e ridimensionare un'icona.
     * 
     * @param path il percorso dell'immagine
     * @param width larghezza desiderata
     * @param height altezza desiderata
     * @return un oggetto ImageIcon ridimensionato
     */
    private ImageIcon loadScaledIcon(String path, int width, int height) {
        URL imageUrl = getClass().getResource(path);
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.out.println("Errore nel caricamento dell'immagine: " + path);
            return null;
        }
    }


    /**
     * Classe per personalizzare la barra di scorrimento.
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
