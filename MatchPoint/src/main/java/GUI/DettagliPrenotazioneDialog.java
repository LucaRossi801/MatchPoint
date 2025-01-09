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

        // Configura il layout principale
        setLayout(new BorderLayout());
        setSize(700, 500); // Dimensioni del dialogo
        setLocationRelativeTo(parent); // Centra rispetto alla finestra padre

        // Crea il pannello superiore con i dettagli
        JPanel panelDettagli = new JPanel(new GridBagLayout());
        panelDettagli.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Verifica se la prenotazione è modificabile
        LocalDateTime oraCorrente = LocalDateTime.now();
        LocalDateTime inizioPrenotazione = LocalDateTime.of(
                prenotazione.getData().toLocalDate(),
                prenotazione.getOraInizio().toLocalTime()
        );
        boolean modificabile = inizioPrenotazione.isAfter(oraCorrente.plusHours(24));

        // Aggiungi i dettagli della prenotazione al pannello
        aggiungiDettagli(panelDettagli, gbc, prenotazione, modificabile);

        // Aggiungi il pannello dei dettagli al dialogo
        add(panelDettagli, BorderLayout.CENTER);

        // Crea il pannello inferiore con i bottoni
        JPanel panelBottoni = new JPanel(new GridBagLayout());
        creaBottoni(panelBottoni, modificabile);
        add(panelBottoni, BorderLayout.SOUTH);
    }

    /**
     * Aggiunge i dettagli della prenotazione al pannello.
     *
     * @param panel         Il pannello a cui aggiungere i dettagli.
     * @param gbc           Le impostazioni di layout.
     * @param prenotazione  La prenotazione da visualizzare/modificare.
     * @param modificabile  Se i dettagli sono modificabili.
     */
    private void aggiungiDettagli(JPanel panel, GridBagConstraints gbc, Prenotazione prenotazione, boolean modificabile) {
        // Centro sportivo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createLabel("Centro Sportivo:"), gbc);
        gbc.gridx = 1;
        panel.add(createValueLabel(centro.getNome()), gbc);

        // Campo
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createLabel("Campo:"), gbc);
        gbc.gridx = 1;
        panel.add(createValueLabel(campo.getTipologiaCampo()), gbc);

        // Data
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createLabel("Data:"), gbc);
        gbc.gridx = 1;
        datePicker = new JXDatePicker();
        datePicker.setDate(prenotazione.getData());
        datePicker.setEnabled(modificabile);
        datePicker.setFormats("dd-MM-yyyy");
        datePicker.getMonthView().setLowerBound(Date.from(LocalDateTime.now().plusHours(24)
                .atZone(ZoneId.systemDefault()).toInstant()));
        datePicker.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(datePicker, gbc);

        // Ora inizio
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createLabel("Ora Inizio:"), gbc);
        gbc.gridx = 1;
        oraInizioSpinner = createCustomTimeSpinner();
        oraInizioSpinner.setValue(formatTime(prenotazione.getOraInizio().toLocalTime()));
        oraInizioSpinner.setEnabled(modificabile);
        panel.add(oraInizioSpinner, gbc);

        // Ora fine
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createLabel("Ora Fine:"), gbc);
        gbc.gridx = 1;
        oraFineSpinner = createCustomTimeSpinner();
        oraFineSpinner.setValue(formatTime(prenotazione.getOraFine().toLocalTime()));
        oraFineSpinner.setEnabled(modificabile);
        panel.add(oraFineSpinner, gbc);

        // Costo
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createLabel("Costo:"), gbc);
        gbc.gridx = 1;
        panel.add(createValueLabel("€" + calcolaCosto(prenotazione)), gbc);
    }

    /**
     * Crea e aggiunge i bottoni al pannello inferiore.
     *
     * @param panel       Il pannello dei bottoni.
     * @param modificabile Se i dettagli sono modificabili.
     */
    private void creaBottoni(JPanel panel, boolean modificabile) {
        GridBagConstraints gbcBottoni = new GridBagConstraints();
        gbcBottoni.insets = new Insets(10, 0, 10, 0);
        gbcBottoni.fill = GridBagConstraints.HORIZONTAL;

        // Bottone "Salva"
        salvaButton = BackgroundPanel.createFlatButton("Salva", this::salvaDettagliPrenotazione, new Dimension(200, 40));
        salvaButton.setBackground(new Color(32, 178, 170));
        salvaButton.setEnabled(modificabile);
        gbcBottoni.gridy = 0;
        panel.add(salvaButton, gbcBottoni);

        // Bottone "Chiudi"
        JButton chiudiButton = BackgroundPanel.createFlatButton("Chiudi", e -> dispose(), new Dimension(200, 30));
        chiudiButton.setBackground(Color.DARK_GRAY);
        chiudiButton.setForeground(Color.GRAY);
        chiudiButton.setFont(new Font("Arial", Font.BOLD, 18));
        gbcBottoni.gridy++;
        panel.add(chiudiButton, gbcBottoni);
    }

    /**
     * Metodo chiamato al salvataggio dei dettagli della prenotazione.
     *
     * @param e L'evento di salvataggio.
     */
    private void salvaDettagliPrenotazione(ActionEvent e) {
        try {
            LocalDateTime nuovaData = datePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Time nuovaOraInizio = Time.valueOf(oraInizioSpinner.getValue() + ":00");
            Time nuovaOraFine = Time.valueOf(oraFineSpinner.getValue() + ":00");

            Prenotazione nuovaPrenotazione = new Prenotazione(id, Date.valueOf(nuovaData.toLocalDate()),
                    nuovaOraInizio, nuovaOraFine, Sessione.getId(), campo.getId());

            if (InserisciPrenotazionePanel.verificaDisponibilita(nuovaPrenotazione)) {
                DataBase.updatePrenotazione(nuovaPrenotazione);
                VediPrenotazioniGiocatorePanel.aggiornaPrenotazioni();
            } else {
                CustomMessage.show("Il campo non è disponibile per l'orario selezionato.", "Attenzione", false);
            }
        } catch (Exception ex) {
            CustomMessage.show("Errore nel salvataggio dei dati: " + ex.getMessage(), "Errore", false);
        }
        dispose();
    }

    /**
     * Crea un'etichetta JLabel con testo in grassetto.
     *
     * @param text Il testo da visualizzare sull'etichetta.
     * @return Un'istanza di JLabel con lo stile predefinito.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        return label;
    }

    /**
     * Crea un'etichetta JLabel con valore in testo normale.
     *
     * @param text Il valore da visualizzare sull'etichetta.
     * @return Un'istanza di JLabel con lo stile predefinito.
     */
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }

    /**
     * Crea un JSpinner personalizzato per selezionare l'orario.
     * Gli orari disponibili sono generati in formato "HH:mm" con incrementi di 30 minuti.
     *
     * @return Un'istanza di JSpinner configurata per selezionare l'orario.
     */
    private JSpinner createCustomTimeSpinner() {
        List<String> times = generateTimeValues();
        JSpinner timeSpinner = new JSpinner(new SpinnerListModel(times));
        timeSpinner.setFont(new Font("Arial", Font.PLAIN, 18));
        ((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
        return timeSpinner;
    }

    /**
     * Genera una lista di stringhe rappresentanti gli orari del giorno in formato "HH:mm".
     * Gli orari vanno dalle 00:00 alle 23:30, con incrementi di 30 minuti.
     *
     * @return Una lista di stringhe in formato "HH:mm".
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
     * Formatta un oggetto LocalTime in una stringa con il formato "HH:mm".
     *
     * @param time L'orario da formattare.
     * @return Una stringa rappresentante l'orario in formato "HH:mm".
     */
    private String formatTime(LocalTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }

    /**
     * Calcola il costo totale di una prenotazione basandosi sugli orari diurno e notturno.
     * Il costo dipende dal tempo trascorso e dalla tariffa oraria del campo.
     *
     * @param p La prenotazione di cui calcolare il costo.
     * @return Il costo totale della prenotazione.
     */
    public static double calcolaCosto(Prenotazione p) {
        // Recupera il campo associato alla prenotazione
        Campo campo = DataBase.getCampoById(p.getCampoId());
        int costoOraNotturna = campo.getCostoOraNotturna();
        int costoOraDiurna = campo.getCostoOraDiurna();

        // Calcola la durata totale della prenotazione in minuti
        long durataInMillis = p.getOraFine().getTime() - p.getOraInizio().getTime();
        long durataInMinuti = durataInMillis / 60000;

        // Definisce il limite tra ora diurna e notturna
        Time ore18 = Time.valueOf("18:00:00");
        double costoTotale = 0.0;

        // Se l'intera prenotazione avviene prima delle 18:00 (diurna)
        if (p.getOraFine().before(ore18)) {
            costoTotale = (durataInMinuti / 60.0) * costoOraDiurna;
        }
        // Se l'intera prenotazione avviene dopo le 18:00 (notturna)
        else if (p.getOraInizio().after(ore18)) {
            costoTotale = (durataInMinuti / 60.0) * costoOraNotturna;
        }
        // Se la prenotazione è divisa tra orari diurno e notturno
        else {
            // Calcola i minuti diurni
            long minutiDiurni = Duration.between(p.getOraInizio().toLocalTime(), ore18.toLocalTime()).toMinutes();
            costoTotale += (minutiDiurni / 60.0) * costoOraDiurna;

            // Calcola i minuti notturni
            long minutiNotturni = durataInMinuti - minutiDiurni;
            costoTotale += (minutiNotturni / 60.0) * costoOraNotturna;
        }

        return costoTotale;
    }

}
