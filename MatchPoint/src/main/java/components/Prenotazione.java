package components;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.util.List;

import GUI.CustomMessage;
import dataBase.DataBase;

public class Prenotazione {
    private Date data;
    private Time oraInizio;
    private Time oraFine;
    private int utenteID;
    private int campoID;

    public Prenotazione(Date data, Time oraInizio, Time oraFine, int utenteID, int campoID) {
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.utenteID = utenteID; 
        this.campoID = campoID; 
    }

    public Date getData() {
        return data;
    }

    public Time getOraInizio() {
        return oraInizio;
    }

    public Time getOraFine() {
        return oraFine;
    }
    
    public Time getDurata() {
        if (oraInizio == null || oraFine == null || oraFine.before(oraInizio)) {
            throw new IllegalArgumentException("Orari non validi. Assicurati che oraFine sia successivo a oraInizio.");
        }

        // Calcolare la durata in millisecondi
        long durataInMillis = oraFine.getTime() - oraInizio.getTime();

        // Convertire la durata in millisecondi in un oggetto Time
        return new Time(durataInMillis);
    }

    public String getDurataInFormatoOreMinuti() {
        Time durata = getDurata();

        // Converting Time to hours and minutes
        long totalMinutes = durata.getTime() / 60000; // Convert milliseconds to minutes
        long ore = totalMinutes / 60;
        long minuti = totalMinutes % 60;

        // Restituisce la durata in formato "HH:mm"
        return String.format("%02d:%02d", ore, minuti);
    }
    
    public int getCampoId () {
    	return campoID;
    }
    
    public int getUtenteId () {
    	return utenteID;
    }
    
    public double calcolaCosto() {
    	//recupera il campo a cui è stata effettuata la prenotazione
    	Campo campo = DataBase.getCampoById(campoID);
    	int costoOraNotturna = campo.costoOraNotturna;
    	int costoOraDiurna = campo.costoOraDiurna;
    	
        // Verifica che gli orari siano validi
        if (oraInizio == null || oraFine == null || oraFine.before(oraInizio)) {
            throw new IllegalArgumentException("Orari non validi. Assicurati che oraFine sia successivo a oraInizio.");
        }

        // Calcola la durata in millisecondi
        long durataInMillis = oraFine.getTime() - oraInizio.getTime();
        
        // Se la durata è negativa, la prenotazione non è valida
        if (durataInMillis < 0) {
            throw new IllegalArgumentException("La durata della prenotazione non può essere negativa.");
        }

        // Converte la durata in minuti
        long durataInMinuti = durataInMillis / 60000;
        
        // Definire gli orari limite per la divisione tra giorno e notte
        Time ore18 = Time.valueOf("18:00:00");
        
        // Calcolare la durata delle ore diurne e notturne
        double costoTotale = 0.0;
        
        // Se la prenotazione finisce prima delle 18:00
        if (oraFine.before(ore18)) {
            costoTotale = (durataInMinuti / 60.0) * costoOraDiurna;
        }
        // Se la prenotazione inizia dopo le 18:00
        else if (oraInizio.after(ore18)) {
            costoTotale = (durataInMinuti / 60.0) * costoOraNotturna;
        }
        // Altrimenti la prenotazione è divisa tra giorno e notte
        else {
            // Ore diurne (dalla partenza fino alle 18:00)
            long minutiDiurni = Duration.between(oraInizio.toLocalTime(), ore18.toLocalTime()).toMinutes();
            costoTotale += (minutiDiurni / 60.0) * costoOraDiurna;

            // Ore notturne (dalle 18:00 in poi)
            long minutiNotturni = durataInMinuti - minutiDiurni;
            costoTotale += (minutiNotturni / 60.0) * costoOraNotturna;
        }
        
        return costoTotale;
    }
    
    public boolean verificaDisponibilita() {
    	
    	Campo campo = DataBase.getCampoById(campoID);
        // Recupera prenotazioni esistenti per lo stesso campo e data
        List<Prenotazione> prenotazioniEsistenti = DataBase.getPrenotazioniByCampo(campoID, campo.getCentroId());

        // Controlla sovrapposizione con le prenotazioni esistenti
        for (Prenotazione prenotazione : prenotazioniEsistenti) {
            Time oraInizioEsistente = prenotazione.getOraInizio();
            Time oraFineEsistente = prenotazione.getOraFine();

            if (isOverlapping(oraInizio, oraFine, oraInizioEsistente, oraFineEsistente)) {
                // Mostra messaggio di avviso tramite CustomMessage
                CustomMessage.show("Il campo è già prenotato per l'intervallo di tempo selezionato.", "Attenzione", false);
                return false;
            }
        }

        // Nessuna sovrapposizione trovata, campo disponibile
        return true;
    }

    private boolean isOverlapping(Time start1, Time end1, Time start2, Time end2) {
        // Verifica se l'intervallo [start1, end1] si sovrappone a [start2, end2]
        return !end1.before(start2) && !start1.after(end2);
    }


    @Override
    public String toString() {
        return String.format("Inizio: %s | Fine: %s | Utente ID: %d | Campo ID: %d",
                oraInizio, oraFine, utenteID, campoID);
    }

}
