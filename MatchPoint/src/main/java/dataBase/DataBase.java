package dataBase;

import java.sql.*;

public class DataBase {
    // Metodo per creare la tabella Utente se non esiste
    private static void createTable(Connection conn) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Utente (" +
                                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "Nome TEXT NOT NULL," +
                                "Cognome TEXT NOT NULL," +
                                "DataNascita DATE NOT NULL," +
                                "Eta INTEGER NOT NULL," +
                                "Username TEXT NOT NULL," +
                                "Password TEXT NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    // Metodo per inserire i dati nella tabella Utente
    private static void insert(Connection conn) throws SQLException {
        String sql = "INSERT INTO Utente(Nome, Cognome, DataNascita, Eta, Username, Password) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Bob");
            pstmt.setString(2, "Marley");
            pstmt.setDate(3, Date.valueOf("2003-11-08"));
            pstmt.setInt(4, 21);
            pstmt.setString(5, "BobbySolo");
            pstmt.setString(6, "BobbyGronde");
            pstmt.executeUpdate();

            pstmt.setString(1, "Jhon");
            pstmt.setString(2, "Abruzzi");
            pstmt.setDate(3, Date.valueOf("1989-07-09"));
            pstmt.setInt(4, 35);
            pstmt.setString(5, "JhonAbru");
            pstmt.setString(6, "TheMafiaBoss123");
            pstmt.executeUpdate();
        }
    }

    // Metodo per selezionare e visualizzare i dati nella tabella Utente
    private static ResultSet select(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	return rs;
    }}
    
    public static ResultSet eseguiSelect(Connection conn, String sql) throws SQLException {
    	ResultSet rs= select(conn, sql);
    	return rs;
    }

    // Metodo principale
    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:src/main/java/matchpointDB.db";  
        
        try (Connection conn = DriverManager.getConnection(url)) {
            // Crea la tabella se non esiste
            createTable(conn);
            
            // Inserisce i dati nella tabella
           // insert(conn);
            
            // Seleziona e mostra i dati della tabella Utente
            selectAll(conn);
        }
        
        System.out.println("CIAO BAMBOLINA!");
    }
}
