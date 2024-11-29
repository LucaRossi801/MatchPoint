package GUI;

import org.jdesktop.swingx.JXDatePicker;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InserisciPrenotazionePanel extends JPanel {
    private Image clearImage;

    // Label dinamiche per visualizzare il centro e il campo selezionati
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

        // Imposta il layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel titolo = new JLabel("Inserisci Prenotazione", JLabel.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 28));
        titolo.setForeground(Color.WHITE);
        gbc.gridwidth = 3; // Occupa più spazio orizzontale
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titolo, gbc);

        gbc.gridwidth = 1;

        // Bottone e label per selezionare il centro
        JButton selezionaCentroButton = BackgroundPanel.createFlatButton(
            "Seleziona Centro",
            e -> {
                centroSelezionatoLabel.setText("Centro: Centro X");
                cardLayout.show(cardPanel, "selezionaCentro");
            },
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

        // Bottone e label per selezionare il campo
        JButton selezionaCampoButton = BackgroundPanel.createFlatButton(
            "Seleziona Campo",
            e -> {
                campoSelezionatoLabel.setText("Campo: Campo Y");
                cardLayout.show(cardPanel, "selezionaCampo");
            },
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
        datePicker.setFormats(new SimpleDateFormat("dd/MM/yyyy")); // Formato della data
        datePicker.setDate(new Date()); // Data predefinita: oggi
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Data:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(datePicker, gbc);

        // Spinner per selezionare l'orario di inizio
        JSpinner orarioInizio = createTimeSpinner();
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Ora Inizio:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(orarioInizio, gbc);

        // Spinner per selezionare l'orario di fine
        JSpinner orarioFine = createTimeSpinner();
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Ora Fine:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(orarioFine, gbc);

        // Bottone per riepilogo
        JButton riepilogoButton = BackgroundPanel.createFlatButton(
            "Mostra Riepilogo",
            e -> JOptionPane.showMessageDialog(this,
                "Centro: " + centroSelezionatoLabel.getText() + "\n" +
                "Campo: " + campoSelezionatoLabel.getText() + "\n" +
                "Data: " + new SimpleDateFormat("dd/MM/yyyy").format(datePicker.getDate()) + "\n" +
                "Orario Inizio: " + orarioInizio.getValue() + "\n" +
                "Orario Fine: " + orarioFine.getValue(),
                "Riepilogo Prenotazione",
                JOptionPane.INFORMATION_MESSAGE
            ),
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

    private JSpinner createTimeSpinner() {
        // Imposta l'ora iniziale alle 08:00
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startTime = calendar.getTime();

        // Modello personalizzato con incremento di 30 minuti
        SpinnerDateModel timeModel = new SpinnerDateModel(startTime, null, null, Calendar.MINUTE);
        JSpinner timeSpinner = new JSpinner(timeModel);

        // Imposta un editor con il formato HH:mm
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);

        // Arrotondamento al multiplo più vicino di 30 minuti
        timeSpinner.addChangeListener(e -> {
            Date currentValue = (Date) timeSpinner.getValue();
            calendar.setTime(currentValue);

            // Arrotonda ai multipli di 30 minuti
            int minutes = calendar.get(Calendar.MINUTE);
            if (minutes % 30 != 0) {
                calendar.set(Calendar.MINUTE, (minutes / 30) * 30);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                timeSpinner.setValue(calendar.getTime());
            }
        });

        // Incremento personalizzato di 30 minuti
        InputMap inputMap = timeSpinner.getInputMap(JSpinner.WHEN_FOCUSED);
        ActionMap actionMap = timeSpinner.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "increment");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "decrement");

        actionMap.put("increment", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar.setTime((Date) timeSpinner.getValue());
                calendar.add(Calendar.MINUTE, 30);
                timeSpinner.setValue(calendar.getTime());
            }
        });

        actionMap.put("decrement", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calendar.setTime((Date) timeSpinner.getValue());
                calendar.add(Calendar.MINUTE, -30);
                timeSpinner.setValue(calendar.getTime());
            }
        });

        return timeSpinner;
    }







    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
