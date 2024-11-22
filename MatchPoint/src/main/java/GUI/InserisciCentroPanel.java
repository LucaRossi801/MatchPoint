package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

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

        // Campi di testo
        JTextField nomeCentroField = new JTextField(20);
        JTextField provinciaField = new JTextField(20);
        JTextField comuneField = new JTextField(20);

        // Etichette e campi
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome Centro:"), gbc);
        gbc.gridx = 1;
        add(nomeCentroField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Provincia:"), gbc);
        gbc.gridx = 1;
        add(provinciaField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Comune:"), gbc);
        gbc.gridx = 1;
        add(comuneField, gbc);

        // Bottone per aprire sovrimpressione
        JButton aggiungiCampoButton = new JButton("Aggiungi Campo");
        aggiungiCampoButton.addActionListener(e -> {
            new AggiungiCampoDialog(SwingUtilities.getWindowAncestor(this));
        });
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(aggiungiCampoButton, gbc);

        // Pulsante "Indietro"
        JButton backButton = new JButton("Indietro");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "gestorePanel"));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
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
