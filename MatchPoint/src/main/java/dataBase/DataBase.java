package dataBase;

import java.sql.*;
import java.util.Date;

public class DataBase {
	// Metodo per creare la tabella Utente se non esiste
	/*
	 * private static void createTable(Connection conn) throws SQLException { String
	 * createTableSQL = "CREATE TABLE IF NOT EXISTS Utente (" +
	 * "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "Nome TEXT NOT NULL," +
	 * "Cognome TEXT NOT NULL," + "DataNascita DATE NOT NULL," +
	 * "Eta INTEGER NOT NULL," + "Username TEXT NOT NULL," +
	 * "Password TEXT NOT NULL)"; String createTableGestore =
	 * "CREATE TABLE IF NOT EXISTS Gestore (" +
	 * "ID INTEGER PRIMARY KEY AUTOINCREMENT," + "Nome TEXT NOT NULL," +
	 * "Cognome TEXT NOT NULL," + "DataNascita DATE NOT NULL," +
	 * "Eta INTEGER NOT NULL," + "Username TEXT NOT NULL," +
	 * "Password TEXT NOT NULL)"; //da aggiungere try (Statement stmt =
	 * conn.createStatement()) { stmt.execute(createTableSQL);
	 * stmt.execute(createTableGestore); } }
	 */

	// Metodo per inserire i dati nella tabella Gestore
	public static void insert(Connection conn, String nome, String cognome, Date dataNascita, int eta, String email, String username, String password, String certificazioni, String competenze) throws SQLException {
		String sql = "INSERT INTO Gestore(Nome, Cognome, DataNascita, Eta, Email, Username, Password, Certificazione, Competenze) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, nome);
			pstmt.setString(2, cognome);
			pstmt.setDate(3, (java.sql.Date) dataNascita);
			pstmt.setInt(4, eta);
			pstmt.setString(5, email);
			pstmt.setString(6, username);
			pstmt.setString(7, password);
			pstmt.setString(8, certificazioni);
			pstmt.setString(9, competenze);
			pstmt.executeUpdate();
		}
	}
	
	// Metodo per inserire i dati nella tabella Giocatore
		public static void insert(Connection conn, String nome, String cognome, Date dataNascita, int eta, String email, String username, String password, String nomeSquadra) throws SQLException {
			String sql = "INSERT INTO Gestore(Nome, Cognome, DataNascita, Eta, Email, Username, Password, NomeSquadra) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, nome);
				pstmt.setString(2, cognome);
				pstmt.setDate(3, (java.sql.Date) dataNascita);
				pstmt.setInt(4, eta);
				pstmt.setString(5, email);
				pstmt.setString(6, username);
				pstmt.setString(7, password);
				pstmt.setString(8, nomeSquadra);
				pstmt.executeUpdate();
			}
		}

	// Metodo privato per eseguire una select
	private static ResultSet select(Connection conn, String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql); // Il chiamante è responsabile della chiusura
	}

	// Metodo pubblico per esecuzione di SELECT e trasformazione in stringa
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

	// Metodo principale
	public static void main(String[] args) throws SQLException {
		String url = "jdbc:sqlite:src/main/java/matchpointDB.db";

		try (Connection conn = DriverManager.getConnection(url)) {
			// Crea la tabella se non esiste
			// createTable(conn);

			// Inserisce i dati nella tabella
			// insert(conn);

			// Seleziona e mostra i dati della tabella Utente

		}

		System.out.println("CIAO PASTICCINO!");
	}
}
