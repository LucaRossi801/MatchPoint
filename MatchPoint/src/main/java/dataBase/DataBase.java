package dataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;
import components.TipologiaCampo;

/**
 * Classe DataBase
 * <p>
 * Fornisce metodi per l'interazione con il database SQLite, inclusi CRUD per
 * utenti, centri sportivi, campi, prenotazioni e altre entità.
 * </p>
 */
public class DataBase {

	/**
	 * Inserisce un nuovo gestore nella tabella Gestore.
	 *
	 * @param conn           Connessione al database.
	 * @param nome           Nome del gestore.
	 * @param cognome        Cognome del gestore.
	 * @param data           Data di nascita del gestore.
	 * @param eta            Età del gestore.
	 * @param email          Email del gestore.
	 * @param username       Username del gestore.
	 * @param password       Password del gestore.
	 * @param certificazioni Certificazioni del gestore.
	 * @param competenze     Competenze del gestore.
	 * @throws SQLException In caso di errore nella query SQL.
	 */
	public static void insert(Connection conn, String nome, String cognome, String data, int eta, String email,
			String username, String password, String certificazioni, String competenze) throws SQLException {
		String sql = "INSERT INTO Gestore(Nome, Cognome, DataNascita, Eta, Email, Username, Password, Certificazione, Competenze) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nome);
			pstmt.setString(2, cognome);
			pstmt.setString(3, data);
			pstmt.setInt(4, eta);
			pstmt.setString(5, email);
			pstmt.setString(6, username);
			pstmt.setString(7, password);
			pstmt.setString(8, certificazioni);
			pstmt.setString(9, competenze);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Ottiene l'ID di un utente dal database.
	 *
	 * @param username  Username dell'utente.
	 * @param tipologia Tipologia dell'utente (es. "Gestore", "Giocatore").
	 * @return L'ID dell'utente o -8 in caso di errore.
	 * @throws SQLException In caso di errore nella query SQL.
	 */
	public static int getIdUtente(String username, String tipologia) throws SQLException {
		String query = "SELECT ID FROM " + tipologia + " WHERE username = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("ID"); // Restituisce l'ID dell'utente
			}
		}
		return -8;
	}

	/**
	 * Inserisce un nuovo giocatore nella tabella Giocatore.
	 *
	 * @param conn        Connessione al database.
	 * @param nome        Nome del giocatore.
	 * @param cognome     Cognome del giocatore.
	 * @param dataNascita Data di nascita del giocatore.
	 * @param eta         Età del giocatore.
	 * @param email       Email del giocatore.
	 * @param username    Username del giocatore.
	 * @param password    Password del giocatore.
	 * @param nomeSquadra Nome della squadra del giocatore.
	 * @throws SQLException In caso di errore nella query SQL.
	 */
	public static void insert(Connection conn, String nome, String cognome, String dataNascita, int eta, String email,
			String username, String password, String nomeSquadra) throws SQLException {
		String sql = "INSERT INTO Giocatore(Nome, Cognome, DataNascita, Eta, Email, Username, Password, NomeSquadra) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nome);
			pstmt.setString(2, cognome);
			pstmt.setString(3, dataNascita);
			pstmt.setInt(4, eta);
			pstmt.setString(5, email);
			pstmt.setString(6, username);
			pstmt.setString(7, password);
			pstmt.setString(8, nomeSquadra);
			pstmt.executeUpdate();
		}
	}

	/**
	 * Esegue una query SELECT sul database e restituisce un ResultSet.
	 * <p>
	 * Nota: Il chiamante è responsabile della chiusura del ResultSet restituito.
	 * </p>
	 *
	 * @param conn Connessione al database.
	 * @param sql  Query SQL da eseguire.
	 * @return Il ResultSet contenente i risultati della query.
	 * @throws SQLException In caso di errore nella query SQL.
	 */
	private static ResultSet select(Connection conn, String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql); // Il chiamante è responsabile della chiusura
	}

	/**
	 * Esegue una query SELECT e restituisce i risultati in formato stringa.
	 *
	 * @param conn Connessione al database.
	 * @param sql  Query SQL da eseguire.
	 * @return I risultati della query in formato stringa.
	 * @throws SQLException In caso di errore nella query SQL.
	 */
	public static String eseguiSelect(Connection conn, String sql) throws SQLException {
		ResultSet rs = select(conn, sql);
		StringBuilder sb = new StringBuilder();

		// Itera sulle righe del ResultSet e costruisce la stringa
		while (rs.next()) {
			// Ottieni il numero di colonne dinamicamente
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			// Concatenazione dei valori di ogni colonna della riga
			for (int i = 1; i <= columnCount; i++) {
				sb.append(rs.getString(i)); // Valore della colonna
				if (i < columnCount) {
					sb.append(" "); // Separatore tra le colonne
				}
			}
			sb.append("\n"); // Separatore tra le righe
		}

		return sb.toString().trim(); // Rimuove l'ultima nuova riga in eccesso
	}

	/**
	 * Ottiene i centri sportivi gestiti da un gestore specifico.
	 *
	 * @param gestoreId ID del gestore.
	 * @return Una mappa con i nomi dei centri come chiavi e oggetti CentroSportivo
	 *         come valori.
	 */
	public static Map<String, CentroSportivo> getCentriSportiviGestiti(int gestoreId) {
		Map<String, CentroSportivo> centri = new HashMap<>();
		String query = "SELECT ID, Nome, Provincia, Comune FROM CentroSportivo WHERE Gestore = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, gestoreId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String nome = rs.getString("Nome");
				String provincia = rs.getString("Provincia");
				String comune = rs.getString("Comune");

				// Crea l'oggetto CentroSportivo
				CentroSportivo centro = new CentroSportivo(id, nome, provincia, comune); // Gestore può essere impostato
																							// successivamente
				centri.put(nome, centro); // Usa il nome come chiave
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return centri;
	}

	/**
	 * Ottiene i campi di un centro sportivo specifico.
	 *
	 * @param centroId ID del centro sportivo.
	 * @return Una lista di oggetti Campo.
	 */
	public static List<Campo> getCampiById(int centroId) {
		List<Campo> campi = new ArrayList<>();
		String query = "SELECT * FROM Campo WHERE CentroSportivo = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, centroId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String tipologia = rs.getString("Tipologia");
				int costoOraNotturna = rs.getInt("CostoOraNotturna");
				int costoOraDiurna = rs.getInt("CostoOraDiurna");
				int lunghezza = rs.getInt("Lunghezza");
				int larghezza = rs.getInt("Larghezza");
				boolean coperto = rs.getBoolean("Coperto");

				// Crea l'oggetto CentroSportivo
				Campo campo = new Campo(id, TipologiaCampo.valueOf(tipologia), costoOraNotturna, costoOraDiurna,
						lunghezza, larghezza, coperto);// Gestore può essere impostato successivamente
				campi.add(campo); // Usa il nome come chiave
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return campi;
	}

	/**
	 * Ottiene una mappa dei campi di un centro sportivo, con descrizioni come
	 * chiavi e ID come valori.
	 *
	 * @param centroId ID del centro sportivo.
	 * @return Una mappa dei campi con descrizioni e ID.
	 */
	public static Map<String, Integer> getCampiCentroMappa(int centroId) {
		Map<String, Integer> campiMappa = new HashMap<>();
		String query = "SELECT ID, Tipologia, Lunghezza, Larghezza FROM Campo WHERE CentroSportivo = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, centroId); // Imposta l'ID del centro sportivo nella query
			ResultSet rs = stmt.executeQuery();

			// Elenco di campi recuperati dal database
			while (rs.next()) {
				int id = rs.getInt("ID");
				String tipologia = rs.getString("Tipologia");
				int lunghezza = rs.getInt("Lunghezza");
				int larghezza = rs.getInt("Larghezza");

				// Crea un nome descrittivo per il campo combinando la tipologia e le dimensioni
				String nome = String.format("%s (%dx%d)", tipologia, lunghezza, larghezza);

				// Aggiungi il nome del campo e l'ID alla mappa
				campiMappa.put(nome, id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Ritorna la mappa con nome e ID dei campi
		return campiMappa;
	}

	/**
	 * Aggiorna le informazioni di un centro sportivo.
	 *
	 * @param centroId  ID del centro sportivo.
	 * @param nome      Nuovo nome del centro.
	 * @param provincia Nuova provincia del centro.
	 * @param comune    Nuovo comune del centro.
	 * @return True se l'aggiornamento è avvenuto con successo, false altrimenti.
	 */
	public static boolean updateCentroSportivo(int centroId, String nome, String provincia, String comune) {
		String query = "UPDATE CentroSportivo SET Nome = ?, Provincia = ?, Comune = ? WHERE ID = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, nome);
			stmt.setString(2, provincia);
			stmt.setString(3, comune);
			stmt.setInt(4, centroId);

			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0; // True se l'aggiornamento è andato a buon fine

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Elimina un campo specifico da un centro sportivo.
	 *
	 * @param centroID         ID del centro sportivo.
	 * @param campoSelezionato ID del campo da eliminare.
	 * @return True se l'eliminazione è avvenuta con successo, false altrimenti.
	 */
	public static boolean eliminaCampo(int centroID, int campoSelezionato) {
		String query = "DELETE FROM Campo WHERE CentroSportivo = ? AND ID = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, centroID);
			stmt.setInt(2, campoSelezionato);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // True se almeno una riga è stata eliminata
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // In caso di errore
		}
	}

	/**
	 * Elimina un centro sportivo specifico.
	 *
	 * @param centroID ID del centro sportivo da eliminare.
	 * @return True se l'eliminazione è avvenuta con successo, false altrimenti.
	 */
	public static boolean eliminaCentro(int centroID) {
		String query = "DELETE FROM CentroSportivo WHERE ID = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, centroID);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // True se almeno una riga è stata eliminata
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // In caso di errore
		}
	}

	/**
	 * Modifica il nome di un campo in un centro sportivo.
	 *
	 * @param centroID         ID del centro sportivo.
	 * @param campoSelezionato Nome attuale del campo.
	 * @param nuovoNome        Nuovo nome del campo.
	 * @return True se l'aggiornamento è avvenuto con successo, false altrimenti.
	 */
	public boolean modificaCampo(int centroID, String campoSelezionato, String nuovoNome) {
		String query = "UPDATE Campo SET Nome = ? WHERE CentroSportivo = ? AND Nome = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setString(1, nuovoNome);
			stmt.setInt(2, centroID);
			stmt.setString(3, campoSelezionato);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // True se almeno una riga è stata aggiornata
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // In caso di errore
		}
	}

	/**
	 * Aggiunge un nuovo campo a un centro sportivo.
	 *
	 * @param centroID   ID del centro sportivo.
	 * @param nuovoCampo Nome del nuovo campo.
	 * @return True se l'inserimento è avvenuto con successo, false altrimenti.
	 */
	public boolean aggiungiCampo(int centroID, String nuovoCampo) {
		String query = "INSERT INTO Campo (CentroSportivo, Nome) VALUES (?, ?)";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setInt(1, centroID);
			stmt.setString(2, nuovoCampo);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0; // True se una riga è stata aggiunta
		} catch (SQLException e) {
			e.printStackTrace();
			return false; // In caso di errore
		}
	}

	/**
	 * Inserisce un nuovo centro sportivo nel database.
	 *
	 * @param nome      Nome del centro sportivo.
	 * @param provincia Provincia del centro sportivo.
	 * @param comune    Comune del centro sportivo.
	 * @param gestore   ID del gestore associato al centro sportivo.
	 */
	public static void insert(String nome, String provincia, String comune, int gestore) {
		String sql = "INSERT INTO CentroSportivo(Nome, Provincia, Comune, Gestore) VALUES (?, ?, ?, ?)";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nome);
			pstmt.setString(2, provincia);
			pstmt.setString(3, comune);
			pstmt.setInt(4, gestore);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserisce un nuovo campo nel database.
	 *
	 * @param tipologia        Tipologia del campo.
	 * @param costoOraNotturna Costo per ora notturna.
	 * @param costoOraDiurna   Costo per ora diurna.
	 * @param lunghezza        Lunghezza del campo.
	 * @param larghezza        Larghezza del campo.
	 * @param coperto          Stato "Coperto" del campo.
	 * @param centro           ID del centro sportivo associato.
	 */
	public static void insert(String tipologia, int CostoOraNotturna, int costoOraDiurna, int lunghezza, int larghezza,
			boolean coperto, int centro) {
		String sql = "INSERT INTO Campo(Tipologia, CostoOraNotturna, CostoOraDiurna, Lunghezza, Larghezza, Coperto, CentroSportivo) VALUES (?, ?, ?, ?, ?, ?, ?)";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, tipologia);
			pstmt.setInt(2, CostoOraNotturna);
			pstmt.setInt(3, costoOraDiurna);
			pstmt.setInt(4, lunghezza);
			pstmt.setInt(5, larghezza);
			pstmt.setBoolean(6, coperto);
			pstmt.setInt(7, centro);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Aggiorna i dettagli di un campo nel database.
	 *
	 * @param campo Oggetto Campo contenente i nuovi dettagli da aggiornare.
	 * @return True se l'aggiornamento è avvenuto con successo, false altrimenti.
	 */
	public static boolean updateCampo(Campo campo) {
		// Query per aggiornare i dettagli del campo
		String query = "UPDATE Campo SET Tipologia = ?, CostoOraNotturna = ?, CostoOraDiurna = ?, "
				+ "Lunghezza = ?, Larghezza = ?, Coperto = ? WHERE ID = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			// Imposta i parametri della query
			stmt.setString(1, campo.getTipologiaCampo().toString()); // Tipologia come stringa
			stmt.setInt(2, campo.getCostoOraNotturna()); // Costo ora notturna
			stmt.setInt(3, campo.getCostoOraDiurna()); // Costo ora diurna
			stmt.setInt(4, campo.getLunghezza()); // Lunghezza
			stmt.setInt(5, campo.getLarghezza()); // Larghezza
			stmt.setBoolean(6, campo.isCoperto()); // Coperto (booleano)
			stmt.setInt(7, campo.getId()); // ID del campo

			// Esegui l'update e verifica il numero di righe aggiornate
			int rowsUpdated = stmt.executeUpdate();
			return rowsUpdated > 0; // True se almeno una riga è stata aggiornata

		} catch (SQLException e) {
			e.printStackTrace(); // Stampa l'errore per il debug
			return false;
		}
	}

	/**
	 * Ottiene le prenotazioni associate a un campo specifico in un centro sportivo.
	 *
	 * @param centroId ID del centro sportivo.
	 * @param campoId  ID del campo sportivo.
	 * @return Lista di oggetti Prenotazione associati al campo.
	 */
	public static List<Prenotazione> getPrenotazioniByCampo(int centroId, int campoId) {
		List<Prenotazione> prenotazioni = new ArrayList<>();
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		String query = "SELECT p.ID, p.Data, p.OraInizio, p.OraFine, p.Utente, p.Campo " + "FROM Prenotazione p "
				+ "JOIN Campo c ON p.Campo = c.ID " + "WHERE c.ID = ? AND c.CentroSportivo = ?";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, campoId);
			stmt.setInt(2, centroId);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String dataString = rs.getString("Data"); // Ottieni la stringa della data

				// Definisci il formato del database (dd-MM-yyyy)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					// Parse la stringa nella data
					Date parsedDate = sdf.parse(dataString);

					// Converti la data in java.sql.Date
					java.sql.Date data = new java.sql.Date(parsedDate.getTime());

					String oraInizioString = rs.getString("OraInizio");
					String oraFineString = rs.getString("OraFine");

					// Aggiungi i secondi se non ci sono
					if (oraInizioString.length() == 5) {
						oraInizioString += ":00"; // Aggiungi ":00" per i secondi
					}
					if (oraFineString.length() == 5) {
						oraFineString += ":00"; // Aggiungi ":00" per i secondi
					}

					Time oraInizio = Time.valueOf(oraInizioString);
					Time oraFine = Time.valueOf(oraFineString);

					int utenteID = rs.getInt("Utente");
					int campoID = rs.getInt("Campo");

					// Crea l'oggetto Prenotazione
					Prenotazione prenotazione = new Prenotazione(id, data, oraInizio, oraFine, utenteID, campoID);
					prenotazioni.add(prenotazione);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prenotazioni;
	}

	/**
	 * Ottiene tutte le prenotazioni di un utente specifico.
	 *
	 * @param utenteID ID dell'utente.
	 * @return Lista di oggetti Prenotazione associati all'utente.
	 */
	public static List<Prenotazione> getAllPrenotazioni(int utenteID) {
		List<Prenotazione> prenotazioni = new ArrayList<>();
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		String query = "SELECT p.ID, p.Data, p.OraInizio, p.OraFine, p.Utente, p.Campo " + "FROM Prenotazione p "
				+ "WHERE p.Utente = ?";

		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, utenteID);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String dataString = rs.getString("Data");

				// Definisci il formato della data
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Formato corretto per il parsing

<<<<<<< Updated upstream
<<<<<<< Updated upstream
	            try {
	                // Converte la stringa in un oggetto java.util.Date
	                java.util.Date dataUtil = sdf.parse(dataString);
=======
				try {

					// Converte la stringa in un oggetto java.util.Date
					java.util.Date dataUtil = sdf.parse(dataString);
>>>>>>> Stashed changes

					// Converte java.util.Date in java.sql.Date
					java.sql.Date data = new java.sql.Date(dataUtil.getTime());

					int ID = rs.getInt("ID");
					String oraInizioString = rs.getString("OraInizio");
					String oraFineString = rs.getString("OraFine");

					// Aggiungi ":00" se non presente
					if (oraInizioString.length() == 5) {
						oraInizioString += ":00";
					}
					if (oraFineString.length() == 5) {
						oraFineString += ":00";
					}

					// Converte le ore in oggetti Time
					Time oraInizio = Time.valueOf(oraInizioString);
					Time oraFine = Time.valueOf(oraFineString);

					int campoID = rs.getInt("Campo");

					// Crea l'oggetto Prenotazione
					Prenotazione prenotazione = new Prenotazione(ID, data, oraInizio, oraFine, utenteID, campoID);
					prenotazioni.add(prenotazione);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

=======
				try {

					// Converte la stringa in un oggetto java.util.Date
					java.util.Date dataUtil = sdf.parse(dataString);

					// Converte java.util.Date in java.sql.Date
					java.sql.Date data = new java.sql.Date(dataUtil.getTime());

					int ID = rs.getInt("ID");
					String oraInizioString = rs.getString("OraInizio");
					String oraFineString = rs.getString("OraFine");

					// Aggiungi ":00" se non presente
					if (oraInizioString.length() == 5) {
						oraInizioString += ":00";
					}
					if (oraFineString.length() == 5) {
						oraFineString += ":00";
					}

					// Converte le ore in oggetti Time
					Time oraInizio = Time.valueOf(oraInizioString);
					Time oraFine = Time.valueOf(oraFineString);

					int campoID = rs.getInt("Campo");

					// Crea l'oggetto Prenotazione
					Prenotazione prenotazione = new Prenotazione(ID, data, oraInizio, oraFine, utenteID, campoID);
					prenotazioni.add(prenotazione);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

>>>>>>> Stashed changes
		return prenotazioni;
	}

	/**
	 * Ottiene il centro sportivo associato a un campo specifico.
	 *
	 * @param campoId ID del campo sportivo.
	 * @return Oggetto CentroSportivo associato al campo, o null se non trovato.
	 */
	public static CentroSportivo getCentroByCampo(int campoId) {
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		String query = "SELECT c.ID, c.Nome, c.Provincia, c.Comune " + "FROM CentroSportivo c "
				+ "JOIN Campo ca ON c.ID = ca.CentroSportivo " + "WHERE ca.ID = ?";

		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, campoId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int centroID = rs.getInt("ID");
				String nome = rs.getString("Nome");
				String provincia = rs.getString("Provincia");
				String comune = rs.getString("Comune");

				// Crea e restituisce un oggetto CentroSportivo
				return new CentroSportivo(centroID, nome, provincia, comune);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Se il campo non è associato a un centro sportivo, restituisci null
		return null;
	}

	/**
	 * Ottiene un centro sportivo dal database in base al nome.
	 *
	 * @param nome Nome del centro sportivo.
	 * @return Oggetto CentroSportivo corrispondente, o null se non trovato.
	 */
	public static CentroSportivo getCentroByName(String nome) {
		CentroSportivo centro = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet resultSet = null;

		try {
			// Query per trovare il centro sportivo con il nome specificato
			String query = "SELECT * FROM CentroSportivo WHERE Nome = ?";
			String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

			// Crea la connessione al database
			conn = DriverManager.getConnection(url);

			// Prepara la query
			stmt = conn.prepareStatement(query);
			stmt.setString(1, nome); // Imposta il parametro per il nome

			// Esegui la query
			resultSet = stmt.executeQuery();

			if (resultSet.next()) {
				// Crea un oggetto CentroSportivo dal risultato della query
				centro = new CentroSportivo(resultSet.getInt("ID"), // Supponendo che l'ID sia un intero
						resultSet.getString("Nome"), resultSet.getString("Provincia"), resultSet.getString("Comune"));
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log dell'errore
		} finally {
			try {
				// Chiudi le risorse per evitare perdite
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return centro; // Restituisce il centro o null se non trovato
	}

	/**
	 * Ottiene i centri sportivi filtrati per provincia.
	 *
	 * @param provinciaS Nome della provincia.
	 * @return Mappa dei centri sportivi nella provincia specificata (nome ->
	 *         oggetto CentroSportivo).
	 */
	public static Map<String, CentroSportivo> getCentriSportiviPerProvincia(String provinciaS) {
		Map<String, CentroSportivo> centri = new HashMap<>();
		String query = "SELECT ID, Nome, Provincia, Comune FROM CentroSportivo WHERE Provincia = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setString(1, provinciaS);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String nome = rs.getString("Nome");
				String provincia = rs.getString("Provincia");
				String comune = rs.getString("Comune");

				// Crea l'oggetto CentroSportivo
				CentroSportivo centro = new CentroSportivo(id, nome, provincia, comune); // Gestore può essere impostato
																							// successivamente
				centri.put(nome, centro); // Usa il nome come chiave
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return centri;
	}

	/**
	 * Ottiene un campo sportivo dal database in base al suo ID.
	 *
	 * @param campoId ID del campo sportivo.
	 * @return Oggetto Campo corrispondente, o null se non trovato.
	 */
	public static Campo getCampoById(int campoId) {
		Campo campo = null;
		String query = "SELECT ID, Tipologia, CostoOraNotturna, CostoOraDiurna, Lunghezza, Larghezza, Coperto, CentroSportivo FROM Campo c WHERE c.ID = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, campoId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String tipologiaString = rs.getString("Tipologia");
				int costoOraNotturna = rs.getInt("CostoOraNotturna");
				int costoOraDiurna = rs.getInt("CostoOraDiurna");
				int lunghezza = rs.getInt("Lunghezza");
				int larghezza = rs.getInt("Larghezza");
				boolean coperto = rs.getBoolean("Coperto");
				int centro = rs.getInt("CentroSportivo");

				// Converti la stringa in TipologiaCampo
				TipologiaCampo tipologia;
				try {
					tipologia = TipologiaCampo.valueOf(tipologiaString);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Tipologia non valida: " + tipologiaString, e);
				}

				// Crea l'oggetto Campo
				campo = new Campo(id, tipologia, costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto,
						centro);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return campo;
	}

	/**
	 * Aggiorna i dettagli di una prenotazione esistente.
	 *
	 * @param prenotazione Oggetto Prenotazione contenente i nuovi dettagli da
	 *                     aggiornare.
	 * @throws SQLException In caso di errore nell'aggiornamento.
	 */
	public static void updatePrenotazione(Prenotazione prenotazione) throws SQLException {
		String sql = "UPDATE Prenotazione " + "SET Data = ?, OraInizio = ?, OraFine = ?" + "WHERE ID = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, prenotazione.getData().toString()); // Usa direttamente java.sql.Date
			pstmt.setString(2, prenotazione.getOraInizio().toString()); // java.sql.Time
			pstmt.setString(3, prenotazione.getOraFine().toString()); // java.sql.Time
			pstmt.setInt(4, prenotazione.getId()); // Imposta ID
			pstmt.executeUpdate();
		}
	}

	/**
	 * Inserisce una nuova prenotazione nel database.
	 *
	 * @param prenotazione Oggetto Prenotazione da inserire.
	 * @throws SQLException In caso di errore nell'inserimento.
	 */
	public static void inserisciPrenotazione(Prenotazione prenotazione) throws SQLException {
		String sql = "INSERT INTO Prenotazione (Data, OraInizio, OraFine, Campo, Utente) VALUES (?, ?, ?, ?, ?)";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// Imposta i valori dei parametri nella query
			pstmt.setString(1, prenotazione.getData().toString()); // Data in formato yyyy-MM-dd
			pstmt.setString(2, prenotazione.getOraInizio().toString()); // Ora in formato HH:mm:ss
			pstmt.setString(3, prenotazione.getOraFine().toString()); // Ora in formato HH:mm:ss
			pstmt.setInt(4, prenotazione.getCampoId()); // ID del campo
			pstmt.setInt(5, prenotazione.getUtenteId()); // ID dell'utente

			// Esegui la query
			pstmt.executeUpdate();
		}
	}

	/**
	 * Ottiene una prenotazione dal database in base al suo ID.
	 *
	 * @param prenotazioneId ID della prenotazione.
	 * @return Oggetto Prenotazione corrispondente, o null se non trovato.
	 */
	public static Prenotazione getPrenotazioneById(int prenotazioneId) {
		Prenotazione prenotazione = null;
		String query = "SELECT ID, Data, OraInizio, OraFine, Utente, Campo FROM Prenotazione p WHERE p.ID = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		try (Connection conn = DriverManager.getConnection(url);
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, prenotazioneId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID");
				String data = rs.getString("Data");
				String oraInizio = rs.getString("OraInizio");
				String oraFine = rs.getString("OraFine");
				int utenteId = rs.getInt("Utente");
				int campoId = rs.getInt("Campo");

				java.sql.Date d = java.sql.Date.valueOf(data);
				Time oi = Time.valueOf(oraInizio);
				Time of = Time.valueOf(oraFine);
				// Crea l'oggetto Prenotazione
				prenotazione = new Prenotazione(id, d, oi, of, utenteId, campoId);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prenotazione;
	}

	/**
	 * Elimina una prenotazione dal database in base ai suoi dettagli.
	 *
	 * @param prenotazione Oggetto Prenotazione da eliminare.
	 * @throws SQLException In caso di errore nell'eliminazione.
	 */
	public static void eliminaPrenotazione(Prenotazione prenotazione) throws SQLException {
		String sql = "DELETE FROM Prenotazione WHERE Data = ? AND OraInizio = ? AND Campo = ? AND Utente = ?";
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

		try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// Imposta i valori dei parametri nella query
			pstmt.setString(1, prenotazione.getData().toString()); // Data in formato yyyy-MM-dd
			pstmt.setString(2, prenotazione.getOraInizio().toString()); // Ora in formato HH:mm:ss
			pstmt.setInt(3, prenotazione.getCampoId()); // ID del campo
			pstmt.setInt(4, prenotazione.getUtenteId()); // ID dell'utente

			// Esegui la query
			pstmt.executeUpdate();
		}
	}

}
