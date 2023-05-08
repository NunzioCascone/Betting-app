package net.codejava;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Overview extends JFrame {
	
	private static int id;
    private JLabel overviewLabel, nomeUtenteLabel, saldoLabel, soldiLabel;	    
	private JButton depositaButton, prelevaButton, returnButton;
	private static final String DB_URL = "jdbc:mysql://localhost/betting";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "BlaBlaBla24.";
	
	public Overview(int id) {
		
		Connection conn = null;
	    PreparedStatement stmt1 = null;
	    
		try {
	        conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
	        // Crea la query per ottenere il saldo e il nome dell'utente con l'id specificato
	        String query = "SELECT nome, saldo FROM utente WHERE id = " + id;

	        // Esegue la query
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(query);

	        // Se esiste un risultato, stampa il saldo e il nome dell'utente nella JLabel
	        if (rs.next()) {
	            String saldo = Double.toString(rs.getDouble("saldo"));
	            String nome = rs.getString("nome");
	            
	            soldiLabel = new JLabel(saldo + " €", JLabel.CENTER);
	            nomeUtenteLabel = new JLabel(nome, JLabel.CENTER);
	        }

	        // Chiude la connessione al database
	        
	        conn.close();
	    } 
		catch (Exception e) {
	        e.printStackTrace();
	    }
	
		
		this.id = id;
		overviewLabel = new JLabel("PROFILO", JLabel.CENTER);
		saldoLabel = new JLabel("SALDO", JLabel.CENTER);
		depositaButton = new JButton("");
		prelevaButton = new JButton("");
		
		//Stile dei label
		
		overviewLabel.setForeground(Color.white);
		nomeUtenteLabel.setForeground(Color.white);
		saldoLabel.setForeground(Color.white);
		soldiLabel.setForeground(Color.white);
		overviewLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		nomeUtenteLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		saldoLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		soldiLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		
		//Stile dei bottoni
		
		ImageIcon imageIcon = new ImageIcon("deposita.jpg");
		ImageIcon imageIcon1 = new ImageIcon("preleva.jpg");
        Image image = imageIcon.getImage().getScaledInstance(340, 100, Image.SCALE_SMOOTH);
        Image image1 = imageIcon1.getImage().getScaledInstance(340, 100, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        imageIcon1 = new ImageIcon(image1);
        depositaButton.setIcon(imageIcon);
        prelevaButton.setIcon(imageIcon1);
		
		
		JPanel panel = new JPanel(new GridLayout(6,1));
		
		setTitle("Riepilogo Account");
		setSize(320, 580);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel.setBackground(Color.black);
		
		panel.add(overviewLabel);
		panel.add(nomeUtenteLabel);
		panel.add(saldoLabel);
		panel.add(soldiLabel);
		panel.add(depositaButton);
		panel.add(prelevaButton);
		
		add(panel);
		setVisible(true);
		
		JPanel exitPanel = new JPanel(new FlowLayout());
		
		returnButton = new JButton("Ritorna al menu");
		
		returnButton.setPreferredSize(new Dimension(260, 40));
		returnButton.setForeground(Color.white);
		returnButton.setBackground(Color.black);
		
		exitPanel.setBackground(Color.black);

		exitPanel.add(returnButton);
		
		add(exitPanel, BorderLayout.SOUTH);
		setVisible(true);
		
		//Bottone per depositare con aggiornamento del saldo
	    
		depositaButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				Connection conn = null;
				
				String dep = "Importo";				
				String input = JOptionPane.showInputDialog(null, dep, "Deposita", JOptionPane.PLAIN_MESSAGE);                         				
				double importo = Double.parseDouble(input);
				
				try {					
					conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
					
					double saldo = getSaldoFromDatabase(conn);
					
					if (importo > 0) {
						saldo += importo;
					    JOptionPane.showMessageDialog(null, "L'importo e' stato accreditato sull'account");
					    soldiLabel.setText(saldo + "€");
						updateSaldoInDatabase(conn, saldo);
					}
					else {
					    JOptionPane.showMessageDialog(null, "Inserisci un importo valido");				
					}
			}
			catch (SQLException ex) {
	            ex.printStackTrace();
	        } 
			finally {
	            try {
	                if (conn != null) {
	                    conn.close();
	                }
	            } 
	            catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
		}		
	});
	
		prelevaButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				Connection conn = null;
				
				String dep = "Importo";				
				String input = JOptionPane.showInputDialog(null, dep, "Preleva", JOptionPane.PLAIN_MESSAGE);                         				
				double importo = Double.parseDouble(input);
				
				try {					
					conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
					
					double saldo = getSaldoFromDatabase(conn);
					
					if (importo <= saldo) {
						saldo -= importo;
					    JOptionPane.showMessageDialog(null, "Somma riscossa con successo");
					    soldiLabel.setText(saldo + "€");
						updateSaldoInDatabase(conn, saldo);
					}
					else if (importo > saldo){
					    JOptionPane.showMessageDialog(null, "Non hai abbastanza credito");				
					}
					else {
						JOptionPane.showMessageDialog(null, "Inserisci un importo valido");	
					}
			}
			catch (SQLException ex) {
	            ex.printStackTrace();
	        } 
			finally {
	            try {
	                if (conn != null) {
	                    conn.close();
	                }
	            } 
	            catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
		}		
	});
		
		returnButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				new Menu(id);
				dispose();
			}
		});
}
	
    private static double getSaldoFromDatabase(Connection conn) throws SQLException {
    	
        String sql = "SELECT saldo FROM utente WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
            
        stmt.setInt(1, id);
        stmt.executeQuery();
            
        ResultSet rs = stmt.executeQuery("SELECT saldo FROM utente WHERE id = '" + id + "'");
        rs.next();
        double saldo = rs.getDouble("saldo");
        rs.close();
        stmt.close();
        return saldo;
    }
	
    private static void updateSaldoInDatabase(Connection conn, double saldo) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE utente SET saldo = " + saldo + " WHERE id = " + id);
        stmt.close();
    }
}