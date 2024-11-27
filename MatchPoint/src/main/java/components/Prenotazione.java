package components;
import java.sql.Time;

public class Prenotazione {
	
		private Time oraInizio;
		private Time oraFine;
		private int utenteID;
		private int campoID;
		
		
		
		public Prenotazione(Time oraInizio, Time oraFine, int utenteID, int campoID) {
			super();
			this.oraInizio = oraInizio;
			this.oraFine = oraFine;
			this.utenteID = utenteID; 
			this.campoID = campoID; 
		}
		@Override
		public String toString() {
		    return String.format("Inizio: %s | Fine: %s | Utente ID: %d | Campo ID: %d",
		            oraInizio, oraFine, utenteID, campoID);
		}

		
}
