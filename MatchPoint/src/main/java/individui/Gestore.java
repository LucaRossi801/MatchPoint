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
	public boolean login(String username, String password) {
		if(this.password.equals(password)&&this.username.equals(username)) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean registrazione(String username, String password) {
	//accesso al database?	
		return true;
	}
}
