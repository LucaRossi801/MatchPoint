package GUI;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import dataBase.DataBase;

import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Classe che gestisce il pannello principale della GUI, utilizzando un layout a
 * schede (CardLayout) per navigare tra le diverse schermate dell'applicazione.
 */
public class BackgroundPanel extends JPanel {
	protected static CardLayout cardLayout; // Gestore di layout a schede per la navigazione tra i pannelli.
	protected static JPanel cardPanel; // Pannello contenitore per i diversi pannelli.
	protected JPanel homePanel; // Pannello per la homepage.
	protected JPanel mainPanel; // Pannello per la vista principale (con immagine nitida e bottoni).
	protected static JPanel loginPanel; // Pannello per la vista di login.
	protected static JPanel playerRegisterPanel; // Pannello per la registrazione dei giocatori.
	protected static JPanel managerRegisterPanel; // Pannello per la registrazione dei gestori.
	protected Image backgroundImage; // Immagine di sfondo per il pannello home.
	protected static Image clearImage; // Immagine nitida per il pannello principale.

	protected boolean isVisible; // Stato per la visibilità della scritta lampeggiante.
	protected Timer blinkTimer; // Timer per gestire il lampeggiamento della scritta.
	protected int blinkInterval; // Intervallo di tempo per il lampeggiamento.

	protected JButton registerButton; // Bottone per il "Register"
	protected JButton loginButton; // Bottone per il "Login"

	/**
	 * Costruttore della classe BackgroundPanel.
	 *
	 * @param blurredImagePath Il percorso dell'immagine sfocata per la homepage.
	 * @param clearImagePath   Il percorso dell'immagine nitida per il pannello
	 *                         principale.
	 */
	protected BackgroundPanel(String blurredImagePath, String clearImagePath) {
		// Imposta il tema FlatLaf
		try {
			UIManager.setLookAndFeel(new FlatLightLaf()); // Usa FlatLightLaf o FlatDarkLaf
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Usa CardLayout per gestire le diverse viste
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);

		// Crea i pannelli per le diverse viste
		homePanel = createHomePanel(blurredImagePath);
		mainPanel = createMainPanel(clearImagePath);
		loginPanel = Login.createLoginPanel();
		playerRegisterPanel = Register.createRegisterPanel("Giocatore");
		managerRegisterPanel = Register.createRegisterPanel("Gestore");

		// Aggiungi i pannelli al CardLayout
		cardPanel.add(homePanel, "home");
		cardPanel.add(mainPanel, "main");
		cardPanel.add(loginPanel, "login");
		cardPanel.add(playerRegisterPanel, "playerRegister");
		cardPanel.add(managerRegisterPanel, "managerRegister");
		cardPanel.add(new CreateGestorePanel(cardLayout, cardPanel), "createGestore");
		cardPanel.add(new InserisciPrenotazionePanel(cardLayout, cardPanel), "inserisciPrenotazione");
		cardPanel.add(new CreateGiocatorePanel(cardLayout, cardPanel), "createGiocatore");
		cardPanel.add(new InserisciCentroPanel(cardLayout, cardPanel), "inserisciCentro");

		// Imposta la vista iniziale come la homepage
		cardLayout.show(cardPanel, "home");

		// Imposta il layout del pannello principale
		setLayout(new BorderLayout());
		add(cardPanel, BorderLayout.CENTER);
	}

	/**
	 * Crea il pannello della homepage con immagine sfocata e scritta lampeggiante.
	 *
	 * @param blurredImagePath Il percorso dell'immagine sfocata.
	 * @return Il pannello della homepage.
	 */
	private JPanel createHomePanel(String blurredImagePath) {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// Disegna l'immagine sfocata
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				} else {
					CustomMessage.show("Immagine sfocata non trovata!", "Errore", false);
				}

				// Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero e stile
				// moderno
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				// Font moderno e dimensione
				Font modernFont = new Font("SansSerif", Font.BOLD, 100);
				g2d.setFont(modernFont);

				String matchPointText = "MATCHPOINT";
				FontMetrics metrics = g2d.getFontMetrics(modernFont);
				int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
				int y = 150; // Posizione in altezza

				// Contorno nero
				g2d.setColor(new Color(0, 0, 0, 120)); // Nero con maggiore opacità
				g2d.drawString(matchPointText, x + 6, y + 6);

				// Gradiente moderno per il testo
				GradientPaint gradient = new GradientPaint(x, y, new Color(50, 220, 210),
						x + metrics.stringWidth(matchPointText), y, new Color(20, 150, 140) // Sfumatura
				);
				g2d.setPaint(gradient);
				g2d.drawString(matchPointText, x, y);

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
			CustomMessage.show("Errore nel caricamento dell'immagine : " + blurredImagePath, "Errore", false);
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

	/**
	 * Crea il pannello principale con immagine nitida e bottoni per la navigazione.
	 *
	 * @param clearImagePath Il percorso dell'immagine nitida.
	 * @return Il pannello principale.
	 */
	private JPanel createMainPanel(String clearImagePath) {
		JPanel panel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// Disegna l'immagine nitida
				if (clearImage != null) {
					g.drawImage(clearImage, 0, 0, getWidth(), getHeight(), this);
					// Disegna la scritta "MATCHPOINT" in alto, centrata con contorno nero e stile
					// moderno
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

					// Font moderno e dimensione
					Font modernFont = new Font("SansSerif", Font.BOLD, 100);
					g2d.setFont(modernFont);

					String matchPointText = "MATCHPOINT";
					FontMetrics metrics = g2d.getFontMetrics(modernFont);
					int x = (getWidth() - metrics.stringWidth(matchPointText)) / 2;
					int y = 150; // Altezza regolata per posizionare più in alto il testo

					// Ombra più visibile
					g2d.setColor(new Color(0, 0, 0, 120)); // Nero con maggiore opacità
					g2d.drawString(matchPointText, x + 6, y + 6);

					// Gradiente moderno per il testo
					GradientPaint gradient = new GradientPaint(x, y, new Color(50, 220, 210), // Colore più deciso
							x + metrics.stringWidth(matchPointText), y, new Color(20, 150, 140) // Sfumatura
					);
					g2d.setPaint(gradient);
					g2d.drawString(matchPointText, x, y);
				}
			}
		};

		// Carica l'immagine nitida
		URL clearImageUrl = getClass().getResource(clearImagePath);
		if (clearImageUrl != null) {
			this.clearImage = new ImageIcon(clearImageUrl).getImage();
		} else {
			CustomMessage.show("Errore nel caricamento dell'immagine : " + clearImagePath, "Errore", false);
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Margini uniformi
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0;
		gbc.weighty = 0;

		// Spazio vuoto (utilizzando un pannello invisibile)
		JPanel spacer = new JPanel();
		spacer.setOpaque(false); // Rende il pannello trasparente
		spacer.setPreferredSize(new Dimension(1, 400)); // Altezza dello spazio
		gbc.gridx = 0;
		gbc.gridy = 0; // Prima riga
		gbc.gridwidth = 2; // Occupa entrambe le colonne
		panel.add(spacer, gbc);

		// Bottone "Register Giocatore"
		gbc.gridx = 0;
		gbc.gridy = 1; // Riga successiva
		gbc.gridwidth = 1; // Reset larghezza
		gbc.anchor = GridBagConstraints.CENTER; // Allineamento centrale
		Dimension largeButtonSize = new Dimension(250, 80);
		JButton registerGiocatoreButton = createFlatButton("Register Giocatore",
				e -> cardLayout.show(cardPanel, "playerRegister"), largeButtonSize);
		panel.add(registerGiocatoreButton, gbc);

		// Bottone "Register Gestore"
		gbc.gridx = 1; // Colonna successiva
		gbc.gridy = 1;
		JButton registerGestoreButton = createFlatButton("Register Gestore",
				e -> cardLayout.show(cardPanel, "managerRegister"), largeButtonSize);
		panel.add(registerGestoreButton, gbc);

		// Bottone "Login"
		gbc.gridx = 0;
		gbc.gridy = 2; // Riga successiva
		gbc.gridwidth = 2; // Occupa entrambe le colonne
		JButton loginButton = createFlatButton("Login", e -> cardLayout.show(cardPanel, "login"),
				new Dimension(250, 80));
		panel.add(loginButton, gbc);

		// Bottone "Quit"
		gbc.gridx = 0;
		gbc.gridy = 3; // Riga successiva sotto il bottone "Login"
		gbc.gridwidth = 2; // Centrare il bottone
		JButton quitButton = createFlatButton("Quit", e -> System.exit(0), new Dimension(300, 40)); // Azione per
																									// chiudere
																									// l'applicazione
		quitButton.setForeground(Color.GRAY); // Sfondo grigio
		quitButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
		panel.add(quitButton, gbc);

		return panel;
	}

	/**
	 * Mostra un pannello specifico dato il suo nome.
	 *
	 * @param name Il nome del pannello da mostrare.
	 */
	protected static void showPanel(String name) {
		try {
			cardLayout.show(cardPanel, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea un bottone con stile FlatLaf.
	 *
	 * @param text   Il testo del bottone.
	 * @param action L'azione da eseguire al click.
	 * @param size   La dimensione del bottone.
	 * @return Il bottone creato.
	 */
	public static JButton createFlatButton(String text, ActionListener action, Dimension size) {
		JButton button = new JButton(text);
		button.addActionListener(action);

		// Imposta stile FlatLaf
		button.setFont(new Font("Arial", Font.BOLD, 22)); // Font più grande per i pulsanti più grandi
		button.putClientProperty("JButton.buttonType", "roundRect"); // Tipo di bottone arrotondato
		button.setBackground(new Color(32, 178, 170)); // Colore sfondo
		button.putClientProperty("JButton.hoverBackgroundColor", new Color(28, 144, 138)); // Colore al passaggio del
																							// mouse

		button.setForeground(new Color(220, 250, 245)); // Colore del testo
		button.setFocusPainted(false); // Rimuove il bordo al click

		button.setPreferredSize(size); // Dimensione personalizzata
		return button;
	}

}