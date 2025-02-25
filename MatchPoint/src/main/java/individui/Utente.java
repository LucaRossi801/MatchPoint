/**
 * Il package individui contiene la definizione delle classi relative agli individui.
 */
package individui;

import java.sql.*;

import GUI.CustomMessage;
import dataBase.DataBase;

//--------------------------------------------------------
// Code generated by Papyrus Java
//--------------------------------------------------------

/**
 * Classe astratta che rappresenta un utente generico nel sistema.
 */
public abstract class Utente {
    public int ID;
    public String nome;
    public String cognome;
    public Date dataNascita;
    public int eta;
    public String username;
    public String password;

    /**
     * Costruttore di default per la classe Utente.
     */
    public Utente() {
        super();
    }

    /**
     * Metodo per eseguire il login di un utente.
     *
     * @param username Lo username dell'utente.
     * @param password La password dell'utente.
     * @return >0 se il login ha successo, 0 se la password è errata, <0 se lo username non è presente.
     */
    public static int login(String username, String password, Connection testConnection) {
        Connection conn = null;
        boolean useTestConnection = (testConnection != null);
        String sql = "SELECT Password FROM Gestore WHERE Username ='" + username + "' UNION SELECT Password FROM Giocatore WHERE Username ='" + username + "'"; // Creazione query
        String ris = "";

        try {
            // Gestione della connessione
            conn = useTestConnection ? testConnection : DriverManager.getConnection("jdbc:sqlite:src/main/java/dataBase/matchpointDB.db");
            ris = DataBase.eseguiSelect(conn, sql); // Esecuzione della query
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Chiusura della connessione se non è una connessione di test
            if (!useTestConnection && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Valutazione del risultato
        if (password.equals(ris)) { // Password corretta
            return 1;
        } else if (ris.equals("")) { // Username non trovato
            return -3;
        } else { // Password errata
            return 0;
        }
    }


    /**
     * Metodo per ottenere il ruolo di un utente (Gestore o Giocatore).
     *
     * @param username       Lo username dell'utente.
     * @param password       La password dell'utente.
     * @param testConnection Connessione di test (opzionale).
     * @return Il ruolo dell'utente ("Gestore", "Giocatore") oppure null se non trovato.
     */
    public static String getRuoloUtente(String username, String password, Connection testConnection) {
        String ruolo = null;
        Connection conn = null;
        boolean useTestConnection = (testConnection != null);

        try {
            // Gestione della connessione
            conn = useTestConnection ? testConnection : DriverManager.getConnection("jdbc:sqlite:src/main/java/dataBase/matchpointDB.db");

            // Query per verificare se l'utente è un Gestore
            String queryGestore = "SELECT username FROM Gestore WHERE username = '" + username + "' AND password = '" + password + "'";
            String risultatoGestore = DataBase.eseguiSelect(conn, queryGestore);

            if (risultatoGestore != null && !risultatoGestore.isEmpty()) {
                ruolo = "Gestore";
            } else {
                // Query per verificare se l'utente è un Giocatore
                String queryGiocatore = "SELECT username FROM Giocatore WHERE username = '" + username + "' AND password = '" + password + "'";
                String risultatoGiocatore = DataBase.eseguiSelect(conn, queryGiocatore);

                if (risultatoGiocatore != null && !risultatoGiocatore.isEmpty()) {
                    ruolo = "Giocatore";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            CustomMessage.show("Errore durante il recupero del ruolo dell'utente!", "Errore", false);
        } finally {
            // Chiusura della connessione se non è una connessione di test
            if (!useTestConnection && conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return ruolo; // Restituisce "Gestore", "Giocatore", oppure null se non trovato
    }

}