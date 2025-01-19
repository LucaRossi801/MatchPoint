package individui_Test;

import org.junit.*;
import individui.Utente;
import components_Test.InMemoryDatabaseUtil;

import static org.junit.Assert.*;

public class Utente_Test {

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

    /**
     * Test per il metodo login con credenziali valide.
     */
    @Test
    public void testLoginCredenzialiValide() throws Exception {

        // Esegue il login con le credenziali corrette
        int risultato = Utente.login("mrossi", "password123");

        assertEquals(1, risultato); // Login dovrebbe avere successo
    }

    /**
     * Test per il metodo login con username inesistente.
     */
    @Test
    public void testLoginUsernameInesistente() throws Exception {
        // Esegue il login con un username inesistente
        int risultato = Utente.login("utente_non_esistente", "password123");

        assertEquals(-3, risultato); // Username non trovato
    }

    /**
     * Test per il metodo login con password errata.
     */
    @Test
    public void testLoginPasswordErrata() throws Exception {
        // Esegue il login con una password errata
        int risultato = Utente.login("mrossi", "errata");

        assertEquals(0, risultato); // Password errata
    }

    /**
     * Test per il metodo getRuoloUtente con un utente di tipo Gestore.
     */
    @Test
    public void testGetRuoloUtenteGestore() throws Exception {
        // Recupera il ruolo del gestore
        String ruolo = Utente.getRuoloUtente("mrossi", "password123");

        assertEquals("Gestore", ruolo); // Dovrebbe restituire "Gestore"
    }

    /**
     * Test per il metodo getRuoloUtente con un utente di tipo Giocatore.
     */
    @Test
    public void testGetRuoloUtenteGiocatore() throws Exception {
        // Recupera il ruolo del giocatore
        String ruolo = Utente.getRuoloUtente("lverdi", "password789");

        assertEquals("giocatore", ruolo); // Dovrebbe restituire "Giocatore"
    }

    /**
     * Test per il metodo getRuoloUtente con credenziali errate.
     */
    @Test
    public void testGetRuoloUtenteCredenzialiErrate() throws Exception {
        // Recupera il ruolo con una password errata
        String ruolo = Utente.getRuoloUtente("lverdi", "errata");

        assertNull(ruolo); // Dovrebbe restituire null
    }

    /**
     * Test per il metodo getRuoloUtente con username inesistente.
     */
    @Test
    public void testGetRuoloUtenteUsernameInesistente() throws Exception {
        // Recupera il ruolo di un utente inesistente
        String ruolo = Utente.getRuoloUtente("nonEsiste", "password123");

        assertNull(ruolo); // Dovrebbe restituire null
    }
}
