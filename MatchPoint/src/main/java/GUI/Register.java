package GUI;

import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.sql.*;

import org.jdatepicker.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import dataBase.DataBase;
import individui.Gestore;
import individui.Giocatore;

public class Register {

	// Creazione della registrazione per i gestori
	protected static JPanel createRegisterPanel(String tipologia) {
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db"; //connessione al database
	    JPanel panel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (BackgroundPanel.clearImage != null) {
	                g.drawImage(BackgroundPanel.clearImage, 0, 0, getWidth(), getHeight(), this);
	            }
	        }
	    };
	    panel.setLayout(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 10, 10); // Margini tra i componenti
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    JLabel titleLabel = new OutlinedLabel("Registrazione " + tipologia, Color.WHITE);
	    titleLabel.setFont(new Font("Montserrat", Font.BOLD, 30));

	    // Aggiungi il titolo in alto al centro
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2; // Il titolo occupa due colonne
	    gbc.anchor = GridBagConstraints.CENTER;
	    panel.add(titleLabel, gbc);

	    // Struttura dati per mappare i campi di input
	    Map<String, JComponent> fields = new HashMap<>();

	    String[] campi;
	    if (tipologia.equals("Gestore")) {
	        campi = new String[]{"Nome", "Cognome", "DataNascita", "Email", "Username", "Password", "Certificazione", "Competenze"};
	    } else {
	        campi = new String[]{"Nome", "Cognome", "DataNascita", "Email", "Username", "Password", "NomeSquadra"};
	    }

	    gbc.gridwidth = 1; // Ogni etichetta e campo occupano una colonna
	    gbc.anchor = GridBagConstraints.WEST; // Allineamento a sinistra
	    int row = 1; // La riga parte da 1 (sotto il titolo)

	    for (String campo : campi) {
	        // Crea l'etichetta
	        JLabel label = new JLabel(campo + ":");
	        label.setFont(new Font("Montserrat", Font.BOLD, 24));
	        gbc.gridx = 0; // Colonna 0 (sinistra)
	        gbc.gridy = row;
	        panel.add(label, gbc);

	        // Crea il campo di input
	        JComponent inputField = null;

	        if (campo.equals("Password")) {
	            inputField = new JPasswordField(20);
	        } else if (campo.equals("DataNascita")) {
	        	UtilDateModel model = new UtilDateModel();
	            Properties properties = new Properties();
	            properties.put("text.today", "Giorno");
	            properties.put("text.month", "Mese");
	            properties.put("text.year", "Anno");
	            // Crea il pannello del selettore di data
	            JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
	            // Crea il date picker con il formatter personalizzato
	            JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	            datePicker.getJFormattedTextField().setFont(new Font("Arial", Font.PLAIN, 18));

	            inputField = datePicker;
	        } else {
	            inputField = new JTextField(20);
	        }

	        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
	        gbc.gridx = 1; // Colonna 1 (destra)
	        panel.add(inputField, gbc);

	        // Salva il campo nella mappa con il suo nome
	        fields.put(campo, inputField);

	        row++; // Passa alla riga successiva
	    }

	    // Aggiungi il pulsante di registrazione
	    JButton registerButton = new JButton("Register");
	    registerButton.setFont(new Font("Arial", Font.BOLD, 18));
	    registerButton.setBackground(new Color(32, 178, 170));
	    registerButton.setForeground(Color.WHITE);
	    registerButton.setFocusPainted(false);
	    gbc.gridx = 0;
	    gbc.gridy = row;
	    gbc.gridwidth = 2; // Il pulsante occupa due colonne
	    gbc.anchor = GridBagConstraints.CENTER;
	    panel.add(registerButton, gbc);
	    gbc.gridx = 0;
	    gbc.gridy = row+1; //Riga successiva
	    gbc.gridwidth = 2; //Anche il pulsante "Back" occupa entrambe le colonne
	    gbc.anchor = GridBagConstraints.CENTER;
	    panel.add(createBackButton(fields), gbc);
	    // Aggiungi un ActionListener al pulsante
	    registerButton.addActionListener(e -> {
	        try {
	            // Leggi i valori dai campi
	            String name = ((JTextField) fields.get("Nome")).getText().trim();
	            String surname = ((JTextField) fields.get("Cognome")).getText().trim();
	            String birthDate=null;
	            JDatePickerImpl datePicker = (JDatePickerImpl) fields.get("DataNascita");
	            Object selectedDate = datePicker.getModel().getValue();

	            if (selectedDate != null) {
	                // Seleziona la data dal modello e convertila in un formato che ti interessa
	                java.util.Date date = (java.util.Date) selectedDate;
	                // Formatta la data come stringa nel formato yyyy-MM-dd
	                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                birthDate = sdf.format(date);  //la data formattata
	            } else {
	                JOptionPane.showMessageDialog(panel, "Errore: Devi selezionare una data di nascita!", "Errore", JOptionPane.WARNING_MESSAGE);
	                return;
	            }
	            String email = ((JTextField) fields.get("Email")).getText().trim();
	            String username = ((JTextField) fields.get("Username")).getText().trim();
	            String password = new String(((JPasswordField) fields.get("Password")).getPassword()).trim();
	            String certifications = tipologia.equals("Gestore") ? ((JTextField) fields.get("Certificazione")).getText().trim() : null;
	            String competences = tipologia.equals("Gestore") ? ((JTextField) fields.get("Competenze")).getText().trim() : null;

	            // Controlla se ci sono campi vuoti
	            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() ||
	                    (tipologia.equals("Gestore") && (certifications.isEmpty() || competences.isEmpty()))) {
	                JOptionPane.showMessageDialog(panel, "Errore: Tutti i campi devono essere compilati!", "Errore", JOptionPane.WARNING_MESSAGE);
	                return;
	            }
	            // Controlla se lo username è già presente nel database
	            String sql = "SELECT Password FROM Gestore WHERE Username ='" + username +
	                         "' UNION SELECT Password FROM Giocatore WHERE Username ='" + username + "'";
	            String ris = "";
	            try (Connection conn = DriverManager.getConnection(url)) {
	                ris = DataBase.eseguiSelect(conn, sql); // Esegui la query
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	                JOptionPane.showMessageDialog(panel, "Errore di connessione al database: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            if (!ris.isEmpty()) {
	                // Lo username esiste già
	                JOptionPane.showMessageDialog(panel, "Errore: Lo username '" + username + "' è già in uso!", "Errore", JOptionPane.WARNING_MESSAGE);
	                return;
	            }
	            // Chiama il metodo di registrazione per i Gestori o Giocatori
	            if (tipologia.equals("Gestore")) {
	                Gestore.registrazione(name, surname, birthDate, email, username, password, certifications, competences);
	                System.out.println("Registrazione completata per il Gestore!");
	            } else {
	                String teamName = ((JTextField) fields.get("NomeSquadra")).getText().trim();
	                if (teamName.isEmpty()) {
	                    JOptionPane.showMessageDialog(panel, "Errore: Tutti i campi devono essere compilati!", "Errore", JOptionPane.WARNING_MESSAGE);
	                    return;
	                }
	                Giocatore.registrazione(name, surname, birthDate, email, username, password, teamName);
	                System.out.println("Registrazione completata per il Giocatore!");
	            }

	            // Messaggio di successo
	            CustomMessage.show("Registrazione effettuata con successo!", "Successo", true);
	            BackgroundPanel.showPanel("login");
	        } catch (Exception ex) {
	            // Messaggio di errore generico
	        	CustomMessage.show("Errore durante la registrazione", "Errore", false);
	        }
	    });
	    
	    return panel;
	    
	    
	}

	// Creazione del bottone "Back" con colore grigio e dimensioni personalizzate
	protected static JButton createBackButton(Map<String, JComponent> fields) {
	    JButton backButton = new JButton("Back");
	    backButton.setFont(new Font("Arial", Font.BOLD, 18));
	    backButton.setBackground(Color.GRAY);
	    backButton.setForeground(Color.WHITE);
	    backButton.setFocusPainted(false);
	    backButton.setPreferredSize(new Dimension(120, 30));

	    backButton.addActionListener(e -> {
	        // Resetta tutti i campi
	        fields.forEach((key, field) -> {
	            if (field instanceof JTextField) {
	                ((JTextField) field).setText("");
	            } else if (field instanceof JDatePickerImpl) {
	                ((JDatePickerImpl) field).getModel().setValue(null);
	            }
	        });

	        // Cambia pannello
	        BackgroundPanel.showPanel("main");
	    });

	    return backButton;
	}

	
	public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
	    private String datePattern = "dd-MM-yyyy";  // Cambia il formato a dd-MM-yyyy
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parse(text);  // Converte la stringa in un oggetto Date
	    }

	    @Override
	    public String valueToString(Object value) {
	        if (value != null) {
	            if (value instanceof GregorianCalendar) {
	                GregorianCalendar calendar = (GregorianCalendar) value;
	                return dateFormatter.format(calendar.getTime());  // Usa il formato dd-MM-yyyy
	            }
	        }
	        return "Seleziona da calendario";
	    }

    }

	
}
