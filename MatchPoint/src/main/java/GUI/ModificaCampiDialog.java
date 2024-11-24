package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import components.CentroSportivo;
import dataBase.DataBase;

public class ModificaCampiDialog extends JDialog {
    private CentroSportivo centroSportivo; // Il centro sportivo selezionato
    private DataBase dataBase; // Classe per interagire con il database
    private DefaultListModel<String> campiListModel; // Modello per i campi sportivi
    private JList<String> campiList; // Lista visibile
    private JButton aggiungiButton, modificaButton, eliminaButton;

    public ModificaCampiDialog(Window parent, CentroSportivo centro, DataBase dataBase) {
        super(parent, "Modifica Campi - " + centro.nome, ModalityType.APPLICATION_MODAL);
        this.centroSportivo = centro;
        this.dataBase = dataBase;

        // Configura il layout del dialog
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent); // Centra il dialog rispetto al parent frame

        // Inizializza l'elenco dei campi
        campiListModel = new DefaultListModel<>();
        campiList = new JList<>(campiListModel);
        caricaCampi(); // Metodo che carica i campi del centro selezionato

        JScrollPane scrollPane = new JScrollPane(campiList);
        add(scrollPane, BorderLayout.CENTER);

        // Pannello dei pulsanti
        JPanel buttonPanel = new JPanel();
        aggiungiButton = new JButton("Aggiungi Campo");
        modificaButton = new JButton("Modifica Campo");
        eliminaButton = new JButton("Elimina Campo");

        // Aggiungi ActionListener ai pulsanti
        aggiungiButton.addActionListener(e -> aggiungiCampo());
        modificaButton.addActionListener(e -> modificaCampo());
        eliminaButton.addActionListener(e -> eliminaCampo());

        buttonPanel.add(aggiungiButton);
        buttonPanel.add(modificaButton);
        buttonPanel.add(eliminaButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Metodo per caricare i campi dal database.
     */
    private void caricaCampi() {
        campiListModel.clear();
        for (String campo : dataBase.getCampiByCentro(centroSportivo.ID)) {
            campiListModel.addElement(campo);
        }
    }

    /**
     * Metodo per aggiungere un nuovo campo.
     */
    private void aggiungiCampo() {
        String nuovoCampo = JOptionPane.showInputDialog(this, "Nome del nuovo campo:", "Aggiungi Campo", JOptionPane.PLAIN_MESSAGE);
        if (nuovoCampo != null && !nuovoCampo.trim().isEmpty()) {
            if (dataBase.aggiungiCampo(centroSportivo.ID, nuovoCampo)) {
                campiListModel.addElement(nuovoCampo);
                JOptionPane.showMessageDialog(this, "Campo aggiunto con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Errore nell'aggiunta del campo.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Metodo per modificare un campo esistente.
     */
    private void modificaCampo() {
        String campoSelezionato = campiList.getSelectedValue();
        if (campoSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un campo da modificare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nuovoNome = JOptionPane.showInputDialog(this, "Nuovo nome del campo:", campoSelezionato);
        if (nuovoNome != null && !nuovoNome.trim().isEmpty()) {
            if (dataBase.modificaCampo(centroSportivo.ID, campoSelezionato, nuovoNome)) {
                campiListModel.set(campiList.getSelectedIndex(), nuovoNome);
                JOptionPane.showMessageDialog(this, "Campo modificato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Errore nella modifica del campo.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Metodo per eliminare un campo.
     */
    private void eliminaCampo() {
        String campoSelezionato = campiList.getSelectedValue();
        if (campoSelezionato == null) {
            JOptionPane.showMessageDialog(this, "Seleziona un campo da eliminare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conferma = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler eliminare il campo \"" + campoSelezionato + "\"?",
                "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);
        if (conferma == JOptionPane.YES_OPTION) {
            if (dataBase.eliminaCampo(centroSportivo.ID, campoSelezionato)) {
                campiListModel.removeElement(campoSelezionato);
                JOptionPane.showMessageDialog(this, "Campo eliminato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Errore nell'eliminazione del campo.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
