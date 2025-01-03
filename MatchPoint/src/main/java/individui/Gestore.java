/**
 * Il package individui contiene la definizione delle classi relative agli individui.
 */
package individui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import dataBase.DataBase;

/**
 * La classe Gestore rappresenta un utente di tipo gestore nel sistema.
 * Estende la classe Utente e include attributi aggiuntivi come certificazione e competenze.
 */
public class Gestore extends Utente {

    public String certificazione;
    public String competenze;

    /**
     * Costruttore di default per la classe Gestore.
     */
    public Gestore() {
        super();
    }

    /**
     * Registra un nuovo Gestore nel sistema.
     *
     * @param nome          Il nome dell'utente.
     * @param cognome       Il cognome dell'utente.
     * @param dataNascita   La data di nascita dell'utente in formato "yyyy-MM-dd".
     * @param email         L'email dell'utente.
     * @param username      Lo username dell'utente.
     * @param password      La password dell'utente.
     * @param certificazioni Le certificazioni dell'utente.
     * @param competenze    Le competenze dell'utente.
     * @return 1 se la registrazione ha successo, -3 se lo username è già in uso.
     */
    public static int registrazione(String nome, String cognome, String dataNascita, String email, String username, String password, String certificazioni, String competenze) {
        String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db"; // URL di connessione al database
        int eta;

        // Calcolo dell'età basato sulla data di nascita
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nascita = LocalDate.parse(dataNascita, formatter);
        LocalDate oggi = LocalDate.now();
        Period periodo = Period.between(nascita, oggi);
        eta = periodo.getYears();

        // Query per verificare se lo username esiste già
        String sql = "SELECT Password FROM Gestore WHERE Username ='" + username + "' UNION SELECT Password FROM Giocatore WHERE Username ='" + username + "'";
        String ris = "";

        try (Connection conn = DriverManager.getConnection(url)) {
            ris = DataBase.eseguiSelect(conn, sql); // Esecuzione della query
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Se lo username non esiste, registra il nuovo utente
        if (ris.equals("")) {
            try (Connection conn = DriverManager.getConnection(url)) {
                DataBase.insert(conn, nome, cognome, dataNascita, eta, email, username, password, certificazioni, competenze);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 1;
        } else {
            return -3;
        }
    }
}