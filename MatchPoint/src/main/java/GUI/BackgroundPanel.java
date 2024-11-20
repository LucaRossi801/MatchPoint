package GUI;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.*;
import individui.Gestore;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

//Classe che gestisce il pannello della GUI
public class BackgroundPanel extends JPanel {
	protected static CardLayout cardLayout;
	protected static JPanel cardPanel;
	protected JPanel homePanel; //Pannello per la homepage
	protected JPanel mainPanel; //Pannello per la vista principale (con immagine nitida e bottoni)
	protected static JPanel loginPanel; //Pannello per la vista di login
	protected JPanel playerRegisterPanel; //Pannello per la vista di registrazione
	protected JPanel managerRegisterPanel; //Pannello per la vista di registrazione

	protected Image backgroundImage;
	protected static Image clearImage;

	protected boolean isVisible; //Booleano per la visibilità della scritta lampeggiante
	protected Timer blinkTimer; //Timer per gestire il lampeggiamento
	protected int blinkInterval; //Intervallo di tempo per il lampeggiamento

	protected JButton registerButton; //Bottone per il "Register"
	protected JButton loginButton; //Bottone per il "Login"

	protected BackgroundPanel(String blurredImagePath, String clearImagePath) {
		this.isVisible = true; //La scritta è visibile inizialmente
		this.blinkInterval = 800; //Durata della visibilità della scritta (in ms)

		//Usa CardLayout per gestire le diverse viste
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		//Crea i pannelli per le diverse viste
		homePanel = createHomePanel(blurredImagePath);
		mainPanel = createMainPanel(clearImagePath);
		loginPanel = RegisterLogin.createLoginPanel();
		playerRegisterPanel = createPlayerRegisterPanel();
		managerRegisterPanel = createManagerRegisterPanel();

		//Aggiungi i pannelli al CardLayout
		cardPanel.add(homePanel, "home");
		cardPanel.add(mainPanel, "main");
		cardPanel.add(loginPanel, "login");
		cardPanel.add(playerRegisterPanel, "playerRegister");
		cardPanel.add(managerRegisterPanel, "managerRegister");

		//Imposta la vista iniziale come la homepage
		cardLayout.show(cardPanel, "home");

		//Imposta il layout del pannello principale
		setLayout(new BorderLayout());
		add(cardPanel, BorderLayout.CENTER);

		//Imposta la dimensione iniziale del pannello
		setPreferredSize(new Dimension(800, 600)); 
	}

	//Crea il pannello della homepage con immagine sfocata e scritta lampeggiante
	private JPanel createHomePanel(String blurredImagePath) {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				//Disegna l'immagine sfocata
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} else {
					System.out.println("Immagine sfocata non trovata!");
				}

				//Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero
				g.setColor(Color.BLACK); //Contorno nero
				g.setFont(new Font("Impact", Font.BOLD, 70)); 
				String matchPointText = "MATCHPOINT";
				FontMetrics metrics = g.getFontMetrics();
				int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
				int y = 100;

				//Contorno nero per la scritta
				g.drawString(matchPointText, x - 2, y - 2);
				g.drawString(matchPointText, x + 2, y - 2);
				g.drawString(matchPointText, x - 2, y + 2);
				g.drawString(matchPointText, x + 2, y + 2);

				//Scritta principale in verde acqua
				g.setColor(new Color(32, 178, 170));
				g.drawString(matchPointText, x, y);

				//Scritta "Clicca ovunque per continuare"
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

		//Carica l'immagine sfocata
		URL blurredImageUrl = getClass().getResource(blurredImagePath);
		if (blurredImageUrl != null) {
			this.backgroundImage = new ImageIcon(blurredImageUrl).getImage();
		} else {
			System.out.println("Errore nel caricamento dell'immagine : " + blurredImagePath);
		}

		//Timer per il lampeggiamento della scritta
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
				cardLayout.show(cardPanel, "main"); //Passa alla vista principale
			}
		});

		return panel;
	}
	
	//Crea il pannello principale
	private JPanel createMainPanel(String clearImagePath) {
	    JPanel panel = new JPanel(new GridBagLayout()) {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);

	            //Disegna l'immagine nitida
	            if (clearImage != null) {
	                g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
	             //Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero
					g.setColor(Color.BLACK); // Contorno nero
					g.setFont(new Font("Impact", Font.BOLD, 70));
					String matchPointText = "MATCHPOINT";
					FontMetrics metrics = g.getFontMetrics();
					int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
					int y = 100;

					//Contorno nero per la scritta
					g.drawString(matchPointText, x - 2, y - 2);
					g.drawString(matchPointText, x + 2, y - 2);
					g.drawString(matchPointText, x - 2, y + 2);
					g.drawString(matchPointText, x + 2, y + 2);

					//Scritta principale in verde acqua
					g.setColor(new Color(32, 178, 170));
					g.drawString(matchPointText, x, y);
	            }
	        }
	    };

	    //Carica l'immagine nitida
	    URL clearImageUrl = getClass().getResource(clearImagePath);
	    if (clearImageUrl != null) {
	        this.clearImage = new ImageIcon(clearImageUrl).getImage();
	    } else {
	        System.out.println("Errore nel caricamento dell'immagine : " + clearImagePath);
	    }
	    
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(10, 10, 10, 10); //Spaziatura tra i componenti
	    gbc.fill = GridBagConstraints.HORIZONTAL; //I pulsanti si allargano orizzontalmente
	    gbc.gridx = 0;
	    gbc.gridy = 0;

	    //Bottone "Register Giocatore"
	    JButton registerGiocatoreButton = new JButton("Register Giocatore");
	    registerGiocatoreButton.setFont(new Font("Arial", Font.BOLD, 20));
	    registerGiocatoreButton.setBackground(new Color(32, 178, 170));
	    registerGiocatoreButton.setForeground(Color.WHITE);
	    registerGiocatoreButton.setFocusPainted(false);
	    registerGiocatoreButton.addActionListener(e -> cardLayout.show(cardPanel, "playerRegister"));
	    panel.add(registerGiocatoreButton, gbc);

	    //Bottone "Register Gestore"
	    gbc.gridx = 1; // Passa alla colonna successiva
	    JButton registerGestoreButton = new JButton("Register Gestore");
	    registerGestoreButton.setFont(new Font("Arial", Font.BOLD, 20));
	    registerGestoreButton.setBackground(new Color(32, 178, 170));
	    registerGestoreButton.setForeground(Color.WHITE);
	    registerGestoreButton.setFocusPainted(false);
	    registerGestoreButton.addActionListener(e -> cardLayout.show(cardPanel, "managerRegister"));
	    panel.add(registerGestoreButton, gbc);

	    //Bottone "Login"
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
	
	//creazione del bottone back di register/login
	protected static JButton createBackButton() {
	    JButton backButton = new JButton("Back");
	    backButton.setFont(new Font("Arial", Font.BOLD, 18));
	    backButton.setBackground(new Color(32, 178, 170));
	    backButton.setForeground(Color.WHITE);
	    backButton.setFocusPainted(false);
	    backButton.addActionListener(e -> cardLayout.show(cardPanel, "main"));
	    return backButton;
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
	        String dob = getDatePicker("dob");
	        String email = getTextFieldValue("email");
	        String username = getTextFieldValue("username");
	        String password = getTextFieldValue("password");
	        String certifications = getTextFieldValue("certifications");
	        String competences = getTextFieldValue("competences");
	       Date birthDate= Date.valueOf("2003-08-01");
	       Gestore.registrazione(name, surname, birthDate, email, username, password, certifications, competences);
	    });
	}

	private String getTextFieldValue(String fieldName) {
	    for (Component component : getRootPane().getComponents()) {
	        if (component instanceof JTextField) {
	            JTextField textField = (JTextField) component;
	            if (textField.getName().equals(fieldName)) {
	            	if(textField.getText().equals("")) {
	            		System.out.println(textField.getName());
	    	            JOptionPane.showMessageDialog(managerRegisterPanel, "Tutti i campi devono essere riempiti!", "Errore", JOptionPane.ERROR_MESSAGE);
	            	}
	            	System.out.println(textField.getName());
	            }
	            else {
	            	return textField.getText();
	            }
	        }
	    }
	    return "";  // Stringa vuota se il campo non viene trovato
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
	    }}
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
	                	JOptionPane.showMessageDialog(managerRegisterPanel, "Tutti i campi devono essere riempiti!", "Errore", JOptionPane.ERROR_MESSAGE);	                } else {
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
	    panel.add(createBackButton(), gbc);


	    return panel;
	}
	
	//Creazione del calendario per data di nascita
	private JDatePickerImpl createDatePicker() {
	    UtilDateModel model = new UtilDateModel(); //Modello per la data
	    
	    //Proprietà per il formato della data
	    Properties properties = new Properties();
	    properties.put("text.day", "Giorno");
	    properties.put("text.month", "Mese");
	    properties.put("text.year", "Anno");

	    //Pannello di selezione della data
	    JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);

	    //Componente del selettore di date
	    JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
	    return datePicker;
	}
	
	//metodo che permette di mostrare un pannello dato il nome del pannello
	protected static void showPanel(String name) {
		 try {
			cardLayout.show(cardPanel, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
public class DateLabelFormatter extends AbstractFormatter {
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

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
	        return "Seleziona da calendario";
	    }
	}

}