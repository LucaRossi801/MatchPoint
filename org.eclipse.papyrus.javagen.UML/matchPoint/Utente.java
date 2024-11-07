package matchPoint;

import java.util.Date;
private static final int COUNT=1; //counter used for ID

public abstract class Utente {

	public int ID;
	public String nome;
	public String cognome;
	public Date dataNascita;
	public int eta;
	


	public Utente(String nome, String cognome, Date dataNascita) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.dataNascita = dataNascita;
	}
	
	public Utente() {
		
	}

	public void registrazione() {
	}

	public void login() {
	}
	
	
}
