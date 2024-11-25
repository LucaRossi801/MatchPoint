package GUI;

import javax.print.attribute.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.AttributeSet;

import components.TipologiaCampo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class AggiungiCampoDialog extends JDialog {
	private boolean isCoperto = false;  // Variabile di istanza per gestire lo stato "Coperto"
	public AggiungiCampoDialog(Window owner, JPanel riepilogoPanel) {
		super(owner, "Aggiungi Campo", ModalityType.APPLICATION_MODAL);
		setLayout(new GridBagLayout());
		setSize(400, 300);
		setLocationRelativeTo(owner);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		int row = 0;

		// Combobox per selezione tipo campo
		JLabel tipoLabel = new JLabel("Tipologia Campo:");
		tipoLabel.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridx = 0;
		gbc.gridy = row;
		add(tipoLabel, gbc);

		JComboBox<TipologiaCampo> tipoComboBox = new JComboBox<>(TipologiaCampo.values());
		tipoComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
		gbc.gridx = 1;
		gbc.gridy = row;
		add(tipoComboBox, gbc);

		// Campi numerici da validare
		String[] campiNumerici = { "CostoOraNotturna", "CostoOraDiurna", "Lunghezza", "Larghezza" };
		Map<String, JTextField> fields = new HashMap<>();
		
		row++;

		// Campo per larghezza
		for (String campo : campiNumerici) {
			JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
			label.setFont(new Font("Montserrat", Font.BOLD, 24)); // Stile originale
			gbc.gridx = 0;
			gbc.gridy=row;
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

		// Checkbox per coperto
		JCheckBox copertoCheckBox = new JCheckBox("Coperto");
		copertoCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 2;
		add(copertoCheckBox, gbc);

		// Bottone Salva
		// Creazione del pulsante Salva con lo stile FlatButton
		JButton salvaButton = BackgroundPanel.createFlatButton(
		    "Salva",
		    e -> {
		        // Recupera i dati dalla JComboBox (TipologiaCampo)
		        String tipologia = ((TipologiaCampo) tipoComboBox.getSelectedItem()).name();
		        // Recupera il valore della JCheckBox per Coperto
		        isCoperto = copertoCheckBox.isSelected();  // Aggiorna isCoperto con il valore della checkbox
		        
		        // Costruisce il riepilogo
		        StringBuilder riepilogo = new StringBuilder("Tipologia Campo: ").append(tipologia).append("\n");

		        // Ciclo sui campi numerici attraverso la HashMap
		        for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
		            String campo = entry.getKey();  // Nome del campo (es. "CostoOraNotturna")
		            JTextField field = entry.getValue();  // JTextField corrispondente

		            String valore = field.getText();
		            if (valore.isEmpty()) {
		                JOptionPane.showMessageDialog(this, "Compila il campo " + campo + "!", "Errore", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            riepilogo.append(campo).append(": ").append(valore).append("\n");
		        }

		        // Aggiungi il valore di Coperto al riepilogo
		        riepilogo.append("Coperto: ").append(isCoperto ? "Sì" : "No").append("\n");

		        
		        // Crea un pannello per il campo con la "X"
		        JPanel campoPanel = new JPanel(new BorderLayout());
		        campoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		        JLabel campoLabel = new JLabel("<html>" + riepilogo.toString().replace("\n", "<br>") + "</html>");
		        campoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		        campoPanel.add(campoLabel, BorderLayout.CENTER);

		        // Pulsante per rimuovere il campo
		        JButton rimuoviButton = BackgroundPanel.createFlatButton(
		            "X",
		            event -> {
		                riepilogoPanel.remove(campoPanel);
		                riepilogoPanel.revalidate();
		                riepilogoPanel.repaint();
		            },
		            new Dimension(40, 40)
		        );
		        rimuoviButton.setForeground(Color.RED); // Colore rosso per la "X"
		        campoPanel.add(rimuoviButton, BorderLayout.EAST);

		        // Aggiungi il pannello al riepilogo
		        riepilogoPanel.add(campoPanel);
		        riepilogoPanel.revalidate();
		        riepilogoPanel.repaint();

		        // Chiudi il dialogo
		        dispose();
		    },
		    new Dimension(200, 50) // Dimensione personalizzata
		);



		
		salvaButton.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridy = row+1;
		gbc.anchor = GridBagConstraints.CENTER;
		add(salvaButton, gbc);


		pack();  // Adatta la finestra ai componenti
		setLocationRelativeTo(owner);  // Posiziona la finestra rispetto alla finestra principale
		setVisible(true);  // Mostra la finestra
	}

	// DocumentFilter per consentire solo numeri interi
	private static class NumericDocumentFilter extends DocumentFilter {
		@Override
		public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
				throws BadLocationException {
			if (string != null && string.matches("\\d*")) { // Accetta solo numeri
				super.insertString(fb, offset, string, attr);
			}
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr)
				throws BadLocationException {
			if (string != null && string.matches("\\d*")) { // Accetta solo numeri
				super.replace(fb, offset, length, string, attr);
			}
		}
		@Override
		public void remove(FilterBypass fb, int offset, int length) {
			try {
				super.remove(fb, offset, length);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}