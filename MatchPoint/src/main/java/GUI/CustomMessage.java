package GUI;

import java.awt.*;
import java.net.URL;
import javax.swing.*;

public class CustomMessage {

    /**
     * Mostra un messaggio personalizzato in una finestra di dialogo.
     * @param message il messaggio da visualizzare
     * @param title il titolo della finestra di dialogo
     * @param success true per un messaggio di successo (verde), false per errore (rosso)
     */
    public static void show(String message, String title, boolean success) {
        // Percorso del logo
        String logoPath = "/GUI/immagini/icona.png";

        JDialog dialog = new JDialog((Frame) null, title, true); // Il terzo parametro `true` lo rende modale
        dialog.setTitle(title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 250); // Dimensione del dialogo
        dialog.setLocationRelativeTo(null); // Centra la finestra
        dialog.setLayout(new BorderLayout());
        dialog.setAlwaysOnTop(true); // Lo rende sempre visibile sopra tutte le finestre
        dialog.toFront();           // Porta la finestra in primo piano
        dialog.repaint();           // Forza un aggiornamento grafico

        // Imposta l'icona della finestra
        if (logoPath != null) {
            URL iconURL = CustomMessage.class.getResource(logoPath);
            if (iconURL != null) {
                ImageIcon icon = new ImageIcon(iconURL);
                dialog.setIconImage(icon.getImage());
            } else {
                System.err.println("Icona non trovata: " + logoPath);
            }
        }

        // Imposta il colore di sfondo
        JPanel panel = new JPanel();
        panel.setBackground(success ? new Color(34, 139, 34) : Color.RED);
        panel.setLayout(new BorderLayout());
        dialog.add(panel);

        // Testo del messaggio
        JLabel messageLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setForeground(Color.WHITE); // Testo bianco
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(messageLabel, BorderLayout.CENTER);

        // Pulsante OK
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setPreferredSize(new Dimension(80, 30)); // Pulsante più piccolo
        okButton.setBackground(Color.WHITE); // Sfondo pulsante
        okButton.setForeground(success ? new Color(32, 178, 170) : Color.RED); // Testo in base al successo
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose()); // Chiude il dialog

        // Pannello per il pulsante
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Trasparente per adattarsi al colore di sfondo
        buttonPanel.add(okButton); // Pulsante centrato
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
