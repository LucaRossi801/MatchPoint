package GUI;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class SelezionaDialog extends JDialog {
    private JList<String> listaElementi;
    private String selezione = null;

    public SelezionaDialog(String titolo, List<String> province, Function<String, List<String>> fetchItemsByProvince) {
        setTitle(titolo);
        setModal(true);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // ComboBox per la selezione della provincia
        JComboBox<String> comboProvince = new JComboBox<>();
        comboProvince.addItem("Seleziona Provincia");
        for (String provincia : province) {
            comboProvince.addItem(provincia);
        }

        // Lista degli elementi
        listaElementi = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaElementi);

        // Aggiorna la lista in base alla provincia selezionata
        comboProvince.addActionListener(e -> {
            String provinciaSelezionata = (String) comboProvince.getSelectedItem();
            if (!provinciaSelezionata.equals("Seleziona Provincia")) {
                List<String> items = fetchItemsByProvince.apply(provinciaSelezionata);
                if (items != null) {
                    listaElementi.setListData(items.toArray(new String[0]));
                } else {
                    listaElementi.setListData(new String[0]);
                }
            } else {
                listaElementi.setListData(new String[0]); // Svuota la lista
            }
        });

        // Pulsanti
        JPanel buttonPanel = new JPanel();
        JButton btnOk = new JButton("Seleziona");
        JButton btnCancel = new JButton("Annulla");

        btnOk.addActionListener(e -> {
            selezione = listaElementi.getSelectedValue();
            dispose();
        });

        btnCancel.addActionListener(e -> {
            selezione = null;
            dispose();
        });

        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);

        // Aggiungi componenti alla dialog
        add(comboProvince, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getSelezione() {
        return selezione;
    }
}
