package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;

public class DettagliPrenotazioneDialog extends JDialog {

    private JTextField dataField;
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
        dataField = new JTextField(prenotazione.getData().toString());
        dataField.setEnabled(modificabile);
        panelDettagli.add(dataField, gbc);

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
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Funzionalità di salvataggio da implementare
                JOptionPane.showMessageDialog(parent, "Dati salvati con successo!");
            }
        });
        panelBottoni.add(salvaButton);

        JButton chiudiButton = new JButton("Chiudi");
        chiudiButton.addActionListener(e -> dispose());
        panelBottoni.add(chiudiButton);

        add(panelBottoni, BorderLayout.SOUTH);
    }
}
