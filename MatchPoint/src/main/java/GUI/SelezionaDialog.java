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

    public SelezionaDialog(String titolo, String filePathProvince, Map<String, Map<String, Integer>> centriByProvince) {
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
                aggiornaCentriPanel(centriPanel, centri);
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

    // Metodo per aggiornare il pannello con i centri
    private void aggiornaCentriPanel(JPanel centriPanel, Map<String, Integer> centri) {
        centriPanel.removeAll();

        if (centri != null) {
            // Controlla se c'è solo un centro per determinare la dimensione
            int centerCount = centri.size();
            int panelHeight = centerCount == 1 ? 50 : 200; // Se c'è un solo centro, il pannello sarà più stretto

            // Imposta la dimensione fissa per il pannello dei centri
            centriPanel.setPreferredSize(new Dimension(centriPanel.getWidth(), panelHeight));

            // Crea un bordo per i centri
            Border border = BorderFactory.createLineBorder(Color.BLACK);

            for (Map.Entry<String, Integer> entry : centri.entrySet()) {
                String centroNome = entry.getKey();
                Integer centroID = entry.getValue();

                JPanel centroItemPanel = new JPanel(new BorderLayout());
                centroItemPanel.setBorder(border);  // Aggiungi il bordo nero
                centroItemPanel.setPreferredSize(new Dimension(centriPanel.getWidth(), 50));  // Distanziamento tra i centri

                JLabel centroLabel = new JLabel(centroNome);
                centroLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));

                // Creiamo un FlatButton per "Seleziona", con una larghezza più stretta e la scritta "V"
                JButton selezionaButton = BackgroundPanel.createFlatButton("V", e -> {
                    selezione = centroNome;
                    selezioneID = centroID;
                    dispose(); // Chiude il dialogo
                }, new Dimension(50, 30)); // Modifica la dimensione per fare il pulsante più stretto

                // Imposta il colore di sfondo del pulsante "Seleziona" su verde
                selezionaButton.setBackground(new Color(34, 139, 34)); // Verde
                selezionaButton.setForeground(Color.WHITE); // Colore del testo bianco

                centroItemPanel.add(centroLabel, BorderLayout.CENTER);
                centroItemPanel.add(selezionaButton, BorderLayout.EAST);
                centriPanel.add(centroItemPanel);
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
