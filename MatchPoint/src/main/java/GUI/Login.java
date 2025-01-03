package GUI;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

import individui.Utente;
import dataBase.DataBase;
import dataBase.Sessione;


public class Login {
	
	private static JTextField usernameField;
    private static JPasswordField passwordField;

	//Metodo per la crezione del pannello di login
	protected static JPanel createLoginPanel() {
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
	    gbc.insets = new Insets(10, 10, 10, 10);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    JLabel titleLabel = new OutlinedLabel("Login", Color.WHITE);
	    titleLabel.setFont(new Font("Montserrat", Font.BOLD, 30));
	    
	    JLabel usernameLabel = new OutlinedLabel("Username:", Color.BLACK);
	    usernameLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
	    usernameField = new JTextField(20);
	    usernameField.setFont(new Font("Arial", Font.PLAIN, 18));

	    JLabel passwordLabel = new OutlinedLabel("Password:", Color.BLACK);
	    passwordLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
	    passwordField = new JPasswordField(20);
	    passwordField.setFont(new Font("Arial", Font.PLAIN, 18));

	 // Crea il pulsante "Login" utilizzando createFlatButton
	    JButton loginButton = BackgroundPanel.createFlatButton(
	        "Login", // Testo del pulsante
	        e -> { // Azione associata al pulsante
	            if (checkEmptyFields(usernameField, passwordField)) {
	                String username = usernameField.getText();
	                String password = new String(passwordField.getPassword());
	              
	                if (validateCredentials(username, password)) {
	                    CustomMessage.show("Login effettuato con successo!", "Successo", true);
	                    resetFields();
	                    
	                    // Determina il ruolo dell'utente
	                    String ruolo = Utente.getRuoloUtente(username, password); // Funzione per ottenere il ruolo dal database
	                    
	                    login(username, ruolo);
	                    Utente.login(username, password);
	                    if ("Gestore".equalsIgnoreCase(ruolo)) {
	                        BackgroundPanel.showPanel("createGestore"); // Mostra pannello Gestore
	                    } else if ("Giocatore".equalsIgnoreCase(ruolo)) {
	                        BackgroundPanel.showPanel("createGiocatore"); // Mostra pannello Giocatore
	                    } else {
	                        CustomMessage.show("Ruolo sconosciuto! Contatta l'amministratore.", "Errore", false);
	                    }
	                } else {
	                    CustomMessage.show("Username o password errati!", "Errore", false);
	                }
	            }
	        },
	        new Dimension(100, 40) // Dimensione del pulsante
	    );
	    
	    // Esegui il login quando premi Invio
	    usernameField.addActionListener(e -> loginButton.doClick());
	    // Esegui il login quando premi Invio
	    passwordField.addActionListener(e -> loginButton.doClick());
	    

	    // Personalizza ulteriormente il pulsante
	    loginButton.setFont(new Font("Arial", Font.BOLD, 20));
	    loginButton.setBackground(new Color(32, 178, 170));
	    loginButton.setForeground(new Color(220, 250, 245));
	    loginButton.setFocusPainted(false);
	    

	    //Posizionamento componenti
	    //Aggiungi il titolo (Login)
	    gbc.gridx = 0; //Colonna
	    gbc.gridy = 0; //Riga
	    gbc.gridwidth = 2; //Il titolo occupa entrambe le colonne
	    gbc.anchor = GridBagConstraints.CENTER; //Centrato
	    panel.add(titleLabel, gbc);

	    //Aggiungi l'etichetta e il campo per il nome utente
	    gbc.gridwidth = 1; //Torna a occupare una colonna
	    gbc.anchor = GridBagConstraints.WEST; //Allineato a sinistra
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    panel.add(usernameLabel, gbc);

	    gbc.gridx = 1; //Campo di testo accanto all'etichetta
	    panel.add(usernameField, gbc);

	    //Aggiungi l'etichetta e il campo per la password
	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    panel.add(passwordLabel, gbc);

	    gbc.gridx = 1;
	    panel.add(passwordField, gbc);

	    //Aggiungi il pulsante di login
	    gbc.gridx = 0;
	    gbc.gridy = 3;
	    gbc.gridwidth = 2; //Il pulsante di login occupa entrambe le colonne
	    gbc.anchor = GridBagConstraints.CENTER; //Centrato
	    panel.add(loginButton, gbc);

	    //Aggiungi il pulsante "Back"
	    gbc.gridx = 0;
	    gbc.gridy = 4; //Riga successiva
	    gbc.gridwidth = 2; //Anche il pulsante "Back" occupa entrambe le colonne
	    gbc.anchor = GridBagConstraints.CENTER;
	    panel.add(createBackButton(usernameField, passwordField), gbc);
	    
	    return panel;
	}
	
	public static void resetFields() {
        if (usernameField != null) {
            usernameField.setText("");
        }
        if (passwordField != null) {
            passwordField.setText("");
        }
    }
	
	//Controlla i campi del login, se sono vuoti restituisce errore
	private static boolean checkEmptyFields(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty()) {
        	CustomMessage.show("Il campo 'Username' non può essere vuoto!", "Errore", false);
            return false;
        }

        if (password.isEmpty()) {
        	CustomMessage.show("Il campo 'Password' non può essere vuoto!", "Errore", false);
            return false;
        }

        return true;
    }
	//Creazione del bottone "Back" con colore grigio
	protected static JButton createBackButton(JTextField usernameField, JPasswordField passwordField) {
	    // Utilizza createFlatButton per creare un pulsante "Back"
	    JButton backButton = BackgroundPanel.createFlatButton(
	        "Back", // Testo del pulsante
	        e -> {
	            // Resetta i campi di input
	            usernameField.setText("");
	            passwordField.setText("");

	            // Cambia pannello
	            BackgroundPanel.showPanel("main");
	            Sessione.logout();
	        },
	        new Dimension(120, 30) // Dimensione del pulsante
	    );

	    // Personalizzazioni specifiche per il pulsante "Back"
        backButton.setForeground(Color.GRAY); // Sfondo grigio
        backButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
	    backButton.setFont(new Font("Arial", Font.BOLD, 18));

	    return backButton;
	}

	public static void login(String username, String tipologia) {
        try {
            Sessione.login(username, tipologia);  // Esegui il login
           } 
        catch (SQLException e) {
        	CustomMessage.show("Errore di login", "Errore", false);
        }
    }
	
	// Metodo validateCredentials con accesso al database
	private static boolean validateCredentials(String username, String password) {
	    String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

	    String query = "SELECT COUNT(*) FROM (" +
	                   "    SELECT Username, Password FROM Gestore " +
	                   "    UNION ALL " +
	                   "    SELECT Username, Password FROM Giocatore" +
	                   ") AS Utenti " +
	                   "WHERE Username = ? AND Password = ?";

	    try (Connection conn = DriverManager.getConnection(url)) {
	        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
	            pstmt.setString(1, username);
	            pstmt.setString(2, password);

	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                return rs.getInt(1) > 0; // Ritorna true se esiste almeno un risultato
	            }
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null,
	            "Errore nel connettersi al database: " + e.getMessage(),
	            "Errore",
	            JOptionPane.ERROR_MESSAGE);
	        e.printStackTrace();
	    }
	    return false; // False se le credenziali non sono valide o si verifica un errore
	}
	
}
