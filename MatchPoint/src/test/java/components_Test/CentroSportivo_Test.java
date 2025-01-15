package components_Test;

import org.junit.*;
import components.CentroSportivo;

import static org.junit.Assert.*;

/**
 * Classe di test per la classe CentroSportivo.
 * Verifica il corretto funzionamento dei costruttori e dei metodi getter e setter.
 */
public class CentroSportivo_Test {

	//Test relativo al costruttore della classe 
    @Test
    public void testCostruttore() {
    	//Vengono impostati dei valori per gli attributi
        int id = 1;
        String nome = "Sport Center";
        String provincia = "Milano";
        String comune = "Rho";
        //Viene generato un nuovo oggetto Centro Sportivo avente i dati inseriti
        CentroSportivo centro = new CentroSportivo(id, nome, provincia, comune);
        //Si controlla che il centro sportivo ottenuto tramite metodo sia uguale a quello atteso
        assertEquals(id, centro.getID());
        assertEquals(nome, centro.getNome());
        assertEquals(provincia, centro.getProvincia());
        assertEquals(comune, centro.getComune());
    }
    
    //Test per il metodo GetterSetter (impostando l'ID del centro)
    @Test
    public void testGetterSetterID() {
    	//Viene generato un nuovo oggetto Centro Sportivo
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");
        
        centro.setID(2);  //Viene impostato un nuovo ID del centro
        assertEquals(2, centro.getID());   //Si controlla che l'ID del centro atteso (2) sia uguale al valore ottenuto con metodo
    }

  //Test per il metodo GetterSetter (impostando il nome del centro)
    @Test
    public void testGetterSetterNome() {
    	//Viene generato un nuovo oggetto Centro Sportivo
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");

        centro.setNome("New Name");	//Viene impostato un nuovo nome per il centro
        assertEquals("New Name", centro.getNome());	//Si controlla che il nome del centro atteso sia uguale al valore ottenuto con metodo
    }
    
  //Test per il metodo GetterSetter (impostando la provincia del centro)
    @Test
    public void testGetterSetterProvincia() {
    	//Viene generato un nuovo oggetto Centro Sportivo
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");

        centro.setProvincia("Torino");	//Viene impostata una nuova provincia per il centro
        assertEquals("Torino", centro.getProvincia());	//Si controlla che la provincia del centro attesa sia uguale al valore ottenuto con metodo
    }
    
  //Test per il metodo GetterSetter (impostando il comune del centro)
    @Test
    public void testGetterSetterComune() {
    	//Viene generato un nuovo oggetto Centro Sportivo
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");

        centro.setComune("Moncalieri");	//Viene impostata un nuovo comune del centro
        assertEquals("Moncalieri", centro.getComune());	//Si controlla che il comune del centro atteso sia uguale al valore ottenuto con metodo
    }
}
