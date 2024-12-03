package GUI;

import components.CentroSportivo;
import localizzazione.FileReaderUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class SelezionaDialog extends JDialog {
    private String selezione = null;
    private Object selezioneID = null;

    public SelezionaDialog(String titolo, String filePathProvince, Map<String, Map<String, Integer>> centriByProvince, Map<String, String> comuniByCentro) {
        setTitle(titolo);
        setModal(true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        // Legge province e comuni dal file CSV
        Map<String, List<String>> provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePathProvince);

        // Creazione di un JPanel per la North con margini
        JPanel northPanel = new JPanel(new BorderLayout());
        JComboBox<String> provinciaComboBox = new JComboBox<>(provinceComuni.keySet().toArray(new String[0]));
        provinciaComboBox.setFont(new Font("Montserrat", Font.PLAIN, 18));
        northPanel.add(provinciaComboBox, BorderLayout.CENTER);
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Margini personalizzati
        add(northPanel, BorderLayout.NORTH);

        // Pannello per visualizzare i centri
        JPanel centriPanel = new JPanel();
        centriPanel.setLayout(new BoxLayout(centriPanel, BoxLayout.Y_AXIS));
        
        // Creazione dello JScrollPane
        JScrollPane scrollPane = new JScrollPane(centriPanel);
        // Aggiungi margini vuoti all'interno dello JScrollPane
        scrollPane.setViewportView(centriPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Aggiungi margini vuoti al viewport
        add(scrollPane, BorderLayout.CENTER);

        // Aggiorna i centri al cambio di provincia
        provinciaComboBox.addActionListener(e -> {
            String provinciaSelezionata = (String) provinciaComboBox.getSelectedItem();
            if (provinciaSelezionata != null) {
                Map<String, Integer> centri = centriByProvince.get(provinciaSelezionata);
                aggiornaCentriPanel(centriPanel, centri, comuniByCentro);
            }
        });

        // Imposta la provincia iniziale
        if (!provinceComuni.isEmpty()) {
            provinciaComboBox.setSelectedIndex(0);
        }

        // Creazione del JPanel per la South con margini
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

    private void aggiornaCentriPanel(JPanel centriPanel, Map<String, Integer> centri, Map<String, String> comuniByCentro) {
        centriPanel.removeAll();

        if (centri != null) {
            // Configura un layout flessibile
            centriPanel.setLayout(new BoxLayout(centriPanel, BoxLayout.Y_AXIS));

            // Imposta un bordo vuoto per evitare che i contenuti tocchino i bordi dello scrollpane
            centriPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            Border border = BorderFactory.createLineBorder(Color.BLACK);

            for (Map.Entry<String, Integer> entry : centri.entrySet()) {
                String centroNome = entry.getKey();
                Integer centroID = entry.getValue();

                // Crea un pannello singolo per il centro con larghezza dinamica
                JPanel centroItemPanel = new JPanel(new BorderLayout());
                centroItemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK), // Bordo nero esterno
                    BorderFactory.createEmptyBorder(10, 10, 10, 10) // Margini interni
                ));
                centroItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Larghezza flessibile
                centroItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allineamento a sinistra

                JLabel centroLabel = new JLabel(centroNome + " (" + comuniByCentro.get(centroNome) + ")");
                centroLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));

                // Crea un FlatButton per "Seleziona"
                JButton selezionaButton = BackgroundPanel.createFlatButton("V", e -> {
                    selezione = centroNome;
                    selezioneID = centroID;
                    dispose(); // Chiude il dialogo
                }, new Dimension(30, 25)); // Dimensioni ridotte

                selezionaButton.setBackground(new Color(34, 139, 34)); // Verde
                selezionaButton.setForeground(Color.WHITE);

                // Aggiungi i componenti al pannello
                centroItemPanel.add(centroLabel, BorderLayout.CENTER);
                centroItemPanel.add(selezionaButton, BorderLayout.EAST);

                // Aggiungi il pannello al contenitore principale
                centriPanel.add(centroItemPanel);

                // Spazio tra i centri
                centriPanel.add(Box.createVerticalStrut(10));
            }
        }

        centriPanel.revalidate();
        centriPanel.repaint();
    }


    // Metodo per ottenere la selezione effettuata
    public String getSelezione() {
        return selezione;
    }

    // Metodo per ottenere l'ID selezionato
    public Object getSelezioneID() {
        return selezioneID;
    }

}
