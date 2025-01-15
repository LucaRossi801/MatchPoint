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
	
	//Test relativo al costruttore della classe (senza l'attributo "ID")
    @Test
    public void testCostruttoreSenzaID() {
    	//Vengono impostati dei valori agli attributi della classe
        Date data = Date.valueOf("2025-01-10");
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("15:30:00");
        int utenteID = 123;
        int campoID = 456;
        
       //Viene creato un nuovo oggetto Prenotazione
        Prenotazione prenotazione = new Prenotazione(data, oraInizio, oraFine, utenteID, campoID);
        
        //Si controlla che i valori ottenuti sinoa uguali a quelli attesi
        assertEquals(data, prenotazione.getData());
        assertEquals(oraInizio, prenotazione.getOraInizio());
        assertEquals(oraFine, prenotazione.getOraFine());
        assertEquals(utenteID, prenotazione.getUtenteId());
        assertEquals(campoID, prenotazione.getCampoId());
    }
    
  //Test relativo al costruttore della classe
    @Test
    public void testCostruttoreConID() {
    	//Vengono impostati dei valori agli attributi della classe
        int ID = 1;
        Date data = Date.valueOf("2025-01-10");
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("15:30:00");
        int utenteID = 123;
        int campoID = 456;

      //Viene creato un nuovo oggetto Prenotazione
        Prenotazione prenotazione = new Prenotazione(ID, data, oraInizio, oraFine, utenteID, campoID);

      //Si controlla che i valori ottenuti siano uguali a quelli attesi
        assertEquals(ID, prenotazione.getId());
        assertEquals(data, prenotazione.getData());
        assertEquals(oraInizio, prenotazione.getOraInizio());
        assertEquals(oraFine, prenotazione.getOraFine());
        assertEquals(utenteID, prenotazione.getUtenteId());
        assertEquals(campoID, prenotazione.getCampoId());
    }

  //Test relativo al metodo GetDurata
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
    
    //Test se la data non è valida
    @Test(expected = IllegalArgumentException.class)
    public void testDurataNonValida() {
    	// Crea orari di inizio e fine
        Time oraInizio = Time.valueOf("12:30:00");
        Time oraFine = Time.valueOf("10:00:00");
        
        // Crea un oggetto Prenotazione con i dati sopra
        Prenotazione prenotazione = new Prenotazione(Date.valueOf("2025-01-10"), oraInizio, oraFine, 123, 456);

        prenotazione.getDurata(); // Deve lanciare IllegalArgumentException
    }
    
    //Test per il metodo che trasforma la durata in formato ore e minuti
    @Test
    public void testDurataInFormatoOreMinuti() {
    	// Crea orari di inizio e fine
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("16:45:00");
        
        // Crea un oggetto Prenotazione con i dati sopra
        Prenotazione prenotazione = new Prenotazione(Date.valueOf("2025-01-10"), oraInizio, oraFine, 123, 456);
        
        String durata = prenotazione.getDurataInFormatoOreMinuti();  // Lancia il metodo
        assertEquals("02:45", durata);	//Controlla che il valore atteso sia uguale a quello ottenuto tramite metodo
    }
    
    //Test relativo al metodo che trasforma la prenotazione in una stringa di caratteri
    @Test
    public void testToString() {
    	// Vengono generati dei valori per gli attributi di una prenotazione
        Date data = Date.valueOf("2025-01-10");
        Time oraInizio = Time.valueOf("14:00:00");
        Time oraFine = Time.valueOf("15:30:00");
        int utenteID = 123;
        int campoID = 456;
        
        // Crea un oggetto Prenotazione con i dati sopra
        Prenotazione prenotazione = new Prenotazione(data, oraInizio, oraFine, utenteID, campoID);
        
        //Crea una stringa con valori identici a quelli attribuiti alle variabili
        String expected = "Inizio: 14:00:00 | Fine: 15:30:00 | Utente ID: 123 | Campo ID: 456";
        assertEquals(expected, prenotazione.toString());	//Controlla che la stringa attesa sia uguale a quella ottenuto con il metodo
    }
}
