// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package individui;

import java.util.Date;

import components.Campo;

public class Giocatore extends Utente {
	public Giocatore(String nome, String cognome, Date dataNascita, String username, String password) {
		super(nome, cognome, dataNascita, username, password);
	}
	public Campo[] campo;

	@Override
	public void registrazione() {
		
	}

	@Override
	public void login() {
		
	}

	@Override
	public boolean registrazione(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean login(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}
}