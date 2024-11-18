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
	private JPanel playerRegisterPanel; // Pannello per la vista di registrazione
	private JPanel managerRegisterPanel; // Pannello per la vista di registrazione

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
		playerRegisterPanel = createPlayerRegisterPanel();
		managerRegisterPanel = createManagerRegisterPanel();

		// Aggiungi i pannelli al CardLayout
		cardPanel.add(homePanel, "home");
		cardPanel.add(mainPanel, "main");
		cardPanel.add(loginPanel, "login");
		cardPanel.add(playerRegisterPanel, "playerRegister");
		cardPanel.add(managerRegisterPanel, "managerRegister");

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

	    // Dimensioni e posizioni dei bottoni
	    int buttonWidth = 220; // Aumentata la larghezza dei bottoni
	    int buttonHeight = 50;
	    int spacing = 30; // Spazio tra i due pulsanti
	    int totalWidth = (2 * buttonWidth) + spacing; // Larghezza totale dei bottoni di registrazione più lo spazio
	    int startX = (800 - totalWidth) / 2; // Calcola la posizione centrale basata su 800 di larghezza

	    // Crea i bottoni "Register Giocatore" e "Register Gestore"
	    JButton registerGiocatoreButton = new JButton("Register Giocatore");
	    registerGiocatoreButton.setFont(new Font("Arial", Font.BOLD, 20));
	    registerGiocatoreButton.setBackground(new Color(32, 178, 170)); // Colore verde acqua
	    registerGiocatoreButton.setForeground(Color.WHITE);
	    registerGiocatoreButton.setFocusPainted(false);
	    registerGiocatoreButton.setBounds(startX, 400, buttonWidth, buttonHeight);
	    registerGiocatoreButton.addActionListener(e -> cardLayout.show(cardPanel, "playerRegister"));
	    panel.add(registerGiocatoreButton);

	    JButton registerGestoreButton = new JButton("Register Gestore");
	    registerGestoreButton.setFont(new Font("Arial", Font.BOLD, 20));
	    registerGestoreButton.setBackground(new Color(32, 178, 170)); // Colore verde acqua
	    registerGestoreButton.setForeground(Color.WHITE);
	    registerGestoreButton.setFocusPainted(false);
	    registerGestoreButton.setBounds(startX + buttonWidth + spacing, 400, buttonWidth, buttonHeight);
	    registerGestoreButton.addActionListener(e -> cardLayout.show(cardPanel, "managerRegister"));
	    panel.add(registerGestoreButton);

	    // Crea il pulsante "Login" con la larghezza totale
	    loginButton = new JButton("Login");
	    loginButton.setFont(new Font("Arial", Font.BOLD, 24)); // Font più grande per "Login"
	    loginButton.setBackground(new Color(32, 178, 170)); // Colore verde acqua
	    loginButton.setForeground(Color.WHITE);
	    loginButton.setFocusPainted(false);
	    loginButton.setBounds(startX, 470, totalWidth, 60); // Posizione e larghezza corrispondente
	    loginButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));
	    panel.add(loginButton);

	    return panel;
	}





	// Crea il pannello di login
	private JPanel createLoginPanel() {
	    JPanel panel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);

	            // Disegna lo sfondo
	            if (clearImage != null) {
	                g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
	            }
	        }
	    };
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	    panel.setOpaque(false);

	    // Creazione dei componenti
	    JLabel usernameLabel = new JLabel("Username:");
	    usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JTextField usernameField = new JTextField();
	    usernameField.setFont(new Font("Arial", Font.PLAIN, 12)); // Font più piccolo
	    usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
	    usernameField.setPreferredSize(new Dimension(100, 20)); // Altezza e larghezza più piccole
	    usernameField.setMaximumSize(new Dimension(100, 20));   // Limita la dimensione massima

	    JLabel passwordLabel = new JLabel("Password:");
	    passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

	    JPasswordField passwordField = new JPasswordField();
	    passwordField.setFont(new Font("Arial", Font.PLAIN, 12)); // Font più piccolo
	    passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
	    passwordField.setPreferredSize(new Dimension(100, 20)); // Altezza e larghezza più piccole
	    passwordField.setMaximumSize(new Dimension(100, 20));   // Limita la dimensione massima

	    JButton loginSubmitButton = new JButton("Login");
	    loginSubmitButton.setFont(new Font("Arial", Font.BOLD, 12)); // Font più piccolo
	    loginSubmitButton.setBackground(new Color(32, 178, 170));
	    loginSubmitButton.setForeground(Color.WHITE);
	    loginSubmitButton.setFocusPainted(false);
	    loginSubmitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

	    loginSubmitButton.addActionListener(e -> {
	        if (checkEmptyFields(usernameField, passwordField)) {
	            // Login se i campi sono corretti
	            String username = usernameField.getText();
	            String password = new String(passwordField.getPassword());
	            System.out.println("Login con username: " + username + " e password: " + password);
	            Utente.login(username, password);
	        }
	    });

	    // Aggiunta dei componenti al pannello con spazi ridotti
	    panel.add(Box.createVerticalGlue());
	    panel.add(usernameLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 5))); // Spazio fisso
	    panel.add(usernameField);
	    panel.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio fisso
	    panel.add(passwordLabel);
	    panel.add(Box.createRigidArea(new Dimension(0, 5))); // Spazio fisso
	    panel.add(passwordField);
	    panel.add(Box.createRigidArea(new Dimension(0, 15))); // Spazio fisso
	    panel.add(loginSubmitButton);
	    panel.add(Box.createVerticalGlue());

	    return panel;
	}



	private JPanel createPlayerRegisterPanel() {
	    JPanel panel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);

	            // Disegna lo sfondo
	            if (clearImage != null) {
	                g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
	            }
	        }
	    };
	    panel.setLayout(null); // Layout assoluto
	    panel.setOpaque(false);

	    // Campi comuni
	    JLabel firstNameLabel = new JLabel("Nome:");
	    firstNameLabel.setBounds(120, 100, 80, 25);
	    JTextField firstNameField = new JTextField();
	    firstNameField.setBounds(200, 100, 150, 25);

	    JLabel lastNameLabel = new JLabel("Cognome:");
	    lastNameLabel.setBounds(120, 140, 80, 25);
	    JTextField lastNameField = new JTextField();
	    lastNameField.setBounds(200, 140, 150, 25);

	    JLabel dobLabel = new JLabel("Data di nascita:");
	    dobLabel.setBounds(120, 180, 100, 25);
	    JTextField dobField = new JTextField("gg/mm/aaaa");
	    dobField.setBounds(200, 180, 150, 25);

	    JLabel usernameLabel = new JLabel("Username:");
	    usernameLabel.setBounds(120, 220, 80, 25);
	    JTextField usernameField = new JTextField();
	    usernameField.setBounds(200, 220, 150, 25);

	    JLabel passwordLabel = new JLabel("Password:");
	    passwordLabel.setBounds(120, 260, 80, 25);
	    JPasswordField passwordField = new JPasswordField();
	    passwordField.setBounds(200, 260, 150, 25);

	    // Campo aggiuntivo per il giocatore
	    JLabel teamNameLabel = new JLabel("Nome Squadra:");
	    teamNameLabel.setBounds(120, 300, 100, 25);
	    JTextField teamNameField = new JTextField();
	    teamNameField.setBounds(200, 300, 150, 25);

	    // Pulsante di registrazione
	    JButton registerButton = new JButton("Register");
	    registerButton.setBounds(200, 340, 150, 25);
	    registerButton.setBackground(new Color(32, 178, 170));
	    registerButton.setForeground(Color.WHITE);
	    registerButton.setFocusPainted(false);

	    registerButton.addActionListener(e -> {
	        String firstName = firstNameField.getText().trim();
	        String lastName = lastNameField.getText().trim();
	        String dob = dobField.getText().trim();
	        String username = usernameField.getText().trim();
	        String password = new String(passwordField.getPassword()).trim();
	        String teamName = teamNameField.getText().trim();

	        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || username.isEmpty() || password.isEmpty() || teamName.isEmpty()) {
	            JOptionPane.showMessageDialog(panel, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
	        } else {
	            System.out.println("Registrazione giocatore effettuata: " + firstName + " " + lastName + ", Squadra: " + teamName);
	        }
	    });

	    // Aggiunta dei componenti
	    panel.add(firstNameLabel);
	    panel.add(firstNameField);
	    panel.add(lastNameLabel);
	    panel.add(lastNameField);
	    panel.add(dobLabel);
	    panel.add(dobField);
	    panel.add(usernameLabel);
	    panel.add(usernameField);
	    panel.add(passwordLabel);
	    panel.add(passwordField);
	    panel.add(teamNameLabel);
	    panel.add(teamNameField);
	    panel.add(registerButton);

	    return panel;
	}
	private JPanel createManagerRegisterPanel() {
	    JPanel panel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);

	            // Disegna lo sfondo
	            if (clearImage != null) {
	                g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
	            }
	        }
	    };
	    panel.setLayout(null); // Layout assoluto
	    panel.setOpaque(false);

	    // Campi comuni
	    JLabel firstNameLabel = new JLabel("Nome:");
	    firstNameLabel.setBounds(120, 100, 80, 25);
	    JTextField firstNameField = new JTextField();
	    firstNameField.setBounds(200, 100, 150, 25);

	    JLabel lastNameLabel = new JLabel("Cognome:");
	    lastNameLabel.setBounds(120, 140, 80, 25);
	    JTextField lastNameField = new JTextField();
	    lastNameField.setBounds(200, 140, 150, 25);

	    JLabel dobLabel = new JLabel("Data di nascita:");
	    dobLabel.setBounds(120, 180, 100, 25);
	    JTextField dobField = new JTextField("gg/mm/aaaa");
	    dobField.setBounds(200, 180, 150, 25);

	    JLabel usernameLabel = new JLabel("Username:");
	    usernameLabel.setBounds(120, 220, 80, 25);
	    JTextField usernameField = new JTextField();
	    usernameField.setBounds(200, 220, 150, 25);

	    JLabel passwordLabel = new JLabel("Password:");
	    passwordLabel.setBounds(120, 260, 80, 25);
	    JPasswordField passwordField = new JPasswordField();
	    passwordField.setBounds(200, 260, 150, 25);

	    // Campi aggiuntivi per il gestore
	    JLabel certificationsLabel = new JLabel("Certificazioni:");
	    certificationsLabel.setBounds(120, 300, 100, 25);
	    JTextField certificationsField = new JTextField();
	    certificationsField.setBounds(200, 300, 150, 25);

	    JLabel skillsLabel = new JLabel("Competenze:");
	    skillsLabel.setBounds(120, 340, 100, 25);
	    JTextField skillsField = new JTextField();
	    skillsField.setBounds(200, 340, 150, 25);

	    // Pulsante di registrazione
	    JButton registerButton = new JButton("Register");
	    registerButton.setBounds(200, 380, 150, 25);
	    registerButton.setBackground(new Color(32, 178, 170));
	    registerButton.setForeground(Color.WHITE);
	    registerButton.setFocusPainted(false);

	    registerButton.addActionListener(e -> {
	        String firstName = firstNameField.getText().trim();
	        String lastName = lastNameField.getText().trim();
	        String dob = dobField.getText().trim();
	        String username = usernameField.getText().trim();
	        String password = new String(passwordField.getPassword()).trim();
	        String certifications = certificationsField.getText().trim();
	        String skills = skillsField.getText().trim();

	        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || username.isEmpty() || password.isEmpty() || certifications.isEmpty() || skills.isEmpty()) {
	            JOptionPane.showMessageDialog(panel, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
	        } else {
	            System.out.println("Registrazione gestore effettuata: " + firstName + " " + lastName + ", Certificazioni: " + certifications + ", Competenze: " + skills);
	        }
	    });

	    // Aggiunta dei componenti
	    panel.add(firstNameLabel);
	    panel.add(firstNameField);
	    panel.add(lastNameLabel);
	    panel.add(lastNameField);
	    panel.add(dobLabel);
	    panel.add(dobField);
	    panel.add(usernameLabel);
	    panel.add(usernameField);
	    panel.add(passwordLabel);
	    panel.add(passwordField);
	    panel.add(certificationsLabel);
	    panel.add(certificationsField);
	    panel.add(skillsLabel);
	    panel.add(skillsField);
	    panel.add(registerButton);

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