package GUI;

import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.sql.*;

import org.jdatepicker.*;
import GUI.BackgroundPanel.DateLabelFormatter;
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
	        JComponent inputField;

	        if (campo.equals("Password")) {
	            inputField = new JPasswordField(20);
	        } else if (campo.equals("DataNascita")) {
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            DateFormatter dateFormatter = new DateFormatter(dateFormat);
	            inputField = new JFormattedTextField(dateFormatter);
	            ((JFormattedTextField) inputField).setColumns(20);
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
	    panel.add(BackgroundPanel.createBackButton(), gbc);
	    // Aggiungi un ActionListener al pulsante
	    registerButton.addActionListener(e -> {
	        try {
	            // Leggi i valori dai campi
	            String name = ((JTextField) fields.get("Nome")).getText().trim();
	            String surname = ((JTextField) fields.get("Cognome")).getText().trim();
	            String birthDate = ((JFormattedTextField) fields.get("DataNascita")).getText().trim();
	            String email = ((JTextField) fields.get("Email")).getText().trim();
	            String username = ((JTextField) fields.get("Username")).getText().trim();
	            String password = new String(((JPasswordField) fields.get("Password")).getPassword()).trim();
	            String certifications = tipologia.equals("Gestore") ? ((JTextField) fields.get("Certificazione")).getText().trim() : null;
	            String competences = tipologia.equals("Gestore") ? ((JTextField) fields.get("Competenze")).getText().trim() : null;

	            // Controlla se ci sono campi vuoti
	            if (name.isEmpty() || surname.isEmpty() || birthDate.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() ||
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
	                Gestore.registrazione(name, surname, Date.valueOf(birthDate), email, username, password, certifications, competences);
	                System.out.println("Registrazione completata per il Gestore!");
	            } else {
	                String teamName = ((JTextField) fields.get("NomeSquadra")).getText().trim();
	                if (teamName.isEmpty()) {
	                    JOptionPane.showMessageDialog(panel, "Errore: Tutti i campi devono essere compilati!", "Errore", JOptionPane.WARNING_MESSAGE);
	                    return;
	                }
	                Giocatore.registrazione(name, surname, Date.valueOf(birthDate), email, username, password, teamName);
	                System.out.println("Registrazione completata per il Giocatore!");
	            }

	            // Messaggio di successo
	            JOptionPane.showMessageDialog(panel, "Registrazione completata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
	        } catch (Exception ex) {
	            // Messaggio di errore generico
	            JOptionPane.showMessageDialog(panel, "Errore durante la registrazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
	        }
	    });
	    
	    return panel;
	}



	/*private JPanel createPlayerRegisterPanel() {

		return createRegisterPanel("Registrazione Giocatore",
				new String[][] { { "Nome:", "name" }, { "Cognome:", "surname" }, { "DataNascita:", "dob" },
						{ "Email:", "email" }, { "Username:", "username" }, { "Password:", "password" },
						{ "Nome Squadra:", "teamName" } },
				e -> {
					// Esegui la registrazione del giocatore
					System.out.println("Registrazione giocatore eseguita!");
				});

	}

	private JPanel createManagerRegisterPanel() {
		return createRegisterPanel("Registrazione Gestore",
				new String[][] { { "Nome:", "name" }, { "Cognome:", "surname" }, { "DataNascita:", "dob" },
						{ "Email:", "email" }, { "Username:", "username" }, { "Password:", "password" },
						{ "Certificazioni:", "certifications" }, { "Competenze:", "competences" } },
				e -> {
					// Raccogli i dati dai campi di testo
					String name = getTextFieldValue("name");
					String surname = getTextFieldValue("surname");
					String dob = getDatePicker("dob");
					String email = getTextFieldValue("email");
					String username = getTextFieldValue("username");
					String password = getTextFieldValue("password");
					String certifications = getTextFieldValue("certifications");
					String competences = getTextFieldValue("competences");
					Date birthDate = Date.valueOf("2003-08-01");
					Gestore.registrazione(name, surname, birthDate, email, username, password, certifications,
							competences);
				});
	}

	private String getTextFieldValue(String fieldName) {
		for (Component component : getRootPane().getComponents()) {
			if (component instanceof JTextField) {
				JTextField textField = (JTextField) component;
				if (textField.getName().equals(fieldName)) {
					if (textField.getText().equals("")) {
						System.out.println(textField.getName());
						JOptionPane.showMessageDialog(managerRegisterPanel, "Tutti i campi devono essere riempiti!",
								"Errore", JOptionPane.ERROR_MESSAGE);
					}
					System.out.println(textField.getName());
				} else {
					return textField.getText();
				}
			}
		}
		return ""; // Stringa vuota se il campo non viene trovato
	}

	private String getDatePicker(String fieldName) {
		// Trova il campo di testo corrispondente al fieldName
		for (Component component : getRootPane().getComponents()) {
			if (component instanceof JDatePicker) {
				JDatePicker datePicker = (JDatePicker) component;
				if (((Component) datePicker).getName().equals(fieldName)) {
					java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
					if (selectedDate != null) {
						java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
						return sqlDate.toString();
					}
				}
			}
		}
		return ""; // Restituisce una stringa vuota se non trova il campo di testo

	}

	private JPanel createRegisterPanel(String title, String[][] fields, ActionListener registerAction) {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (clearImage != null) {
					g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new OutlinedLabel(title, Color.WHITE);
		titleLabel.setFont(new Font("Montserrat", Font.BOLD, 30));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(titleLabel, gbc);

		// Array per i campi di input
		Component[] inputFields = new Component[fields.length];

		for (int i = 0; i < fields.length; i++) {
			JLabel label = new OutlinedLabel(fields[i][0], Color.BLACK);
			label.setFont(new Font("Montserrat", Font.BOLD, 20));
			label.setForeground(Color.BLACK);

			Component inputField;
			if ("dob".equals(fields[i][1])) {
				// Usa JDatePicker per il campo "dob"
				UtilDateModel model = new UtilDateModel();
				Properties properties = new Properties();
				properties.put("text.today", "Oggi");
				properties.put("text.month", "Mese");
				properties.put("text.year", "Anno");

				JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
				JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

				datePicker.getJFormattedTextField().setFont(new Font("Arial", Font.PLAIN, 18));
				inputField = datePicker;
				inputField.setName(fields[i][1]);
			} else if ("password".equals(fields[i][1])) {
				inputField = new JPasswordField(20);
				inputField.setFont(new Font("Arial", Font.PLAIN, 18));
				inputField.setName(fields[i][1]);
			} else {
				inputField = new TextField(20);
				inputField.setName(fields[i][1]);
				inputField.setFont(new Font("Arial", Font.PLAIN, 18));
			}

			inputFields[i] = inputField;

			gbc.gridwidth = 1;
			gbc.anchor = GridBagConstraints.WEST;
			gbc.gridx = 0;
			gbc.gridy = i + 1;
			panel.add(label, gbc);

			gbc.gridx = 1;
			panel.add(inputField, gbc);
		}

		JButton registerButton = new JButton("Registrati");
		registerButton.setFont(new Font("Arial", Font.BOLD, 20));
		registerButton.setBackground(new Color(32, 178, 170));
		registerButton.setForeground(Color.WHITE);
		registerButton.setFocusPainted(false);

		registerButton.addActionListener(e -> {
			for (int i = 0; i < inputFields.length; i++) {
				if ("dob".equals(fields[i][1])) {
					JDatePickerImpl datePicker = (JDatePickerImpl) inputFields[i];
					java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
					if (selectedDate == null) {
						JOptionPane.showMessageDialog(BackgrounPanel.managerRegisterPanel,
								"Tutti i campi devono essere riempiti!", "Errore", JOptionPane.ERROR_MESSAGE);
					} else {
					}
				}
			}
			registerAction.actionPerformed(e);
		});

		gbc.gridx = 0;
		gbc.gridy = fields.length + 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(registerButton, gbc);

		// Aggiungi il pulsante "Back" sotto il pulsante "Registrati"
		gbc.gridx = 0;
		gbc.gridy = fields.length + 2; // Una riga sotto il pulsante "Registrati"
		gbc.gridwidth = 2;
		panel.add(BackgroundPanel.createBackButton(), gbc);

		return panel;
	}*/
}
