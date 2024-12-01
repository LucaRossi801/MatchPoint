package GUI;

import org.jdesktop.swingx.JXDatePicker;

import components.CentroSportivo;
import dataBase.DataBase;
import localizzazione.FileReaderUtils;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class InserisciPrenotazionePanel extends JPanel {
    private Image clearImage;

    private JLabel centroSelezionatoLabel;
    private JLabel campoSelezionatoLabel;

    public InserisciPrenotazionePanel(CardLayout cardLayout, JPanel cardPanel) {
        // Carica l'immagine di sfondo
        URL clearImageUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
        if (clearImageUrl != null) {
            clearImage = new ImageIcon(clearImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine: GUI/immagini/sfondohome.png");
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel titolo = new JLabel("Inserisci Prenotazione", JLabel.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 28));
        titolo.setForeground(Color.WHITE);
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titolo, gbc);

        gbc.gridwidth = 1;

        // Bottone per selezionare il centro
        JButton selezionaCentroButton = BackgroundPanel.createFlatButton(
            "Seleziona Centro",
            e -> apriSelezionaCentroDialog(),
            new Dimension(250, 50)
        );
        selezionaCentroButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(selezionaCentroButton, gbc);

        centroSelezionatoLabel = new JLabel("Centro: Non selezionato", JLabel.LEFT);
        centroSelezionatoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centroSelezionatoLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        add(centroSelezionatoLabel, gbc);

        // Bottone per selezionare il campo
        JButton selezionaCampoButton = BackgroundPanel.createFlatButton(
            "Seleziona Campo",
            e -> apriSelezionaCampoDialog(),
            new Dimension(250, 50)
        );
        selezionaCampoButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(selezionaCampoButton, gbc);

        campoSelezionatoLabel = new JLabel("Campo: Non selezionato", JLabel.LEFT);
        campoSelezionatoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        campoSelezionatoLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        add(campoSelezionatoLabel, gbc);

        // Date Picker per la selezione della data
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));
        datePicker.setDate(new Date());
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Data:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(datePicker, gbc);

        // Spinner per orario di inizio e fine
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Ora Inizio:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(createCustomTimeSpinner(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Ora Fine:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(createCustomTimeSpinner(), gbc);

        // Bottone "Riepilogo"
        JButton riepilogoButton = BackgroundPanel.createFlatButton(
            "Mostra Riepilogo",
            e -> mostraRiepilogo(datePicker),
            new Dimension(250, 50)
        );
        riepilogoButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(riepilogoButton, gbc);

        // Bottone "Torna Indietro"
        JButton backButton = BackgroundPanel.createFlatButton(
            "Torna Indietro",
            e -> cardLayout.show(cardPanel, "menuPrincipale"),
            new Dimension(250, 50)
        );
        backButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(backButton, gbc);
    }

    private void apriSelezionaCentroDialog() {
        String filePath = "src/main/java/localizzazione/comuni.csv"; // Percorso del file CSV
        Map<String, List<String>> provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePath);

        if (provinceComuni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Errore: Nessuna provincia trovata nel file CSV.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crea il pannello con la ComboBox per la provincia
        JPanel selezionePanel = new JPanel(new FlowLayout());
        selezionePanel.add(new JLabel("Provincia:"));

        JComboBox<String> provinciaComboBox = new JComboBox<>(provinceComuni.keySet().toArray(new String[0]));
        provinciaComboBox.setFont(new Font("Montserrat", Font.PLAIN, 18));
        selezionePanel.add(provinciaComboBox);

        // Mostra una finestra di dialogo per selezionare la provincia
        int risultato = JOptionPane.showConfirmDialog(
            this,
            selezionePanel,
            "Seleziona Provincia",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (risultato == JOptionPane.OK_OPTION) {
            String provinciaSelezionata = (String) provinciaComboBox.getSelectedItem();
            if (provinciaSelezionata != null) {
                // Ottieni i centri per la provincia selezionata
                Map<String, CentroSportivo> centriDisponibili = DataBase.getCentriSportiviPerProvincia(provinciaSelezionata);
                if (centriDisponibili.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nessun centro disponibile per la provincia selezionata.", "Avviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

              /*  // Crea il dialogo per selezionare il centro
                SelezionaDialog selezionaCentroDialog = new SelezionaDialog(
                    "Seleziona Centro",
                    centriDisponibili
                );
                selezionaCentroDialog.setVisible(true);

                // Ottieni la selezione e aggiorna la label
                String centroSelezionato = selezionaCentroDialog.getSelezione();
                if (centroSelezionato != null) {
                    centroSelezionatoLabel.setText("Centro: " + centroSelezionato);*/
            }
        }
    }


    private void apriSelezionaCampoDialog() {
        String centroCorrente = centroSelezionatoLabel.getText().replace("Centro: ", "").trim();
        if (centroCorrente.equals("Non selezionato")) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un centro.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
       /* List<String> campi = DataBase.getCampiPerCentro(centroCorrente);
        if (campi == null || campi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun campo disponibile.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SelezionaDialog dialog = new SelezionaDialog("Seleziona Campo", null, campi);
        dialog.setVisible(true);

        String selezione = dialog.getSelezione();
        if (selezione != null) {
            campoSelezionatoLabel.setText("Campo: " + selezione);
        }
    }*/

    private void mostraRiepilogo(JXDatePicker datePicker) {
        JOptionPane.showMessageDialog(this,
            "Centro: " + centroSelezionatoLabel.getText() + "\n" +
            "Campo: " + campoSelezionatoLabel.getText() + "\n" +
            "Data: " + new SimpleDateFormat("dd/MM/yyyy").format(datePicker.getDate()),
            "Riepilogo Prenotazione",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private JSpinner createCustomTimeSpinner() {
        List<String> times = generateTimeValues();
        JSpinner timeSpinner = new JSpinner(new SpinnerListModel(times));
        timeSpinner.setValue(times.get(0));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
