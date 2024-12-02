package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        JScrollPane scrollPane = new JScrollPane(prenotazioniArea) {
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
        scrollPane.setPreferredSize(new Dimension(900, 400));
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
            cardLayout.show(cardPanel, "createGestore");
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
        prenotazioniArea.setText(""); // Pulisci l'area di testo

        // Recupera la sessione corrente per ottenere l'utente loggato
        int utenteID = Sessione.getId();

        // Recupera tutte le prenotazioni dell'utente dal database
        List<Prenotazione> prenotazioni = DataBase.getAllPrenotazioni(utenteID);

        // Ordina le prenotazioni per data e ora
        prenotazioni.sort((p1, p2) -> {
            LocalDateTime dt1 = LocalDateTime.of(p1.getData().toLocalDate(), p1.getOraInizio().toLocalTime());
            LocalDateTime dt2 = LocalDateTime.of(p2.getData().toLocalDate(), p2.getOraInizio().toLocalTime());
            return dt1.compareTo(dt2);
        });
        
        
        
        // Visualizza ogni prenotazione
        for (Prenotazione prenotazione : prenotazioni) {
        	//crea l'oggetto campo e centro
        	Campo campo = DataBase.getCampoById(prenotazione.getCampoId());
        	CentroSportivo centro = DataBase.getCentroByCampo(prenotazione.getCampoId());
        	
        	String dettagliPrenotazione = String.format(
        		    "Data: %s | Ora: %s - %s | Campo: %s | Dimensioni: %s x %s | Località: %s, %s",
        		    prenotazione.getData(),
        		    prenotazione.getOraInizio(),
        		    prenotazione.getOraFine(),
        		    campo.getTipologiaCampo(),
        		    campo.getLunghezza(),
        		    campo.getLarghezza(),
        		    centro.getComune(),
        		    centro.getProvincia()
        		);


            JButton prenotazioneButton = new JButton(dettagliPrenotazione);
            prenotazioneButton.setFont(new Font("Arial", Font.PLAIN, 14));
            prenotazioneButton.setBackground(new Color(200, 230, 250));
            prenotazioneButton.setForeground(Color.DARK_GRAY);
            prenotazioneButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            prenotazioneButton.addActionListener(e -> {
                // Mostra una schermata di dialogo con i dettagli della prenotazione
                mostraDettagliPrenotazione(prenotazione);
            });

            // Aggiungi il pulsante all'area delle prenotazioni
            prenotazioniArea.append(dettagliPrenotazione + "\n");
        }
    }


    private void mostraDettagliPrenotazione(Prenotazione prenotazione) {
    	//crea l'oggetto campo e centro
    	Campo campo = DataBase.getCampoById(prenotazione.getCampoId());
    	CentroSportivo centro = DataBase.getCentroByCampo(prenotazione.getCampoId());

        String dettagli = String.format(
            "Dettagli Prenotazione:\n\n" +
            "Centro Sportivo:\n  Nome: %s\n  Comune: %s\n  Provincia: %s\n\n" +
            "Campo:\n  Tipologia: %s\n  Dimensioni: %s\n\n" +
            "Prenotazione:\n  Data: %s\n  Ora: %s\n\n" +
            "Utente:\n",
            centro.getNome(),
            centro.getComune(),
            centro.getProvincia(),
            campo.getTipologiaCampo(),
            //campo.getDimensioni(),
            prenotazione.getData(),
            prenotazione.getOraInizio()
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
