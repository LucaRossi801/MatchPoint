package GUI;

import javax.swing.*;
import java.net.URL;

/**
 * Classe principale per l'avvio dell'interfaccia grafica dell'applicazione
 * MatchPoint. Contiene i metodi per creare e gestire i principali pannelli,
 * come login e registrazione.
 */
public class HomePage {
	private JFrame frame; // Finestra principale dell'applicazione
	private JTextField loginUsernameField; // Campo di testo per l'username del login
	private JPasswordField loginPasswordField; // Campo di testo per la password del login

	/**
	 * Metodo principale per avviare l'interfaccia utente.
	 * 
	 * @param args Argomenti della riga di comando (non utilizzati).
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				HomePage window = new HomePage();
			}
		});
	}

	/**
	 * Costruttore della classe HomePage. Inizializza la finestra principale.
	 */
	public HomePage() {
		initialize();
	}

	/**
	 * Inizializza la finestra principale e i componenti grafici. Imposta l'icona
	 * dell'applicazione e il pannello di sfondo.
	 */
	public void initialize() {
		frame = new JFrame("MatchPoint");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Carica l'icona dell'applicazione
		URL iconUrl = getClass().getResource("/GUI/immagini/icona.png");
		if (iconUrl != null) {
			frame.setIconImage(new ImageIcon(iconUrl).getImage());
		} else {
			CustomMessage.show("Immagine dell'icona non trovata!", "Errore", false);
		}

		// Aggiunge il pannello di sfondo
		BackgroundPanel backgroundPanel = new BackgroundPanel("/GUI/immagini/sfondohomesfocato.png",
				"/GUI/immagini/sfondohome.png");
		frame.add(backgroundPanel);
		frame.pack();
		frame.setLocationRelativeTo(null); // Centra la finestra
		frame.setVisible(true);
	}

	/**
	 * Crea il pannello di login. Al momento contiene solo la struttura base.
	 * 
	 * @return Un pannello JPanel con i componenti del login.
	 */
	private JPanel createLoginPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}

	/**
	 * Crea il pannello di registrazione. Include i campi per il nome, cognome, data
	 * di nascita, username e password, con controlli per i campi vuoti.
	 * 
	 * @return Un pannello JPanel con i componenti per la registrazione.
	 */
	private JPanel createRegisterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Componenti del form di registrazione
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

		// Aggiunta dei componenti al pannello
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

			// Controlla che tutti i campi siano pieni
			if (firstName.isEmpty() || lastName.isEmpty() || day.isEmpty() || month.isEmpty() || year.isEmpty()
					|| username.isEmpty() || password.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Tutti i campi sono obbligatori!", "Errore",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		return panel;
	}
}
