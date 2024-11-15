package individui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import dataBase.DataBase;

//--------------------------------------------------------
//Code generated by Papyrus Java
//--------------------------------------------------------

public abstract class Utente {
	public int ID;
	public String nome;
	public String cognome;
	public Date dataNascita;
	public int eta;
	public String username;
	public String password;

	public Utente(String nome, String cognome, Date dataNascita, String username, String password) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
		this.username=username;
		this.password=password;
	}

	public abstract int registrazione(String username, String password);

	public int login(String username, String password) {
		String sql="SELECT password FROM Utente WHERE username ="+username;
		ResultSet select = null;
		String url = "jdbc:sqlite:src/main/java/matchpointDB.db"; 
		try  (Connection conn = DriverManager.getConnection(url)){
			select = DataBase.eseguiSelect(conn, sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} //risultato della query
		String RisSelect =select.toString();
		if(password==RisSelect) {
			return 1;
		}
		else if(RisSelect!=password) {
			System.out.println("Password errata!");
			return 0;
		}
		else {
			System.out.println("Username errato!");
			return 0;
		}
		
	}

}
