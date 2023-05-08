package net.codejava;

import java.util.*;
import java.util.List;
import javax.swing.*;
import net.codejava.Menu;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Registrazione extends JFrame {
	
	private JTextField campoNome;
    private JPasswordField campoPassword;

    public Registrazione() {
        // Imposta le dimensioni della finestra
        setSize(320, 480);

        // Imposta il titolo della finestra
        setTitle("Registrazione");
        setLocationRelativeTo(null);
        
        // Crea un pannello per i campi di input
        JPanel pannelloInput = new JPanel(new GridLayout(4, 1));

        // Crea un campo di testo per il nome utente
        JLabel etichettaNome = new JLabel("Nome utente", JLabel.CENTER);
        campoNome = new JTextField();

        // Crea un campo di testo per la password
        JLabel etichettaPassword = new JLabel("Password", JLabel.CENTER);
        campoPassword = new JPasswordField();
        
		etichettaPassword.setForeground(Color.white);
		etichettaNome.setForeground(Color.white);
		etichettaPassword.setFont(new Font("Verdana", Font.PLAIN, 20));
		etichettaNome.setFont(new Font("Verdana", Font.PLAIN, 20));
		campoNome.setPreferredSize(new Dimension(200, 100));
		Insets insets = new Insets(20, 20, 20, 20);
		campoNome.setMargin(insets);
		campoPassword.setPreferredSize(new Dimension(200, 100));
		campoPassword.setMargin(insets);
		pannelloInput.setBackground(Color.black);        
        pannelloInput.add(etichettaNome);
        pannelloInput.add(campoNome);
        pannelloInput.add(etichettaPassword);
        pannelloInput.add(campoPassword);
        
        
        // Crea un pulsante per la registrazione
        JButton pulsanteRegistrazione = new JButton("Registrazione");
        
        pulsanteRegistrazione.setFont(new Font("Verdana", Font.PLAIN, 20));
        pulsanteRegistrazione.setPreferredSize(new Dimension(200, 80));
        pulsanteRegistrazione.setForeground(Color.white);
        pulsanteRegistrazione.setBackground(Color.black);
        
        pulsanteRegistrazione.addActionListener(e -> {
            try {
                // Carica il driver JDBC per MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Crea la connessione al database
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/betting", "root", "BlaBlaBla24.");

                // Crea la query per l'inserimento dei dati
                String query = "INSERT INTO accesso (nome, password) VALUES (?, ?)";
                String query2 = "INSERT INTO utente (nome, saldo) VALUES (?, 0)";
                // Crea lo statement e imposta i parametri
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, campoNome.getText());
                stmt.setString(2, new String(campoPassword.getPassword()));
                
                PreparedStatement stmt1 = conn.prepareStatement(query2);
                stmt1.setString(1, campoNome.getText());

                // Esegue la query
                stmt.executeUpdate();
                stmt1.executeUpdate();

                // Chiude la connessione al database
                stmt.close();
                stmt1.close();
                conn.close();

                // Mostra un messaggio di conferma
                JOptionPane.showMessageDialog(this, "Registrazione effettuata con successo");
                
                new Login();
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Errore durante la registrazione");
            }
        });

        // Crea un pannello per il pulsante di registrazione
        JPanel pannelloRegistrazione = new JPanel();
        pannelloRegistrazione.setBackground(Color.black);
        pannelloRegistrazione.add(pulsanteRegistrazione);

        // Crea un pannello principale per la finestra
        JPanel pannelloPrincipale = new JPanel(new BorderLayout());
        pannelloPrincipale.setBackground(Color.black);
        pannelloPrincipale.add(pannelloInput, BorderLayout.CENTER);
        pannelloPrincipale.add(pannelloRegistrazione, BorderLayout.SOUTH);

        // Aggiungi il pannello principale alla finestra
        add(pannelloPrincipale);

        // Imposta la chiusura della finestra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Mostra la finestra
        setVisible(true);
    }

    public static void main(String[] args) {
        new Registrazione();
    }
}