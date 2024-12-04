package GUI;

import org.jdesktop.swingx.JXDatePicker;
import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DettagliPrenotazioneDialog extends JDialog {

    private JXDatePicker datePicker;
    private JSpinner oraInizioSpinner;
    private JSpinner oraFineSpinner;
    private JButton salvaButton;

    public DettagliPrenotazioneDialog(JFrame parent, Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
        super(parent, "Dettagli Prenotazione", true);

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
        panelDettagli.add(createValueLabel("€" + prenotazione.calcolaCosto()), gbc);

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

    private void salvaDettagliPrenotazione(ActionEvent e) {
        try {
            LocalDateTime nuovaData = datePicker.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            String oraInizio = (String) oraInizioSpinner.getValue();
            String oraFine = (String) oraFineSpinner.getValue();

            // Logica di validazione e salvataggio

            JOptionPane.showMessageDialog(this, "Dati salvati con successo!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Errore nel salvataggio dei dati: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        return label;
    }

    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }

    private JSpinner createCustomTimeSpinner() {
        List<String> times = generateTimeValues();
        JSpinner timeSpinner = new JSpinner(new SpinnerListModel(times));
        timeSpinner.setFont(new Font("Arial", Font.PLAIN, 18));
        ((DefaultEditor) timeSpinner.getEditor()).getTextField().setEditable(false);
        return timeSpinner;
    }

    private List<String> generateTimeValues() {
        List<String> times = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            times.add(String.format("%02d:00", h));
            times.add(String.format("%02d:30", h));
        }
        return times;
    }
    
    // Metodo per formattare LocalTime in formato HH:mm
    private String formatTime(LocalTime time) {
        return String.format("%02d:%02d", time.getHour(), time.getMinute());
    }


}
