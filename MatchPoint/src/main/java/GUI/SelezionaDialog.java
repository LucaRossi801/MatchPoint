package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelezionaDialog extends JDialog {
    private String selezione = null;
    private Object selezioneID = null;  // Utilizza Object per poter memorizzare sia String che Integer

    // Costruttore generico per Map<String, Object> (String o Integer)
    public SelezionaDialog(String titolo, Map<String, ?> itemsMap) {
        setTitle(titolo);
        setModal(true);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Crea la lista dei nomi da visualizzare
        List<String> itemList = new ArrayList<>(itemsMap.keySet());

        // Crea una JList per visualizzare i nomi
        JList<String> list = new JList<>(itemList.toArray(new String[0]));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selezione = list.getSelectedValue(); // Salva la selezione
                if (selezione != null) {
                    selezioneID = itemsMap.get(selezione); // Ottieni il valore
                }
            }
        });

        // Layout e componenti del dialogo
        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);

        // Bottone per confermare la selezione
        JButton confirmButton = new JButton("Conferma");
        confirmButton.addActionListener(e -> dispose()); // Chiude il dialogo
        add(confirmButton, BorderLayout.SOUTH);

        setSize(300, 200); // Imposta la dimensione del dialogo
        setLocationRelativeTo(null); // Centra il dialogo
    }


    // Metodo per ottenere la selezione effettuata (nome)
    public String getSelezione() {
        return selezione;
    }

    // Metodo per ottenere l'ID selezionato (String o Integer)
    public Object getSelezioneID() {
    	System.out.println(selezioneID);
        return selezioneID;
    }
}
