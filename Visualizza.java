package net.codejava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Visualizza extends JPanel {

    public Visualizza() {
        // Imposta il layout del pannello a box layout verticale
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Ottieni la lista dei nomi degli utenti
        String[] nomi = getNomiUtenti();

        // Aggiungi una label per ogni nome
        for (int i = 0; i < nomi.length; i++) {
            JLabel label = new JLabel(nomi[i]);
            add(label);
        }
        
        JOptionPane.showMessageDialog(null, nomi, "Lista utenti", JOptionPane.PLAIN_MESSAGE);
    }

    private String[] getNomiUtenti() {
    	String[] nomi = null;
    	int conteggio = 0;
        try {
            // Connessione al database
            String url = "jdbc:mysql://localhost:3306/betting";
            String user = "root";
            String password = "BlaBlaBla24.";
            Connection con = DriverManager.getConnection(url, user, password);

            // Esecuzione della query per ottenere i nomi degli utenti
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT nome FROM utente");
            
            if(rs.last()) {
            	conteggio = rs.getRow();
            	rs.beforeFirst();
            }
            
            nomi = new String[conteggio];           
            int i = 0;
            
            // Aggiunta dei nomi alla lista
            while (rs.next()) {
            	String nome = rs.getString("nome");
                nomi[i] = nome;
                i++;
            }

            // Chiusura delle risorse
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nomi;
    }
}
