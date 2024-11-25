package components;

import java.sql.SQLException;

import dataBase.DataBase;

public class Sessione {
	private static int id;
    private static String username;   // Username dell'utente loggato
    private static String tipologia;  // Categoria dell'utente ("Gestore" o "Giocatore")

    public Sessione() {
    	id=0;
    	username=null;
    	tipologia=null;
    }
    
    // Metodo per effettuare il login
    public static void login(String username, String tipologia) throws SQLException {
<<<<<<< Updated upstream
    	DataBase.getIdUtente(username, tipologia);
        Sessione.username = username;
        Sessione.tipologia = tipologia;
=======
        Sessione.username = username;
        Sessione.tipologia = tipologia;
        id= DataBase.getIdUtente(username, tipologia);
>>>>>>> Stashed changes
    }

    // Metodo per effettuare il logout
    public static void logout() {
    	id=-9;
        username = null;
        tipologia = null;
    }

    // Getter per ottenere lo username dell'utente loggato
    public static String getUsername() {
        return username;
    }
    
 // Getter per ottenere lo username dell'utente loggato
    public static int getId() {
        return id;
    }

    // Getter per ottenere la categoria dell'utente loggato
    public static String getTipologia() {
        return tipologia;
    }

    // Metodo per verificare se un utente è loggato
    public static boolean isUtenteLoggato() {
        return username != null && tipologia != null;
    }
}
