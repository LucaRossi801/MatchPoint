package GUI;

import localizzazione.FileReaderUtils;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Classe SelezionaDialog rappresenta un dialogo per la selezione di un centro
 * sportivo. Mostra una lista di province e, in base alla selezione, visualizza
 * i centri disponibili.
 */
public class SelezionaDialog extends JDialog {
	private String selezione = null;
	private Object selezioneID = null;

	/**
	 * Costruttore per creare il dialogo di selezione dei centri sportivi.
	 *
	 * @param titolo           Il titolo del dialogo.
	 * @param filePathProvince Il percorso del file CSV contenente province e
	 *                         comuni.
	 * @param centriByProvince Mappa delle province con i centri sportivi associati.
	 * @param comuniByCentro   Mappa dei centri sportivi con i rispettivi comuni.
	 */
	public SelezionaDialog(String titolo, String filePathProvince, Map<String, Map<String, Integer>> centriByProvince,
			Map<String, String> comuniByCentro) {
		setTitle(titolo);
		setModal(true);
		setSize(500, 400);
		setLayout(new BorderLayout());

		// Legge province e comuni dal file CSV
		Map<String, List<String>> provinceComuni = FileReaderUtils.leggiProvinceEComuni(filePathProvince);

		// Creazione di un JPanel per la North con margini
		JPanel northPanel = new JPanel(new BorderLayout());
		JComboBox<String> provinciaComboBox = new JComboBox<>(provinceComuni.keySet().toArray(new String[0]));
		provinciaComboBox.setFont(new Font("Montserrat", Font.PLAIN, 18));
		northPanel.add(provinciaComboBox, BorderLayout.CENTER);
		northPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Margini personalizzati
		add(northPanel, BorderLayout.NORTH);

		// Pannello per visualizzare i centri
		JPanel centriPanel = new JPanel();
		centriPanel.setLayout(new BoxLayout(centriPanel, BoxLayout.Y_AXIS));

		// Creazione dello JScrollPane
		JScrollPane scrollPane = new JScrollPane(centriPanel);
		// Aggiungi margini vuoti all'interno dello JScrollPane
		scrollPane.setViewportView(centriPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Aggiungi margini vuoti al viewport
		add(scrollPane, BorderLayout.CENTER);

		// Aggiorna i centri al cambio di provincia
		provinciaComboBox.addActionListener(e -> {
			String provinciaSelezionata = (String) provinciaComboBox.getSelectedItem();
			if (provinciaSelezionata != null) {
				Map<String, Integer> centri = centriByProvince.get(provinciaSelezionata);
				aggiornaCentriPanel(centriPanel, centri, comuniByCentro);
			}
		});

		// Imposta la provincia iniziale
		if (!provinceComuni.isEmpty()) {
			provinciaComboBox.setSelectedIndex(0);
		}

		// Creazione del JPanel per la South con margini
		JPanel southPanel = new JPanel(new BorderLayout());
		JButton chiudiButton = BackgroundPanel.createFlatButton("Chiudi", e -> dispose(), new Dimension(100, 40));
		chiudiButton.setFont(new Font("Montserrat", Font.PLAIN, 18));
		chiudiButton.setForeground(Color.GRAY); // Sfondo grigio
		chiudiButton.setBackground(Color.DARK_GRAY); // Sfondo al passaggio del mouse
		southPanel.add(chiudiButton, BorderLayout.CENTER);
		southPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Margini personalizzati
		add(southPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null); // Centra il dialogo
	}

	/**
	 * Aggiorna il pannello con la lista dei centri sportivi in base alla provincia
	 * selezionata.
	 *
	 * @param centriPanel    Il pannello in cui visualizzare i centri.
	 * @param centri         Mappa dei centri sportivi associati alla provincia
	 *                       selezionata.
	 * @param comuniByCentro Mappa dei centri sportivi con i rispettivi comuni.
	 */
	private void aggiornaCentriPanel(JPanel centriPanel, Map<String, Integer> centri,
			Map<String, String> comuniByCentro) {
		centriPanel.removeAll();

		if (centri != null) {
			// Configura un layout flessibile
			centriPanel.setLayout(new BoxLayout(centriPanel, BoxLayout.Y_AXIS));

			// Imposta un bordo vuoto per evitare che i contenuti tocchino i bordi dello
			// scrollpane
			centriPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			for (Map.Entry<String, Integer> entry : centri.entrySet()) {
				String centroNome = entry.getKey();
				Integer centroID = entry.getValue();

				// Crea un pannello singolo per il centro con larghezza dinamica
				JPanel centroItemPanel = new JPanel(new BorderLayout());
				centroItemPanel
						.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), // Bordo
																													// nero
																													// esterno
								BorderFactory.createEmptyBorder(10, 10, 10, 10) // Margini interni
						));
				centroItemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // Larghezza flessibile
				centroItemPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allineamento a sinistra

				JLabel centroLabel = new JLabel(centroNome + " (" + comuniByCentro.get(centroNome) + ")");
				centroLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));

				// Crea un FlatButton per "Seleziona"
				JButton selezionaButton = BackgroundPanel.createFlatButton("V", e -> {
					selezione = centroNome;
					selezioneID = centroID;
					dispose(); // Chiude il dialogo
				}, new Dimension(30, 25)); // Dimensioni ridotte
				selezionaButton.setFont(new Font("Montserrat", Font.PLAIN, 14));
				selezionaButton.setBackground(new Color(34, 139, 34)); // Verde
				selezionaButton.setForeground(Color.WHITE);

				// Aggiungi i componenti al pannello
				centroItemPanel.add(centroLabel, BorderLayout.CENTER);
				centroItemPanel.add(selezionaButton, BorderLayout.EAST);

				// Aggiungi il pannello al contenitore principale
				centriPanel.add(centroItemPanel);

				// Spazio tra i centri
				centriPanel.add(Box.createVerticalStrut(10));
			}
		}

		centriPanel.revalidate();
		centriPanel.repaint();
	}

	/**
	 * Restituisce il nome del centro sportivo selezionato.
	 *
	 * @return Il nome del centro sportivo selezionato, oppure null se nessuna
	 *         selezione è stata effettuata.
	 */
	public String getSelezione() {
		return selezione;
	}

	/**
	 * Restituisce l'ID del centro sportivo selezionato.
	 *
	 * @return L'ID del centro sportivo selezionato, oppure null se nessuna
	 *         selezione è stata effettuata.
	 */
	public Object getSelezioneID() {
		return selezioneID;
	}

}
