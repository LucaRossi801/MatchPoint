package GUI;

import javax.swing.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.*;

import com.formdev.flatlaf.FlatLightLaf;

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
	protected static JPanel playerRegisterPanel; //Pannello per la vista di registrazione
	protected static JPanel managerRegisterPanel; //Pannello per la vista di registrazione
	protected Image backgroundImage;
	protected static Image clearImage;

	protected boolean isVisible; //Booleano per la visibilità della scritta lampeggiante
	protected Timer blinkTimer; //Timer per gestire il lampeggiamento
	protected int blinkInterval; //Intervallo di tempo per il lampeggiamento

	protected JButton registerButton; //Bottone per il "Register"
	protected JButton loginButton; //Bottone per il "Login"

	protected BackgroundPanel(String blurredImagePath, String clearImagePath) {
		// Imposta il tema FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf()); // Usa FlatLightLaf o FlatDarkLaf
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
		
		this.isVisible = true; //La scritta è visibile inizialmente
		this.blinkInterval = 800; //Durata della visibilità della scritta (in ms)

		//Usa CardLayout per gestire le diverse viste
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		//Crea i pannelli per le diverse viste
		homePanel = createHomePanel(blurredImagePath);
		mainPanel = createMainPanel(clearImagePath);
		loginPanel = Login.createLoginPanel();
		playerRegisterPanel = Register.createRegisterPanel("Giocatore");
		managerRegisterPanel = Register.createRegisterPanel("Gestore");
		
		//Aggiungi i pannelli al CardLayout
		cardPanel.add(homePanel, "home");
		cardPanel.add(mainPanel, "main");
		cardPanel.add(loginPanel, "login");
		cardPanel.add(playerRegisterPanel, "playerRegister");
		cardPanel.add(managerRegisterPanel, "managerRegister");
		cardPanel.add(new CreateGestorePanel(cardLayout, cardPanel), "createGestore");
		cardPanel.add(new CreateGiocatorePanel(cardLayout, cardPanel), "createGiocatore");
		
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
			    Graphics2D g2d = (Graphics2D) g;
			    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    // Font moderno e dimensione
			    Font modernFont = new Font("SansSerif", Font.BOLD, 100);
			    g2d.setFont(modernFont);

			    String matchPointText = "MATCHPOINT";
			    FontMetrics metrics = g2d.getFontMetrics(modernFont);
			    int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
			    int y = 150; // Altezza regolata per posizionare più in alto il testo

			    // Ombra sottile
			    g2d.setColor(new Color(0, 0, 0, 80)); // Nero trasparente
			    g2d.drawString(matchPointText, x + 5, y + 5);

			    // Colore principale
			    g2d.setColor(new Color(32, 178, 170)); // Verde acqua
			    g2d.drawString(matchPointText, x, y);

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
				    Graphics2D g2d = (Graphics2D) g;
				    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				    // Font moderno e dimensione
				    Font modernFont = new Font("SansSerif", Font.BOLD, 100);
				    g2d.setFont(modernFont);

				    String matchPointText = "MATCHPOINT";
				    FontMetrics metrics = g2d.getFontMetrics(modernFont);
				    int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
				    int y = 150; // Altezza regolata per posizionare più in alto il testo

				    // Ombra sottile
				    g2d.setColor(new Color(0, 0, 0, 80)); // Nero trasparente
				    g2d.drawString(matchPointText, x + 5, y + 5);

				    // Colore principale
				    g2d.setColor(new Color(32, 178, 170)); // Verde acqua
				    g2d.drawString(matchPointText, x, y);
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
	    gbc.insets = new Insets(10, 10, 10, 10); // Margini uniformi
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.weightx = 0; 
	    gbc.weighty = 0; 

	    // Bottone "Register Giocatore"
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.CENTER; // Allineamento centrale
	    Dimension largeButtonSize = new Dimension(250, 80);
	    JButton registerGiocatoreButton = createFlatButton("Register Giocatore", 
	        e -> cardLayout.show(cardPanel, "playerRegister"), largeButtonSize);
	    panel.add(registerGiocatoreButton, gbc);

	    // Bottone "Register Gestore"
	    gbc.gridx = 1; // Colonna successiva
	    JButton registerGestoreButton = createFlatButton("Register Gestore", 
	        e -> cardLayout.show(cardPanel, "managerRegister"), largeButtonSize);
	    panel.add(registerGestoreButton, gbc);

	    // Bottone "Login"
	    gbc.gridx = 0;
	    gbc.gridy = 1; // Riga successiva
	    gbc.gridwidth = 2; // Occupa entrambe le colonne
	    JButton loginButton = createFlatButton("Login", 
	        e -> cardLayout.show(cardPanel, "login"), new Dimension(300, 90));
	    panel.add(loginButton, gbc);





	    return panel;
	}
	
	
	
	//metodo che permette di mostrare un pannello dato il nome del pannello
	protected static void showPanel(String name) {
		 try {
			cardLayout.show(cardPanel, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	// Aggiorna i pulsanti per usare il tema FlatLaf
	public static JButton createFlatButton(String text, ActionListener action, Dimension size) {
	    JButton button = new JButton(text);
	    button.addActionListener(action);

	    // Imposta stile FlatLaf
	    button.setFont(new Font("Arial", Font.BOLD, 22)); // Font più grande per i pulsanti più grandi
	    button.putClientProperty("JButton.buttonType", "roundRect"); // Tipo di bottone arrotondato
	    button.putClientProperty("JButton.backgroundColor", new Color(32, 178, 170)); // Colore sfondo
	    button.putClientProperty("JButton.hoverBackgroundColor", new Color(28, 144, 138)); // Colore al passaggio del mouse

	    button.setForeground(Color.BLACK); // Colore del testo
	    button.setFocusPainted(false); // Rimuove il bordo al click

	    button.setPreferredSize(size); // Dimensione personalizzata
	    return button;
	}


}