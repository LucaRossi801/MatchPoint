package GUI;

import javax.swing.*;
import java.awt.*;
import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;

public class DettagliPrenotazioneDialog extends JDialog {

    public DettagliPrenotazioneDialog(JFrame parent, Prenotazione prenotazione, Campo campo, CentroSportivo centro) {
        super(parent, "Dettagli Prenotazione", true);

        // Configurazione del layout
        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Pannello superiore con dettagli della prenotazione
        JPanel panelDettagli = new JPanel();
        panelDettagli.setLayout(new GridBagLayout());
        panelDettagli.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Aggiunta dei dettagli
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelDettagli.add(new JLabel("Centro Sportivo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(centro.getNome()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Comune:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(centro.getComune()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Provincia:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(centro.getProvincia()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Campo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(campo.getTipologiaCampo()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Dimensioni:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(campo.getLunghezza() + " x " + campo.getLarghezza()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Data:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(prenotazione.getData().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Ora Inizio:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(prenotazione.getOraInizio().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Ora Fine:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel(prenotazione.getOraFine().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panelDettagli.add(new JLabel("Costo:"), gbc);
        gbc.gridx = 1;
        panelDettagli.add(new JLabel("€" + prenotazione.calcolaCosto()), gbc);

        add(panelDettagli, BorderLayout.CENTER);

        // Pannello inferiore con pulsanti
        JPanel panelBottoni = new JPanel();
        JButton chiudiButton = new JButton("Chiudi");
        chiudiButton.addActionListener(e -> dispose());
        panelBottoni.add(chiudiButton);

        add(panelBottoni, BorderLayout.SOUTH);
    }
}
