package components_Test;

import org.junit.*;

import individui.Gestore;

import static org.junit.Assert.*;

import java.util.UUID;

/**
 * Classe di test per la classe Gestore.
 * I test utilizzano un database SQLite in-memory per isolare i dati durante il testing.
 */
public class Gestore_Test {

    private InMemoryDatabaseUtil dbUtil;

    @Before
    public void setUp() throws Exception {
        // Inizializza il database in-memory
        dbUtil = new InMemoryDatabaseUtil();
        dbUtil.setUp();
    }

    @After
    public void tearDown() throws Exception {
        if (dbUtil != null) {
            dbUtil.tearDown(); // Chiude il database
        }
    }

    @Test
    public void testRegistrazioneSuccesso() throws Exception {
        String nome = "Mario";
        String cognome = "Rossi";
        String dataNascita = "1990-05-15";
        String email = "abc";

        // Genera un username unico utilizzando UUID
        String username = "mario_rossi_" + UUID.randomUUID().toString().substring(0, 10);

        String password = "password123";
        String certificazioni = "9";
        String competenze = "Gestione eventi";

        int risultato = Gestore.registrazione(
            nome, cognome, dataNascita, email, username, password, certificazioni, competenze, dbUtil.getConnection()
        );
        assertEquals(1, risultato); // La registrazione dovrebbe avere successo.
    }

    @Test
    public void testRegistrazioneUsernameGiaInUso() throws Exception {
        String nome = "Luca";
        String cognome = "Rossi";
        String dataNascita = "1985-03-10";
        String email = "luca.bianchi@dsda.com";
        String username = "mrossi"; // Username già in uso (presente nella configurazione iniziale del database)
        String password = "ciao";
        String certificazioni = "8";
        String competenze = "Organizzazione";

        int risultato = Gestore.registrazione(
            nome, cognome, dataNascita, email, username, password, certificazioni, competenze, dbUtil.getConnection()
        );
        assertEquals(-3, risultato); // La registrazione dovrebbe fallire con username già in uso.
    }

}
