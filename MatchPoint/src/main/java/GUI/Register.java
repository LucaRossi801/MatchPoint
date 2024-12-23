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
import javax.swing.Timer;

import java.sql.*;

import org.jdesktop.swingx.JXDatePicker; // Assicurati di aggiungere questa libreria
import dataBase.DataBase;
import individui.Gestore;
import individui.Giocatore;

public class Register {


	protected static JPanel createRegisterPanel(String tipologia) {
	    String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db"; // connessione al database
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
	        JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
	        label.setFont(new Font("Montserrat", Font.BOLD, 24));
	        gbc.gridx = 0;
	        gbc.gridy = row;
	        panel.add(label, gbc);

	        JComponent inputField = null;

	        if (campo.equals("Password")) {
	            inputField = new JPasswordField(20);
	            addCharacterLimit((JPasswordField) inputField, 20, panel);
	        } else if (campo.equals("DataNascita")) {
	            JXDatePicker datePicker = new JXDatePicker();
	            datePicker.setFont(new Font("Arial", Font.PLAIN, 18));
	            datePicker.setFormats("dd-MM-yyyy");
	            inputField = datePicker;
	        } else {
	            inputField = new JTextField(20);
	            int maxLength;

	            // Utilizza lo switch tradizionale
	            switch (campo) {
	                case "Nome":
	                case "Cognome":
	                case "Username":
	                case "NomeSquadra":
	                case "Certificazione":
	                case "Competenze":
	                    maxLength = 20;
	                    break;
	                case "Email":
	                    maxLength = 40;
	                    break;
	                default:
	                    maxLength = 20; // Default per gli altri campi
	                    break;
	            }

	            addCharacterLimit((JTextField) inputField, maxLength, panel);
	        }

	        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
	        gbc.gridx = 1;
	        panel.add(inputField, gbc);

	        fields.put(campo, inputField);

	        row++;
	    }


	    // Pulsante di registrazione
	    JButton registerButton = BackgroundPanel.createFlatButton(
	        "Register",
	        e -> {
	            try {
	                // Leggi i valori dai campi
	                String name = ((JTextField) fields.get("Nome")).getText().trim();
	                String surname = ((JTextField) fields.get("Cognome")).getText().trim();
	                String birthDate = null;

	                JXDatePicker datePicker = (JXDatePicker) fields.get("DataNascita");
	                if (datePicker.getDate() != null) {
	                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	                    birthDate = sdf.format(datePicker.getDate());
	                } else {
	                    CustomMessage.show("Compilare la data di nascita!", "Errore", false);
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
	                    CustomMessage.show("Tutti i campi devono essere compilati!", "Errore", false);
	                    return;
	                }

	                // Controlla se lo username è già presente nel database
	                String sql = "SELECT Password FROM Gestore WHERE Username ='" + username +
	                             "' UNION SELECT Password FROM Giocatore WHERE Username ='" + username + "'";
	                String ris = "";
	                try (Connection conn = DriverManager.getConnection(url)) {
	                    ris = DataBase.eseguiSelect(conn, sql);
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    CustomMessage.show("Errore di connessione al DataBase", "Errore", false);
	                    return;
	                }

	                if (!ris.isEmpty()) {
	                    CustomMessage.show("Lo username '" + username + "' è già in uso!", "Errore", false);
	                    return;
	                }
	                
	                // Controlla se l'email è già presente nel database
	                String emailSql = "SELECT Email FROM Gestore WHERE Email ='" + email +
	                                  "' UNION SELECT Email FROM Giocatore WHERE Email ='" + email + "'";
	                String emailRis = "";
	                try (Connection conn = DriverManager.getConnection(url)) {
	                    emailRis = DataBase.eseguiSelect(conn, emailSql);
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                    CustomMessage.show("Errore di connessione al DataBase", "Errore", false);
	                    return;
	                }

	                if (!emailRis.isEmpty()) {
	                    CustomMessage.show("L'email '" + email + "' è già in uso!", "Errore", false);
	                    return;
	                }


	                // Chiama il metodo di registrazione per i Gestori o Giocatori
	                if (tipologia.equals("Gestore")) {
	                    Gestore.registrazione(name, surname, birthDate, email, username, password, certifications, competences);
	                    System.out.println("Registrazione completata per il Gestore!");
	                } else {
	                    String teamName = ((JTextField) fields.get("NomeSquadra")).getText().trim();
	                    if (teamName.isEmpty()) {
	                        CustomMessage.show("Tutti i campi devono essere compilati!", "Errore", false);
	                        return;
	                    }
	                    Giocatore.registrazione(name, surname, birthDate, email, username, password, teamName);
	                    System.out.println("Registrazione completata per il Giocatore!");
	                }

	                CustomMessage.show("Registrazione effettuata con successo!", "Successo", true);
	                BackgroundPanel.showPanel("login");
	            } catch (Exception ex) {
	                CustomMessage.show("Errore durante la registrazione", "Errore", false);
	            }
	            
	            // Pulizia campi
	            resetFields(fields);
	        },
	        new Dimension(300, 50)
	    );

	    registerButton.setFont(new Font("Arial", Font.BOLD, 18));
	    registerButton.setBackground(new Color(32, 178, 170));
	    registerButton.setForeground(new Color(220, 250, 245));
	    registerButton.setFocusPainted(false);

	    gbc.gridx = 0;
	    gbc.gridy = row;
	    gbc.gridwidth = 2;
	    gbc.anchor = GridBagConstraints.CENTER;
	    panel.add(registerButton, gbc);

	    // Pulsante "Back"
	    gbc.gridy = row + 1;
	    gbc.gridwidth = 2;
	    gbc.anchor = GridBagConstraints.CENTER;
	    panel.add(createBackButton(fields), gbc);

	    return panel;
	}


	protected static JButton createBackButton(Map<String, JComponent> fields) {
	    // Usa createFlatButton per creare il pulsante "Back"
	    JButton backButton = BackgroundPanel.createFlatButton(
	        "Back", // Testo del pulsante
	        e -> {
	        	// Pulizia campi
	            resetFields(fields);

	            // Cambia pannello
	            BackgroundPanel.showPanel("main");
	        },
	        new Dimension(120, 30) // Dimensione del pulsante
	    );

	    // Personalizzazioni specifiche per il pulsante "Back"
	    backButton.setForeground(Color.GRAY); // Testo grigio
	    backButton.setBackground(Color.DARK_GRAY); // Sfondo scuro
	    backButton.setFont(new Font("Arial", Font.BOLD, 18));

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
	
	public static void resetFields(Map<String, JComponent> fields) {
	    fields.forEach((key, field) -> {
	        if (field instanceof JTextField) {
	            ((JTextField) field).setText(""); // Resetta il testo
	        } else if (field instanceof JPasswordField) {
	            ((JPasswordField) field).setText(""); // Resetta la password
	        } else if (field instanceof JXDatePicker) {
	            ((JXDatePicker) field).setDate(null); // Resetta la data
	        }
	    });
	}
	
	private static void addCharacterLimit(JTextField field, int maxLength, JPanel containerPanel) {
	    // Crea la JLabel per il messaggio di avviso
	    JLabel warningLabel = new JLabel();
	    warningLabel.setForeground(Color.RED);
	    warningLabel.setFont(new Font("Arial", Font.PLAIN, 12));
	    warningLabel.setPreferredSize(new Dimension(200, 20)); // Imposta una larghezza fissa per la label
	    warningLabel.setHorizontalAlignment(SwingConstants.LEFT);
	    warningLabel.setVisible(false); // Nascondi inizialmente

	    // Usa un layout null per avere un controllo assoluto sulla posizione
	    JPanel fieldPanel = new JPanel();
	    fieldPanel.setLayout(null); // Layout assoluto
	    fieldPanel.setOpaque(false); // Non colorare il pannello

	    // Imposta la posizione e la dimensione della JTextField
	    field.setBounds(0, 0, 200, 30); // Cambia queste dimensioni e posizione in base alle tue esigenze
	    fieldPanel.add(field); // Aggiungi la JTextField al pannello

	    // Imposta la posizione e la dimensione della JLabel
	    warningLabel.setBounds(210, 5, 200, 20); // Cambia queste coordinate per regolare la posizione della scritta
	    fieldPanel.add(warningLabel); // Aggiungi la JLabel al pannello

	    // Configura il layout principale per il pannello
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 1; // Posiziona nella colonna giusta
	    gbc.gridy = GridBagConstraints.RELATIVE; // Riga successiva
	    gbc.fill = GridBagConstraints.HORIZONTAL; // Riempie orizzontalmente
	    gbc.insets = new Insets(5, 0, 5, 0); // Imposta margini
	    containerPanel.add(fieldPanel, gbc); // Aggiungi il pannello al container

	    // Listener per limitare i caratteri
	    field.addKeyListener(new java.awt.event.KeyAdapter() {
	        @Override
	        public void keyTyped(java.awt.event.KeyEvent e) {
	            if (field.getText().length() >= maxLength) {
	                e.consume(); // Ignora ulteriori input
	                warningLabel.setText("Massimo " + maxLength + " caratteri!");
	                warningLabel.setVisible(true);

	                // Nascondi la scritta dopo 1.5 secondi
	                Timer timer = new Timer(1500, evt -> warningLabel.setVisible(false));
	                timer.setRepeats(false);
	                timer.start();
	            } else {
	                warningLabel.setVisible(false); // Nascondi se sotto il limite
	            }
	        }
	    });
	}

}
