package GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CreateGestorePanel extends JPanel {
    private Image clearImage;

    public CreateGestorePanel(CardLayout cardLayout, JPanel cardPanel) {
        // Carica l'immagine di sfondo
        URL clearImageUrl = getClass().getClassLoader().getResource("GUI/immagini/sfondohome.png");
        if (clearImageUrl != null) {
            clearImage = new ImageIcon(clearImageUrl).getImage();
        } else {
            System.out.println("Errore nel caricamento dell'immagine: GUI/immagini/sfondohome.png");
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;

        Dimension buttonSize = new Dimension(300, 90);

        // Crea pulsanti
        JButton inserisciCentroButton = creaBottone("Inserisci Centro", "GUI/immagini/add_icon.png", buttonSize);
        inserisciCentroButton.addActionListener(e -> cardLayout.show(cardPanel, "inserisciCentro"));
        gbc.gridy = 0;
        add(inserisciCentroButton, gbc);

        JButton modificaCentroButton = creaBottone("Modifica Centro", "GUI/immagini/edit_icon.png", buttonSize);
        modificaCentroButton.addActionListener(e -> cardLayout.show(cardPanel, "modificaCentro"));
        gbc.gridy = 1;
        add(modificaCentroButton, gbc);

        JButton vediPrenotazioniButton = creaBottone("Vedi Prenotazioni", "GUI/immagini/list_icon.png", buttonSize);
        vediPrenotazioniButton.addActionListener(e -> cardLayout.show(cardPanel, "vediPrenotazioni"));
        gbc.gridy = 2;
        add(vediPrenotazioniButton, gbc);

        JButton backButton = creaBottone("Back", null, new Dimension(150, 50));
        backButton.setBackground(Color.GRAY);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));
        gbc.gridy = 3;
        add(backButton, gbc);
    }

    private JButton creaBottone(String testo, String percorsoIcona, Dimension size) {
        JButton button = new JButton(testo);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setPreferredSize(size);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(15);

        // Aggiungi icona PNG se disponibile
        if (percorsoIcona != null) {
            try {
                ImageIcon icon = new ImageIcon(percorsoIcona);
                // Ridimensiona l'icona per adattarla alla dimensione desiderata
                Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            } catch (Exception ex) {
                System.out.println("Errore nel caricamento dell'icona PNG: " + percorsoIcona);
            }
        }

        // Stile FlatLaf
        button.putClientProperty("JButton.buttonType", "roundRect"); // Stile moderno arrotondato
        button.putClientProperty("JButton.backgroundColor", new Color(32, 178, 170));
        button.putClientProperty("JButton.hoverBackgroundColor", new Color(28, 144, 138));

        return button;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (clearImage != null) {
            g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
