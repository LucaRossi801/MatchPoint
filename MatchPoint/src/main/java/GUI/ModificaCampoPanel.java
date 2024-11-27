package GUI;
import javax.swing.*;

import components.Campo;
import components.TipologiaCampo;
import dataBase.DataBase;

import java.awt.*;
import java.net.URL;
import java.util.List;

public class ModificaCampoPanel extends JPanel {
	private Image clearImage;
    private JComboBox<String> campiComboBox;
    private  JComboBox<TipologiaCampo>  tipologiaField;
    private JTextField costoOraNotturnaField, costoOraDiurnaField, lunghezzaField, larghezzaField;
    private JCheckBox copertoCheckBox;
    private JButton salvaCampoButton;
    private Integer centroId;
    private List<Campo> campi; // Lista dei campi del centro

    public ModificaCampoPanel(Integer centroId) {
        this.centroId = centroId;

        // Carica l'immagine di sfondo
        try {
            URL clearImageUrl = getClass().getResource("/GUI/immagini/sfondohome.png");
            if (clearImageUrl != null) {
                clearImage = new ImageIcon(clearImageUrl).getImage();
            } else {
                System.err.println("Errore nel caricamento dell'immagine: /GUI/immagini/sfondohome.png");
            }
        } catch (Exception e) {
            System.err.println("Eccezione durante il caricamento dell'immagine: " + e.getMessage());
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Carica i campi del centro
        caricaCampi();

     // Pannello superiore per selezione del campo
        JPanel selezioneCampoPanel = new JPanel(new FlowLayout());
        selezioneCampoPanel.setBackground(new Color(32, 178, 170)); // Sfondo verde

        JLabel campoLabel = new OutlinedLabel("Seleziona Campo:", Color.WHITE);
        campoLabel.setFont(new Font("Montserrat", Font.BOLD, 24)); // Font come richiesto
        campoLabel.setForeground(Color.WHITE);
        selezioneCampoPanel.add(campoLabel);

        campiComboBox = new JComboBox<>();
        campiComboBox.setFont(new Font("Arial", Font.PLAIN, 18)); // Font coerente con il design
        for (Campo campo : campi) {
            // Formattazione: "TipologiaCampo (LunghezzaxLarghezza)"
            String item = String.format("%s (%dx%d)",
                                        campo.getTipologiaCampo().toString(),
                                        campo.getLunghezza(),
                                        campo.getLarghezza());
            campiComboBox.addItem(item);
        }
        campiComboBox.addActionListener(e -> aggiornaDettagliCampo());
        selezioneCampoPanel.add(campiComboBox);

        // Posiziona il pannello superiore per la selezione del campo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Occupa due colonne
        add(selezioneCampoPanel, gbc);
        gbc.gridwidth = 1; // Occupa due colonne
        // TextField per gli attributi del campo
        JLabel tipologiaLabel = new OutlinedLabel("Tipologia:", Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(tipologiaLabel, gbc);

        tipologiaField = new JComboBox<>(TipologiaCampo.values());
        tipologiaField.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 1;
        add(tipologiaField, gbc);
        costoOraNotturnaField = creaCampo("Costo Ora Notturna:", 2, gbc, Color.BLACK);
        costoOraDiurnaField = creaCampo("Costo Ora Diurna:", 3, gbc, Color.BLACK);
        lunghezzaField = creaCampo("Lunghezza:", 4, gbc, Color.BLACK);
        larghezzaField = creaCampo("Larghezza:", 5, gbc, Color.BLACK);

        // CheckBox per il campo "Coperto"
        JLabel copertoLabel = new OutlinedLabel("Coperto:", Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(copertoLabel, gbc);

        copertoCheckBox = new JCheckBox();
        gbc.gridx = 1;
        add(copertoCheckBox, gbc);

        
        JButton aggiungiCampoButton = BackgroundPanel.createFlatButton("Aggiungi campi", e-> {
        	 salvaModificheCampo();
        }, new Dimension(150, 40));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(aggiungiCampoButton, gbc);
        
        JButton eliminaCampoButton = BackgroundPanel.createFlatButton("Elimina campo selezionato", e-> {
       	 salvaModificheCampo();
       }, new Dimension(150, 40));
       gbc.gridx = 0;
       gbc.gridy = 8;
       gbc.gridwidth = 2;
       eliminaCampoButton.setBackground(Color.RED); // Sfondo al passaggio del mouse
       add(eliminaCampoButton, gbc);
       
       salvaCampoButton = BackgroundPanel.createFlatButton("Salva Modifiche", e-> {
      	 salvaModificheCampo();
      }, new Dimension(150, 50));
      gbc.gridx = 0;
      gbc.gridy = 9;
      gbc.gridwidth = 2;
      add(salvaCampoButton, gbc);
        
        
        JButton backButton = BackgroundPanel.createFlatButton("Back", e -> {
			BackgroundPanel.showPanel("modificaCentro"); // Torna al pannello di login
		}, new Dimension(150, 30) // Dimensione personalizzata del bottone
		);
		// Personalizza colore per il pulsante "Back"
		backButton.setForeground(Color.GRAY); // Sfondo grigio
		backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
		backButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo per il pulsante "Back"
		gbc.gridy = 10; // Quarta riga
		add(backButton, gbc);

		
        // Carica i dettagli del primo campo
        aggiornaDettagliCampo();
    }

    // Metodo aggiornato per creare campi con le OutlinedLabel
    private JTextField creaCampo(String labelText, int gridY, GridBagConstraints gbc, Color labelColor) {
        JLabel label = new OutlinedLabel(labelText, labelColor); // Usa OutlinedLabel con il colore specificato
        gbc.gridx = 0;
        gbc.gridy = gridY;
        add(label, gbc);

        JTextField textField = new JTextField(15);
        gbc.gridx = 1;
        add(textField, gbc);

        return textField;
    }


    private JTextField creaCampo(String label, int row, GridBagConstraints gbc) {
        JLabel campoLabel = new JLabel(label);
        gbc.gridx = 0;
        gbc.gridy = row;
        add(campoLabel, gbc);

        JTextField campoField = new JTextField(20);
        gbc.gridx = 1;
        add(campoField, gbc);

        return campoField;
    }

    private void caricaCampi() {
        // Recupera i campi associati al centro dal database
        campi = DataBase.getCampiById(centroId);
        if (campi == null || campi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nessun campo trovato per il centro selezionato.", "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aggiornaDettagliCampo() {
        int index = campiComboBox.getSelectedIndex();
        if (index >= 0) {
            Campo campo = campi.get(index);
            tipologiaField.setSelectedItem(TipologiaCampo.valueOf(campo.getTipologiaCampo()));
            costoOraNotturnaField.setText(String.valueOf(campo.getCostoOraNotturna()));
            costoOraDiurnaField.setText(String.valueOf(campo.getCostoOraDiurna()));
            lunghezzaField.setText(String.valueOf(campo.getLunghezza()));
            larghezzaField.setText(String.valueOf(campo.getLarghezza()));
            copertoCheckBox.setSelected(campo.isCoperto());
        }
    }

    private void salvaModificheCampo() {
        int index = campiComboBox.getSelectedIndex();
        if (index >= 0) {
            Campo campo = campi.get(index);

            try {
                // Aggiorna i valori del campo
                campo.setTipologiaCampo(TipologiaCampo.valueOf(tipologiaField.getSelectedItem().toString()));
                campo.setCostoOraNotturna(Integer.parseInt(costoOraNotturnaField.getText()));
                campo.setCostoOraDiurna(Integer.parseInt(costoOraDiurnaField.getText()));
                campo.setLunghezza(Integer.parseInt(lunghezzaField.getText()));
                campo.setLarghezza(Integer.parseInt(larghezzaField.getText()));
                campo.setCoperto(copertoCheckBox.isSelected());

                // Salva le modifiche nel database
                boolean success = DataBase.updateCampo(campo);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Modifiche salvate con successo.", "Successo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Errore nel salvataggio delle modifiche.", "Errore",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Errore nei dati inseriti: " + e.getMessage(), "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (clearImage != null) {
			g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
