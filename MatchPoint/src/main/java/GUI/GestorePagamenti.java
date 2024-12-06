package GUI;

import components.Prenotazione;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GestorePagamenti {
	private JDialog pagamentoDialog; // Dialogo per il pagamento
	private JPanel mainPanel;       // Pannello principale
    private boolean pagamentoEffettuato; // Stato del pagamento

    public boolean isPagamentoEffettuato() {
        return pagamentoEffettuato;
    }

    // Metodo per mostrare la schermata di pagamento iniziale
    public void mostraSchermataPagamento(Prenotazione prenotazione, double vecchioCosto) {
    	pagamentoEffettuato = false; // Stato iniziale del pagamento
        double costo = prenotazione.calcolaCosto();

        // Inizializza il dialogo
        pagamentoDialog = new JDialog((Frame) null, "Pagamento", true); // Modale
        pagamentoDialog.setSize(450, 250);
        pagamentoDialog.setLocationRelativeTo(null); // Centra la finestra
        pagamentoDialog.setLayout(new BorderLayout());
        pagamentoDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Inizializza il pannello principale
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 255, 153)); // Giallo chiaro
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margini interni
        pagamentoDialog.add(mainPanel);

        // Etichetta del messaggio
        if (vecchioCosto!=0) {
            JLabel messaggio = new JLabel(
                    "<html><center>La differenza dovuta è di <b>€ " + (costo-vecchioCosto) + "</b>.<br>Inserire la carta per continuare.</center></html>",
                    SwingConstants.CENTER
                );
                messaggio.setFont(new Font("Arial", Font.BOLD, 18));
                messaggio.setForeground(Color.DARK_GRAY); // Colore del testo
                mainPanel.add(messaggio, BorderLayout.CENTER);
        }else {
	        JLabel messaggio = new JLabel(
	            "<html><center>Il pagamento è di <b>€ " + costo + "</b>.<br>Inserire la carta per continuare.</center></html>",
	            SwingConstants.CENTER
	        );
	        messaggio.setFont(new Font("Arial", Font.BOLD, 18));
	        messaggio.setForeground(Color.DARK_GRAY); // Colore del testo
	        mainPanel.add(messaggio, BorderLayout.CENTER);
        }

        // Listener per il clic del mouse
        pagamentoDialog.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getX() >= 0 && e.getX() <= pagamentoDialog.getWidth() &&
                    e.getY() >= 0 && e.getY() <= pagamentoDialog.getHeight()) {
                    pagamentoEffettuato = true; // Pagamento riuscito
                    mostraSchermataConferma(true);
                } else {
                    pagamentoEffettuato = false; // Pagamento fallito
                    mostraSchermataConferma(false);
                }
            }
        });

        pagamentoDialog.setVisible(true); // Mostra il dialogo e aspetta
    }

    // Metodo per mostrare la schermata di conferma del pagamento
    private void mostraSchermataConferma(boolean successo) {
        if (mainPanel != null) {
            // Cambia il colore del pannello in base al risultato
            mainPanel.setBackground(successo ? new Color(34, 139, 34) : new Color(255, 69, 0)); // Verde o rosso

            // Aggiorna il messaggio
            for (Component component : mainPanel.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel label = (JLabel) component;
                    label.setText("<html><center>" +
                        (successo ? "Pagamento effettuato con successo!" : "Pagamento fallito") +
                        "</center></html>");
                    label.setForeground(Color.WHITE); // Colore del testo per contrasto
                    label.repaint(); // Aggiorna il label
                }
            }

            mainPanel.repaint(); // Aggiorna il pannello visivamente

            // Usa un Timer per chiudere il dialogo dopo 2 secondi
            new Timer(2000, e -> pagamentoDialog.dispose()).start(); // Chiude il dialogo dopo 2000ms (2 secondi)
        }
    }


 // Metodo per gestire il pagamento di una prenotazione modificata
    public void gestisciPagamentoModificato(Prenotazione nuovaPrenotazione, Prenotazione vecchiaPrenotazione) {
        double nuovoCosto = nuovaPrenotazione.calcolaCosto();
        double vecchioCosto = vecchiaPrenotazione.calcolaCosto();

        // Gestione pagamento in caso di aumento del costo
        if (nuovoCosto > vecchioCosto) {
            mostraSchermataPagamento(nuovaPrenotazione, vecchioCosto);
        } else if (nuovoCosto < vecchioCosto) {
            double differenza = vecchioCosto - nuovoCosto;

            // Mostra una finestra di conferma per il rimborso
            boolean scelta = CustomMessageWithChoice.show(
            	    "Il costo è diminuito di € " + differenza + ". Vuoi un rimborso?",
            	    "Rimborso",
            	    true // Successo (verde)
            	);

            	if (scelta) {
            	    mostraSchermataRimborso(differenza);
            	}
        }
    }

    // Metodo per mostrare la schermata di rimborso
    private void mostraSchermataRimborso(double rimborso) {
        // Crea una finestra per il rimborso
        JFrame rimborsoFrame = new JFrame("Rimborso");
        rimborsoFrame.setSize(400, 200);
        rimborsoFrame.setLocationRelativeTo(null); // Centra la finestra
        rimborsoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crea il messaggio di rimborso
        JLabel messaggio = new JLabel("Rimborso di € " + rimborso + " effettuato.", SwingConstants.CENTER);
        messaggio.setFont(new Font("Arial", Font.BOLD, 16));
        messaggio.setForeground(Color.WHITE); // Colore del testo

        // Imposta il colore del background (verde per successo)
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(34, 139, 34)); // Verde chiaro
        contentPane.setLayout(new BorderLayout());
        contentPane.add(messaggio, BorderLayout.CENTER);

        // Imposta il contenuto del frame
        rimborsoFrame.setContentPane(contentPane);
        rimborsoFrame.setVisible(true);

        // Usa un Timer per chiudere la finestra dopo 3 secondi
        new Timer(3000, e -> rimborsoFrame.dispose()).start();
    }


    
}
