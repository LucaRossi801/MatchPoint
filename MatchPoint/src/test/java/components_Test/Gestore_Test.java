package components_Test;

import org.junit.*;

import individui.Gestore;

import static org.junit.Assert.*;

import java.util.UUID;

/**
 * Classe di test per la classe Gestore.
 * Dato che i dati vengono inseriti nel database viene generata una stringa
 * username randomica altrimenti il test avrebbe successo solo alla prima
 * esecuzione e fallimento in tutte le altre (username già in uso);
 * permane comunque una possibilità di fallimento poichè potrebbe essere
 * generato uno username già in uso.
 */
public class Gestore_Test {

	@Test
	public void testRegistrazioneSuccesso() {
	    String nome = "Mario";
	    String cognome = "Rossi";
	    String dataNascita = "1990-05-15";
	    String email = "mario.rossi@example.com";

	    // Genera un username unico utilizzando UUID
	    String username = "mario_rossi_" + UUID.randomUUID().toString().substring(0, 10);

	    String password = "password123";
	    String certificazioni = "Certificazione A";
	    String competenze = "Gestione eventi";

	    int risultato = Gestore.registrazione(nome, cognome, dataNascita, email, username, password, certificazioni, competenze);
	    assertEquals(1, risultato); // La registrazione dovrebbe avere successo.
	}

    /**
     * Test per la registrazione di un gestore con uno username già in uso.
     */
    @Test
    public void testRegistrazioneUsernameGiaInUso() {
        String nome = "Luca";
        String cognome = "Rossi";
        String dataNascita = "1985-03-10";
        String email = "luca.bianchi@example.com";
        String username = "CIUCCO"; // Username già in uso
        String password = "ciao";
        String certificazioni = "Certificazione B";
        String competenze = "Organizzazione";

        int risultato = Gestore.registrazione(nome, cognome, dataNascita, email, username, password, certificazioni, competenze);
        assertEquals(-3, risultato); // La registrazione dovrebbe fallire con username già in uso.
    }

    /**
     * Test per la registrazione con una data di nascita non valida.
     */
    @Test
    public void testRegistrazioneDataNascitaNonValida() {
        String nome = "Giulia";
        String cognome = "Verdi";
        String dataNascita = "abcd-ef-gh"; // Data non valida
        String email = "giulia.verdi@example.com";
        String username = "giulia_verdi";
        String password = "password789";
        String certificazioni = "Certificazione C";
        String competenze = "Amministrazione";

        Exception exception = assertThrows(Exception.class, () -> {
            Gestore.registrazione(nome, cognome, dataNascita, email, username, password, certificazioni, competenze);
        });

        String messaggioAtteso = "Text 'abcd-ef-gh' could not be parsed";
        assertTrue(exception.getMessage().contains(messaggioAtteso)); // Dovrebbe essere lanciata un'eccezione per data non valida.
    }

    /**
     * Test per la registrazione con parametri null o vuoti.
     */
    @Test
    public void testRegistrazioneParametriNulli() {
        String nome = "";
        String cognome = "";
        String dataNascita = null;
        String email = "";
        String username = "";
        String password = "";
        String certificazioni = null;
        String competenze = null;

        Exception exception = assertThrows(Exception.class, () -> {
            Gestore.registrazione(nome, cognome, dataNascita, email, username, password, certificazioni, competenze);
        });

        assertNotNull(exception);  // Dovrebbe essere lanciata un'eccezione per parametri nulli o vuoti.
    }
}
