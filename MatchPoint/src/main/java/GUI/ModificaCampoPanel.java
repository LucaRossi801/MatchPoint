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
    private JToggleButton switchButton = new JToggleButton();
    private JButton salvaCampoButton;
    private Integer centroId;
    private static List<Campo> campi; // Lista dei campi del centro
    private boolean isCoperto = false; // Variabile di istanza per gestire lo stato "Coperto"


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

        // Pannello per Coperto
        JPanel copertoPanel = new JPanel(new GridBagLayout());
        copertoPanel.setOpaque(false); // Rendi trasparente il pannello

        // Etichetta "Coperto:"
        JLabel copertoLabel = new JLabel("Coperto:");
        copertoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        copertoLabel.setHorizontalAlignment(SwingConstants.LEFT); // Allineamento testo
        copertoLabel.setForeground(Color.BLACK); // Testo nero per leggibilità

        // Configura GridBagConstraints per il pannello Coperto
        GridBagConstraints copertoGbc = new GridBagConstraints();
        copertoGbc.insets = new Insets(0, 10, 0, 10); // Margini uniformi
        copertoGbc.anchor = GridBagConstraints.WEST; // Allineamento sinistra
        copertoGbc.gridx = 0; // Colonna 0
        copertoGbc.gridy = 0;
        copertoPanel.add(copertoLabel, copertoGbc); // Aggiungi etichetta

        // Switch per Coperto
        switchButton.setPreferredSize(new Dimension(50, 25));
        switchButton.setFocusPainted(false);
        switchButton.setBorder(BorderFactory.createEmptyBorder()); // Rimuovi bordi
        switchButton.setBackground(null); // Rimuovi lo sfondo
        switchButton.setContentAreaFilled(false); // Disabilita l'area di contenuto
        switchButton.setOpaque(false); // Rendi trasparente il bottone

        // Aggiungi il pulsante al pannello con le stesse impostazioni
        copertoGbc.gridx = 1; // Colonna 1 per lo switch
        copertoPanel.add(switchButton, copertoGbc);

     		
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
    		switchButton.addActionListener(e -> isCoperto = switchButton.isSelected());

    		// Aggiungi lo switch al pannello
    		copertoGbc.gridx = 1; // Colonna 1
    		copertoPanel.add(switchButton, copertoGbc);

    		// Aggiungi il pannello Coperto alla finestra principale
    		gbc.gridx = 0;
    		gbc.gridy = 6;
    		gbc.gridwidth = 2; // Il pannello occupa due colonne
    		add(copertoPanel, gbc);
 
        
        JButton eliminaCampoButton = BackgroundPanel.createFlatButton("Elimina campo selezionato", e-> {
        	eliminaCampo();
       	 
       }, new Dimension(150, 40));
       gbc.gridx = 0;
       gbc.gridy = 7;
       gbc.gridwidth = 2;
       eliminaCampoButton.setBackground(Color.RED); // Sfondo al passaggio del mouse
       add(eliminaCampoButton, gbc);
       
       salvaCampoButton = BackgroundPanel.createFlatButton("Salva Modifiche", e-> {
      	 salvaModificheCampo();
      }, new Dimension(150, 50));
      gbc.gridx = 0;
      gbc.gridy = 8;
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
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
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
        
        // Controllo se la lista dei campi è vuota
        if (campi == null || campi.isEmpty()) {
            CustomMessage.show("Nessun campo trovato per il centro selezionato.", "Informazione", false);
            BackgroundPanel.showPanel("modificaCentro");
            return; // Interrompi il caricamento se non ci sono campi
        }
    }

    private void aggiornaComboBox() {
        campiComboBox.removeAllItems(); // Svuota la ComboBox
        
        // Aggiungi i campi alla ComboBox
        for (Campo campo : campi) {
            String item = String.format("%s (%dx%d)",
                                        campo.getTipologiaCampo().toString(),
                                        campo.getLunghezza(),
                                        campo.getLarghezza());
            campiComboBox.addItem(item);
        }

        // Controllo se la lista dei campi è vuota
        if (campi.isEmpty()) {
            //CustomMessage.show("Nessun campo disponibile. Torna alla schermata precedente.", "Informazione", false);
            BackgroundPanel.showPanel("modificaCentro");
        } else {
            // Aggiorna i dettagli del primo campo
            aggiornaDettagliCampo();
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
            
            // Aggiorna lo stato dello switch
            isCoperto = campo.isCoperto();
            switchButton.setSelected(isCoperto); // Imposta lo stato dello switch
        }
    }

    
    private void eliminaCampo() {
        int index = campiComboBox.getSelectedIndex();
        if (index >= 0) {
            Campo campo = campi.get(index);
            if (DataBase.eliminaCampo(centroId, campo.getId())) {
                CustomMessage.show("Campo Eliminato Correttamente!", "Successo", true);

                // Aggiorna la lista dei campi
                caricaCampi();

                // Aggiorna la JComboBox
                aggiornaComboBox();
            } else {
                CustomMessage.show("Errore durante l'eliminazione", "Errore", false);
            }
        } else {
            CustomMessage.show("Nessun campo selezionato per l'eliminazione.", "Errore", false);
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
                campo.setCoperto(isCoperto); // Passa il valore dello slider

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
