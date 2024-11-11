package dataBase;

import java.sql.*;

public class DataBase {
	private static void insert(Connection conn) throws SQLException {
		String sql = "INSERT INTO Utente(Nome, Cognome, DataNascita, Eta, Username, Password) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "Bob");
			pstmt.setString(1, "Marley");
			pstmt.setDate(1, Date.valueOf("2003-11-08"));
			pstmt.setInt(1, 21);
			pstmt.setString(1, "BobbyOne");
			pstmt.setString(1, "BobbyGronde");
			pstmt.executeUpdate();

			pstmt.setString(1, "Jhon");
			pstmt.setString(1, "Abruzzi");
			pstmt.setDate(1, Date.valueOf("1989-07-09"));
			pstmt.setInt(1, 35);
			pstmt.setString(1, "JhonAbru");
			pstmt.setString(1, "TheMafiaBoss123");
			pstmt.executeUpdate();
		}
	}

	public static void main(String[] args) throws SQLException {
		String url = "\"C:\\Users\\CIUCCO\\Desktop\\UNI\\III ANNO\\I SEMESTRE\\INGEGNERIA DEL SOFTWARE\\Eclipse\\ProgettoWorkspace\\ingsw\\DataBase\\MatchPointDB.db\"";
		try (Connection conn = DriverManager.getConnection(url)) {
			insert(conn);
			//query(conn);
		}

	}
}
