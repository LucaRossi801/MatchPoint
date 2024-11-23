package GUI;

import components.TipologiaCampo; // Importa l'enumerazione
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AggiungiCampoDialog extends JDialog {
    private boolean isCoperto = false;

    public AggiungiCampoDialog(Window owner, JTextArea riepilogoArea) {
        super(owner, "Aggiungi Campo", ModalityType.APPLICATION_MODAL);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campi numerici da validare
        String[] campiNumerici = {"CostoOraNotturna", "CostoOraDiurna", "Lunghezza", "Larghezza"};
        Map<String, JTextField> fields = new HashMap<>();

        int row = 0;

        // Aggiungi il menu a tendina per TipologiaCampo
        JLabel tipoLabel = new OutlinedLabel("TipologiaCampo:", Color.BLACK);
        tipoLabel.setFont(new Font("Montserrat", Font.BOLD, 24)); // Stile originale
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(tipoLabel, gbc);

        JComboBox<TipologiaCampo> tipoComboBox = new JComboBox<>(TipologiaCampo.values());
        tipoComboBox.setFont(new Font("Arial", Font.PLAIN, 20)); // Font personalizzato
        gbc.gridx = 1;
        add(tipoComboBox, gbc);

        row++; // Avanza la riga

     // Aggiungi i campi di testo numerici
        for (String campo : campiNumerici) {
            JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
            label.setFont(new Font("Montserrat", Font.BOLD, 24)); // Stile originale
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            add(label, gbc);

            JTextField field = new JTextField(30); // Larghezza dei campi di testo
            field.setFont(new Font("Arial", Font.PLAIN, 20)); // Font personalizzato
            ((PlainDocument) field.getDocument()).setDocumentFilter(new NumericDocumentFilter());
            gbc.gridx = 1;
            add(field, gbc);

            fields.put(campo, field);

            row++;
        }

        // Bottone per selezionare "Coperto"
        final JButton[] copertoButtonHolder = new JButton[1]; // Array per uso in lambda
        copertoButtonHolder[0] = BackgroundPanel.createFlatButton(
                "Non coperto",
                e -> {
                    isCoperto = !isCoperto;
                    copertoButtonHolder[0].setText(isCoperto ? "Coperto" : "Non coperto");
                },
                new Dimension(200, 40) // Dimensione personalizzata
        );

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
                    // Costruisce il riepilogo
                    StringBuilder riepilogo = new StringBuilder(riepilogoArea.getText());
                    for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
                        riepilogo.append(entry.getKey()).append(": ").append(entry.getValue().getText()).append("\n");
                    }
                    riepilogo.append("Coperto: ").append(isCoperto ? "Sì" : "No").append("\n");
                    riepilogoArea.setText(riepilogo.toString());
                    dispose(); // Chiude il dialogo
                },
                new Dimension(200, 40)
        );
        salvaButton.setFont(new Font("Arial", Font.BOLD, 18));

        gbc.gridy = row + 1;
        add(salvaButton, gbc);

        // Configurazione finestra
        pack(); // Adatta dimensioni al contenuto
        setLocationRelativeTo(owner);
        setVisible(true);
    }
    // DocumentFilter per consentire solo numeri interi
    private static class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d*")) { // Accetta solo numeri
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.matches("\\d*")) { // Accetta solo numeri
                super.replace(fb, offset, length, string, attr);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }
    }
}
