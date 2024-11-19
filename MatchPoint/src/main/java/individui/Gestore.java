package individui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.*;
import java.util.Date;

import dataBase.DataBase;

public class Gestore extends Utente {
	
	public String certificazione;
	public String competenze;
	public Gestore() {
		super();
	}

	public static int registrazione(String nome, String cognome, Date dataNascita, String email, String username, String password, String certificazioni, String competenze) {
		String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db"; //connessione al database
		LocalDate dataNascitaLD = dataNascita.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(); //calcolo età a partire dalla data di nscita
		Period periodo = Period.between(dataNascitaLD, LocalDate.now());
        int eta=  periodo.getYears();
        String sql="SELECT Password FROM Gestore WHERE Username ='"+username+"' UNION SELECT Password FROM Giocatore WHERE Username ='"+username+"'";//creazione query
		String ris="";
		try  (Connection conn = DriverManager.getConnection(url)){
			ris = DataBase.eseguiSelect(conn, sql);//esecuzione select
		} catch (SQLException e) {
			e.printStackTrace();
		} //risultato della query
		System.out.println(ris);
		if(ris.equals("")) {		//se username non esiste
			System.out.println("Username valido");
			try  (Connection conn = DriverManager.getConnection(url)){
				DataBase.insert(conn, nome, cognome, dataNascita, eta, email, username, password, certificazioni, competenze);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			return 1;
		}
		
		else {
			System.out.println("Username non valido!");
			return -3;
		}
	}
	
	

}
