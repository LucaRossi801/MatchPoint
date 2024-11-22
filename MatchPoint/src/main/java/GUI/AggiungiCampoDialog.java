package GUI;

import javax.swing.*;
import java.awt.*;

public class AggiungiCampoDialog extends JDialog {
    private boolean isCoperto = false;

    public AggiungiCampoDialog(Window owner) {
        super(owner, "Aggiungi Campo", ModalityType.APPLICATION_MODAL);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campi
        JTextField tipologiaCampoField = new JTextField(20);
        JTextField costoOraNotturnaField = new JTextField(10);
        JTextField costoOraDiurnaField = new JTextField(10);
        JTextField lunghezzaField = new JTextField(10);
        JTextField larghezzaField = new JTextField(10);

        // Etichette e campi
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Tipologia Campo:"), gbc);
        gbc.gridx = 1;
        add(tipologiaCampoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Costo Ora Notturna:"), gbc);
        gbc.gridx = 1;
        add(costoOraNotturnaField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Costo Ora Diurna:"), gbc);
        gbc.gridx = 1;
        add(costoOraDiurnaField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Lunghezza:"), gbc);
        gbc.gridx = 1;
        add(lunghezzaField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Larghezza:"), gbc);
        gbc.gridx = 1;
        add(larghezzaField, gbc);

        // Bottone "Coperto"
        JButton copertoButton = new JButton("Non coperto");
        copertoButton.addActionListener(e -> {
            isCoperto = !isCoperto;
            copertoButton.setText(isCoperto ? "Coperto" : "Non coperto");
        });
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(copertoButton, gbc);

        // Bottone "Salva"
        JButton salvaButton = new JButton("Salva");
        salvaButton.addActionListener(e -> {
            // Logica di salvataggio qui
            dispose();
        });
        gbc.gridy = 6;
        add(salvaButton, gbc);

        setSize(400, 400);
        setLocationRelativeTo(owner);
        setVisible(true);
    }
}
