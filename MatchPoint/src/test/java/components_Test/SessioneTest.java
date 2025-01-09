package components_Test;

import org.junit.*;
import dataBase.DataBase;
import dataBase.Sessione;
import static org.junit.Assert.*;

import java.sql.SQLException;

public class SessioneTest {

    @Before
    public void setUp() {
        // Resetto lo stato della sessione prima di ogni test.
        Sessione.logout();
    }

    @Test
    public void testLoginSuccess() {
        // Dati per il login valido
        String username = "abianchi";
        String tipologia = "Giocatore";

        try {
            // Provo il login
            Sessione.login(username, tipologia);

            // Verifico che i dati siano corretti
            assertTrue(Sessione.isUtenteLoggato());
            assertEquals(username, Sessione.getUsername());
            assertEquals(tipologia, Sessione.getTipologia());
            assertEquals(DataBase.getIdUtente(username, tipologia), Sessione.getId());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLogout() {
        // Dati per il login valido
        String username = "mario";
        String tipologia = "Giocatore";

        try {
            // Effettuo il login
            Sessione.login(username, tipologia);

            // Verifico che l'utente sia loggato
            assertTrue(Sessione.isUtenteLoggato());

            // Effettuo il logout
            Sessione.logout();

            // Verifico che l'utente sia disconnesso
            assertFalse(Sessione.isUtenteLoggato());
            assertNull(Sessione.getUsername());
            assertNull(Sessione.getTipologia());
            assertEquals(-9, Sessione.getId());
        } catch (SQLException e) {
            fail("Login fallito: " + e.getMessage());
        }
    }

    @Test
    public void testIsUtenteLoggato() {
        // Verifico che inizialmente non ci sia nessun utente loggato
        assertFalse(Sessione.isUtenteLoggato());

        try {
            // Effettuo il login
            Sessione.login("mario", "Giocatore");

            // Verifico che l'utente sia loggato
            assertTrue(Sessione.isUtenteLoggato());

            // Effettuo il logout
            Sessione.logout();

            // Verifico che l'utente sia disconnesso
            assertFalse(Sessione.isUtenteLoggato());
        } catch (SQLException e) {
            fail("Login o logout fallito: " + e.getMessage());
        }
    }

    @Test
    public void testGetUsername() {
        // Verifico che inizialmente lo username sia null
        assertNull(Sessione.getUsername());

        try {
            // Effettuo il login
            Sessione.login("testuser", "Giocatore");

            // Verifico che lo username sia corretto
            assertEquals("testuser", Sessione.getUsername());
        } catch (SQLException e) {
            fail("Login fallito: " + e.getMessage());
        }
    }

    @Test
    public void testGetTipologia() {
        // Verifico che inizialmente la tipologia sia null
        assertNull(Sessione.getTipologia());

        try {
            // Effettuo il login
            Sessione.login("admin", "Gestore");

            // Verifico che la tipologia sia corretta
            assertEquals("Gestore", Sessione.getTipologia());
        } catch (SQLException e) {
            fail("Login fallito: " + e.getMessage());
        }
    }

    @Test
    public void testLogoutWithoutLogin() {
        // Effettuo il logout senza aver effettuato il login
        Sessione.logout();

        // Verifico che la sessione sia vuota
        assertFalse(Sessione.isUtenteLoggato());
        assertNull(Sessione.getUsername());
        assertNull(Sessione.getTipologia());
        assertEquals(-9, Sessione.getId());
    }
}
