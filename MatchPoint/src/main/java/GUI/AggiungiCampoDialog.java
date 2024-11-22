package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AggiungiCampoDialog extends JDialog {
    private boolean isCoperto = false;

    public AggiungiCampoDialog(Window owner) {
        super(owner, "Aggiungi Campo", ModalityType.APPLICATION_MODAL);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campi
        String[] campi = {"TipologiaCampo", "CostoOraNotturna", "CostoOraDiurna", "Lunghezza", "Larghezza"};
        Map<String, JTextField> fields = new HashMap<>();

        int row = 0;

        for (String campo : campi) {
            JLabel label = new JLabel(campo + ":");
            label.setFont(new Font("Montserrat", Font.BOLD, 24));
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            add(label, gbc);

            JTextField field = new JTextField(20);
            field.setFont(new Font("Arial", Font.PLAIN, 18));
            gbc.gridx = 1;
            add(field, gbc);

            fields.put(campo, field);

            row++;
        }

        // Bottone per selezionare "Coperto"
     // Dichiarazione iniziale
        final JButton[] copertoButtonHolder = new JButton[1]; // Array per consentire la modifica in lambda

        // Creazione del pulsante
        copertoButtonHolder[0] = BackgroundPanel.createFlatButton(
            "Non coperto", // Testo iniziale
            e -> { 
                isCoperto = !isCoperto; // Cambia lo stato di "Coperto"
                copertoButtonHolder[0].setText(isCoperto ? "Coperto" : "Non coperto"); // Aggiorna il testo del pulsante
            },
            new Dimension(200, 40) // Dimensione del pulsante
        );

        // Riferimento finale al pulsante
        JButton copertoButton = copertoButtonHolder[0];
        copertoButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font specifico




        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(copertoButton, gbc);

        // Bottone Salva
        JButton salvaButton = BackgroundPanel.createFlatButton(
            "Salva",
            e -> {
                // Aggiungere il codice per salvare i dati inseriti
                dispose();
            },
            new Dimension(200, 40)
        );
        salvaButton.setFont(new Font("Arial", Font.BOLD, 18));
        salvaButton.setBackground(new Color(32, 178, 170));
        salvaButton.setForeground(new Color(220, 250, 245));

        gbc.gridy = row + 1;
        add(salvaButton, gbc);

        setSize(500, 500);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
