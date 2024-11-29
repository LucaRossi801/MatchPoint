package GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class InserisciPrenotazionePanel extends JPanel {
    private Image clearImage;

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
        gbc.insets = new Insets(15, 15, 15, 15); // Spaziatura uniforme
        gbc.fill = GridBagConstraints.HORIZONTAL; // Riempie orizzontalmente

        // Titolo
        JLabel titolo = new JLabel("Inserisci Prenotazione", JLabel.CENTER);
        titolo.setFont(new Font("Arial", Font.BOLD, 28));
        titolo.setForeground(Color.WHITE); // Colore del titolo
        gbc.gridwidth = 2; // Occupa due colonne
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titolo, gbc);

        gbc.gridwidth = 1; // Torna a una colonna

        // Bottone e label per selezionare il centro
        JButton selezionaCentroButton = BackgroundPanel.createFlatButton(
            "Seleziona Centro", 
            e -> cardLayout.show(cardPanel, "selezionaCentro"),
            new Dimension(200, 50)
        );
        selezionaCentroButton.setForeground(Color.WHITE); // Colore del testo
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Centro:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(selezionaCentroButton, gbc);

        // Bottone e label per selezionare il campo
        JButton selezionaCampoButton = BackgroundPanel.createFlatButton(
            "Seleziona Campo", 
            e -> cardLayout.show(cardPanel, "selezionaCampo"),
            new Dimension(200, 50)
        );
        selezionaCampoButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Campo:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(selezionaCampoButton, gbc);

        // Campo orario per selezionare l'orario di inizio
        JSpinner orarioInizio = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorInizio = new JSpinner.DateEditor(orarioInizio, "HH:mm");
        orarioInizio.setEditor(editorInizio);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Ora Inizio:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(orarioInizio, gbc);

        // Campo orario per selezionare l'orario di fine
        JSpinner orarioFine = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorFine = new JSpinner.DateEditor(orarioFine, "HH:mm");
        orarioFine.setEditor(editorFine);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Ora Fine:", JLabel.RIGHT), gbc);
        gbc.gridx = 1;
        add(orarioFine, gbc);

        // Bottone per riepilogo
        JButton riepilogoButton = BackgroundPanel.createFlatButton(
            "Mostra Riepilogo", 
            e -> JOptionPane.showMessageDialog(this, 
                "Centro: Selezionato\n" +
                "Campo: Selezionato\n" +
                "Orario Inizio: " + editorInizio.getFormat().format(orarioInizio.getValue()) + "\n" +
                "Orario Fine: " + editorFine.getFormat().format(orarioFine.getValue()),
                "Riepilogo Prenotazione",
                JOptionPane.INFORMATION_MESSAGE
            ),
            new Dimension(200, 50)
        );
        riepilogoButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(riepilogoButton, gbc);

        // Bottone "Torna Indietro"
        JButton backButton = BackgroundPanel.createFlatButton(
            "Torna Indietro", 
            e -> cardLayout.show(cardPanel, "menuPrincipale"), 
            new Dimension(200, 50)
        );
        backButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(backButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
