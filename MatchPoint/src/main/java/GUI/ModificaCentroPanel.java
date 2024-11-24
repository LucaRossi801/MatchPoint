package GUI;

import javax.swing.*;
import components.CentroSportivo;
import dataBase.DataBase;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class ModificaCentroPanel extends JPanel {
    private JComboBox<String> centriComboBox;
    private JTextField nomeField, provinciaField, comuneField;
    private JButton salvaButton, modificaCampiButton;
    private Map<String, CentroSportivo> centriGestiti; // Mappa con nome del centro come chiave e CentroSportivo come valore
    private int utenteId; // ID dell'utente loggato
    private DataBase dataBase; // Riferimento alla classe DataBase

    public ModificaCentroPanel(CardLayout cardLayout, JPanel cardPanel) {
     
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Sfondo chiaro

        // Recupera i centri gestiti tramite il metodo di DataBase
        centriGestiti = DataBase.getCentriSportiviGestiti(utenteId);

        // Pannello superiore per selezione del centro
        JPanel selezionePanel = new JPanel(new FlowLayout());
        selezionePanel.setBackground(new Color(32, 178, 170));

        JLabel selezionaLabel = new JLabel("Seleziona Centro:");
        selezionaLabel.setForeground(Color.WHITE);
        selezionePanel.add(selezionaLabel);

        centriComboBox = new JComboBox<>(centriGestiti.keySet().toArray(new String[0]));
        centriComboBox.addActionListener(e -> aggiornaDettagliCentro());
        selezionePanel.add(centriComboBox);

        add(selezionePanel, BorderLayout.NORTH);

        // Pannello centrale per i dettagli del centro
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

        // Pannello inferiore per i pulsanti
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 248, 255));

        salvaButton = new JButton("Salva Modifiche");
        salvaButton.addActionListener(e -> salvaModifiche());
        buttonPanel.add(salvaButton);

        modificaCampiButton = new JButton("Modifica Campi");
        modificaCampiButton.addActionListener(e -> modificaCampi());
        buttonPanel.add(modificaCampiButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Popola i dettagli del primo centro selezionato
        aggiornaDettagliCentro();
    }

    /**
     * Aggiorna i campi di input con i dettagli del centro selezionato.
     */
    private void aggiornaDettagliCentro() {
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        if (centroSelezionato != null) {
            CentroSportivo centro = centriGestiti.get(centroSelezionato);
            nomeField.setText(centro.nome);
            provinciaField.setText(centro.provincia);
            comuneField.setText(centro.comune);
        }
    }

    /**
     * Salva le modifiche al centro selezionato nel database.
     */
    private void salvaModifiche() {
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        if (centroSelezionato != null) {
            CentroSportivo centro = centriGestiti.get(centroSelezionato);

            String nuovoNome = nomeField.getText();
            String nuovaProvincia = provinciaField.getText();
            String nuovoComune = comuneField.getText();

            // Usa il metodo di DataBase per aggiornare il centro
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

    /**
     * Naviga a una schermata per modificare i campi del centro selezionato.
     */
    private void modificaCampi() {
        // Ottieni il nome del centro selezionato
        String centroSelezionato = (String) centriComboBox.getSelectedItem();
        if (centroSelezionato != null) {
            // Ottieni l'oggetto CentroSportivo corrispondente
            CentroSportivo centro = centriGestiti.get(centroSelezionato);

            // Crea un JDialog per modificare i campi del centro selezionato
            ModificaCampiDialog modificaCampiDialog = new ModificaCampiDialog(SwingUtilities.getWindowAncestor(this), centro, dataBase);
            modificaCampiDialog.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un centro sportivo per modificarne i campi.",
                    "Attenzione", JOptionPane.WARNING_MESSAGE);
        }
    }
}
