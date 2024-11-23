package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class InserisciCentroPanel extends JPanel {
    private Image background;
	private final JTextArea riepilogoArea; // Variabile d'istanza

    public InserisciCentroPanel(CardLayout cardLayout, JPanel cardPanel) {
        // Inizializza la JTextArea
        riepilogoArea = new JTextArea(10, 30); // Inizializza la variabile d'istanza
        riepilogoArea.setEditable(false);
        riepilogoArea.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(riepilogoArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

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
            JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
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
                // Apre il dialog passando la variabile d'istanza riepilogoArea
                new AggiungiCampoDialog(SwingUtilities.getWindowAncestor(this), riepilogoArea);
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

        // Aggiungi il JTextArea per il riepilogo
        gbc.gridy = row + 1;
        gbc.gridwidth = 2;
        add(scrollPane, gbc);

        // Bottone Inserisci Centro
        JButton inserisciCentroButton = BackgroundPanel.createFlatButton(
            "Inserisci Centro",
            e -> {
                // Recupera i dati inseriti e fai l'inserimento
                String nomeCentro = fields.get("NomeCentro").getText();
                String provincia = fields.get("Provincia").getText();
                String comune = fields.get("Comune").getText();

                // Controlla se tutti i campi sono stati compilati
                if (nomeCentro.isEmpty() || provincia.isEmpty() || comune.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Logica per salvare il centro
                System.out.println("Centro inserito: Nome=" + nomeCentro + ", Provincia=" + provincia + ", Comune=" + comune);
                JOptionPane.showMessageDialog(this, "Centro inserito con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            },
            new Dimension(300, 50)
        );

        inserisciCentroButton.setFont(new Font("Arial", Font.BOLD, 18));
        inserisciCentroButton.setBackground(new Color(0, 128, 128));
        inserisciCentroButton.setForeground(Color.WHITE);

        gbc.gridy = row + 2;
        add(inserisciCentroButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
