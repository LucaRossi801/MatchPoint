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

	// Metodo per esecuzione del login
	public static int login(String username, String password) {
		String sql="SELECT Password FROM Utente WHERE Username ='"+username+"'";//creazione query
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db"; //connessione al database
		String ris="";
		try  (Connection conn = DriverManager.getConnection(url)){
			ris = DataBase.eseguiSelect(conn, sql);//esecuzione select
		} catch (SQLException e) {
			e.printStackTrace();
		} //risultato della query
		System.out.println(ris);
		if(password.equals(ris)) {//confronto con password inserita
			return 1;
		}
		else if(ris.equals("")) {
			System.out.println("Username non trovato!");//se la select non produce risultati
			return -3;
		}
		else {
			System.out.println("Password errata!");
			return 0;
		}
		
	}

}
