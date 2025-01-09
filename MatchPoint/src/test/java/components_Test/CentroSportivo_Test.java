package components_Test;

import org.junit.*;
import components.CentroSportivo;

import static org.junit.Assert.*;

/**
 * Classe di test per la classe CentroSportivo.
 * Verifica il corretto funzionamento dei costruttori e dei metodi getter e setter.
 */
public class CentroSportivo_Test {

    @Test
    public void testCostruttore() {
        int id = 1;
        String nome = "Sport Center";
        String provincia = "Milano";
        String comune = "Rho";

        CentroSportivo centro = new CentroSportivo(id, nome, provincia, comune);

        assertEquals(id, centro.getID());
        assertEquals(nome, centro.getNome());
        assertEquals(provincia, centro.getProvincia());
        assertEquals(comune, centro.getComune());
    }

    @Test
    public void testGetterSetterID() {
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");

        centro.setID(2);
        assertEquals(2, centro.getID());
    }

    @Test
    public void testGetterSetterNome() {
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");

        centro.setNome("New Name");
        assertEquals("New Name", centro.getNome());
    }

    @Test
    public void testGetterSetterProvincia() {
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");

        centro.setProvincia("Torino");
        assertEquals("Torino", centro.getProvincia());
    }

    @Test
    public void testGetterSetterComune() {
        CentroSportivo centro = new CentroSportivo(1, "Sport Center", "Milano", "Rho");

        centro.setComune("Moncalieri");
        assertEquals("Moncalieri", centro.getComune());
    }
}
