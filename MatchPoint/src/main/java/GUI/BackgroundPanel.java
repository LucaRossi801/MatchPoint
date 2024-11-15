package GUI;

import javax.swing.*;

import individui.Utente;

import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private JPanel homePanel; // Pannello per la homepage
	private JPanel mainPanel; // Pannello per la vista principale (con immagine nitida e bottoni)
	private JPanel loginPanel; // Pannello per la vista di login
	private JPanel registerPanel; // Pannello per la vista di registrazione

	private Image backgroundImage;
	private Image clearImage;

	private boolean isVisible; // Booleano per la visibilità della scritta lampeggiante
	private Timer blinkTimer; // Timer per gestire il lampeggiamento
	private int blinkInterval; // Intervallo di tempo per il lampeggiamento

	private JButton registerButton; // Bottone per il "Register"
	private JButton loginButton; // Bottone per il "Login"

	public BackgroundPanel(String blurredImagePath, String clearImagePath) {
		this.isVisible = true; // La scritta è visibile inizialmente
		this.blinkInterval = 800; // Durata della visibilità della scritta (in ms)

		// Usa CardLayout per gestire le diverse viste
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		// Crea i pannelli per le diverse viste
		homePanel = createHomePanel(blurredImagePath);
		mainPanel = createMainPanel(clearImagePath);
		loginPanel = createLoginPanel();
		registerPanel = createRegisterPanel();

		// Aggiungi i pannelli al CardLayout
		cardPanel.add(homePanel, "home");
		cardPanel.add(mainPanel, "main");
		cardPanel.add(loginPanel, "login");
		cardPanel.add(registerPanel, "register");

		// Imposta la vista iniziale come la homepage
		cardLayout.show(cardPanel, "home");

		// Imposta il layout del pannello principale
		setLayout(new BorderLayout());
		add(cardPanel, BorderLayout.CENTER);

		// Imposta la dimensione del pannello (ad esempio 800x600)
		setPreferredSize(new Dimension(800, 600)); // Imposta una dimensione esplicita per il pannello
	}

	// Crea il pannello della homepage con immagine sfocata e scritta lampeggiante
	private JPanel createHomePanel(String blurredImagePath) {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// Disegna l'immagine sfocata
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} else {
					System.out.println("Immagine sfocata non trovata!");
				}

				// Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero
				g.setColor(Color.BLACK); // Contorno nero
				g.setFont(new Font("Impact", Font.BOLD, 70)); // Font grande e grassetto
				String matchPointText = "MATCHPOINT";
				FontMetrics metrics = g.getFontMetrics();
				int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
				int y = 100;

				// Contorno nero per la scritta
				g.drawString(matchPointText, x - 2, y - 2);
				g.drawString(matchPointText, x + 2, y - 2);
				g.drawString(matchPointText, x - 2, y + 2);
				g.drawString(matchPointText, x + 2, y + 2);

				// Scritta principale in verde acqua
				g.setColor(new Color(32, 178, 170));
				g.drawString(matchPointText, x, y);

				// Scritta "Clicca ovunque per continuare"
				if (isVisible) {
					g.setColor(Color.BLACK);
					g.setFont(new Font("Montserrat", Font.BOLD, 24));
					String text = "Clicca ovunque per continuare!".toUpperCase();
					FontMetrics metricsText = g.getFontMetrics();
					int xText = (getWidth() - metricsText.stringWidth(text)) / 2;
					int yText = (int) (getHeight() * 0.75);
					g.drawString(text, xText, yText);
				}
			}
		};

		// Carica l'immagine sfocata
		URL blurredImageUrl = getClass().getResource(blurredImagePath);
		if (blurredImageUrl != null) {
			this.backgroundImage = new ImageIcon(blurredImageUrl).getImage();
		} else {
			System.out.println("Errore nel caricamento dell'immagine : " + blurredImagePath);
		}

		// Timer per il lampeggiamento della scritta
		blinkTimer = new Timer(blinkInterval, e -> {
			isVisible = !isVisible;
			blinkInterval = isVisible ? 300 : 800;
			blinkTimer.setDelay(blinkInterval);
			repaint();
		});
		blinkTimer.start();

		// Aggiungi il mouse listener
		panel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				cardLayout.show(cardPanel, "main"); // Passa alla vista principale
			}
		});

		return panel;
	}

	// Crea il pannello della vista principale con immagine nitida e bottoni
	private JPanel createMainPanel(String clearImagePath) {
		JPanel panel = new JPanel(null) { // Usa un layout null per posizionare i bottoni manualmente
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// Disegna l'immagine nitida
				if (clearImage != null) {
					g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
				}

				// Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero
				g.setColor(Color.BLACK); // Contorno nero
				g.setFont(new Font("Impact", Font.BOLD, 70)); // Font grande e grassetto
				String matchPointText = "MATCHPOINT";
				FontMetrics metrics = g.getFontMetrics();
				int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
				int y = 100;

				// Contorno nero per la scritta
				g.drawString(matchPointText, x - 2, y - 2);
				g.drawString(matchPointText, x + 2, y - 2);
				g.drawString(matchPointText, x - 2, y + 2);
				g.drawString(matchPointText, x + 2, y + 2);

				// Scritta principale in verde acqua
				g.setColor(new Color(32, 178, 170));
				g.drawString(matchPointText, x, y);
			}
		};

		// Carica l'immagine nitida
		URL clearImageUrl = getClass().getResource(clearImagePath);
		if (clearImageUrl != null) {
			this.clearImage = new ImageIcon(clearImageUrl).getImage();
		} else {
			System.out.println("Errore nel caricamento dell'immagine : " + clearImagePath);
		}

		// Crea i bottoni "Register" e "Login"
		registerButton = new JButton("Register");
		registerButton.setFont(new Font("Arial", Font.BOLD, 20));
		registerButton.setBackground(new Color(32, 178, 170)); // Colore verde acqua
		registerButton.setForeground(Color.WHITE);
		registerButton.setFocusPainted(false);
		registerButton.setBounds(300, 400, 200, 50); // Posizione
		registerButton.addActionListener(e -> cardLayout.show(cardPanel, "register"));
		panel.add(registerButton);

		loginButton = new JButton("Login");
		loginButton.setFont(new Font("Arial", Font.BOLD, 20));
		loginButton.setBackground(new Color(32, 178, 170)); // Colore verde acqua
		loginButton.setForeground(Color.WHITE);
		loginButton.setFocusPainted(false);
		loginButton.setBounds(300, 470, 200, 50); // Posizione
		loginButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));
		panel.add(loginButton);

		return panel;
	}

	// Crea il pannello di login
	private JPanel createLoginPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel usernameLabel = new JLabel("Username:");
		JTextField usernameField = new JTextField(20);
		JLabel passwordLabel = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField(20);

		JButton loginSubmitButton = new JButton("Login");

		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(passwordLabel);
		panel.add(passwordField);
		panel.add(loginSubmitButton);
		
		 loginSubmitButton.addActionListener(e -> {
        if (checkEmptyFields(usernameField, passwordField)) {
            // Login se i campi sono corretti
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            System.out.println("Login con username: " + username + " e password: " + password);
            Utente.login(username, password);
        }
    });

		return panel;
	}

	private JPanel createRegisterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Creazione dei campi di testo
		JLabel firstNameLabel = new JLabel("Nome:");
		JTextField firstNameField = new JTextField(20);
		JLabel lastNameLabel = new JLabel("Cognome:");
		JTextField lastNameField = new JTextField(20);

		// Creazione dei campi per la data di nascita (giorno, mese, anno)
		JLabel dobLabel = new JLabel("Data di nascita (gg/mm/aaaa):");

		// Creazione dei campi separati per giorno, mese, anno
		JPanel datePanel = new JPanel();
		JTextField dayField = new JTextField(2); // Campo per il giorno
		JTextField monthField = new JTextField(2); // Campo per il mese
		JTextField yearField = new JTextField(4); // Campo per l'anno
		datePanel.add(dayField);
		datePanel.add(new JLabel("/"));
		datePanel.add(monthField);
		datePanel.add(new JLabel("/"));
		datePanel.add(yearField);

		// Creazione dei campi per username e password
		JLabel usernameLabel = new JLabel("Username:");
		JTextField usernameField = new JTextField(20);
		JLabel passwordLabel = new JLabel("Password:");
		JPasswordField passwordField = new JPasswordField(20);

		JButton registerSubmitButton = new JButton("Register");

		// Aggiungi tutti i componenti al pannello
		panel.add(firstNameLabel);
		panel.add(firstNameField);
		panel.add(lastNameLabel);
		panel.add(lastNameField);
		panel.add(dobLabel);
		panel.add(datePanel); // Pannello della data
		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(passwordLabel);
		panel.add(passwordField);
		panel.add(registerSubmitButton);

		// Gestire l'evento di registrazione
		registerSubmitButton.addActionListener(e -> {
			String day = dayField.getText();
			String month = monthField.getText();
			String year = yearField.getText();

			// Validazione dei dati
			if (day.isEmpty() || month.isEmpty() || year.isEmpty()) {
				JOptionPane.showMessageDialog(panel, "Compila correttamente la data di nascita.");
				return;
			}

			// Prova a concatenare la data
			try {
				int d = Integer.parseInt(day);
				int m = Integer.parseInt(month);
				int y = Integer.parseInt(year);

				// Validazione del giorno, mese e anno
				if (m < 1 || m > 12) {
					JOptionPane.showMessageDialog(panel, "Il mese deve essere tra 1 e 12.");
					return;
				}

				if (d < 1 || d > 31) {
					JOptionPane.showMessageDialog(panel, "Il giorno deve essere tra 1 e 31.");
					return;
				}

				if (y < 1900 || y > 2025) {
					JOptionPane.showMessageDialog(panel, "L'anno deve essere tra 1900 e 2025.");
					return;
				}

				// Data valida
				String birthDate = String.format("%02d/%02d/%04d", d, m, y);
				System.out.println("Data di nascita: " + birthDate);
				// Puoi usare la data in un formato concatenato o fare ulteriori elaborazioni
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(panel, "Inserisci numeri validi per la data.");
			}
		});

		return panel;
	}
	 private boolean checkEmptyFields(JTextField usernameField, JPasswordField passwordField) {
	        String username = usernameField.getText().trim();
	        String password = new String(passwordField.getPassword()).trim();

	        if (username.isEmpty()) {
	            JOptionPane.showMessageDialog(loginPanel, "Il campo 'Username' non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        if (password.isEmpty()) {
	            JOptionPane.showMessageDialog(loginPanel, "Il campo 'Password' non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
	            return false;
	        }

	        return true;
	    }
}