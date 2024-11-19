package individui;

import java.util.Date;

public class Gestore extends Utente {
	
	public String certificazione;
	public String competenze;
	public Gestore(String nome, String cognome, Date dataNascita, String certificazione, String competenze, String username, String password) {
		super(nome, cognome, dataNascita, username, password);
		this.certificazione=certificazione;
		this.competenze=competenze;
	}

	@Override
	public int registrazione(String nome, String cognome, Date dataNascita, String email, String username, String password, String Certificazioni, String Competenze) {
	//accesso al database?	
		return 1;
	}
}
