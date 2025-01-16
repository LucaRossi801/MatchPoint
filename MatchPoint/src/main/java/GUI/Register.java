package GUI;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.NavigationFilter;

import org.jdesktop.swingx.JXDatePicker; // Assicurati di aggiungere questa libreria
import org.jdesktop.swingx.JXMonthView;

import dataBase.DataBase;
import individui.Gestore;
import individui.Giocatore;

/**
 * Classe Register che fornisce un pannello grafico per la registrazione di
 * utenti. Gestisce la registrazione di Gestori e Giocatori con validazioni e
 * salvataggio nel database.
 */
public class Register {

	/**
     * Crea un pannello di registrazione specifico per la tipologia di utente.
     *
     * @param tipologia La tipologia di utente da registrare ("Gestore" o "Giocatore").
     * @return Il pannello di registrazione configurato.
     */
    protected static JPanel createRegisterPanel(String tipologia) {
        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db"; // connessione al database
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
        gbc.insets = new Insets(10, 10, 10, 10); // Margini tra i componenti
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new OutlinedLabel("Registrazione " + tipologia, Color.WHITE);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 30));

        // Aggiungi il titolo in alto al centro
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Il titolo occupa due colonne
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Struttura dati per mappare i campi di input
        Map<String, JComponent> fields = new HashMap<>();

        String[] campi;
        if (tipologia.equals("Gestore")) {
            campi = new String[] { "Nome", "Cognome", "DataNascita", "Email", "Username", "Password", "Certificazione",
                    "Competenze" };
        } else {
            campi = new String[] { "Nome", "Cognome", "DataNascita", "Email", "Username", "Password", "NomeSquadra" };
        }

        gbc.gridwidth = 1; // Ogni etichetta e campo occupano una colonna
        gbc.anchor = GridBagConstraints.WEST; // Allineamento a sinistra
        int row = 1; // La riga parte da 1 (sotto il titolo)

        for (String campo : campi) {
            JLabel label = new OutlinedLabel(campo + ":", Color.BLACK);
            label.setFont(new Font("Montserrat", Font.BOLD, 24));
            gbc.gridx = 0;
            gbc.gridy = row;
            panel.add(label, gbc);

            JComponent inputField = null;

            if (campo.equals("Password")) {
                inputField = new JPasswordField(20);
                addCharacterLimit((JPasswordField) inputField, 20, panel);
            } else if (campo.equals("DataNascita")) {
                // Creazione del JXDatePicker con la selezione dell'anno tramite JComboBox
                JPanel datePanel =YearSelectorDatePicker.createDatePicker();
                inputField = datePanel;  // Usa il pannello con il JXDatePicker e JComboBox
            } else {
                inputField = new JTextField(20);
                int maxLength;

                // Utilizza lo switch tradizionale
                switch (campo) {
                case "Nome":
                case "Cognome":
                case "Username":
                case "NomeSquadra":
                case "Certificazione":
                case "Competenze":
                    maxLength = 20;
                    break;
                case "Email":
                    maxLength = 40;
                    break;
                default:
                    maxLength = 20; // Default per gli altri campi
                    break;
                }

                addCharacterLimit((JTextField) inputField, maxLength, panel);
            }

            inputField.setFont(new Font("Arial", Font.PLAIN, 18));
            gbc.gridx = 1;
            panel.add(inputField, gbc);

            fields.put(campo, inputField);

            row++;
        }

		// Pulsante di registrazione
		JButton registerButton = BackgroundPanel.createFlatButton("Register", e -> {
			try {
				// Leggi i valori dai campi
				String name = ((JTextField) fields.get("Nome")).getText().trim();
				String surname = ((JTextField) fields.get("Cognome")).getText().trim();
				String birthDate = null;

				JXDatePicker datePicker = (JXDatePicker) fields.get("DataNascita");
				if (datePicker.getDate() != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					birthDate = sdf.format(datePicker.getDate());
				} else {
					CustomMessage.show("Compilare la data di nascita!", "Errore", false);
					return;
				}

				String email = ((JTextField) fields.get("Email")).getText().trim();
				String username = ((JTextField) fields.get("Username")).getText().trim();
				String password = new String(((JPasswordField) fields.get("Password")).getPassword()).trim();
				String certifications = tipologia.equals("Gestore")
						? ((JTextField) fields.get("Certificazione")).getText().trim()
						: null;
				String competences = tipologia.equals("Gestore")
						? ((JTextField) fields.get("Competenze")).getText().trim()
						: null;

				// Controlla se ci sono campi vuoti
				if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()
						|| (tipologia.equals("Gestore") && (certifications.isEmpty() || competences.isEmpty()))) {
					CustomMessage.show("Tutti i campi devono essere compilati!", "Errore", false);
					return;
				}

				// Controlla se lo username è già presente nel database
				String sql = "SELECT Password FROM Gestore WHERE Username ='" + username
						+ "' UNION SELECT Password FROM Giocatore WHERE Username ='" + username + "'";
				String ris = "";
				try (Connection conn = DriverManager.getConnection(url)) {
					ris = DataBase.eseguiSelect(conn, sql);
				} catch (SQLException ex) {
					ex.printStackTrace();
					CustomMessage.show("Errore di connessione al DataBase", "Errore", false);
					return;
				}

				if (!ris.isEmpty()) {
					CustomMessage.show("Lo username '" + username + "' è già in uso!", "Errore", false);
					return;
				}

				// Controlla se l'email è già presente nel database
				String emailSql = "SELECT Email FROM Gestore WHERE Email ='" + email
						+ "' UNION SELECT Email FROM Giocatore WHERE Email ='" + email + "'";
				String emailRis = "";
				try (Connection conn = DriverManager.getConnection(url)) {
					emailRis = DataBase.eseguiSelect(conn, emailSql);
				} catch (SQLException ex) {
					ex.printStackTrace();
					CustomMessage.show("Errore di connessione al DataBase", "Errore", false);
					return;
				}

				if (!emailRis.isEmpty()) {
					CustomMessage.show("L'email '" + email + "' è già in uso!", "Errore", false);
					return;
				}

				// Chiama il metodo di registrazione per i Gestori o Giocatori
				if (tipologia.equals("Gestore")) {
					Gestore.registrazione(name, surname, birthDate, email, username, password, certifications,
							competences, null);
				} else {
					String teamName = ((JTextField) fields.get("NomeSquadra")).getText().trim();
					if (teamName.isEmpty()) {
						CustomMessage.show("Tutti i campi devono essere compilati!", "Errore", false);
						return;
					}
					try (Connection conn = DriverManager.getConnection(url)) {
					    Giocatore.registrazione(conn, name, surname, birthDate, email, username, password, teamName);
					}
				}

				BackgroundPanel.showPanel("login");
			} catch (Exception ex) {
				CustomMessage.show("Errore durante la registrazione", "Errore", false);
			}

			// Pulizia campi
			resetFields(fields);
		}, new Dimension(300, 50));

		registerButton.setFont(new Font("Arial", Font.BOLD, 18));
		registerButton.setBackground(new Color(32, 178, 170));
		registerButton.setForeground(new Color(220, 250, 245));
		registerButton.setFocusPainted(false);

		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(registerButton, gbc);

		// Pulsante "Back"
		gbc.gridy = row + 1;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		panel.add(createBackButton(fields), gbc);

		return panel;
	}

	/**
	 * Crea un pulsante "Back" che resetta i campi e torna al pannello principale.
	 *
	 * @param fields Una mappa contenente i campi di input del form di
	 *               registrazione.
	 * @return Il pulsante "Back" configurato.
	 */
	protected static JButton createBackButton(Map<String, JComponent> fields) {
		// Usa createFlatButton per creare il pulsante "Back"
		JButton backButton = BackgroundPanel.createFlatButton("Back", // Testo del pulsante
				e -> {
					// Pulizia campi
					resetFields(fields);

					// Cambia pannello
					BackgroundPanel.showPanel("main");
				}, new Dimension(120, 30) // Dimensione del pulsante
		);

		// Personalizzazioni specifiche per il pulsante "Back"
		backButton.setForeground(Color.GRAY); // Testo grigio
		backButton.setBackground(Color.DARK_GRAY); // Sfondo scuro
		backButton.setFont(new Font("Arial", Font.BOLD, 18));

		return backButton;
	}

	/**
	 * Formatter per i campi di data nel formato "dd-MM-yyyy".
	 */
	public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
		private String datePattern = "dd-MM-yyyy"; // Cambia il formato a dd-MM-yyyy
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		/**
		 * Converte una stringa in un oggetto Date.
		 *
		 * @param text La stringa rappresentante la data.
		 * @return Un oggetto Date.
		 * @throws ParseException Se la stringa non rispetta il formato.
		 */
		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parse(text); // Converte la stringa in un oggetto Date
		}

		/**
		 * Converte un oggetto Date in una stringa nel formato "dd-MM-yyyy".
		 *
		 * @param value L'oggetto Date.
		 * @return La stringa rappresentante la data.
		 */
		@Override
		public String valueToString(Object value) {
			if (value != null) {
				if (value instanceof GregorianCalendar) {
					GregorianCalendar calendar = (GregorianCalendar) value;
					return dateFormatter.format(calendar.getTime()); // Usa il formato dd-MM-yyyy
				}
			}
			return "Seleziona da calendario";
		}

	}

	/**
	 * Resetta tutti i campi di input in una mappa fornita.
	 *
	 * @param fields Una mappa con i campi di input (JTextField, JPasswordField,
	 *               JXDatePicker).
	 */
	public static void resetFields(Map<String, JComponent> fields) {
		fields.forEach((key, field) -> {
			if (field instanceof JTextField) {
				((JTextField) field).setText(""); // Resetta il testo
			} else if (field instanceof JPasswordField) {
				((JPasswordField) field).setText(""); // Resetta la password
			} else if (field instanceof JXDatePicker) {
				((JXDatePicker) field).setDate(null); // Resetta la data
			}
		});
	}

	/**
	 * Aggiunge un limite massimo di caratteri a un campo di input JTextField.
	 * Mostra un messaggio di avviso se si supera il limite.
	 *
	 * @param field          Il JTextField a cui applicare il limite.
	 * @param maxLength      Il numero massimo di caratteri consentiti.
	 * @param containerPanel Il pannello contenitore per posizionare l'etichetta di
	 *                       avviso.
	 */
	private static void addCharacterLimit(JTextField field, int maxLength, JPanel containerPanel) {
		// Crea la JLabel per il messaggio di avviso
		JLabel warningLabel = new JLabel();
		warningLabel.setForeground(Color.RED);
		warningLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		warningLabel.setPreferredSize(new Dimension(200, 20)); // Imposta una larghezza fissa per la label
		warningLabel.setHorizontalAlignment(SwingConstants.LEFT);
		warningLabel.setVisible(false); // Nascondi inizialmente

		// Usa un layout null per avere un controllo assoluto sulla posizione
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null); // Layout assoluto
		fieldPanel.setOpaque(false); // Non colorare il pannello

		// Imposta la posizione e la dimensione della JTextField
		field.setBounds(0, 0, 200, 30); // Cambia queste dimensioni e posizione in base alle tue esigenze
		fieldPanel.add(field); // Aggiungi la JTextField al pannello

		// Imposta la posizione e la dimensione della JLabel
		warningLabel.setBounds(210, 5, 200, 20); // Cambia queste coordinate per regolare la posizione della scritta
		fieldPanel.add(warningLabel); // Aggiungi la JLabel al pannello

		// Configura il layout principale per il pannello
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1; // Posiziona nella colonna giusta
		gbc.gridy = GridBagConstraints.RELATIVE; // Riga successiva
		gbc.fill = GridBagConstraints.HORIZONTAL; // Riempie orizzontalmente
		gbc.insets = new Insets(5, 0, 5, 0); // Imposta margini
		containerPanel.add(fieldPanel, gbc); // Aggiungi il pannello al container

		// Listener per limitare i caratteri
		field.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {
				if (field.getText().length() >= maxLength) {
					e.consume(); // Ignora ulteriori input
					warningLabel.setText("Massimo " + maxLength + " caratteri!");
					warningLabel.setVisible(true);

					// Nascondi la scritta dopo 1.5 secondi
					Timer timer = new Timer(1500, evt -> warningLabel.setVisible(false));
					timer.setRepeats(false);
					timer.start();
				} else {
					warningLabel.setVisible(false); // Nascondi se sotto il limite
				}
			}
		});
	}
	
	public static class YearSelectorDatePicker {
		 public static JPanel createDatePicker() {
			        JPanel datePanel = new JPanel();
			        datePanel.setLayout(new BorderLayout());

			        // Crea il JXDatePicker
			        JXDatePicker datePicker = new JXDatePicker();
			        datePicker.setFont(new Font("Arial", Font.PLAIN, 18));
			        datePicker.setFormats("dd-MM-yyyy");

			        // Imposta la data corrente come predefinita
			        LocalDateTime oraCorrente = LocalDateTime.now();
			        Date today = Date.from(oraCorrente.atZone(ZoneId.systemDefault()).toInstant());
			        datePicker.setDate(today);

			        // Imposta il limite massimo sulla data
			        datePicker.getMonthView().setUpperBound(today);

			        // Crea la JComboBox per gli anni
			        JComboBox<Integer> yearComboBox = new JComboBox<>();
			        int currentYear = oraCorrente.getYear();
			        for (int year = currentYear; year >= 1900; year--) {
			            yearComboBox.addItem(year);
			        }

			        // Imposta l'anno corrente come selezione predefinita
			        yearComboBox.setSelectedItem(currentYear);

			        // Listener per cambiare l'anno selezionato
			        yearComboBox.addActionListener(e -> {
			            int selectedYear = (int) yearComboBox.getSelectedItem();
			            Calendar calendar = Calendar.getInstance();
			            Date currentDate = datePicker.getDate();

			            if (currentDate != null) {
			                calendar.setTime(currentDate);
			            } else {
			                calendar.setTime(today); // Fallback alla data odierna se nulla
			            }

			            // Aggiorna l'anno mantenendo il giorno e mese attuali
			            calendar.set(Calendar.YEAR, selectedYear);

			            // Se la data supera il limite massimo, impostala al massimo consentito
			            Date updatedDate = calendar.getTime();
			            if (updatedDate.after(today)) {
			                updatedDate = today;
			            }

			            datePicker.setDate(updatedDate);
			        });

			        // Aggiungi i componenti al pannello
			        datePanel.add(yearComboBox, BorderLayout.NORTH);
			        datePanel.add(datePicker, BorderLayout.CENTER);

			        return datePanel;}
		}
	}
