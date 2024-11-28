package localizzazione;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileReaderUtils {
    public static Map<String, List<String>> leggiProvinceEComuni(String filePath) {
        Map<String, List<String>> provinceComuni = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Ignora la prima riga (titoli)
            if ((line = br.readLine()) != null) {
                // Legge e scarta la prima riga
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";"); // Cambia il separatore in ";"
                if (parts.length >= 2) {
                    String provincia = parts[0].trim(); // Colonna sigla_provincia
                    String comune = parts[1].trim(); // Colonna denominazione_ita

                    provinceComuni.putIfAbsent(provincia, new ArrayList<>());
                    provinceComuni.get(provincia).add(comune);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return provinceComuni;
    }
}
