package components;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.util.List;

import GUI.CustomMessage;
import dataBase.DataBase;

public class Prenotazione {
    private int ID;
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
    
    public Prenotazione(int ID, Date data, Time oraInizio, Time oraFine, int utenteID, int campoID) {
        this.ID = ID;
    	this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.utenteID = utenteID; 
        this.campoID = campoID; 
    }
    
    public int getId() {
    	return ID;
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
            throw new IllegalArgumentException("Orari non validi. Assicurati che ora fine sia successivo a ora inizio.");
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
    
  
    
    @Override
    public String toString() {
        return String.format("Inizio: %s | Fine: %s | Utente ID: %d | Campo ID: %d",
                oraInizio, oraFine, utenteID, campoID);
    }

}
