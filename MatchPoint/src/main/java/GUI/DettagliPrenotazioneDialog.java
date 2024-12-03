package GUI;

import org.jdesktop.swingx.JXDatePicker;
import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DettagliPrenotazioneDialog extends JDialog {

    private JXDatePicker datePicker;
    private JTextField oraInizioField;
    private JTextField oraFineField;
    private JButton salvaButton;

    public DettagliPrenotazioneDialog(JFrame parent, Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
        super(parent, "Dettagli Prenotazione", true);

        // Configurazione del layout
        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Pannello superiore con dettagli della prenotazione
        JPanel panelDettagli = new JPanel(new GridBagLayout());
        panelDettagli.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Calcolo delle condizioni per abilitare/disabilitare i campi
        LocalDateTime oraCorrente = LocalDateTime.now();
        LocalDateTime inizioPrenotazione = LocalDateTime.of(prenotazione.getData().toLocalDate(), prenotazione.getOraInizio().toLocalTime());
        boolean modificabile = inizioPrenotazione.isAfter(oraCorrente.plusHours(24));

        // Centro Sportivo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDettagli.add(new JLabel("Centro Sportivo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(centro.getNome()), gbc);

        // Campo
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Campo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(campo.getTipologiaCampo()), gbc);

        // Data
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;

        datePicker = new JXDatePicker();
        datePicker.setDate(prenotazione.getData());
        datePicker.setEnabled(modificabile);
        datePicker.setFormats("dd-MM-yyyy");

        // Imposta il limite minimo del selettore della data
        datePicker.getMonthView().setLowerBound(Date.from(oraCorrente.plusHours(24).atZone(ZoneId.systemDefault()).toInstant()));

        panelDettagli.add(datePicker, gbc);

        // Ora Inizio
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Ora Inizio:"), gbc);
        gbc.gridx = 1;
        oraInizioField = new JTextField(prenotazione.getOraInizio().toString());
        oraInizioField.setEnabled(modificabile);
        panelDettagli.add(oraInizioField, gbc);

        // Ora Fine
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Ora Fine:"), gbc);
        gbc.gridx = 1;
        oraFineField = new JTextField(prenotazione.getOraFine().toString());
        oraFineField.setEnabled(modificabile);
        panelDettagli.add(oraFineField, gbc);

        // Costo
        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Costo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel("€" + prenotazione.calcolaCosto()), gbc);

        add(panelDettagli, BorderLayout.CENTER);

        // Pannello inferiore con pulsanti
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        salvaButton = new JButton("Salva");
        salvaButton.setEnabled(modificabile); // Abilitato solo se i campi sono modificabili
        salvaButton.addActionListener(this::salvaDettagliPrenotazione);
        panelBottoni.add(salvaButton);

        JButton chiudiButton = new JButton("Chiudi");
        chiudiButton.addActionListener(e -> dispose());
        panelBottoni.add(chiudiButton);

        add(panelBottoni, BorderLayout.SOUTH);
    }

    private void salvaDettagliPrenotazione(ActionEvent e) {
        try {
            LocalDateTime nuovaData = datePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String oraInizio = oraInizioField.getText();
            String oraFine = oraFineField.getText();

            // Convalida data e ora
            LocalDateTime oraCorrente = LocalDateTime.now().plusHours(24);
            LocalTime timeInizio = LocalTime.parse(oraInizio);
            LocalTime timeFine = LocalTime.parse(oraFine);

            if (nuovaData.isBefore(oraCorrente.toLocalDate().atTime(LocalTime.MIN))) {
                throw new IllegalArgumentException("La data selezionata deve essere almeno 24 ore dopo l'ora corrente.");
            }
            if (timeInizio.isAfter(timeFine) || timeInizio.equals(timeFine)) {
                throw new IllegalArgumentException("L'orario di inizio deve essere precedente all'orario di fine.");
            }

            // Qui puoi aggiungere la logica per salvare i dati modificati.
            JOptionPane.showMessageDialog(this, "Dati salvati con successo!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
