package GUI;

import java.awt.*;
import javax.swing.*;

public class CustomMessage {

    /**
     * Mostra un messaggio personalizzato in una finestra di dialogo.
     * @param message il messaggio da visualizzare
     * @param title il titolo della finestra di dialogo
     * @param success true per un messaggio di successo (verde acqua), false per errore (rosso)
     */
    public static void show(String message, String title, boolean success) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(null); // Centra la finestra
        dialog.setLayout(new BorderLayout());

        // Imposta il colore di sfondo
        JPanel panel = new JPanel();
        panel.setBackground(success ? new Color(32, 178, 170) : Color.RED);
        panel.setLayout(new BorderLayout());
        dialog.add(panel);

        // Testo del messaggio
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setForeground(Color.WHITE); // Testo bianco
        panel.add(messageLabel, BorderLayout.CENTER);

        // Pulsante per chiudere
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 18));
        okButton.setBackground(Color.WHITE); // Sfondo del pulsante
        okButton.setForeground(success ? new Color(32, 178, 170) : Color.RED); // Testo in base al successo
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose()); // Chiude il dialog
        panel.add(okButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
