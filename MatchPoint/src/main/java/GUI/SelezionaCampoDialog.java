package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Map;

public class SelezionaCampoDialog extends JDialog {
    private String selezione = null;
    private Integer selezioneID = null;

    public SelezionaCampoDialog(String titolo, Map<String, Integer> campi) {
        setTitle(titolo);
        setModal(true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Pannello per visualizzare i campi
        JPanel campiPanel = new JPanel();
        campiPanel.setLayout(new BoxLayout(campiPanel, BoxLayout.Y_AXIS));
        
        // Creazione dello JScrollPane per la lista dei campi
        JScrollPane scrollPane = new JScrollPane(campiPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margini vuoti al viewport
        add(scrollPane, BorderLayout.CENTER);

        // Aggiungi ogni campo con il pulsante "Seleziona"
        for (Map.Entry<String, Integer> entry : campi.entrySet()) {
            String campoNome = entry.getKey();
            Integer campoID = entry.getValue();

            // Pannello per ogni campo
            JPanel campoItemPanel = new JPanel(new BorderLayout());
            campoItemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK), // Bordo nero esterno
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // Margini interni
            ));
            campoItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Larghezza dinamica
            campoItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allineamento a sinistra

            // Etichetta del campo
            JLabel campoLabel = new JLabel(campoNome);
            campoLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));

            // Pulsante "Seleziona" con la lettera "V"
            JButton selezionaButton = BackgroundPanel.createFlatButton("V", e -> {
                selezione = campoNome;
                selezioneID = campoID;
                dispose(); // Chiude il dialogo
            }, new Dimension(30, 25)); // Dimensioni ridotte per il pulsante

            selezionaButton.setBackground(new Color(34, 139, 34)); // Verde
            selezionaButton.setForeground(Color.WHITE); // Colore del testo bianco
            selezionaButton.setFont(new Font("Montserrat", Font.PLAIN, 14));

            // Aggiungi i componenti al pannello
            campoItemPanel.add(campoLabel, BorderLayout.CENTER);
            campoItemPanel.add(selezionaButton, BorderLayout.EAST);

            // Aggiungi il pannello al contenitore principale
            campiPanel.add(campoItemPanel);

            // Spazio tra i campi
            campiPanel.add(Box.createVerticalStrut(10));
        }

        // Creazione del pannello per la South con il pulsante "Chiudi"
        JPanel southPanel = new JPanel(new BorderLayout());
        JButton chiudiButton = BackgroundPanel.createFlatButton("Chiudi", e -> dispose(), new Dimension(100, 40));
        chiudiButton.setFont(new Font("Montserrat", Font.PLAIN, 18));
        chiudiButton.setForeground(Color.GRAY); // Sfondo grigio
        chiudiButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
        southPanel.add(chiudiButton, BorderLayout.CENTER);
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Margini personalizzati
        add(southPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Centra il dialogo
    }

    // Metodo per ottenere la selezione effettuata
    public String getSelezione() {
        return selezione;
    }

    // Metodo per ottenere l'ID selezionato
    public Integer getSelezioneID() {
        return selezioneID;
    }
}
