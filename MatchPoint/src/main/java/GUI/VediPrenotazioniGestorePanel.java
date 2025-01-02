package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.Campo;
import components.CentroSportivo;
import dataBase.DataBase;
import dataBase.Sessione;
import components.Prenotazione;

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
        prenotazioniArea.setOpaque(false); // Rendi completamente trasparente
        prenotazioniArea.setBackground(new Color(255, 255, 255, 180)); // Sfondo semitrasparente
        prenotazioniArea.setForeground(Color.BLACK); // Colore del testo
        prenotazioniArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     // Configurazione dello JScrollPane
        JScrollPane scrollPane = new JScrollPane(prenotazioniArea) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                // Colore per il contorno (più chiaro)
                g2d.setColor(new Color(180, 180, 180, 150)); // Grigio chiaro semitrasparente
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        // Imposta lo sfondo del viewport (interno)
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(new Color(230, 230, 230, 180)); // Grigio molto chiaro semitrasparente

        // Rendi il `JScrollPane` opaco
        scrollPane.setOpaque(false);

        // Configurazione dei bordi e della dimensione
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY , 2), // Colore del bordo grigio scuro
            "Prenotazioni Giorno per Giorno",
            0,
            0,
            new Font("Arial", Font.BOLD, 14),
            Color.GRAY
        ));
        
        // Modifica la velocità dello scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Incremento per ogni "tick" della rotella
        scrollPane.getVerticalScrollBar().setBlockIncrement(60); // Incremento per clic nella barra

        // Aggiungi lo JScrollPane al layout
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
        // Recupera il centro selezionato e il campo selezionato
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        Campo campoSelezionato = (Campo) campiComboBox.getSelectedItem();
        
        JPanel contenitorePrenotazioni = new JPanel();
        contenitorePrenotazioni.setLayout(new BoxLayout(contenitorePrenotazioni, BoxLayout.Y_AXIS));
        contenitorePrenotazioni.setOpaque(false); // Trasparente per vedere lo sfondo

        // Ripulisci l'area di testo
        prenotazioniArea.setText("");

        if (centroSelezionato == null || campoSelezionato == null) {
            prenotazioniArea.setText("Seleziona un centro sportivo e un campo per visualizzare le prenotazioni.");
            return;
        }

        try {
            // Ottieni gli ID del centro e del campo
            int centroId = centriSportivi.get(centroSelezionato).getID();
            int campoId = campoSelezionato.getId();

            // Recupera le prenotazioni dal database
            List<Prenotazione> prenotazioni = DataBase.getPrenotazioniByCampo(centroId, campoId);

            if (prenotazioni.isEmpty()) {
                prenotazioniArea.setText("Nessuna prenotazione per questo campo.");
                return;
            }

            StringBuilder builder = new StringBuilder();
            Map<String, List<Prenotazione>> prenotazioniPerGiorno = raggruppaPrenotazioniPerGiorno(prenotazioni);

            // Ordina le date in ordine decrescente (più recente in cima)
            List<String> giorniOrdinati = new ArrayList<>(prenotazioniPerGiorno.keySet());
            Collections.sort(giorniOrdinati, Collections.reverseOrder()); // Ordina in ordine decrescente

            for (String giorno : giorniOrdinati) {
                List<Prenotazione> prenotazioniDelGiorno = prenotazioniPerGiorno.get(giorno);

                // Header per il giorno
                JLabel headerGiorno = new JLabel("Prenotazioni per il giorno: " + giorno);
                headerGiorno.setFont(new Font("Arial", Font.BOLD, 18));
                headerGiorno.setForeground(new Color(16, 139, 135));
                headerGiorno.setAlignmentX(Component.CENTER_ALIGNMENT);  // Centra il testo
                contenitorePrenotazioni.add(headerGiorno);

                // Linea divisoria tra giorno e prenotazioni (linea originale)
                contenitorePrenotazioni.add(creaLineaSeparatrice());

                // Spazio tra la riga del giorno e la prima prenotazione
                contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 15)));

                for (Prenotazione prenotazione : prenotazioniDelGiorno) {
                    // Crea il pannello per ogni prenotazione
                    JPanel cardPrenotazione = creaCardPrenotazione(prenotazione);
                    cardPrenotazione.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra la prenotazione
                    contenitorePrenotazioni.add(cardPrenotazione);
                    // Aggiungi uno spazio tra le prenotazioni
                    contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio di 10 pixel
                }

                // Aggiungi spazio tra l'ultima prenotazione e la linea separatrice spessa
                contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio aggiuntivo

                // Linea separatrice più spessa tra il gruppo di prenotazioni e il prossimo giorno
                contenitorePrenotazioni.add(creaLineaSeparatriceSpessa());

                // Spaziatura tra giorni
                contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 15)));
            }

            // Aggiunge il contenitore al pannello con lo scroll
            JScrollPane scrollPane = (JScrollPane) getComponent(1); // Assumendo che sia il secondo componente
            scrollPane.setViewportView(contenitorePrenotazioni);

            prenotazioniArea.setText(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            prenotazioniArea.setText("Errore durante il caricamento delle prenotazioni.");
        }
    }


    /**
     * Crea una linea separatrice originale tra i giorni.
     */
    private JSeparator creaLineaSeparatrice() {
        JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
        separatore.setForeground(new Color(16, 139, 135));
        separatore.setMaximumSize(new Dimension(800, 2)); // Limita la larghezza
        separatore.setPreferredSize(new Dimension(800, 2)); // Imposta una dimensione preferita
        separatore.setAlignmentX(Component.CENTER_ALIGNMENT); // Centra il separatore
        return separatore;
    }


    /**
     * Crea una linea separatrice più spessa tra i gruppi di prenotazioni.
     */
    private JPanel creaLineaSeparatriceSpessa() {
        JPanel separatore = new JPanel();
        separatore.setMaximumSize(new Dimension(800, 4)); // Limita la larghezza
        separatore.setPreferredSize(new Dimension(800, 4)); // Altezza maggiore per una linea più evidente
        separatore.setBackground(new Color(16, 139, 135)); // Colore della linea
        return separatore;
    }




    /**
     * Crea un pannello per rappresentare una prenotazione.
     */
    private JPanel creaCardPrenotazione(Prenotazione prenotazione) {
        JPanel card = new JPanel(new GridBagLayout());

        // Recupera la data e l'orario di fine della prenotazione
        Date dataPrenotazione = prenotazione.getData();
        Time oraInizio = prenotazione.getOraInizio();
        Time oraFine = prenotazione.getOraFine();

        // Converte java.sql.Date in LocalDate
        LocalDate localDate = dataPrenotazione.toLocalDate();

        // Converte Time in LocalTime
        LocalTime localTimeInizio = oraInizio.toLocalTime();
        LocalTime localTimeFine = oraFine.toLocalTime();

        // Combina LocalDate e LocalTime per ottenere un LocalDateTime
        LocalDateTime inizioPrenotazione = localDate.atTime(localTimeInizio);
        LocalDateTime finePrenotazione = localDate.atTime(localTimeFine);

        // Controlla se la prenotazione è passata
        LocalDateTime oraCorrente = LocalDateTime.now();
        boolean prenotazionePassata = finePrenotazione.isBefore(oraCorrente);

        // Crea la card con un colore di sfondo a seconda dello stato della prenotazione
        card.setBackground(prenotazionePassata ? new Color(200, 200, 200) : new Color(230, 240, 250));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(16, 139, 135), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Ridotto la larghezza massima del rettangolo
        card.setMaximumSize(new Dimension(750, 100)); 

        // Configurazione layout della card
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Ora inizio - Ora fine
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(new JLabel("⏰ Orario: " + prenotazione.getOraInizio() + " - " + prenotazione.getOraFine()), gbc);

        // Durata
        gbc.gridx = 1;
        card.add(new JLabel("🕒 Durata: " + prenotazione.getDurataInFormatoOreMinuti() + " h"), gbc);

        // Costo
        gbc.gridx = 2;
        card.add(new JLabel("💶 Costo: €" + DettagliPrenotazioneDialog.calcolaCosto(prenotazione)), gbc);

        return card;
    }

   
    /**
     * Raggruppa le prenotazioni per giorno.
     */
    private Map<String, List<Prenotazione>> raggruppaPrenotazioniPerGiorno(List<Prenotazione> prenotazioni) {
        Map<String, List<Prenotazione>> prenotazioniPerGiorno = new HashMap<>();
        for (Prenotazione prenotazione : prenotazioni) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        	String giorno = prenotazione.getData().toLocalDate().format(formatter);
            prenotazioniPerGiorno.computeIfAbsent(giorno, k -> new ArrayList<>()).add(prenotazione);
        }
        return prenotazioniPerGiorno;
    }


    /**
     * Mostra un messaggio di errore all'utente.
     */
    private void mostraErrore(String messaggio) {
        JOptionPane.showMessageDialog(this, messaggio, "Errore", JOptionPane.ERROR_MESSAGE);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // Colore verde acqua semitrasparente
        Color semiTransparentAqua = new Color(16, 139, 135, 128); // RGB (16, 139, 135) con trasparenza (128)


        // Disegna un rettangolo trasparente sopra lo sfondo
        g2d.setColor(semiTransparentAqua);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Disegna l'immagine di sfondo
        if (clearImage != null) {
            g2d.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
            
        }

        g2d.dispose();
    }

}
