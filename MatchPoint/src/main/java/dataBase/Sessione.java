package dataBase;

import java.sql.SQLException;

/**
 * Classe Sessione
 * <p>
 * Questa classe gestisce le informazioni relative alla sessione dell'utente
 * attualmente loggato. Tiene traccia dell'ID, dello username e della tipologia
 * dell'utente (ad esempio, "Gestore" o "Giocatore").
 * </p>
 */
public class Sessione {
	private static int id; // ID dell'utente loggato
	private static String username; // Username dell'utente loggato
	private static String tipologia; // Categoria dell'utente ("Gestore" o "Giocatore")

	/**
	 * Costruttore di default
	 * <p>
	 * Inizializza i campi della sessione con valori predefiniti.
	 * </p>
	 */
	public Sessione() {
		id = 0;
		username = null;
		tipologia = null;
	}

	/**
	 * Metodo per effettuare il login.
	 * <p>
	 * Memorizza le informazioni dell'utente loggato, come lo username, la tipologia
	 * e l'ID ottenuto dal database.
	 * </p>
	 *
	 * @param username  lo username dell'utente che effettua il login.
	 * @param tipologia la tipologia dell'utente ("Gestore" o "Giocatore").
	 * @throws SQLException se si verifica un errore durante l'accesso al database.
	 */
	public static void login(String username, String tipologia) throws SQLException {
		Sessione.username = username;
		Sessione.tipologia = tipologia;
		id = DataBase.getIdUtente(username, tipologia);
	}

	/**
	 * Metodo per effettuare il logout.
	 * <p>
	 * Resetta i dati della sessione, come lo username, la tipologia e l'ID.
	 * </p>
	 */
	public static void logout() {
		id = -9; // Valore arbitrario per indicare il logout
		username = null;
		tipologia = null;
	}

	/**
	 * Restituisce lo username dell'utente attualmente loggato.
	 *
	 * @return lo username dell'utente loggato, oppure {@code null} se nessun utente
	 *         è loggato.
	 */
	public static String getUsername() {
		return username;
	}

	/**
	 * Restituisce l'ID dell'utente attualmente loggato.
	 *
	 * @return l'ID dell'utente loggato, oppure {@code 0} o un valore arbitrario se
	 *         nessun utente è loggato.
	 */
	public static int getId() {
		return id;
	}

	/**
	 * Restituisce la tipologia dell'utente attualmente loggato.
	 *
	 * @return la tipologia dell'utente loggato, oppure {@code null} se nessun
	 *         utente è loggato.
	 */
	public static String getTipologia() {
		return tipologia;
	}

	/**
	 * Verifica se un utente è loggato.
	 *
	 * @return {@code true} se un utente è loggato; {@code false} altrimenti.
	 */
	public static boolean isUtenteLoggato() {
		return username != null && tipologia != null;
	}
}
