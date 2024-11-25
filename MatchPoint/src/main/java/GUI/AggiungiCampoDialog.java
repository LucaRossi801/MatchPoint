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
		tipoLabel.setFont(new Font("Arial", Font.BOLD, 18));
		gbc.gridx = 0;
		gbc.gridy = row;
		add(tipoLabel, gbc);

		JComboBox<TipologiaCampo> tipoComboBox = new JComboBox<>(TipologiaCampo.values());
		tipoComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 1;
		gbc.gridy = row;
		add(tipoComboBox, gbc);

		// Campi numerici da validare
		String[] campiNumerici = { "CostoOraNotturna", "CostoOraDiurna", "Lunghezza", "Larghezza" };
		Map<String, JTextField> fields = new HashMap<>();
		
		row++;

		// Campi
		for (String campo : campiNumerici) {
			JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
			label.setFont(new Font("Montserrat", Font.BOLD, 18)); // Stile originale
			gbc.gridx = 0;
			gbc.gridy=row;
			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			add(label, gbc);

			JTextField field = new JTextField(30); // Larghezza dei campi di testo
			field.setFont(new Font("Arial", Font.PLAIN, 18)); // Font personalizzato
			((PlainDocument) field.getDocument()).setDocumentFilter(new NumericDocumentFilter());
			gbc.gridx = 1;
			add(field, gbc);

			fields.put(campo, field);
			
			row++;

		}

		// Switch per Coperto
		// Crea un pannello orizzontale per la scritta e lo switch
		JPanel copertoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 55, 0)); // Layout orizzontale con spazio tra i componenti
		copertoPanel.setBackground(Color.WHITE); // Sfondo bianco come richiesto

		// Etichetta "Coperto:"
		JLabel copertoLabel = new OutlinedLabel("Coperto:", Color.BLACK);
		copertoLabel.setFont(new Font("Arial", Font.BOLD, 18));
		copertoPanel.add(copertoLabel); // Aggiungi la scritta al pannello

		// Switch per Coperto
		JToggleButton switchButton = new JToggleButton();
		switchButton.setPreferredSize(new Dimension(50, 25));
		switchButton.setFocusPainted(false);
		switchButton.setBorder(BorderFactory.createEmptyBorder());
		switchButton.setBackground(Color.WHITE); // Sfondo bianco
		switchButton.setOpaque(true);

		// Creazione delle icone per gli stati
		Icon offIcon = new Icon() {
		    @Override
		    public void paintIcon(Component c, Graphics g, int x, int y) {
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setColor(Color.LIGHT_GRAY); // Sfondo spento
		        g2d.fillRoundRect(x, y, getIconWidth(), getIconHeight(), 25, 25);

		        g2d.setColor(Color.WHITE); // Pallino
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
		        g2d.setColor(new Color(50, 200, 50)); // Sfondo acceso
		        g2d.fillRoundRect(x, y, getIconWidth(), getIconHeight(), 25, 25);

		        g2d.setColor(Color.WHITE); // Pallino
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

		// Assegna le icone agli stati
		switchButton.setIcon(offIcon); // Stato non selezionato
		switchButton.setSelectedIcon(onIcon); // Stato selezionato

		// Aggiorna il valore di isCoperto in base allo stato
		switchButton.addActionListener(e -> {
		    isCoperto = switchButton.isSelected();
		});

		// Aggiungi lo switch al pannello
		copertoPanel.add(switchButton);

		// Aggiungi il pannello alla GUI
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 2; // Il pannello occupa due colonne
		add(copertoPanel, gbc);



		// Bottone Salva
		// Creazione del pulsante Salva con lo stile FlatButton
		JButton salvaButton = BackgroundPanel.createFlatButton(
		    "Salva",
		    e -> {
		        // Recupera i dati dalla JComboBox (TipologiaCampo)
		        String tipologia = ((TipologiaCampo) tipoComboBox.getSelectedItem()).name();
		        // Recupera il valore della JCheckBox per Coperto
		        isCoperto = switchButton.isSelected(); // Aggiorna isCoperto con il valore del pulsante toggle
		        
		        // Costruisce il riepilogo
		        StringBuilder riepilogo = new StringBuilder("Tipologia Campo: ").append(tipologia).append("\n");

		        // Ciclo sui campi numerici attraverso la HashMap
		        for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
		            String campo = entry.getKey();  // Nome del campo (es. "CostoOraNotturna")
		            JTextField field = entry.getValue();  // JTextField corrispondente

		            String valore = field.getText();
		            if (valore.isEmpty()) {
		                CustomMessage.show("Compila il campo " + campo + "!", "Errore", false);
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


		row++;
		
		salvaButton.setFont(new Font("Arial", Font.BOLD, 16));
		gbc.gridy = row;
		gbc.anchor = GridBagConstraints.CENTER;
		add(salvaButton, gbc);
		
		row++;

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