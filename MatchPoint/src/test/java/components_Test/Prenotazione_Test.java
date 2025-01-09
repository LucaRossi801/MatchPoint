package components_Test;

import org.junit.*;
import components.Prenotazione;

import java.sql.Date;
import java.sql.Time;

import static org.junit.Assert.*;

/**
 * Classe di test per la classe Prenotazione.
 * Verifica il funzionamento dei costruttori, getter, metodi di durata e rappresentazione testuale.
 */
public class Prenotazione_Test {

    @Test
    public void testCostruttoreSenzaID() {
        Date data = Date.valueOf("2025-01-10");
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("15:30:00");
        int utenteID = 123;
        int campoID = 456;

        Prenotazione prenotazione = new Prenotazione(data, oraInizio, oraFine, utenteID, campoID);

        assertEquals(data, prenotazione.getData());
        assertEquals(oraInizio, prenotazione.getOraInizio());
        assertEquals(oraFine, prenotazione.getOraFine());
        assertEquals(utenteID, prenotazione.getUtenteId());
        assertEquals(campoID, prenotazione.getCampoId());
    }

    @Test
    public void testCostruttoreConID() {
        int ID = 1;
        Date data = Date.valueOf("2025-01-10");
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("15:30:00");
        int utenteID = 123;
        int campoID = 456;

        Prenotazione prenotazione = new Prenotazione(ID, data, oraInizio, oraFine, utenteID, campoID);

        assertEquals(ID, prenotazione.getId());
        assertEquals(data, prenotazione.getData());
        assertEquals(oraInizio, prenotazione.getOraInizio());
        assertEquals(oraFine, prenotazione.getOraFine());
        assertEquals(utenteID, prenotazione.getUtenteId());
        assertEquals(campoID, prenotazione.getCampoId());
    }

    @Test
    public void testGetDurata() {
        // Crea orari di inizio e fine
        Time oraInizio = Time.valueOf("10:00:00");
        Time oraFine = Time.valueOf("12:30:00");

        // Crea un oggetto Prenotazione con i dati sopra
        Prenotazione prenotazione = new Prenotazione(1, java.sql.Date.valueOf("2025-01-09"), oraInizio, oraFine, 101, 1001);

        // Calcola la durata della prenotazione
        Time durata = prenotazione.getDurata();

        // Verifica la durata (dovrebbe essere 2 ore e 30 minuti)
        long expectedDurataMillis = 2 * 60 * 60 * 1000 + 30 * 60 * 1000; // 2 ore e 30 minuti in millisecondi
        assertEquals(expectedDurataMillis, durata.getTime());

        // Verifica il formato della durata in ore e minuti
        String durataFormattata = prenotazione.getDurataInFormatoOreMinuti();
        assertEquals("02:30", durataFormattata);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDurataNonValida() {
        Time oraInizio = Time.valueOf("12:30:00");
        Time oraFine = Time.valueOf("10:00:00");
        Prenotazione prenotazione = new Prenotazione(Date.valueOf("2025-01-10"), oraInizio, oraFine, 123, 456);

        prenotazione.getDurata(); // Deve lanciare IllegalArgumentException
    }

    @Test
    public void testDurataInFormatoOreMinuti() {
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("16:45:00");
        Prenotazione prenotazione = new Prenotazione(Date.valueOf("2025-01-10"), oraInizio, oraFine, 123, 456);

        String durata = prenotazione.getDurataInFormatoOreMinuti();
        assertEquals("02:45", durata);
    }

    @Test
    public void testToString() {
        Date data = Date.valueOf("2025-01-10");
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("15:30:00");
        int utenteID = 123;
        int campoID = 456;

        Prenotazione prenotazione = new Prenotazione(data, oraInizio, oraFine, utenteID, campoID);

        String expected = "Inizio: 14:00:00 | Fine: 15:30:00 | Utente ID: 123 | Campo ID: 456";
        assertEquals(expected, prenotazione.toString());
    }
}
