package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Classe personalizzata per creare un'etichetta JLabel con testo bordato.
 * Consente di avere un bordo di colore contrastante attorno al testo per una
 * migliore leggibilità.
 */
public class OutlinedLabel extends JLabel {
	private Color outlineColor; // Variabile d'istanza per il colore del contorno

	/**
	 * Costruttore per inizializzare l'etichetta bordata.
	 * 
	 * @param text  Il testo da visualizzare nell'etichetta.
	 * @param color Il colore del testo principale. Deve essere {@code Color.BLACK}
	 *              o {@code Color.WHITE}.
	 * @throws IllegalArgumentException Se il colore passato non è supportato.
	 */
	public OutlinedLabel(String text, Color color) { // Si passa come variabile il colore che desidero interno (o nero
														// con bordo bianco o viceversa)
		super(text);
		setFont(new Font("Montserrat", Font.BOLD, 20)); // Font personalizzato

		// Imposta outlineColor e il colore del testo in base al parametro passato
		if (color == Color.WHITE) {
			this.outlineColor = Color.BLACK; // Colore del bordo nero
			setForeground(Color.WHITE); // Colore interno del testo
		} else if (color == Color.BLACK) {
			this.outlineColor = Color.WHITE; // Colore del bordo bianco
			setForeground(Color.BLACK); // Colore interno del testo
		} else {
			throw new IllegalArgumentException("Colore non supportato. Usa Color.BLACK o Color.WHITE.");
		}
	}

	/**
	 * Override del metodo di rendering per disegnare il bordo attorno al testo.
	 * 
	 * @param g Il contesto grafico utilizzato per disegnare.
	 */
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		String text = getText();
		if (text != null && !text.isEmpty()) {
			FontMetrics fm = g2.getFontMetrics(getFont());
			int x = 0; // Posizione orizzontale
			int y = fm.getAscent(); // Posizione verticale

			// Disegna il bordo
			g2.setColor(outlineColor);
			g2.drawString(text, x - 1, y - 1);
			g2.drawString(text, x + 1, y - 1);
			g2.drawString(text, x - 1, y + 1);
			g2.drawString(text, x + 1, y + 1);

			// Disegna il testo principale
			g2.setColor(getForeground());
			g2.drawString(text, x, y);
		}

		g2.dispose();
	}

}