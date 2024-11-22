package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InserisciCentroPanel extends JPanel {
    private Image background;

    public InserisciCentroPanel(CardLayout cardLayout, JPanel cardPanel) {
        // Carica immagine di sfondo
        URL backgroundUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
        if (backgroundUrl != null) {
            background = new ImageIcon(backgroundUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine di sfondo.");
        }

        // Layout e configurazione
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titolo
        JLabel titleLabel = new OutlinedLabel("Inserisci Centro", Color.WHITE);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Il titolo occupa due colonne
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Mappa per gestire i campi
        Map<String, JTextField> fields = new HashMap<>();

        // Campi di testo
        String[] campi = {"NomeCentro", "Provincia", "Comune"};
        int row = 1; // Riga di partenza

        for (String campo : campi) {
            // Etichetta
            JLabel label = new JLabel(campo + ":");
            label.setFont(new Font("Montserrat", Font.BOLD, 24));
            gbc.gridx = 0; // Colonna sinistra
            gbc.gridy = row;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            add(label, gbc);

            // Campo di input
            JTextField inputField = new JTextField(20);
            inputField.setFont(new Font("Arial", Font.PLAIN, 18));
            gbc.gridx = 1; // Colonna destra
            add(inputField, gbc);

            // Salva il campo nella mappa
            fields.put(campo, inputField);

            row++;
        }

        // Bottone per aggiungere campo
        JButton aggiungiCampoButton = BackgroundPanel.createFlatButton(
            "Aggiungi Campo",
            e -> {
                // Apre il dialog
                new AggiungiCampoDialog(SwingUtilities.getWindowAncestor(this));
            },
            new Dimension(300, 50)
        );
        aggiungiCampoButton.setFont(new Font("Arial", Font.BOLD, 18));
        aggiungiCampoButton.setBackground(new Color(32, 178, 170));
        aggiungiCampoButton.setForeground(new Color(220, 250, 245));
        aggiungiCampoButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(aggiungiCampoButton, gbc);

        // Bottone Indietro
        JButton backButton = BackgroundPanel.createFlatButton(
            "Indietro",
            e -> {
                // Cambia schermata
                cardLayout.show(cardPanel, "gestorePanel");
            },
            new Dimension(120, 30)
        );
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setForeground(Color.GRAY);
        backButton.setBackground(Color.DARK_GRAY);

        gbc.gridy = row + 1;
        add(backButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
