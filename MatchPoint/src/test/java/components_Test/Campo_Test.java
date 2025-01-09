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
            dbUtil.tearDown(); // Chiude il database
        }
    }

    @Test
    public void testCostruttoreBase() {
        TipologiaCampo tipologia = TipologiaCampo.badminton;
        int costoOraNotturna = 50;
        int costoOraDiurna = 30;
        int lunghezza = 100;
        int larghezza = 50;
        boolean coperto = true;

        Campo campo = new Campo(tipologia, costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto);

        assertEquals(tipologia, campo.tipologiaCampo);
        assertEquals(costoOraNotturna, campo.getCostoOraNotturna());
        assertEquals(costoOraDiurna, campo.getCostoOraDiurna());
        assertEquals(lunghezza, campo.getLunghezza());
        assertEquals(larghezza, campo.getLarghezza());
        assertTrue(campo.isCoperto());
    }

    @Test
    public void testCostruttoreConID() {
        int id = 1;
        TipologiaCampo tipologia = TipologiaCampo.calcioA7;
        int costoOraNotturna = 40;
        int costoOraDiurna = 20;
        int lunghezza = 80;
        int larghezza = 40;
        boolean coperto = false;

        Campo campo = new Campo(id, tipologia, costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto);

        assertEquals(id, campo.getId());
        assertEquals(tipologia, campo.tipologiaCampo);
        assertEquals(costoOraNotturna, campo.getCostoOraNotturna());
        assertEquals(costoOraDiurna, campo.getCostoOraDiurna());
        assertEquals(lunghezza, campo.getLunghezza());
        assertEquals(larghezza, campo.getLarghezza());
        assertFalse(campo.isCoperto());
    }

    @Test
    public void testCostruttoreCompleto() {
        int id = 2;
        TipologiaCampo tipologia = TipologiaCampo.volley;
        int costoOraNotturna = 60;
        int costoOraDiurna = 40;
        int lunghezza = 30;
        int larghezza = 15;
        boolean coperto = true;
        int centroID = 5;

        Campo campo = new Campo(id, tipologia, costoOraNotturna, costoOraDiurna, lunghezza, larghezza, coperto, centroID);

        assertEquals(id, campo.getId());
        assertEquals(centroID, campo.getCentroId());
        assertEquals(tipologia, campo.tipologiaCampo);
        assertEquals(costoOraNotturna, campo.getCostoOraNotturna());
        assertEquals(costoOraDiurna, campo.getCostoOraDiurna());
        assertEquals(lunghezza, campo.getLunghezza());
        assertEquals(larghezza, campo.getLarghezza());
        assertTrue(campo.isCoperto());
    }

    @Test
    public void testClone() {
        Campo campoOriginale = new Campo(TipologiaCampo.basket, 70, 50, 28, 15, true);

        Campo campoClonato = campoOriginale.clone();

        assertEquals(campoOriginale, campoClonato);
        assertNotSame(campoOriginale, campoClonato); // Il clone deve essere un nuovo oggetto.
    }

    @Test
    public void testEquals() {
        Campo campo1 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);
        Campo campo2 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);
        Campo campoDiverso = new Campo(TipologiaCampo.tennis, 60, 40, 30, 15, false);

        assertEquals(campo1, campo2);
        assertNotEquals(campo1, campoDiverso);
    }

    @Test
    public void testHashCode() {
        Campo campo1 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);
        Campo campo2 = new Campo(TipologiaCampo.calcioA11, 50, 30, 100, 50, true);

        assertEquals(campo1.hashCode(), campo2.hashCode());
    }
}
