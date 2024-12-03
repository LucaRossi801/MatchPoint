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
        centroSelezionatoLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Font più grande
        centroSelezionatoLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        add(centroSelezionatoLabel, gbc);

        // Bottone per selezionare il campo
        JButton selezionaCampoButton = creaFlatButton("Seleziona Campo", e -> apriSelezionaCampoDialog());
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(selezionaCampoButton, gbc);

        campoSelezionatoLabel = new JLabel("Campo: Non selezionato", JLabel.LEFT);
        campoSelezionatoLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Font più grande
        campoSelezionatoLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        add(campoSelezionatoLabel, gbc);

        // Date Picker per la selezione della data
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setFormats(new SimpleDateFormat("dd-MM-yyyy"));
        datePicker.setDate(new Date());
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new OutlinedLabel("Data:", Color.BLACK), gbc); // OutlinedLabel
        gbc.gridx = 1;
        add(datePicker, gbc);

        // Spinner per orario di inizio e fine
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new OutlinedLabel("Ora Inizio:", Color.BLACK), gbc); // OutlinedLabel
        gbc.gridx = 1;
        add(createCustomTimeSpinner(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new OutlinedLabel("Ora Fine:", Color.BLACK), gbc); // OutlinedLabel
        gbc.gridx = 1;
        add(createCustomTimeSpinner(), gbc);

        // Bottone "Riepilogo"
        JButton riepilogoButton = creaFlatButton("Mostra Riepilogo", e -> mostraRiepilogo(datePicker));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(riepilogoButton, gbc);

        JButton backButton = BackgroundPanel.createFlatButton(
                "Back",
                e -> {
                    Login.resetFields(); // Svuota i campi di testo
                    BackgroundPanel.showPanel("createGiocatore"); // Torna al pannello di login
                },
                new Dimension(150, 30) // Dimensione personalizzata del bottone
        );
        // Personalizza colore per il pulsante "Back"
        backButton.setForeground(Color.GRAY); // Sfondo grigio
        backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
        backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back"
        gbc.gridy = 7; // Quarta riga
        add(backButton, gbc);
    }


    private JButton creaFlatButton(String testo, java.awt.event.ActionListener azione) {
        JButton button = BackgroundPanel.createFlatButton(testo, azione, new Dimension(250, 50));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void apriSelezionaCentroDialog() {
        // Carica le province e i comuni dal file CSV
        String filePathProvince = "src/main/java/localizzazione/comuni.csv"; // Assicurati che questo sia il percorso corretto
        Map<String, List<String>> provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePathProvince);

        // Mappa che conterrà i centri per provincia
        Map<String, Map<String, Integer>> centriByProvince = new HashMap<>();

        // Per ogni provincia, otteniamo i centri dal database
        for (String provincia : provinceComuni.keySet()) {
            Map<String, CentroSportivo> centri = DataBase.getCentriSportiviPerProvincia(provincia);
            Map<String, Integer> centriMap = new HashMap<>();
            for (CentroSportivo centro : centri.values()) {
                // Qui mettiamo l'ID del centro come Integer
                centriMap.put(centro.getNome(), centro.getID());
            }
            centriByProvince.put(provincia, centriMap);
        }

        // Mostra il dialogo per selezionare la provincia e il centro sportivo
        SelezionaDialog selezionaDialog = new SelezionaDialog("Seleziona Centro", filePathProvince, centriByProvince);
        selezionaDialog.setVisible(true);

        // Ottieni la selezione fatta dall'utente
        String centroSelezionato = selezionaDialog.getSelezione();
        Integer centroID = (Integer) selezionaDialog.getSelezioneID(); // ID come Integer

        if (centroSelezionato != null) {
            centroSelezionatoLabel.setText(centroSelezionato);
            System.out.println("Centro ID selezionato: " + centroID);
        } else {
            centroSelezionatoLabel.setText("Centro: Non selezionato");
        }
    }



    private void apriSelezionaCampoDialog() {
        String centroCorrente = centroSelezionatoLabel.getText().replace("Centro: ", "").trim();

        if (centroCorrente.equals("Non selezionato")) {
            JOptionPane.showMessageDialog(this, "Seleziona prima un centro.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Recupera l'ID del centro
        CentroSportivo centro = DataBase.getCentroByName(centroCorrente);
        if (centro == null) {
            JOptionPane.showMessageDialog(this, "Centro non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ottieni i campi del centro
        Map<String, Integer> campi = DataBase.getCampiCentroMappa(centro.getID());
        if (campi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun campo disponibile per il centro selezionato.", 
                "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostra dialogo per selezionare il campo
        Object[] opzioni = campi.keySet().toArray();
        String campoSelezionato = (String) JOptionPane.showInputDialog(this, 
            "Seleziona il campo:", "Campi disponibili", JOptionPane.PLAIN_MESSAGE, 
            null, opzioni, opzioni[0]);

        if (campoSelezionato != null) {
            campoSelezionatoLabel.setText("Campo: " + campoSelezionato);
            System.out.println("Campo ID selezionato: " + campi.get(campoSelezionato));
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
