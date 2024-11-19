package GUI;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.impl.*;
import individui.Gestore;
import individui.Utente;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

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
	    JPanel panel = new JPanel(new GridBagLayout()) {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);

	            // Disegna l'immagine nitida
	            if (clearImage != null) {
	                g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
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
	        }
	    };

	    // Carica l'immagine nitida
	    URL clearImageUrl = getClass().getResource(clearImagePath);
	    if (clearImageUrl != null) {
	        this.clearImage = new ImageIcon(clearImageUrl).getImage();
	    } else {
	        System.out.println("Errore nel caricamento dell'immagine : " + clearImagePath);
	    }
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 10, 10); // Spaziatura tra i componenti
	    gbc.fill = GridBagConstraints.HORIZONTAL; // I pulsanti si allargano orizzontalmente
	    gbc.gridx = 0;
	    gbc.gridy = 0;

	    // Bottone "Register Giocatore"
	    JButton registerGiocatoreButton = new JButton("Register Giocatore");
	    registerGiocatoreButton.setFont(new Font("Arial", Font.BOLD, 20));
	    registerGiocatoreButton.setBackground(new Color(32, 178, 170));
	    registerGiocatoreButton.setForeground(Color.WHITE);
	    registerGiocatoreButton.setFocusPainted(false);
	    registerGiocatoreButton.addActionListener(e -> cardLayout.show(cardPanel, "playerRegister"));
	    panel.add(registerGiocatoreButton, gbc);

	    // Bottone "Register Gestore"
	    gbc.gridx = 1; // Passa alla colonna successiva
	    JButton registerGestoreButton = new JButton("Register Gestore");
	    registerGestoreButton.setFont(new Font("Arial", Font.BOLD, 20));
	    registerGestoreButton.setBackground(new Color(32, 178, 170));
	    registerGestoreButton.setForeground(Color.WHITE);
	    registerGestoreButton.setFocusPainted(false);
	    registerGestoreButton.addActionListener(e -> cardLayout.show(cardPanel, "managerRegister"));
	    panel.add(registerGestoreButton, gbc);

	    // Bottone "Login"
	    gbc.gridx = 0;
	    gbc.gridy = 1; // Passa alla riga successiva
	    gbc.gridwidth = 2; // Occupa entrambe le colonne
	    JButton loginButton = new JButton("Login");
	    loginButton.setFont(new Font("Arial", Font.BOLD, 24));
	    loginButton.setBackground(new Color(32, 178, 170));
	    loginButton.setForeground(Color.WHITE);
	    loginButton.setFocusPainted(false);
	    loginButton.addActionListener(e -> cardLayout.show(cardPanel, "login"));
	    panel.add(loginButton, gbc);

	    return panel;
	}






	// Crea il pannello di login
	private JPanel createLoginPanel() {
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

	    JLabel usernameLabel = new JLabel("Username:");
	    usernameLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
	    usernameLabel.setForeground(Color.WHITE);
	    JTextField usernameField = new JTextField(20);
	    usernameField.setFont(new Font("Arial", Font.PLAIN, 18));

	    JLabel passwordLabel = new JLabel("Password:");
	    passwordLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
	    passwordLabel.setForeground(Color.WHITE);
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

	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    panel.add(usernameLabel, gbc);

	    gbc.gridx = 1;
	    panel.add(usernameField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    panel.add(passwordLabel, gbc);

	    gbc.gridx = 1;
	    panel.add(passwordField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.gridwidth = 2;
	    panel.add(loginButton, gbc);

	    return panel;
	}

	private JPanel createPlayerRegisterPanel() {
	    return createRegisterPanel("Registrazione Giocatore", new String[][]{
	            {"Nome:", "name"},
	            {"Cognome:", "surname"},
	            {"DataNascita:", "dob"},
	            {"Email:", "email"},
	            {"Username:", "username"},
	            {"Password:", "password"},
	            {"Nome Squadra:", "teamName"}
	    }, e -> {
	        // Esegui la registrazione del giocatore
	        System.out.println("Registrazione giocatore eseguita!");
	    });
	}

	private JPanel createManagerRegisterPanel() {
	    return createRegisterPanel("Registrazione Gestore", new String[][]{
	        {"Nome:", "name"},
	        {"Cognome:", "surname"},
	        {"DataNascita:", "dob"},
	        {"Email:", "email"},
	        {"Username:", "username"},
	        {"Password:", "password"},
	        {"Certificazioni:", "certifications"},
	        {"Competenze:", "competences"}
	    }, e -> {
	        // Raccogli i dati dai campi di testo
	        String name = getTextFieldValue("name");
	        String surname = getTextFieldValue("surname");
	        String dob = getTextFieldValue("dob");
	        String email = getTextFieldValue("email");
	        String username = getTextFieldValue("username");
	        String password = getTextFieldValue("password");
	        String certifications = getTextFieldValue("certifications");
	        String competences = getTextFieldValue("competences");

	        Date birthDate= Date.valueOf(dob);
	       Gestore.registrazione(name, surname, birthDate, email, username, password, certifications, competences);
	    });
	}

	private String getTextFieldValue(String fieldName) {
	    // Trova il campo di testo corrispondente al fieldName
	    for (Component component : getRootPane().getComponents()) {
	        if (component instanceof JTextField) {
	            JTextField textField = (JTextField) component;
	            if (textField.getName().equals(fieldName)) {
	                return textField.getText();
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

	    JLabel titleLabel = new JLabel(title);
	    titleLabel.setFont(new Font("Montserrat", Font.BOLD, 30));
	    titleLabel.setForeground(Color.WHITE);

	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    gbc.anchor = GridBagConstraints.CENTER;
	    panel.add(titleLabel, gbc);

	    // Array per i campi di input
	    Component[] inputFields = new Component[fields.length];

	    for (int i = 0; i < fields.length; i++) {
	        JLabel label = new JLabel(fields[i][0]);
	        label.setFont(new Font("Montserrat", Font.BOLD, 20));
	        label.setForeground(Color.WHITE);

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
	        } else if ("password".equals(fields[i][1])) {
	            inputField = new JPasswordField(20);
	            inputField.setFont(new Font("Arial", Font.PLAIN, 18));
	        } else {
	            inputField = new JTextField(20);
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
	                if (selectedDate != null) {
	                    java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
	                    System.out.println("Data di nascita (SQL): " + sqlDate);
	                } else {
	                    System.out.println("Nessuna data selezionata.");
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

	    return panel;
	}
	
	private JDatePickerImpl createDatePicker() {
	    UtilDateModel model = new UtilDateModel(); // Modello per la data
	    model.setSelected(true); // Imposta la data attuale come predefinita (opzionale)

	    // Proprietà per il formato della data
	    Properties properties = new Properties();
	    properties.put("text.today", "Oggi");
	    properties.put("text.month", "Mese");
	    properties.put("text.year", "Anno");

	    // Pannello di selezione della data
	    JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);

	    // Componente del selettore di date
	    JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	    return datePicker;
	}
	public class DateLabelFormatter extends AbstractFormatter {
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parse(text);
	    }

	    @Override
	    public String valueToString(Object value) {
	        if (value instanceof java.util.Calendar) {
	            java.util.Calendar calendar = (java.util.Calendar) value;
	            return dateFormatter.format(calendar.getTime());
	        }
	        return "";
	    }
	}

/*	private JPanel createRegisterPanel(String[][] additionalFields, ActionListener registerAction) {
	    JPanel panel = new JPanel(new GridBagLayout()) {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            // Disegna lo sfondo
	            if (clearImage != null) {
	                g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
	            }
	        }
	    };
	    panel.setOpaque(false);

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5); // Margini tra i componenti
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.CENTER;

	    // Campi comuni
	    JLabel firstNameLabel = new JLabel("Nome:");
	    firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    JTextField firstNameField = new JTextField(18);
	    firstNameField.setPreferredSize(new Dimension(400, 30)); // Altezza e larghezza più piccole
	    firstNameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font più piccolo
	    panel.add(firstNameLabel, gbc);
	    gbc.gridx++;
	    panel.add(firstNameField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy++;
	    JLabel lastNameLabel = new JLabel("Cognome:");
	    lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    JTextField lastNameField = new JTextField(18);
	    lastNameField.setPreferredSize(new Dimension(400, 30)); // Altezza e larghezza più piccole
	    lastNameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font più piccolo
	    panel.add(lastNameLabel, gbc);
	    gbc.gridx++;
	    panel.add(lastNameField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy++;
	    JLabel dobLabel = new JLabel("Data di nascita:");
	    dobLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    JTextField dobField = new JTextField("gg/mm/aaaa", 18);
	    dobField.setPreferredSize(new Dimension(400, 30)); // Altezza e larghezza più piccole
	    dobField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font più piccolo
	    panel.add(dobLabel, gbc);
	    gbc.gridx++;
	    panel.add(dobField, gbc);
	    
	    gbc.gridx = 0;
	    gbc.gridy++;
	    JLabel emailLabel = new JLabel("E-mail:");
	    emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    JTextField emailField = new JTextField(18);
	    emailField.setPreferredSize(new Dimension(400, 30)); // Altezza e larghezza più piccole
	    emailField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font più piccolo
	    panel.add(emailLabel, gbc);
	    gbc.gridx++;
	    panel.add(emailField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy++;
	    JLabel usernameLabel = new JLabel("Username:");
	    usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    JTextField usernameField = new JTextField(18);
	    usernameField.setPreferredSize(new Dimension(400, 30)); // Altezza e larghezza più piccole
	    usernameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font più piccolo
	    panel.add(usernameLabel, gbc);
	    gbc.gridx++;
	    panel.add(usernameField, gbc);

	    gbc.gridx = 0;
	    gbc.gridy++;
	    JLabel passwordLabel = new JLabel("Password:");
	    passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
	    JPasswordField passwordField = new JPasswordField(18);
	    passwordField.setPreferredSize(new Dimension(400, 30)); // Altezza e larghezza più piccole
	    passwordField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font più piccolo
	    panel.add(passwordLabel, gbc);
	    gbc.gridx++;
	    panel.add(passwordField, gbc);

	    // Campi aggiuntivi specificati
	    for (String[] field : additionalFields) {
	        gbc.gridx = 0;
	        gbc.gridy++;
	        JLabel label = new JLabel(field[0]);
		    label.setFont(new Font("Arial", Font.PLAIN, 18));
	        JTextField textField = new JTextField(18);
	        textField.setPreferredSize(new Dimension(400, 30)); // Altezza e larghezza più piccole
	        textField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font più piccolo
	        textField.setName(field[1]); // Usa il nome per distinguere i campi
	        panel.add(label, gbc);
	        gbc.gridx++;
	        panel.add(textField, gbc);
	    }

	    // Pulsante di registrazione
	    gbc.gridx = 0;
	    gbc.gridy++;
	    gbc.gridwidth = 2;
	    gbc.fill = GridBagConstraints.CENTER;
	    JButton registerButton = new JButton("Register");
	    registerButton.setFont(new Font("Arial", Font.BOLD, 18)); // Font più piccolo
	    registerButton.setBackground(new Color(32, 178, 170));
	    registerButton.setForeground(Color.WHITE);
	    registerButton.setFocusPainted(false);

	    registerButton.addActionListener(registerAction);

	    panel.add(registerButton, gbc);

	    return panel;
	}


	private JPanel createPlayerRegisterPanel() {
	    return createRegisterPanel(
	        new String[][]{
	            {"Nome Squadra:", "teamName"}
	        },
	        e -> {
	            // Gestione della registrazione del giocatore
	            System.out.println("Registrazione Giocatore completata.");
	        }
	    );
	}

	private JPanel createManagerRegisterPanel() {
	    // Creazione del pannello principale
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	    // Bottone per la registrazione
	    JButton registerButton = new JButton("Register");
	    registerButton.addActionListener(e -> {
	        try {
	            // Recupera i dati dai campi (già dichiarati fuori da questo metodo)
	            String nome = nomeField.getText().trim();
	            String cognome = cognomeField.getText().trim();
	            String email = emailField.getText().trim();
	            String username = usernameField.getText().trim();
	            String password = new String(passwordField.getPassword()).trim();
	            String dataNascitaText = dataNascitaField.getText().trim();
	            String certificazioni = certificazioniField.getText().trim();
	            String competenze = competenzeField.getText().trim();

	            // Validazione dei dati
	            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || username.isEmpty() ||
	                password.isEmpty() || dataNascitaText.isEmpty() || certificazioni.isEmpty() || competenze.isEmpty()) {
	                throw new IllegalArgumentException("Tutti i campi devono essere compilati!");
	            }

	            // Conversione della data di nascita
	            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	            Date dataNascita = dateFormat.parse(dataNascitaText);

	            // Chiamata al metodo di registrazione
	            int result = Gestore.registrazione(nome, cognome, dataNascita, email, username, password, certificazioni, competenze);
	            if (result == 1) {
	                JOptionPane.showMessageDialog(null, "Registrazione completata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
	            } else {
	                JOptionPane.showMessageDialog(null, "Errore durante la registrazione!", "Errore", JOptionPane.ERROR_MESSAGE);
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
	            ex.printStackTrace();
	        }
	    });

	    // Aggiunta del bottone al pannello
	    panel.add(registerButton);
	    panel.add(Box.createVerticalStrut(10)); // Spaziatura

	    return panel;
	}
*/

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