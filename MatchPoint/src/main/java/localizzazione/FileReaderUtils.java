package localizzazione;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Classe di utilità per la lettura di file di testo contenenti province e
 * comuni. Questa classe fornisce un metodo per leggere un file CSV e restituire
 * una mappa che associa ogni provincia ai comuni appartenenti a essa.
 */
public class FileReaderUtils {

	/**
	 * Legge un file CSV e restituisce una mappa che associa ogni provincia a una
	 * lista di comuni.
	 * <p>
	 * Il file deve avere un formato CSV con il separatore ";". La prima colonna
	 * rappresenta la provincia (sigla_provincia) e la seconda colonna il comune
	 * (denominazione_ita). La prima riga del file viene ignorata, dato che contiene
	 * i titoli delle colonne.
	 * </p>
	 *
	 * @param filePath il percorso del file CSV da leggere.
	 * @return una mappa che associa ogni sigla di provincia a una lista di comuni
	 *         appartenenti.
	 */
	public static Map<String, List<String>> leggiProvinceEComuni(String filePath) {
		// Mappa per contenere le province e i rispettivi comuni
		Map<String, List<String>> provinceComuni = new TreeMap<>();

		// Utilizza un BufferedReader per leggere il file
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Ignora la prima riga (assumendo che contenga i titoli delle colonne)
			if ((line = br.readLine()) != null) {
				// La riga viene letta e ignorata
			}

			// Leggi ogni riga successiva del file
			while ((line = br.readLine()) != null) {
				// Divide la riga in parti utilizzando il separatore ";"
				String[] parts = line.split(";");

				// Verifica che la riga abbia almeno due colonne (provincia e comune)
				if (parts.length >= 2) {
					// Estrae e pulisce i valori di provincia e comune
					String provincia = parts[0].trim(); // Prima colonna: sigla_provincia
					String comune = parts[1].trim(); // Seconda colonna: denominazione_ita

					// Aggiunge la provincia alla mappa se non è già presente
					provinceComuni.putIfAbsent(provincia, new ArrayList<>());

					// Aggiunge il comune alla lista associata alla provincia
					provinceComuni.get(provincia).add(comune);
				}
			}
		} catch (IOException e) {
			// Stampa lo stack trace in caso di errore di lettura del file
			e.printStackTrace();
		}

		// Restituisce la mappa con le province e i comuni
		return provinceComuni;
	}
}
