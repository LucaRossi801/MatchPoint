package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HomePage {
    private JFrame frame;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    public static void main(String[] args) {
        // Avvio dell'interfaccia utente
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                HomePage window = new HomePage();
            }
        });
    }

    public HomePage() {
        initialize();
    }

    public void initialize() {
        // Verifica e carica l'icona dell'applicazione
        frame = new JFrame("MatchPoint");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        URL iconUrl = getClass().getResource("/GUI/immagini/icona.png"); // Percorso dell'icona
        if (iconUrl != null) {
            frame.setIconImage(new ImageIcon(iconUrl).getImage()); // Imposta l'icona
        } else {
            System.out.println("Immagine dell'icona non trovata!"); // Aggiungi un messaggio di errore nel caso l'icona
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel("/GUI/immagini/sfondohomesfocato.png",
                "/GUI/immagini/sfondohome.png");
        frame.add(backgroundPanel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centra la finestra
        frame.setVisible(true);
    }

    // Metodo per verificare che i campi non siano vuoti
    private boolean checkEmptyFields(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo 'Username' non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Il campo 'Password' non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // Pannello di login con controlli per i campi vuoti
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel loginUsernameLabel = new JLabel("Username:");
        loginUsernameField = new JTextField(20);
        JLabel loginPasswordLabel = new JLabel("Password:");
        loginPasswordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");

        panel.add(loginUsernameLabel);
        panel.add(loginUsernameField);
        panel.add(loginPasswordLabel);
        panel.add(loginPasswordField);
        panel.add(loginButton);

        // Azione del pulsante Login
        loginButton.addActionListener(e -> {
            if (checkEmptyFields(loginUsernameField, loginPasswordField)) {
                // Login se i campi sono corretti
                String username = loginUsernameField.getText();
                String password = new String(loginPasswordField.getPassword());
                System.out.println("Login con username: " + username + " e password: " + password);
            }
        });

        return panel;
    }

    // Metodo per creare il pannello di registrazione (verifica anche i campi vuoti)
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel firstNameLabel = new JLabel("Nome:");
        JTextField firstNameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Cognome:");
        JTextField lastNameField = new JTextField(20);

        JLabel dobLabel = new JLabel("Data di nascita (gg/mm/aaaa):");
        JPanel datePanel = new JPanel();
        JTextField dayField = new JTextField(2);
        JTextField monthField = new JTextField(2);
        JTextField yearField = new JTextField(4);
        datePanel.add(dayField);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthField);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearField);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton registerSubmitButton = new JButton("Register");

        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(dobLabel);
        panel.add(datePanel);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(registerSubmitButton);

        // Azione del pulsante di registrazione
        registerSubmitButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String day = dayField.getText().trim();
            String month = monthField.getText().trim();
            String year = yearField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // Controllo per verificare che tutti i campi siano pieni
            if (firstName.isEmpty() || lastName.isEmpty() || day.isEmpty() || month.isEmpty() || year.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                // Procedi con la registrazione se i campi sono completi
                System.out.println("Registrazione con nome: " + firstName + " e cognome: " + lastName);
            }
        });

        return panel;
    }
}
