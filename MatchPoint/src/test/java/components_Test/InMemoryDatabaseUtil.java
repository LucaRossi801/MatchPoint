package components_Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility per configurare un database SQLite in-memory per i test.
 */
public class InMemoryDatabaseUtil {

    private Connection connection;

    /**
     * Configura un database in memoria e crea le tabelle necessarie.
     *
     * @throws SQLException se si verifica un errore durante la configurazione.
     */
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            // Creazione delle tabelle
            stmt.execute("CREATE TABLE Gestore (" +
                    " ID INTEGER NOT NULL UNIQUE," +
                    " Nome TEXT NOT NULL," +
                    " Cognome TEXT NOT NULL," +
                    " DataNascita TEXT NOT NULL," +
                    " Eta INTEGER NOT NULL," +
                    " Email VARCHAR(50) NOT NULL UNIQUE," +
                    " Username VARCHAR(20) NOT NULL UNIQUE," +
                    " Password VARCHAR(20) NOT NULL," +
                    " Certificazione NUMERIC NOT NULL," +
                    " Competenze TEXT NOT NULL," +
                    " PRIMARY KEY(ID AUTOINCREMENT)" +
                    ");");
            stmt.execute("CREATE TABLE Giocatore (" +
                    " ID INTEGER NOT NULL UNIQUE," +
                    " Nome TEXT NOT NULL," +
                    " Cognome TEXT NOT NULL," +
                    " DataNascita DATE NOT NULL," +
                    " Eta INTEGER NOT NULL," +
                    " Email VARCHAR(50) NOT NULL UNIQUE," +
                    " Username VARCHAR(20) NOT NULL UNIQUE," +
                    " Password VARCHAR(20) NOT NULL," +
                    " NomeSquadra INTEGER NOT NULL," +
                    " PRIMARY KEY(ID AUTOINCREMENT)" +
                    ");");

            // Aggiungi altre tabelle come necessario...
            // ...

            // Inserisci dati di esempio
            addSampleData();
        }
    }

    /**
     * Aggiunge dati di esempio al database.
     *
     * @throws SQLException se si verifica un errore durante l'inserimento.
     */
    private void addSampleData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Dati per la tabella Gestore
            stmt.execute("INSERT INTO Gestore (Nome, Cognome, DataNascita, Eta, Email, Username, Password, Certificazione, Competenze) VALUES " +
                    "('Mario', 'Rossi', '1980-01-01', 43, 'mario.rossi@example.com', 'mrossi', 'password123', 1, 'Gestione');");
            stmt.execute("INSERT INTO Gestore (Nome, Cognome, DataNascita, Eta, Email, Username, Password, Certificazione, Competenze) VALUES " +
                    "('Anna', 'Bianchi', '1985-02-15', 38, 'anna.bianchi@example.com', 'abianchi', 'password456', 1, 'Organizzazione');");

            // Dati per la tabella Giocatore
            stmt.execute("INSERT INTO Giocatore (Nome, Cognome, DataNascita, Eta, Email, Username, Password, NomeSquadra) VALUES " +
                    "('Luca', 'Verdi', '1990-06-20', 33, 'luca.verdi@example.com', 'lverdi', 'password789', 1);");
            stmt.execute("INSERT INTO Giocatore (Nome, Cognome, DataNascita, Eta, Email, Username, Password, NomeSquadra) VALUES " +
                    "('Sara', 'Neri', '1995-09-15', 28, 'sara.neri@example.com', 'sneri', 'password101', 1);");

            // Aggiungi altri dati come necessario...
        }
    }

    /**
     * Ottieni la connessione al database in memoria.
     *
     * @return La connessione al database.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Cancella tutti i dati dalle tabelle.
     *
     * @throws SQLException se si verifica un errore durante la pulizia.
     */
    public void clearData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Gestore;");
            stmt.execute("DELETE FROM Giocatore;");
            // Cancella dati da altre tabelle se necessario...
        }
    }

    /**
     * Chiude la connessione al database.
     *
     * @throws SQLException se si verifica un errore durante la chiusura.
     */
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
