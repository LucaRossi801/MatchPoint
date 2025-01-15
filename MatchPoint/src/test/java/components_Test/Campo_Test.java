package components_Test;

import org.junit.*;
import components.Campo;
import components.TipologiaCampo;

import static org.junit.Assert.*;

/**
 * Classe di test per la classe Campo.
 * Verifica i metodi e i costruttori della classe Campo.
 */
public class Campo_Test {

    private InMemoryDatabaseUtil dbUtil;

    @Before
    public void setUp() throws Exception {
        // Inizializza il database in-memory
        dbUtil = new InMemoryDatabaseUtil();
        dbUtil.setUp();
    }

    @After
    public void tearDown() throws Exception {
        if (dbUtil != null) {
            dbUtil.tearDown(); // Chiude il database in-memory
        }
    }
    
    //Test relativo al costruttore della classe (senza l'attributo "id" e "CentroId")
    @Test
    public void testCostruttoreBase() {
    	//Vengono impostati dei valori alle variabili
        TipologiaCampo tipologia = TipologiaCampo.badminton;
        int costoOraNotturna = 50;
        int costoOraDiurna = 30;
        int lunghezza = 100;
        int larghezza = 50;
        boolean coperto = true;
        //Crea un oggetto campo con le variabili specificate nelle righe precedenti
        Campo campo = new Campo(tipologia, costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto);
        //Controlla per ogni attributo del campo che il valore attesto corrisponda a quello ottenuto
        assertEquals(tipologia, campo.tipologiaCampo);
        assertEquals(costoOraNotturna, campo.getCostoOraNotturna());
        assertEquals(costoOraDiurna, campo.getCostoOraDiurna());
        assertEquals(lunghezza, campo.getLunghezza());
        assertEquals(larghezza, campo.getLarghezza());
        assertTrue(campo.isCoperto()); //Essendo un valore booleano si utilizza assertTrue
    }
    
    //Test relativo al costruttore della classe (anche l'attributo "id") 
    @Test
    public void testCostruttoreConID() {
    	//Vengono impostati dei valori alle variabili
        int id = 1; 
        TipologiaCampo tipologia = TipologiaCampo.calcioA7;
        int costoOraNotturna = 40;
        int costoOraDiurna = 20;
        int lunghezza = 80;
        int larghezza = 40;
        boolean coperto = false;
      //Crea un oggetto campo con le variabili specificate nelle righe precedenti
        Campo campo = new Campo(id, tipologia, costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto);
      //Controlla per ogni attributo del campo che il valore attesto corrisponda a quello ottenuto
        assertEquals(id, campo.getId());
        assertEquals(tipologia, campo.tipologiaCampo);
        assertEquals(costoOraNotturna, campo.getCostoOraNotturna());
        assertEquals(costoOraDiurna, campo.getCostoOraDiurna());
        assertEquals(lunghezza, campo.getLunghezza());
        assertEquals(larghezza, campo.getLarghezza());
        assertFalse(campo.isCoperto()); //Essendo un valore booleano si utilizza assertTrue
    }

  //Test relativo al costruttore completo della classe (anche con l'id del centro sportivo a cui appartiene)
    @Test
    public void testCostruttoreCompleto() {
    	//Vengono impostati dei valori alle variabili
        int id = 2;
        TipologiaCampo tipologia = TipologiaCampo.volley;
        int costoOraNotturna = 60;
        int costoOraDiurna = 40;
        int lunghezza = 30;
        int larghezza = 15;
        boolean coperto = true;
        int centroID = 5;
      //Crea un oggetto campo con le variabili specificate nelle righe precedenti
        Campo campo = new Campo(id, tipologia, costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto, centroID);
      //Controlla per ogni attributo del campo che il valore attesto corrisponda a quello ottenuto
        assertEquals(id, campo.getId());
        assertEquals(centroID, campo.getCentroId());
        assertEquals(tipologia, campo.tipologiaCampo);
        assertEquals(costoOraNotturna, campo.getCostoOraNotturna());
        assertEquals(costoOraDiurna, campo.getCostoOraDiurna());
        assertEquals(lunghezza, campo.getLunghezza());
        assertEquals(larghezza, campo.getLarghezza());
        assertTrue(campo.isCoperto()); //Essendo un valore booleano si utilizza assertTrue
    }
    
    //Test del metodo Clone (permette la clonazione di una campo)
    @Test
    public void testClone() {
    	//Viene generato un campo
        Campo campoOriginale = new Campo(TipologiaCampo.basket, 70, 50, 28, 15, true);
        //Viene creato un clone del campo precedente con la funzione "clone()"
        Campo campoClonato = campoOriginale.clone();
         
        assertEquals(campoOriginale, campoClonato);  //Si controlla che siamo uguali (oggetto atteso = oggetto ottenuto)
        assertNotSame(campoOriginale, campoClonato); // Il clone deve essere un nuovo oggetto.
    }
    
    //Test per il metodo Equals
    @Test
    public void testEquals() {
    	//Vengono generati due campi con le stesse caratteristiche
        Campo campo1 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);
        Campo campo2 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);
        Campo campoDiverso = new Campo(TipologiaCampo.tennis, 60, 40, 30, 15, false);  //Viene generato un campo diverso dagli altri due
        
        assertEquals(campo1, campo2);	//Si controlla che i primi due siano uguali
        assertNotEquals(campo1, campoDiverso);   //Si controlla che il primo e l'ultimo siano diversi
    }
    
    //Test per controllare gli HashCode
    @Test
    public void testHashCode() {
    	//Vengono creati due campi con le stesse caratteristiche
        Campo campo1 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);
        Campo campo2 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);
        
        assertEquals(campo1.hashCode(), campo2.hashCode());  //Si controlla che entrambi i campi abbiano lo stesso Hash Code
    }
}
