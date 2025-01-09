package components_Test;

import org.junit.*;
import individui.Giocatore;

import static org.junit.Assert.*;

import java.util.UUID;

/**
 * Classe di test per la classe Giocatore.
 * I test utilizzano un database SQLite in-memory per isolare i dati durante il testing.
 */
public class Giocatore_Test {

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
        String nome = "Marco";
        String cognome = "Verdi";
        String dataNascita = "1995-07-20";
        String email = "marco.vdferdi@example.com";

        // Genera un username unico utilizzando UUID
        String username = "marco_verdi_" + UUID.randomUUID().toString().substring(0, 10);

        String password = "securePassword";
        String nomeSquadra = "TeamAlpha";

        int risultato = Giocatore.registrazione(
            nome, cognome, dataNascita, email, username, password, nomeSquadra
        );
        assertEquals(1, risultato); // La registrazione dovrebbe avere successo.
    }

    @Test
    public void testRegistrazioneUsernameGiaInUso() throws Exception {
        String nome = "Andrea";
        String cognome = "Bianchi";
        String dataNascita = "1990-03-15";
        String email = "andrea.bianchfei@example.com";
        String username = "lverdi"; // Username già in uso (presente nella configurazione iniziale del database)
        String password = "password123";
        String nomeSquadra = "TeamBeta";

        int risultato = Giocatore.registrazione(
            nome, cognome, dataNascita, email, username, password, nomeSquadra
        );
        assertEquals(-3, risultato); // La registrazione dovrebbe fallire con username già in uso.
    }

    @Test
    public void testRegistrazioneEmailGiaInUso() throws Exception {
        String nome = "Sara";
        String cognome = "Neri";
        String dataNascita = "1988-12-10";
        String email = "sara.neri@example.com"; // Email già in uso (presente nella configurazione iniziale del database)
        String username = "sara_neri";
        String password = "anotherPassword";
        String nomeSquadra = "TeamGamma";

        int risultato = Giocatore.registrazione(
            nome, cognome, dataNascita, email, username, password, nomeSquadra
        );
        assertEquals(-3, risultato); // La registrazione dovrebbe fallire con email già in uso.
    }
}
