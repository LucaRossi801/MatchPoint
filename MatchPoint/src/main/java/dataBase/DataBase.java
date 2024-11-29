package dataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

import GUI.CustomMessage;
import components.Campo;
import components.CentroSportivo;
import components.Prenotazione;
import components.TipologiaCampo;


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
	                CentroSportivo centro = new CentroSportivo(id, nome, provincia, comune); // Gestore può essere impostato successivamente
	                centri.put(nome, centro); // Usa il nome come chiave
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return centri;
	    }
	 
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
	               Campo campo = new Campo(id,TipologiaCampo.valueOf(tipologia), costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto);// Gestore può essere impostato successivamente
	                campi.add(campo); // Usa il nome come chiave
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return campi;
	    }
	 
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
	    
	    /*ublic static List<Campo> getCampiByCentro(int centroID) {
	        String query = "SELECT * FROM Campo WHERE CentroSportivo = ?";
	        List<String> campi = new ArrayList<>();

	        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
		    try (Connection conn = DriverManager.getConnection(url);
		         PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, centroID);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    campi.add(rs.getString("ID"));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return campi;
	    }*/
	    
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

	    public static boolean updateCampo(Campo campo) {
	        // Query per aggiornare i dettagli del campo
	        String query = "UPDATE Campo SET Tipologia = ?, CostoOraNotturna = ?, CostoOraDiurna = ?, " +
	                       "Lunghezza = ?, Larghezza = ?, Coperto = ? WHERE ID = ?";
	        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

	        try (Connection conn = DriverManager.getConnection(url);
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            // Imposta i parametri della query
	            stmt.setString(1, campo.getTipologiaCampo().toString()); // Tipologia come stringa
	            stmt.setInt(2, campo.getCostoOraNotturna());             // Costo ora notturna
	            stmt.setInt(3, campo.getCostoOraDiurna());               // Costo ora diurna
	            stmt.setInt(4, campo.getLunghezza());                    // Lunghezza
	            stmt.setInt(5, campo.getLarghezza());                    // Larghezza
	            stmt.setBoolean(6, campo.isCoperto());                   // Coperto (booleano)
	            stmt.setInt(7, campo.getId());                           // ID del campo

	            // Esegui l'update e verifica il numero di righe aggiornate
	            int rowsUpdated = stmt.executeUpdate();
	            return rowsUpdated > 0; // True se almeno una riga è stata aggiornata

	        } catch (SQLException e) {
	            e.printStackTrace(); // Stampa l'errore per il debug
	            return false;
	        }
	    }




	    public static List<Prenotazione> getPrenotazioniByCampo(int centroId, int campoId) {
	        List<Prenotazione> prenotazioni = new ArrayList<>();
	        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";

	        String query = "SELECT p.Data, p.OraInizio, p.OraFine, p.Utente, p.Campo " +
	                       "FROM Prenotazione p " +
	                       "JOIN Campo c ON p.Campo = c.ID " +
	                       "WHERE c.ID = ? AND c.CentroSportivo = ?";
	        try (Connection conn = DriverManager.getConnection(url);
	             PreparedStatement stmt = conn.prepareStatement(query)) {

	            stmt.setInt(1, campoId);
	            stmt.setInt(2, centroId);

	            ResultSet rs = stmt.executeQuery();

	            while (rs.next()) {
	                String dataString = rs.getString("Data");  // Ottieni la stringa della data

	                // Definisci il formato del database (dd-MM-yyyy)
	                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	                try {
	                    // Parse la stringa nella data
	                    Date parsedDate = sdf.parse(dataString);
	                    
	                    // Converti la data in java.sql.Date
	                    java.sql.Date data = new java.sql.Date(parsedDate.getTime());
	                    
	                    // Ora puoi utilizzare data
	                    System.out.println(data);  // Mostra la data nel formato corretto

	                    String oraInizioString = rs.getString("OraInizio");
	                    String oraFineString = rs.getString("OraFine");

	                    // Aggiungi i secondi se non ci sono
	                    if (oraInizioString.length() == 5) {
	                        oraInizioString += ":00";  // Aggiungi ":00" per i secondi
	                    }
	                    if (oraFineString.length() == 5) {
	                        oraFineString += ":00";  // Aggiungi ":00" per i secondi
	                    }

	                    Time oraInizio = Time.valueOf(oraInizioString);
	                    Time oraFine = Time.valueOf(oraFineString);

	                    int utenteID = rs.getInt("Utente");
	                    int campoID = rs.getInt("Campo");

	                    // Crea l'oggetto Prenotazione
	                    Prenotazione prenotazione = new Prenotazione(data, oraInizio, oraFine, utenteID, campoID);
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



		
		
		public static CentroSportivo getCentroByName(String nome) {
	        if (nome == null || nome.trim().isEmpty()) {
	            return null; // Nome non valido
	        }

	        CentroSportivo centro = null;
	        try {
	            // Query per trovare il centro sportivo con il nome specificato
	            String query = "SELECT * FROM CentroSportivo WHERE Nome = ?";
		        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db";
	            Connection conn = DriverManager.getConnection(url);
	             PreparedStatement stmt = conn.prepareStatement(query);
	            ResultSet resultSet = stmt.executeQuery();

	            if (resultSet.next()) {
	                // Crea un oggetto CentroSportivo dal risultato della query
	                centro = new CentroSportivo(
	                    resultSet.getInt("id"), // Supponendo che l'ID sia un intero
	                    resultSet.getString("nome"),
	                    resultSet.getString("indirizzo"),
	                    resultSet.getString("telefono")
	                    
	                 
	                );
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); // Log dell'errore
	        }

	        return centro; // Restituisce il centro o null se non trovato
	    }

}
