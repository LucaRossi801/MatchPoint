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
import java.util.*;
import java.util.List;

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
        JButton selezionaCentroButton = creaFlatButton("Seleziona Centro", e -> apriSelezionaCentroDialog());
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(selezionaCentroButton, gbc);

        centroSelezionatoLabel = new JLabel("Centro: Non selezionato", JLabel.LEFT);
        centroSelezionatoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centroSelezionatoLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        add(centroSelezionatoLabel, gbc);

        // Bottone per selezionare il campo
        JButton selezionaCampoButton = creaFlatButton("Seleziona Campo", e -> apriSelezionaCampoDialog());
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
        JButton riepilogoButton = creaFlatButton("Mostra Riepilogo", e -> mostraRiepilogo(datePicker));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(riepilogoButton, gbc);

        // Bottone "Torna Indietro"
        JButton backButton = creaFlatButton("Torna Indietro", e -> cardLayout.show(cardPanel, "menuPrincipale"));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(backButton, gbc);
    }

    private JButton creaFlatButton(String testo, java.awt.event.ActionListener azione) {
        JButton button = BackgroundPanel.createFlatButton(testo, azione, new Dimension(250, 50));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void apriSelezionaCentroDialog() {
        String filePath = "src/main/java/localizzazione/comuni.csv"; // Percorso del file CSV
        Map<String, List<String>> provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePath);

        if (provinceComuni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Errore: Nessuna provincia trovata nel file CSV.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Seleziona la provincia
        Map<String, String> provinceMap = new HashMap<>();
        for (String provincia : provinceComuni.keySet()) {
            provinceMap.put(provincia, provincia); // Mappa Provincia -> Provincia
        }

        // Usa il dialogo per selezionare la provincia
        SelezionaDialog selezionaProvinciaDialog = new SelezionaDialog("Seleziona Provincia", provinceMap);
        selezionaProvinciaDialog.setVisible(true);

        String provinciaSelezionata = selezionaProvinciaDialog.getSelezione();
        if (provinciaSelezionata == null) {
            JOptionPane.showMessageDialog(this, "Nessuna provincia selezionata.", "Errore", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Usa il database per ottenere una mappa Nome->ID per i centri della provincia selezionata
        Map<String, CentroSportivo> centriMap = DataBase.getCentriSportiviPerProvincia(provinciaSelezionata);
        Map<String, String> centriNomiID = new HashMap<>();
        for (Map.Entry<String, CentroSportivo> entry : centriMap.entrySet()) {
            centriNomiID.put(entry.getValue().getNome(), entry.getKey()); // Nome come chiave, ID come valore
        }

        // Mostra il dialogo per i centri sportivi
        SelezionaDialog selezionaCentroDialog = new SelezionaDialog("Seleziona Centro Sportivo", centriNomiID);
        selezionaCentroDialog.setVisible(true);

        String centroSelezionato = selezionaCentroDialog.getSelezione();
        String centroID = (String) selezionaCentroDialog.getSelezioneID(); // Seleziona l'ID corretto

        if (centroSelezionato != null) {
            centroSelezionatoLabel.setText(centroSelezionato);
        } else {
            centroSelezionatoLabel.setText("Non selezionato");
        }
    }

    private void apriSelezionaCampoDialog() {
        String centroCorrente = centroSelezionatoLabel.getText().trim();

        if (centroCorrente.equals("Non selezionato")) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un centro.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("Centro :  "+centroCorrente);
        CentroSportivo c = DataBase.getCentroByName(centroCorrente);
        int centroId = c.getID();

        if (centroId == -1) {
            JOptionPane.showMessageDialog(this, "Centro non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, Integer> campiMap = DataBase.getCampiCentroMappa(centroId);

        if (campiMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun campo disponibile per il centro selezionato.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SelezionaDialog selezionaCampoDialog = new SelezionaDialog("Seleziona Campo", campiMap);
        selezionaCampoDialog.setVisible(true);

        String campoSelezionato = selezionaCampoDialog.getSelezione();
        if (campoSelezionato != null) {
            campoSelezionatoLabel.setText("Campo: " + campoSelezionato);
        } else {
            campoSelezionatoLabel.setText("Campo: Non selezionato");
        }
    }

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
