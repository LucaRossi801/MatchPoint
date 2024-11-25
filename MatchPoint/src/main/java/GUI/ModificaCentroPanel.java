package GUI;

import javax.swing.*;
import components.CentroSportivo;
import components.Sessione;
import dataBase.DataBase;

import java.awt.*;
import java.util.Map;

public class ModificaCentroPanel extends JPanel {
    private JComboBox<String> centriComboBox; // ComboBox per selezione centri
    private JTextField nomeField, provinciaField, comuneField;
    private JButton salvaButton, modificaCampiButton;
    private Map<String, CentroSportivo> centriGestiti; // Mappa nome-centro
    private Integer utenteId; // ID dell'utente loggato
    private DataBase dataBase; // Database di riferimento

    public ModificaCentroPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255));

        // Inizializza ID utente dalla sessione
        utenteId = Sessione.getId();

        // Pannello superiore per selezione del centro
        JPanel selezionePanel = new JPanel(new FlowLayout());
        selezionePanel.setBackground(new Color(32, 178, 170));
        JLabel selezionaLabel = new JLabel("Seleziona Centro:");
        selezionaLabel.setForeground(Color.WHITE);
        selezionePanel.add(selezionaLabel);

        centriComboBox = new JComboBox<>();
        centriComboBox.addActionListener(e -> aggiornaDettagliCentro());
        selezionePanel.add(centriComboBox);

        add(selezionePanel, BorderLayout.NORTH);

        // Pannello centrale per dettagli del centro
        JPanel dettagliPanel = new JPanel(new GridBagLayout());
        dettagliPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        dettagliPanel.add(new JLabel("Nome:"), gbc);
        nomeField = new JTextField(20);
        gbc.gridx = 1;
        dettagliPanel.add(nomeField, gbc);

        // Provincia
        gbc.gridx = 0;
        gbc.gridy = 1;
        dettagliPanel.add(new JLabel("Provincia:"), gbc);
        provinciaField = new JTextField(20);
        gbc.gridx = 1;
        dettagliPanel.add(provinciaField, gbc);

        // Comune
        gbc.gridx = 0;
        gbc.gridy = 2;
        dettagliPanel.add(new JLabel("Comune:"), gbc);
        comuneField = new JTextField(20);
        gbc.gridx = 1;
        dettagliPanel.add(comuneField, gbc);

        add(dettagliPanel, BorderLayout.CENTER);

        // Pannello inferiore per pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255));

        salvaButton = new JButton("Salva Modifiche");
        salvaButton.addActionListener(e -> salvaModifiche());
        buttonPanel.add(salvaButton);

        modificaCampiButton = new JButton("Modifica Campi");
        modificaCampiButton.addActionListener(e -> modificaCampi());
        buttonPanel.add(modificaCampiButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Carica i centri sportivi gestiti
        caricaCentriGestiti();
    }

    private void caricaCentriGestiti() {
        // Controlla se l'utente è loggato
        utenteId = Sessione.getId();
        if (utenteId != null && utenteId > 0) {
            centriGestiti = dataBase.getCentriSportiviGestiti(utenteId); // Ottieni centri gestiti
            centriComboBox.removeAllItems(); // Svuota ComboBox

            if (centriGestiti != null && !centriGestiti.isEmpty()) {
                for (String nomeCentro : centriGestiti.keySet()) {
                    centriComboBox.addItem(nomeCentro);
                }
                aggiornaDettagliCentro(); // Aggiorna dettagli del primo centro
            } else {
                JOptionPane.showMessageDialog(this, "Nessun centro sportivo trovato per l'utente loggato.",
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Effettua il login per modificare i centri sportivi.",
                    "Errore", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aggiornaDettagliCentro() {
        // Aggiorna i dettagli del centro selezionato
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        if (centroSelezionato != null && centriGestiti != null) {
            CentroSportivo centro = centriGestiti.get(centroSelezionato);
            if (centro != null) {
                nomeField.setText(centro.nome);
                provinciaField.setText(centro.provincia);
                comuneField.setText(centro.comune);
            }
        }
    }

    private void salvaModifiche() {
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        if (centroSelezionato != null && centriGestiti != null) {
            CentroSportivo centro = centriGestiti.get(centroSelezionato);

            String nuovoNome = nomeField.getText();
            String nuovaProvincia = provinciaField.getText();
            String nuovoComune = comuneField.getText();

            boolean success = dataBase.updateCentroSportivo(centro.ID, nuovoNome, nuovaProvincia, nuovoComune);

            if (success) {
                JOptionPane.showMessageDialog(this, "Modifiche salvate con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                centro.nome = nuovoNome;
                centro.provincia = nuovaProvincia;
                centro.comune = nuovoComune;
            } else {
                JOptionPane.showMessageDialog(this, "Errore nel salvataggio delle modifiche.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void modificaCampi() {
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        if (centroSelezionato != null && centriGestiti != null) {
            CentroSportivo centro = centriGestiti.get(centroSelezionato);

            ModificaCampiDialog modificaCampiDialog = new ModificaCampiDialog(SwingUtilities.getWindowAncestor(this), centro, dataBase);
            modificaCampiDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un centro sportivo per modificarne i campi.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }
}
