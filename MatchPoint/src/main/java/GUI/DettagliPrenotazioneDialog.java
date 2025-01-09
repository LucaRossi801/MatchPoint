package GUI;

import org.jdesktop.swingx.JXDatePicker;
import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;
import dataBase.DataBase;
import dataBase.Sessione;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Finestra di dialogo per visualizzare e modificare i dettagli di una prenotazione.
 * Permette di aggiornare la data, l'ora di inizio e di fine di una prenotazione
 * solo se la prenotazione è modificabile (almeno 24 ore prima dell'inizio).
 */
public class DettagliPrenotazioneDialog extends JDialog {
	private JXDatePicker datePicker; // Componente per selezionare la data
    private JSpinner oraInizioSpinner; // Spinner per selezionare l'orario di inizio
    private JSpinner oraFineSpinner; // Spinner per selezionare l'orario di fine
    private JButton salvaButton; // Bottone per salvare i dettagli della prenotazione
    private Campo campo; // Campo associato alla prenotazione
    private CentroSportivo centro; // Centro sportivo associato alla prenotazione
    private int id; // ID della prenotazione
    
    /**
     * Costruttore per creare la finestra di dialogo dei dettagli di una prenotazione.
     *
     * @param parent       La finestra padre (JFrame) da cui viene aperto il dialogo.
     * @param prenotazione La prenotazione da visualizzare/modificare.
     * @param campo        Il campo sportivo associato alla prenotazione.
     * @param centro       Il centro sportivo associato alla prenotazione.
     */
    public DettagliPrenotazioneDialog(JFrame parent, Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
    	 super(parent, "Dettagli Prenotazione", true);
         this.campo = campo;
         this.centro = centro;
         this.id = prenotazione.getId();
        // Configurazione del layout principale
        setLayout(new BorderLayout());
        setSize(700, 500); // Dimensione del dialogo
        setLocationRelativeTo(parent);

        // Pannello superiore con dettagli della prenotazione
        JPanel panelDettagli = new JPanel(new GridBagLayout());
        panelDettagli.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Calcolo delle condizioni per abilitare/disabilitare i campi
        LocalDateTime oraCorrente = LocalDateTime.now();
        LocalDateTime inizioPrenotazione = LocalDateTime.of(prenotazione.getData().toLocalDate(), prenotazione.getOraInizio().toLocalTime());
        boolean modificabile = inizioPrenotazione.isAfter(oraCorrente.plusHours(24));

        // Centro Sportivo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDettagli.add(createLabel("Centro Sportivo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(createValueLabel(centro.getNome()), gbc);

        // Campo
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(createLabel("Campo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(createValueLabel(campo.getTipologiaCampo()), gbc);

        // Data
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(createLabel("Data:"), gbc);
        gbc.gridx = 1;

        datePicker = new JXDatePicker();
        datePicker.setDate(prenotazione.getData());
        datePicker.setEnabled(modificabile);
        datePicker.setFormats("dd-MM-yyyy");
        datePicker.getMonthView().setLowerBound(Date.from(oraCorrente.plusHours(24).atZone(ZoneId.systemDefault()).toInstant()));
        datePicker.setFont(new Font("Arial", Font.PLAIN, 18));

        panelDettagli.add(datePicker, gbc);

        // Ora Inizio
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(createLabel("Ora Inizio:"), gbc);
        gbc.gridx = 1;
        oraInizioSpinner = createCustomTimeSpinner();
        oraInizioSpinner.setValue(formatTime(prenotazione.getOraInizio().toLocalTime())); // Imposta l'orario della prenotazione
        oraInizioSpinner.setEnabled(modificabile);
        panelDettagli.add(oraInizioSpinner, gbc);

        // Ora Fine
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(createLabel("Ora Fine:"), gbc);
        gbc.gridx = 1;
        oraFineSpinner = createCustomTimeSpinner();
        oraFineSpinner.setValue(formatTime(prenotazione.getOraFine().toLocalTime())); // Imposta l'orario della prenotazione
        oraFineSpinner.setEnabled(modificabile);
        panelDettagli.add(oraFineSpinner, gbc);



        // Costo
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(createLabel("Costo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(createValueLabel("€" + calcolaCosto(prenotazione)), gbc);

        add(panelDettagli, BorderLayout.CENTER);

        // Pannello inferiore con pulsanti centrati verticalmente
        JPanel panelBottoni = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBottoni = new GridBagConstraints();
        gbcBottoni.insets = new Insets(10, 0, 10, 0);
        gbcBottoni.fill = GridBagConstraints.HORIZONTAL;

        // Bottone "Salva"
        salvaButton = BackgroundPanel.createFlatButton("Salva", this::salvaDettagliPrenotazione, new Dimension(200, 40));
        salvaButton.setBackground(new Color(32, 178, 170)); // Verde acqua
        salvaButton.setEnabled(modificabile);
        gbcBottoni.gridy = 0;
        panelBottoni.add(salvaButton, gbcBottoni);

        // Bottone "Chiudi"
        JButton chiudiButton = BackgroundPanel.createFlatButton("Chiudi", e -> dispose(), new Dimension(200, 30));
        chiudiButton.setBackground(Color.DARK_GRAY);
        chiudiButton.setForeground(Color.GRAY);
        chiudiButton.setFont(new Font("Arial", Font.BOLD, 18));
        gbcBottoni.gridy++;
        panelBottoni.add(chiudiButton, gbcBottoni);

        add(panelBottoni, BorderLayout.SOUTH);
    }
    
    /**
     * Salva i dettagli aggiornati di una prenotazione.
     * Recupera i nuovi dati inseriti dall'utente, verifica la disponibilità del campo e gestisce eventuali pagamenti o aggiornamenti nel database.
     * 
     * @param e L'evento associato all'azione del pulsante "Salva".
     */
    private void salvaDettagliPrenotazione(ActionEvent e) {
        try {
            // Recupera la nuova data, ora di inizio e ora di fine
            LocalDateTime nuovaData = datePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String oraInizioString = oraInizioSpinner.getValue().toString();  // Ora di inizio come stringa
            String oraFineString = oraFineSpinner.getValue().toString();  // Ora di fine come stringa
            
            // Assicurati che le stringhe siano nel formato corretto "HH:mm:ss"
            Time nuovaOraInizio = Time.valueOf(oraInizioString + ":00"); // Aggiungi ":00" per i secondi
            Time nuovaOraFine = Time.valueOf(oraFineString + ":00"); // Aggiungi ":00" per i secondi

            // Recupera l'ID dell'utente e dell'ID del campo dalla prenotazione
            int utenteID = Sessione.getId();  // ID utente della prenotazione
            int campoID = campo.getId();    // ID campo della prenotazione

            // Crea una nuova prenotazione basata sui dati inseriti
            Prenotazione nuovaPrenotazione = new Prenotazione(
                this.id,
                Date.valueOf(nuovaData.toLocalDate()), // Converti LocalDate in Date
                nuovaOraInizio,
                nuovaOraFine,
                utenteID, // ID utente dalla prenotazione
                campoID   // ID campo dalla prenotazione
            );

            // Verifica la disponibilità usando il metodo della classe Prenotazione
            if (InserisciPrenotazionePanel.verificaDisponibilita(nuovaPrenotazione)) {
                try {
                    // Recupera la prenotazione precedente dal database
                    Prenotazione vecchiaPrenotazione = DataBase.getPrenotazioneById(this.id);

                    // Inizializza il gestore dei pagamenti
                    GestorePagamenti gestorePagamenti = new GestorePagamenti();

                    // Gestisci il pagamento modificato
                    gestorePagamenti.gestisciPagamentoModificato(nuovaPrenotazione, vecchiaPrenotazione);

                    // Aggiorna la prenotazione nel database
                    DataBase.updatePrenotazione(nuovaPrenotazione);
                    
                   VediPrenotazioniGiocatorePanel.aggiornaPrenotazioni();
                   
                } catch (SQLException exc) {
                    exc.printStackTrace();
                    CustomMessage.show("Errore durante il salvataggio della prenotazione.", "Errore", false);
                }
            } else {
                CustomMessage.show("Il campo non è disponibile per l'orario selezionato.", "Attenzione", false);
            }
        } catch (Exception ex) {
            // Mostra un messaggio di errore se qualcosa va storto
            CustomMessage.show("Errore nel salvataggio dei dati: " + ex.getMessage(), "Errore", false);
        }
        dispose(); // Chiude la finestra dopo il salvataggio
    }




    /**
     * Crea un'etichetta JLabel con testo specificato e stile predefinito.
     * 
     * @param text Il testo da visualizzare sull'etichetta.
     * @return Un'istanza di JLabel con il testo e lo stile specificato.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        return label;
    }
    
    /**
     * Crea un'etichetta JLabel per visualizzare un valore con stile predefinito.
     * 
     * @param text Il valore da visualizzare sull'etichetta.
     * @return Un'istanza di JLabel con il valore e lo stile specificato.
     */
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }
    
    /**
     * Crea un JSpinner personalizzato per la selezione di orari.
     * Gli orari disponibili sono generati automaticamente con incrementi di 30 minuti.
     * 
     * @return Un'istanza di JSpinner configurata con orari personalizzati.
     */
    private JSpinner createCustomTimeSpinner() {
        List<String> times = generateTimeValues();
        JSpinner timeSpinner = new JSpinner(new SpinnerListModel(times));
        timeSpinner.setFont(new Font("Arial", Font.PLAIN, 18));
        ((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
        return timeSpinner;
    }
    
    /**
     * Genera una lista di valori orari in formato "HH:mm", con incrementi di 30 minuti.
     * 
     * @return Una lista di stringhe rappresentanti gli orari disponibili.
     */
    private List<String> generateTimeValues() {
        List<String> times = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            times.add(String.format("%02d:00", h));
            times.add(String.format("%02d:30", h));
        }
        return times;
    }
    
    /**
     * Formatta un'istanza di {@link LocalTime} nel formato "HH:mm".
     * 
     * @param time L'oggetto LocalTime da formattare.
     * @return Una stringa rappresentante l'orario nel formato "HH:mm".
     */
    private String formatTime(LocalTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }
    
    /**
     * Calcola il costo di una prenotazione in base agli orari e ai costi del campo.
     * Il costo varia tra tariffa diurna e notturna, in base agli orari di prenotazione.
     * 
     * @param p La prenotazione per cui calcolare il costo.
     * @return Il costo totale della prenotazione.
     */
    public static double calcolaCosto(Prenotazione p) {
        // Recupera il campo a cui è stata effettuata la prenotazione
        Campo campo = DataBase.getCampoById(p.getCampoId());
        int costoOraNotturna = campo.getCostoOraNotturna();
        int costoOraDiurna = campo.getCostoOraDiurna();

        // Calcola la durata in millisecondi
        long durataInMillis = p.getOraFine().getTime() - p.getOraInizio().getTime();

        // Converte la durata in minuti
        long durataInMinuti = durataInMillis / 60000;

        // Definire gli orari limite per la divisione tra giorno e notte
        Time ore18 = Time.valueOf("18:00:00");

        // Calcolare la durata delle ore diurne e notturne
        double costoTotale = 0.0;

        // Se la prenotazione finisce prima delle 18:00
        if (p.getOraFine().before(ore18)) {
            costoTotale = (durataInMinuti / 60.0) * costoOraDiurna;
        }
        // Se la prenotazione inizia dopo le 18:00
        else if (p.getOraInizio().after(ore18)) {
            costoTotale = (durataInMinuti / 60.0) * costoOraNotturna;
        }
        // Altrimenti la prenotazione è divisa tra giorno e notte
        else {
            // Ore diurne (dalla partenza fino alle 18:00)
            long minutiDiurni = Duration.between(p.getOraInizio().toLocalTime(), ore18.toLocalTime()).toMinutes();
            costoTotale += (minutiDiurni / 60.0) * costoOraDiurna;

            // Ore notturne (dalle 18:00 in poi)
            long minutiNotturni = durataInMinuti - minutiDiurni;
            costoTotale += (minutiNotturni / 60.0) * costoOraNotturna;
        }

        return costoTotale;
    }

}