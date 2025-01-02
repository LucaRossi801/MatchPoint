package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
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
import dataBase.Sessione;
import components.Prenotazione;

public class VediPrenotazioniGiocatorePanel extends JPanel {
    private static JScrollPane scrollPane;
	private static JTextArea prenotazioniArea;
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
        
        // Modifica la velocità dello scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Incremento per ogni "tick" della rotella
        scrollPane.getVerticalScrollBar().setBlockIncrement(60); // Incremento per clic nella barra

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
    public static void aggiornaPrenotazioni() {
        // Ripulisce l'area di prenotazioni
        try {
			prenotazioniArea.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}

        // Recupera l'ID utente della sessione corrente
        int utenteID = Sessione.getId();

        // Recupera tutte le prenotazioni dell'utente
        List<Prenotazione> prenotazioni = DataBase.getAllPrenotazioni(utenteID);

        // Ordina le prenotazioni per data e ora
        prenotazioni.sort((p1, p2) -> {
            LocalDateTime dt1 = LocalDateTime.of(p1.getData().toLocalDate(), p1.getOraInizio().toLocalTime());
            LocalDateTime dt2 = LocalDateTime.of(p2.getData().toLocalDate(), p2.getOraInizio().toLocalTime());
            return dt2.compareTo(dt1); // Ordina per data e ora in ordine decrescente
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
    private static JPanel creaCardPrenotazione(Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
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

        // Orario (a sinistra, metà altezza)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        card.add(new JLabel("⏰ Orario: " + prenotazione.getOraInizio() + " - " + prenotazione.getOraFine()), gbc);

        // Barra verticale (separator) spostata leggermente più a sinistra
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 3; // Occupa tre righe
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0;
        gbc.insets = new Insets(0, 0, 0, 30); // Aggiunto un piccolo offset a sinistra
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setForeground(Color.GRAY);
        card.add(separator, gbc);

        // Località (a destra, metà altezza)
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1; // Ripristina l'altezza a una riga
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        card.add(new JLabel("📍 Località: " + centro.getComune() + ", " + centro.getProvincia()), gbc);

        // Durata (centrata)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        card.add(new JLabel("  🕒 Durata: " + prenotazione.getDurataInFormatoOreMinuti()), gbc);

        // Tipo di campo (con dimensioni tra parentesi, sotto la durata)
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        card.add(new JLabel("  ⚽ " + campo.getTipologiaCampo() + " (" + campo.getLunghezza() + "x" + campo.getLarghezza() + ")"), gbc);

        // Costo (in basso, centrato)
        gbc.gridy = 2;
        card.add(new JLabel("  💶 Costo: €" + DettagliPrenotazioneDialog.calcolaCosto(prenotazione)), gbc);
        // Aggiungi l'icona del cestino in fondo a destra
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;

     // Carica l'immagine come ImageIcon
        ImageIcon trashIcon = new ImageIcon(VediPrenotazioniGiocatorePanel.class.getResource("/GUI/immagini/trashIcon.png"));

        // Ridimensiona l'immagine
        Image img = trashIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // Cambia la dimensione come desiderato
        trashIcon = new ImageIcon(img);

        // Crea il JLabel con l'icona ridimensionata
        JLabel trashIconLabel = new JLabel(trashIcon);

        // Impostare la dimensione e la posizione dell'icona
        trashIconLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Imposta una dimensione adeguata
        trashIconLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cambia il cursore al passaggio del mouse

        trashIconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Verifica se la prenotazione è già passata o se mancano meno di 24 ore
                if (prenotazionePassata || menoDi24Ore) {
                    CustomMessage.show(
                        "Non è possibile eliminare prenotazioni già passate o entro 24 ore",
                        "Errore",
                        false // Colore rosso per errore
                    );
                    return; // Esci senza procedere
                }

                // Mostra il messaggio di conferma tramite CustomMessageWithChoice
                boolean conferma = CustomMessageWithChoice.show(
                    "Sei sicuro di voler cancellare questa prenotazione?",
                    "Conferma Cancellazione",
                    false // Utilizza il colore rosso per il messaggio di errore
                );

                if (conferma) {
                    // L'utente ha confermato la cancellazione, quindi procedi con l'eliminazione
                    try {
                        DataBase.eliminaPrenotazione(prenotazione); // Elimina la prenotazione
                        aggiornaPrenotazioni(); // Aggiorna l'elenco delle prenotazioni dopo l'eliminazione
                    } catch (SQLException ex) {
                        // Se si verifica un errore durante l'eliminazione, mostra un messaggio di errore
                        CustomMessageWithChoice.show(
                            "Errore nell'eliminazione della prenotazione.",
                            "Errore",
                            false // Colore rosso per errore
                        );
                        ex.printStackTrace();
                    }
                } else {
                    // L'utente ha annullato l'operazione
                    CustomMessage.show(
                        "Cancellazione annullata.",
                        "Annullato",
                        false // Colore rosso per errore (oppure puoi usare un colore neutro)
                    );
                }
            }
        });

        card.add(trashIconLabel, gbc);

        // Aggiunge azione clic per mostrare i dettagli
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostraDettagliPrenotazione(scrollPane, prenotazione, campo, centro);
            }
        });

        return card;
    }



    
    private static Map<String, List<Prenotazione>> raggruppaPrenotazioniPerGiorno(List<Prenotazione> prenotazioni) {
        Map<String, List<Prenotazione>> prenotazioniPerGiorno = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Prenotazione prenotazione : prenotazioni) {
            String giorno = prenotazione.getData().toLocalDate().format(formatter);
            prenotazioniPerGiorno.computeIfAbsent(giorno, k -> new ArrayList<>()).add(prenotazione);
        }

        // Ordinamento delle prenotazioni per giorno in ordine decrescente (ora più tardi prima)
        for (List<Prenotazione> prenotazioniDelGiorno : prenotazioniPerGiorno.values()) {
            prenotazioniDelGiorno.sort((p1, p2) -> {
                LocalDateTime ora1 = LocalDateTime.of(p1.getData().toLocalDate(), p1.getOraInizio().toLocalTime());
                LocalDateTime ora2 = LocalDateTime.of(p2.getData().toLocalDate(), p2.getOraInizio().toLocalTime());
                return ora2.compareTo(ora1); // Ordinamento decrescente per orario
            });
        }

        return prenotazioniPerGiorno;
    }

    
    private static JSeparator creaLineaSeparatrice() {
        JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
        separatore.setForeground(new Color(16, 139, 135));
        separatore.setPreferredSize(new Dimension(800, 2)); // Linea sottile
        return separatore;
    }

    private static JSeparator creaLineaSeparatriceSpessa() {
        JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
        separatore.setForeground(new Color(16, 139, 135)); // Colore della linea
        separatore.setPreferredSize(new Dimension(800, 5)); // Altezza maggiore
        return separatore;
    }






    /**
     * Mostra una finestra di dialogo con i dettagli di una prenotazione.
     */
    private static void mostraDettagliPrenotazione(Component parentComponent, Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parentComponent);
        DettagliPrenotazioneDialog dialog = new DettagliPrenotazioneDialog(parentFrame, prenotazione, campo, centro);
        dialog.setVisible(true);
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
