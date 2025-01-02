// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dataBase.DataBase;
import individui.Gestore;

public class CentroSportivo
{
	//private Gestore gestore;
	private int ID;
	private String nome;
	private String provincia;	
	private String comune;
	
	public CentroSportivo(int iD, String nome, String provincia, String comune) {
		super();
		ID = iD;
		this.nome = nome;
		this.provincia = provincia;
		this.comune = comune;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getComune() {
		return comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public static void inserisci(String nome, String provincia, String comune) {
			String url = "jdbc:sqlite:src/main/java/dataBase/matchpointDB.db"; //connessione al database
			try  (Connection conn = DriverManager.getConnection(url)){
				DataBase.insert(conn, nome, provincia, comune, Sessione.getId());
			} catch (SQLException e) {
				e.printStackTrace();
			} 
	}

	
}
