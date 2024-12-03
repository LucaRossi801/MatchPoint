package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import components.Campo;
import components.CentroSportivo;
import dataBase.DataBase;
import components.Prenotazione;
import components.Sessione;

public class VediPrenotazioniGiocatorePanel extends JPanel {
    private JScrollPane scrollPane;
	private JTextArea prenotazioniArea;
    private Map<String, CentroSportivo> centriSportivi;
    private Image clearImage;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public VediPrenotazioniGiocatorePanel(CardLayout cardLayout, JPanel cardPanel) {
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

        // Area per visualizzare le prenotazioni
        prenotazioniArea = new JTextArea();
        prenotazioniArea.setOpaque(false); // Rendi completamente trasparente
        prenotazioniArea.setBackground(new Color(255, 255, 255, 180)); // Sfondo semitrasparente
        prenotazioniArea.setForeground(Color.BLACK); // Colore del testo
        prenotazioniArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurazione dello JScrollPane
        scrollPane = new JScrollPane(prenotazioniArea) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
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
        scrollPane.setPreferredSize(new Dimension(900, 600));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2), // Colore del bordo grigio scuro
                "Prenotazioni Giorno per Giorno",
                0,
                0,
                new Font("Arial", Font.BOLD, 14),
                Color.GRAY
        ));

        // Aggiungi lo JScrollPane al layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(scrollPane, gbc);

        // Bottone Indietro
        JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
            prenotazioniArea.setText("");
            cardLayout.show(cardPanel, "createGiocatore");
        }, new Dimension(120, 30));
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.GRAY);
        backButton.setBackground(Color.DARK_GRAY);

        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        aggiornaPrenotazioni();
    }

 
    /**
     * Aggiorna l'area delle prenotazioni in base al campo selezionato.
     */
    private void aggiornaPrenotazioni() {
        // Ripulisce l'area di prenotazioni
        prenotazioniArea.setText("");

        // Recupera l'ID utente della sessione corrente
        int utenteID = Sessione.getId();

        // Recupera tutte le prenotazioni dell'utente
        List<Prenotazione> prenotazioni = DataBase.getAllPrenotazioni(utenteID);

        // Ordina le prenotazioni per data e ora
        prenotazioni.sort((p1, p2) -> {
            LocalDateTime oraCorrente = LocalDateTime.now();
            LocalDateTime dt1 = LocalDateTime.of(p1.getData().toLocalDate(), p1.getOraInizio().toLocalTime());
            LocalDateTime dt2 = LocalDateTime.of(p2.getData().toLocalDate(), p2.getOraInizio().toLocalTime());
            
            // Ordinamento cronologico puro
            return dt1.compareTo(dt2);
        });



        // Raggruppa le prenotazioni per giorno
        Map<String, List<Prenotazione>> prenotazioniPerGiorno = raggruppaPrenotazioniPerGiorno(prenotazioni);

        // Ordina le date in ordine cronologico
        List<String> giorniOrdinati = new ArrayList<>(prenotazioniPerGiorno.keySet());
        
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	     // Converti le date in LocalDate per ordinamento corretto
	     List<LocalDate> dateOrdinabili = new ArrayList<>();
	     for (String giorno : giorniOrdinati) {
	         dateOrdinabili.add(LocalDate.parse(giorno, formatter));
	     }
	
	     // Ordina le date in ordine cronologico decrescente
	     dateOrdinabili.sort(Collections.reverseOrder());
	
	     // Riconverti le date in stringhe
	     giorniOrdinati.clear();
	     for (LocalDate data : dateOrdinabili) {
	         giorniOrdinati.add(data.format(formatter));
	     }

        // Crea il pannello contenitore per le prenotazioni
        JPanel contenitorePrenotazioni = new JPanel();
        contenitorePrenotazioni.setLayout(new BoxLayout(contenitorePrenotazioni, BoxLayout.Y_AXIS));
        contenitorePrenotazioni.setOpaque(false);

        if (prenotazioni.isEmpty()) {
            prenotazioniArea.setText("Non ci sono prenotazioni per l'utente corrente.");
            return;
        }

        
        // Popola il contenitore con le prenotazioni
        for (String giorno : giorniOrdinati) {
            List<Prenotazione> prenotazioniDelGiorno = prenotazioniPerGiorno.get(giorno);

            // Debug per assicurarsi che l'ordine sia corretto
            System.out.println("Giorno: " + giorno);
            for (Prenotazione prenotazione : prenotazioniDelGiorno) {
                System.out.println(" - Prenotazione: " + prenotazione.getData() + " " + prenotazione.getOraInizio());
            }

            // Header per il giorno
            JLabel headerGiorno = new JLabel("Prenotazioni per il giorno: " + giorno);
            headerGiorno.setFont(new Font("Arial", Font.BOLD, 18));
            headerGiorno.setForeground(new Color(16, 139, 135));
            headerGiorno.setAlignmentX(Component.CENTER_ALIGNMENT);
            contenitorePrenotazioni.add(headerGiorno);

            // Ordina le prenotazioni del giorno in ordine crescente di orario
            contenitorePrenotazioni.add(creaLineaSeparatrice());

            for (Prenotazione prenotazione : prenotazioniDelGiorno) {
                Campo campo = DataBase.getCampoById(prenotazione.getCampoId());
                CentroSportivo centro = DataBase.getCentroByCampo(prenotazione.getCampoId());
                JPanel cardPrenotazione = creaCardPrenotazione(prenotazione, campo, centro);
                contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 10)));
                contenitorePrenotazioni.add(cardPrenotazione);
            }

            // Spazio e separatore tra i gruppi di giorni
            contenitorePrenotazioni.add(Box.createRigidArea(new Dimension(0, 10)));
            contenitorePrenotazioni.add(creaLineaSeparatriceSpessa());
        }


        // Aggiorna la viewport dello scrollPane
        if (scrollPane != null) {
            scrollPane.setViewportView(contenitorePrenotazioni);
        } else {
            System.err.println("Errore: scrollPane non inizializzato correttamente.");
        }
    }


    /**
     * Crea un pannello rettangolare per rappresentare una prenotazione.
     */
    private JPanel creaCardPrenotazione(Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
        JPanel card = new JPanel(new GridBagLayout());

        // Configura data e orari della prenotazione
        LocalDateTime oraCorrente = LocalDateTime.now();
        LocalDateTime inizioPrenotazione = LocalDateTime.of(prenotazione.getData().toLocalDate(), prenotazione.getOraInizio().toLocalTime());
        LocalDateTime finePrenotazione = LocalDateTime.of(prenotazione.getData().toLocalDate(), prenotazione.getOraFine().toLocalTime());

        boolean prenotazionePassata = finePrenotazione.isBefore(oraCorrente);
        boolean menoDi24Ore = !prenotazionePassata &&
                              inizioPrenotazione.isAfter(oraCorrente) &&
                              inizioPrenotazione.isBefore(oraCorrente.plusHours(24));

        // Applicazione sfondo basata sullo stato della prenotazione
        if (prenotazionePassata) {
            card.setBackground(new Color(200, 200, 200)); // Grigio
        } else if (menoDi24Ore) {
            card.setBackground(new Color(253, 218, 13)); // Giallo
        } else {
            card.setBackground(new Color(230, 240, 250)); // Blu chiaro
        }

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(16, 139, 135), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Imposta dimensione massima del pannello
        card.setMaximumSize(new Dimension(750, 100));

        // Layout e configurazione dei componenti
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Data e ora
        gbc.gridx = 0;
        gbc.gridy = 0;
        card.add(new JLabel("📅 Data: " + prenotazione.getData()), gbc);

        gbc.gridx = 1;
        card.add(new JLabel("⏰ Orario: " + prenotazione.getOraInizio() + " - " + prenotazione.getOraFine()), gbc);

        // Dettagli del campo
        gbc.gridx = 0;
        gbc.gridy = 1;
        card.add(new JLabel("🏟️ Campo: " + campo.getTipologiaCampo()), gbc);

        gbc.gridx = 1;
        card.add(new JLabel("📏 Dimensioni: " + campo.getLunghezza() + " x " + campo.getLarghezza()), gbc);

        // Località
        gbc.gridx = 2;
        gbc.gridy = 1;
        card.add(new JLabel("📍 Località: " + centro.getComune() + ", " + centro.getProvincia()), gbc);

        // Durata
        gbc.gridx = 0;
        gbc.gridy = 2;
        card.add(new JLabel("🕒 Durata: " + prenotazione.getDurataInFormatoOreMinuti()), gbc);

        // Costo
        gbc.gridx = 1;
        card.add(new JLabel("💶 Costo: €" + prenotazione.calcolaCosto()), gbc);

        // Aggiunge azione clic per mostrare i dettagli
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostraDettagliPrenotazione(prenotazione, campo, centro);
            }
        });

        return card;
    }
    
    private Map<String, List<Prenotazione>> raggruppaPrenotazioniPerGiorno(List<Prenotazione> prenotazioni) {
        Map<String, List<Prenotazione>> prenotazioniPerGiorno = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Prenotazione prenotazione : prenotazioni) {
            String giorno = prenotazione.getData().toLocalDate().format(formatter);
            prenotazioniPerGiorno.computeIfAbsent(giorno, k -> new ArrayList<>()).add(prenotazione);
        }
        return prenotazioniPerGiorno;
    }

    
    private JSeparator creaLineaSeparatrice() {
        JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
        separatore.setForeground(new Color(16, 139, 135));
        separatore.setPreferredSize(new Dimension(800, 2)); // Linea sottile
        return separatore;
    }

    private JSeparator creaLineaSeparatriceSpessa() {
        JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
        separatore.setForeground(new Color(16, 139, 135)); // Colore della linea
        separatore.setPreferredSize(new Dimension(800, 5)); // Altezza maggiore
        return separatore;
    }






    /**
     * Mostra una finestra di dialogo con i dettagli di una prenotazione.
     */
    private void mostraDettagliPrenotazione(Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
        String dettagli = String.format(
            "Dettagli Prenotazione:\n\n" +
            "Centro Sportivo:\n  Nome: %s\n  Comune: %s\n  Provincia: %s\n\n" +
            "Campo:\n  Tipologia: %s\n  Dimensioni: %s x %s\n\n" +
            "Prenotazione:\n  Data: %s\n  Ora: %s - %s\n\n" +
            "Costo: €%.2f",
            centro.getNome(),
            centro.getComune(),
            centro.getProvincia(),
            campo.getTipologiaCampo(),
            campo.getLunghezza(),
            campo.getLarghezza(),
            prenotazione.getData(),
            prenotazione.getOraInizio(),
            prenotazione.getOraFine(),
            prenotazione.calcolaCosto()
        );

        JOptionPane.showMessageDialog(this, dettagli, "Dettagli Prenotazione", JOptionPane.INFORMATION_MESSAGE);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
