package GUI;

import javax.print.attribute.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.AttributeSet;

import components.Campo;
import components.TipologiaCampo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * Classe AggiungiCampoDialog
 * <p>
 * Questa classe rappresenta un dialogo per l'aggiunta di un nuovo campo da parte dell'utente.
 * Permette di selezionare i dettagli del campo, come la tipologia, i costi e le dimensioni,
 * e di salvarli in una lista mantenuta a livello di classe.
 * </p>
 */
public class AggiungiCampoDialog extends JDialog {

    // Lista statica che mantiene tutti i campi salvati.
    private static ArrayList<Campo> campiSalvati = new ArrayList<>();

    // Variabile di istanza per gestire lo stato "Coperto".
    private boolean isCoperto = false;

    /**
     * Costruttore per creare un dialogo per l'aggiunta di un nuovo campo.
     *
     * @param owner          la finestra principale a cui è legato il dialogo.
     * @param riepilogoPanel il pannello che visualizza il riepilogo dei campi aggiunti.
     */
    public AggiungiCampoDialog(Window owner, JPanel riepilogoPanel) {
        super(owner, "Aggiungi Campo", ModalityType.APPLICATION_MODAL);

        // Configurazione della finestra principale del dialogo
        setLayout(new GridBagLayout());
        setSize(400, 300);
        setLocationRelativeTo(owner);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Sezione 1: Selezione della tipologia del campo
        JLabel tipoLabel = new JLabel("Tipologia Campo:");
        tipoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = row;
        add(tipoLabel, gbc);

        JComboBox<TipologiaCampo> tipoComboBox = new JComboBox<>(TipologiaCampo.values());
        tipoComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        add(tipoComboBox, gbc);

        row++;

        // Sezione 2: Campi numerici da validare
        String[] campiNumerici = {"CostoOraNotturna", "CostoOraDiurna", "Lunghezza", "Larghezza"};
        Map<String, JTextField> fields = new HashMap<>();

        for (String campo : campiNumerici) {
            JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
            label.setFont(new Font("Montserrat", Font.BOLD, 18));
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;
            add(label, gbc);

            JTextField field = new JTextField(30);
            field.setFont(new Font("Arial", Font.PLAIN, 18));

            // Applica un filtro per accettare solo numeri.
            ((PlainDocument) field.getDocument()).setDocumentFilter(new NumericDocumentFilter());

            gbc.gridx = 1;
            add(field, gbc);

            fields.put(campo, field);
            row++;
        }

        // Sezione 3: Switch per lo stato "Coperto"
        JPanel copertoPanel = new JPanel(new GridBagLayout());
        copertoPanel.setBackground(getBackground());

        JLabel copertoLabel = new JLabel("Coperto:");
        copertoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        copertoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        GridBagConstraints copertoGbc = new GridBagConstraints();
        copertoGbc.insets = new Insets(0, 10, 0, 10);
        copertoGbc.anchor = GridBagConstraints.WEST;
        copertoGbc.gridx = 0;
        copertoGbc.gridy = 0;
        copertoPanel.add(copertoLabel, copertoGbc);

        JToggleButton switchButton = createSwitchButton();
        switchButton.setBackground(getBackground()); // Sfondo uguale alla finestra
        switchButton.setContentAreaFilled(false); // Disabilita l'area predefinita del contenuto
		switchButton.setOpaque(false); // Assicurati che lo sfondo non sia dipinto
        switchButton.addActionListener(e -> isCoperto = switchButton.isSelected());

        copertoGbc.gridx = 1;
        copertoPanel.add(switchButton, copertoGbc);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        add(copertoPanel, gbc);

        row++;

        // Sezione 4: Bottone "Salva"
        JButton salvaButton = BackgroundPanel.createFlatButton("Salva", e -> {
            // Validazione e salvataggio dei dati inseriti
            salvaCampo(tipoComboBox, fields, switchButton, riepilogoPanel);
        }, new Dimension(200, 50));

        salvaButton.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        add(salvaButton, gbc);

        row++;

        pack();
        setLocationRelativeTo(owner);
        setVisible(true);
    }

    /**
     * Crea un pulsante di tipo switch per lo stato "Coperto".
     *
     * @return un pulsante JToggleButton con icone personalizzate.
     */
    private JToggleButton createSwitchButton() {
        JToggleButton switchButton = new JToggleButton();
        switchButton.setPreferredSize(new Dimension(50, 25));
        switchButton.setFocusPainted(false);
        switchButton.setBorder(BorderFactory.createEmptyBorder());

        Icon offIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.fillRoundRect(x, y, getIconWidth(), getIconHeight(), 25, 25);
                g2d.setColor(Color.WHITE);
                g2d.fillOval(x + 2, y + 2, 21, 21);
            }

            @Override
            public int getIconWidth() {
                return 50;
            }

            @Override
            public int getIconHeight() {
                return 25;
            }
        };

        Icon onIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(50, 200, 50));
                g2d.fillRoundRect(x, y, getIconWidth(), getIconHeight(), 25, 25);
                g2d.setColor(Color.WHITE);
                g2d.fillOval(x + 26, y + 2, 21, 21);
            }

            @Override
            public int getIconWidth() {
                return 50;
            }

            @Override
            public int getIconHeight() {
                return 25;
            }
        };

        switchButton.setIcon(offIcon);
        switchButton.setSelectedIcon(onIcon);
        return switchButton;
    }

    /**
     * Salva i dettagli del nuovo campo nella lista principale e aggiorna il riepilogo.
     *
     * @param tipoComboBox    la JComboBox per selezionare la tipologia del campo.
     * @param fields          i campi di input numerici.
     * @param switchButton    il pulsante di stato "Coperto".
     * @param riepilogoPanel  il pannello per visualizzare i campi aggiunti.
     */
    private void salvaCampo(JComboBox<TipologiaCampo> tipoComboBox, Map<String, JTextField> fields,
                            JToggleButton switchButton, JPanel riepilogoPanel) {
        String tipologia = ((TipologiaCampo) tipoComboBox.getSelectedItem()).name();
        StringBuilder riepilogo = new StringBuilder("Tipologia Campo: ").append(tipologia).append("\n");

        for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
            String campo = entry.getKey();
            JTextField field = entry.getValue();
            String valore = field.getText();

            if (valore.isEmpty()) {
                CustomMessage.show("Compila il campo " + campo + "!", "Errore", false);
                return;
            }
            riepilogo.append(campo).append(": ").append(valore).append("\n");
        }

        TipologiaCampo tipologiaCampo = (TipologiaCampo) tipoComboBox.getSelectedItem();
        int costoOraDiurna = Integer.parseInt(fields.get("CostoOraDiurna").getText());
        int costoOraNotturna = Integer.parseInt(fields.get("CostoOraNotturna").getText());
        int lunghezza = Integer.parseInt(fields.get("Lunghezza").getText());
        int larghezza = Integer.parseInt(fields.get("Larghezza").getText());
        isCoperto = switchButton.isSelected();

        Campo nuovoCampo = new Campo(tipologiaCampo, costoOraDiurna, costoOraNotturna, lunghezza, larghezza, isCoperto);
        campiSalvati.add(nuovoCampo);

        riepilogo.append("Coperto: ").append(isCoperto ? "Sì" : "No").append("\n");

        JPanel campoPanel = new JPanel(new BorderLayout());
        campoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel campoLabel = new JLabel("<html>" + riepilogo.toString().replace("\n", "<br>") + "</html>");
        campoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        campoPanel.add(campoLabel, BorderLayout.CENTER);

        JButton rimuoviButton = BackgroundPanel.createFlatButton("X", event -> {
            campiSalvati.remove(nuovoCampo);
            riepilogoPanel.remove(campoPanel);
            riepilogoPanel.revalidate();
            riepilogoPanel.repaint();
        }, new Dimension(40, 40));
        campoPanel.add(rimuoviButton, BorderLayout.EAST);

        riepilogoPanel.add(campoPanel);
        riepilogoPanel.revalidate();
        riepilogoPanel.repaint();

        dispose();
    }

    /**
     * Filtro per consentire solo l'inserimento di numeri interi nei campi di input.
     */
    private static class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("\\d*")) {
                super.replace(fb, offset, length, string, attr);
            }
        }
    }

    /**
     * Restituisce la lista di campi salvati.
     *
     * @return la lista di campi.
     */
    public static ArrayList<Campo> getCampi() {
        return campiSalvati;
    }
}
