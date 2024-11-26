package dataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GUI.CustomMessage;
import components.CentroSportivo;

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
	public static void insert(Connection conn, String nome, String cognome, String data, int eta, String email, String username, String password, String certificazioni, String competenze) throws SQLException {
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
	
	

    //Metodo per ottenere l'ID di un utente
    public static int getIdUtente(String username, String tipologia) throws SQLException {
    	System.out.println(tipologia);
    	System.out.println(username);
        String query = "SELECT ID FROM "+tipologia+" WHERE username = ?";
        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
	    try (Connection conn = DriverManager.getConnection(url);
	         PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	System.out.println(rs.getInt("ID"));
                return rs.getInt("ID"); // Restituisce l'ID dell'utente
            }
        }
		return -8;
    }
	// Metodo per inserire i dati nella tabella Giocatore
		public static void insert(Connection conn, String nome, String cognome, String dataNascita, int eta, String email, String username, String password, String nomeSquadra) throws SQLException {
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
	                CentroSportivo centro = new CentroSportivo(null, id, nome, provincia, comune); // Gestore può essere impostato successivamente
	                centri.put(nome, centro); // Usa il nome come chiave
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return centri;
	    }
	 
	 public boolean updateCentroSportivo(int centroId, String nome, String provincia, String comune) {
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

	 public boolean eliminaCampo(int centroID, String campoSelezionato) {
	        String query = "DELETE FROM Campo WHERE CentroSportivo = ? AND Nome = ?";
	        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		    try (Connection conn = DriverManager.getConnection(url);
		         PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, centroID);
	            stmt.setString(2, campoSelezionato);
	            int rowsAffected = stmt.executeUpdate();
	            return rowsAffected > 0; // True se almeno una riga è stata eliminata
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false; // In caso di errore
	        }
	    }

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
	    
	    public List<String> getCampiByCentro(int centroID) {
	        String query = "SELECT nome FROM Campo WHERE CentroSportivo = ?";
	        List<String> campi = new ArrayList<>();

	        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		    try (Connection conn = DriverManager.getConnection(url);
		         PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, centroID);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    campi.add(rs.getString("Nome"));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return campi;
	    }

	    public static void insert(Connection conn, String nome, String provincia, String comune, int gestore) {
	        String sql = "INSERT INTO CentroSportivo(Nome, Provincia, Comune, Gestore) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, nome);
	            pstmt.setString(2, provincia);
	            pstmt.setString(3, comune);
	            pstmt.setInt(4, gestore);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    public static void insert(Connection conn, String tipologia, int CostoOraNotturna, int costoOraDiurna, int lunghezza, int larghezza, boolean coperto, int centro) {
	        String sql = "INSERT INTO Campo(Tipologia, CostoOraNotturna, CostoOraDiurna, Lunghezza, Larghezza, Coperto, CentroSportivo) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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


}