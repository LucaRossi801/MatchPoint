package components_Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InMemoryDatabaseUtil {

    private Connection connection;

    // Inizializza la connessione al database in-memory
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement stmt = connection.createStatement()) {
            // Creazione delle tabelle
            stmt.execute("CREATE TABLE \"Campo\" (\r\n"
                    + " \"ID\" INTEGER NOT NULL UNIQUE,\r\n"
                    + " \"Tipologia\" TEXT NOT NULL,\r\n"
                    + " \"CostoOraNotturna\" FLOAT NOT NULL,\r\n"
                    + " \"CostoOraDiurna\" FLOAT NOT NULL,\r\n"
                    + " \"Lunghezza\" FLOAT NOT NULL,\r\n"
                    + " \"Larghezza\" FLOAT NOT NULL,\r\n"
                    + " \"Coperto\" BOOLEAN NOT NULL,\r\n"
                    + " \"CentroSportivo\" INTEGER NOT NULL,\r\n"
                    + " PRIMARY KEY(\"ID\" AUTOINCREMENT),\r\n"
                    + " FOREIGN KEY(\"CentroSportivo\") REFERENCES \"CentroSportivo\"(\"ID\") ON DELETE CASCADE\r\n"
                    + ");");
            stmt.execute("CREATE TABLE \"CentroSportivo\" (\r\n"
                    + " \"ID\" INTEGER NOT NULL UNIQUE,\r\n"
                    + " \"Nome\" TEXT NOT NULL,\r\n"
                    + " \"Provincia\" TEXT NOT NULL,\r\n"
                    + " \"Comune\" TEXT NOT NULL,\r\n"
                    + " \"Gestore\" INTEGER NOT NULL,\r\n"
                    + " PRIMARY KEY(\"ID\" AUTOINCREMENT),\r\n"
                    + " FOREIGN KEY(\"Gestore\") REFERENCES \"Gestore\"(\"ID\")\r\n"
                    + ");");
            stmt.execute("CREATE TABLE \"Gestore\" (\r\n"
                    + " \"ID\" INTEGER NOT NULL UNIQUE,\r\n"
                    + " \"Nome\" TEXT NOT NULL,\r\n"
                    + " \"Cognome\" TEXT NOT NULL,\r\n"
                    + " \"DataNascita\" TEXT NOT NULL,\r\n"
                    + " \"Eta\" INTEGER NOT NULL,\r\n"
                    + " \"Email\" VARCHAR(50) NOT NULL UNIQUE,\r\n"
                    + " \"Username\" Varchar(20) NOT NULL UNIQUE,\r\n"
                    + " \"Password\" Varchar(20) NOT NULL,\r\n"
                    + " \"Certificazione\" NUMERIC NOT NULL,\r\n"
                    + " \"Competenze\" TEXT NOT NULL,\r\n"
                    + " PRIMARY KEY(\"ID\" AUTOINCREMENT)\r\n"
                    + ");");
            stmt.execute("CREATE TABLE \"Giocatore\" (\r\n"
                    + " \"ID\" INTEGER NOT NULL UNIQUE,\r\n"
                    + " \"Nome\" TEXT NOT NULL,\r\n"
                    + " \"Cognome\" TEXT NOT NULL,\r\n"
                    + " \"DataNascita\" DATE NOT NULL,\r\n"
                    + " \"Eta\" INTEGER NOT NULL,\r\n"
                    + " \"Email\" VARCHAR(50) NOT NULL UNIQUE,\r\n"
                    + " \"Username\" VARCHAR(20) NOT NULL UNIQUE,\r\n"
                    + " \"Password\" VARCHAR(20) NOT NULL,\r\n"
                    + " \"NomeSquadra\" INTEGER NOT NULL,\r\n"
                    + " PRIMARY KEY(\"ID\" AUTOINCREMENT)\r\n"
                    + ");");
            stmt.execute("CREATE TABLE \"Prenotazione\" (\r\n"
                    + " \"ID\" INTEGER NOT NULL UNIQUE,\r\n"
                    + " \"Data\" DATE NOT NULL,\r\n"
                    + " \"OraInizio\" TIME NOT NULL,\r\n"
                    + " \"OraFine\" TIME NOT NULL,\r\n"
                    + " \"Campo\" INTEGER NOT NULL,\r\n"
                    + " \"Utente\" INTEGER NOT NULL,\r\n"
                    + " PRIMARY KEY(\"ID\" AUTOINCREMENT),\r\n"
                    + " FOREIGN KEY(\"Campo\") REFERENCES \"Campo\"(\"ID\"),\r\n"
                    + " FOREIGN KEY(\"Utente\") REFERENCES \"Giocatore\"(\"ID\")\r\n"
                    + ");");

            // Inserimenti di esempio
            stmt.execute("INSERT INTO \"Gestore\" (Nome, Cognome, DataNascita, Eta, Email, Username, Password, Certificazione, Competenze) VALUES "
                    + "('Mario', 'Rossi', '1980-01-01', 43, 'mario.rossi@example.com', 'mrossi', 'password123', 1, 'Gestione');");
            stmt.execute("INSERT INTO \"Gestore\" (Nome, Cognome, DataNascita, Eta, Email, Username, Password, Certificazione, Competenze) VALUES "
                    + "('Anna', 'Bianchi', '1985-02-15', 38, 'anna.bianchi@example.com', 'abianchi', 'password456', 1, 'Organizzazione');");

            stmt.execute("INSERT INTO \"CentroSportivo\" (Nome, Provincia, Comune, Gestore) VALUES "
                    + "('Centro Olimpico', 'Milano', 'Milano', 1);");
            stmt.execute("INSERT INTO \"CentroSportivo\" (Nome, Provincia, Comune, Gestore) VALUES "
                    + "('Sport Village', 'Roma', 'Roma', 2);");

            stmt.execute("INSERT INTO \"Campo\" (Tipologia, CostoOraNotturna, CostoOraDiurna, Lunghezza, Larghezza, Coperto, CentroSportivo) VALUES "
                    + "('Volley', 50.0, 30.0, 25.0, 15.0, 1, 1);");
            stmt.execute("INSERT INTO \"Campo\" (Tipologia, CostoOraNotturna, CostoOraDiurna, Lunghezza, Larghezza, Coperto, CentroSportivo) VALUES "
                    + "('Tennis', 40.0, 20.0, 23.0, 11.0, 0, 2);");

            stmt.execute("INSERT INTO \"Giocatore\" (Nome, Cognome, DataNascita, Eta, Email, Username, Password, NomeSquadra) VALUES "
                    + "('Luca', 'Verdi', '1990-06-20', 33, 'luca.verdi@example.com', 'lverdi', 'password789', 1);");
            stmt.execute("INSERT INTO \"Giocatore\" (Nome, Cognome, DataNascita, Eta, Email, Username, Password, NomeSquadra) VALUES "
                    + "('Sara', 'Neri', '1995-09-15', 28, 'sara.neri@example.com', 'sneri', 'password101', 1);");

            stmt.execute("INSERT INTO \"Prenotazione\" (Data, OraInizio, OraFine, Campo, Utente) VALUES "
                    + "('2025-01-10', '18:00', '20:00', 1, 1);");
            stmt.execute("INSERT INTO \"Prenotazione\" (Data, OraInizio, OraFine, Campo, Utente) VALUES "
                    + "('2025-01-11', '19:00', '21:00', 2, 2);");
        }
    }


    // Ottieni la connessione al database
    public Connection getConnection() {
        return connection;
    }

    // Pulisci e chiudi la connessione
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
