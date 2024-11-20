package GUI;

import java.awt.*;
import javax.swing.*;
import individui.Utente;

public class Login {

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
	    JTextField usernameField = new JTextField(20);
	    usernameField.setFont(new Font("Arial", Font.PLAIN, 18));

	    JLabel passwordLabel = new OutlinedLabel("Password:", Color.BLACK);
	    passwordLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
	    JPasswordField passwordField = new JPasswordField(20);
	    passwordField.setFont(new Font("Arial", Font.PLAIN, 18));

	    JButton loginButton = new JButton("Login");
	    loginButton.setFont(new Font("Arial", Font.BOLD, 20));
	    loginButton.setBackground(new Color(32, 178, 170));
	    loginButton.setForeground(Color.WHITE);
	    loginButton.setFocusPainted(false);

	    loginButton.addActionListener(e -> {
	    	if(checkEmptyFields(usernameField, passwordField)) {
	    		String username = usernameField.getText();
		        String password = new String(passwordField.getPassword());
		        Utente.login(username, password);
	    	}
	    });
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
	    panel.add(BackgroundPanel.createBackButton(), gbc);


	    return panel;
	}
	
	//Controlla i campi del login, se sono vuoti restituisce errore
	private static boolean checkEmptyFields(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(BackgroundPanel.loginPanel, "Il campo 'Username' non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(BackgroundPanel.loginPanel, "Il campo 'Password' non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
	
	
}
