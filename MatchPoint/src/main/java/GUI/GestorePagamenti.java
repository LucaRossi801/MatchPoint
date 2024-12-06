package GUI;

import components.Prenotazione;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GestorePagamenti {
	private boolean pagamentoEffettuato; // Stato del pagamento
	
    // Metodo per mostrare la schermata di pagamento iniziale
    public void mostraSchermataPagamento(Prenotazione prenotazione) {
    	pagamentoEffettuato=false;
        double costo = prenotazione.calcolaCosto();

        // Crea il dialogo per il pagamento
        JDialog pagamentoDialog = new JDialog();
        pagamentoDialog.setTitle("Pagamento");
        pagamentoDialog.setSize(400, 200);
        pagamentoDialog.setLocationRelativeTo(null);
        pagamentoDialog.setLayout(new BorderLayout());

        // Messaggio di pagamento
        JLabel messaggio = new JLabel("Il pagamento è di € " + costo + ". Inserire la carta.", SwingConstants.CENTER);
        messaggio.setFont(new Font("Arial", Font.BOLD, 16));
        pagamentoDialog.add(messaggio, BorderLayout.CENTER);

        // Imposta un listener per il clic del mouse
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
                pagamentoDialog.dispose();
            }
        });

        pagamentoDialog.setVisible(true);
    }

    // Metodo per mostrare la schermata di conferma del pagamento
    private void mostraSchermataConferma(boolean successo) {
        JFrame confermaFrame = new JFrame();
        confermaFrame.setSize(400, 200);
        confermaFrame.setLocationRelativeTo(null);
        confermaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel messaggio = new JLabel(successo ? "Pagamento effettuato con successo" : "Pagamento fallito", SwingConstants.CENTER);
        messaggio.setFont(new Font("Arial", Font.BOLD, 16));
        confermaFrame.add(messaggio);
        confermaFrame.getContentPane().setBackground(successo ? Color.GREEN : Color.RED);

        confermaFrame.setVisible(true);
    }

    // Metodo per gestire il pagamento di una prenotazione modificata
    public void gestisciPagamentoModificato(Prenotazione nuovaPrenotazione, Prenotazione vecchiaPrenotazione) {
        double nuovoCosto = nuovaPrenotazione.calcolaCosto();
        double vecchioCosto = vecchiaPrenotazione.calcolaCosto();

        if (nuovoCosto > vecchioCosto) {
            mostraSchermataPagamento(nuovaPrenotazione);
        } else if (nuovoCosto < vecchioCosto) {
            double differenza = vecchioCosto - nuovoCosto;

            int scelta = JOptionPane.showConfirmDialog(null,
                "Il costo è diminuito di € " + differenza + ". Vuoi un rimborso?",
                "Rimborso",
                JOptionPane.YES_NO_OPTION);

            if (scelta == JOptionPane.YES_OPTION) {
                mostraSchermataRimborso(differenza);
            }
        }
    }

    // Metodo per mostrare la schermata di rimborso
    private void mostraSchermataRimborso(double rimborso) {
        JFrame rimborsoFrame = new JFrame();
        rimborsoFrame.setSize(400, 200);
        rimborsoFrame.setLocationRelativeTo(null);
        rimborsoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel messaggio = new JLabel("Rimborso di € " + rimborso + " effettuato.", SwingConstants.CENTER);
        messaggio.setFont(new Font("Arial", Font.BOLD, 16));
        rimborsoFrame.add(messaggio);
        rimborsoFrame.getContentPane().setBackground(Color.BLUE);

        rimborsoFrame.setVisible(true);
    }
    
    public boolean isPagamentoEffettuato() {
        return pagamentoEffettuato;
    }
}
