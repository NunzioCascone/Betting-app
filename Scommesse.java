package net.codejava;

import java.util.*;
import java.util.Timer;
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

public class Scommesse extends JFrame {
	
	private static int id;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/betting";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "BlaBlaBla24.";
    private JLabel partitaLabel, importoLabel, vittoriaLabel, pareggioLabel, sconfittaLabel;
    private JToggleButton winButton, drawButton, lostButton;
    private JButton scommettiButton, ritornaButton;
    private JTextField importoField;
    
    public Scommesse(int id) {  
    	
    	
		JPanel panel = new JPanel(new GridLayout(4,1));
		

		panel.setBackground(Color.black);

		setTitle("Menu");
		setSize(320, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        importoLabel = new JLabel("Importo da scommettere", JLabel.CENTER);
        importoField = new JTextField(null, JTextField.CENTER);
		winButton = new JToggleButton("1");
		drawButton = new JToggleButton("X");
		lostButton = new JToggleButton("2");
        scommettiButton = new JButton("Scommetti");
        ritornaButton = new JButton("Menu'");
        importoLabel.setForeground(Color.white);
        importoLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        importoField.setFont(new Font("Verdana", Font.PLAIN, 20));
		winButton.setFont(new Font("Verdana", Font.PLAIN, 20));
		drawButton.setFont(new Font("Verdana", Font.PLAIN, 20));
		lostButton.setFont(new Font("Verdana", Font.PLAIN, 20));
		ritornaButton.setFont(new Font("Verdana", Font.PLAIN, 20));
		scommettiButton.setFont(new Font("Verdana", Font.PLAIN, 20));
		importoField.setPreferredSize(new Dimension(80, 60));
		Insets insets = new Insets(20, 20, 20, 20);
		importoField.setMargin(insets);
        
    	this.id = id;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;      
        
        double quota_vittoria = 0;
        double quota_pareggio = 0;
        double quota_sconfitta = 0;  
        String squadre = null;
        
        try {
            // Connessione al database
        	
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Selezione delle squadre
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM squadre ORDER BY Rand() LIMIT 1");
            
            while(rs.next()) {
            	squadre = rs.getString("nome");
            	quota_vittoria = rs.getDouble("quota_vittoria");
            	quota_pareggio = rs.getDouble("quota_pareggio");
            	quota_sconfitta = rs.getDouble("quota_sconfitta"); 
            	
            	partitaLabel = new JLabel(squadre, JLabel.CENTER);
            	partitaLabel.setForeground(Color.white);
            	partitaLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        double temp = quota_vittoria;
        double temp1 = quota_pareggio;
        double temp2 = quota_sconfitta;
 
        scommettiButton.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
        		
        		Connection conn = null;
        		double importo = Double.parseDouble(importoField.getText());
        		try {
        			conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
            		String risultato = scommetti(partitaLabel.getText());           	
            		
            		double saldo = getSaldoFromDatabase(conn);
            		
            		if(saldo > 0 && importo <= saldo) {
                		if(winButton.isSelected()) {
                			if(risultato.equals("Vittoria della squadra in casa")) {
                    			saldo += (temp * importo);
    							JOptionPane.showMessageDialog(null,"Importo accreditato al saldo: " + (temp*importo) + " €");
                			}
                			else {
                				saldo -= importo;
                				JOptionPane.showMessageDialog(null,"Hai perso!");
                			}
                		}
                		else if(drawButton.isSelected()) {
                			if(risultato.equals("Pareggio")) {
                    			saldo += (temp1 * importo);
                    			JOptionPane.showMessageDialog(null,"Importo accreditato al saldo: " + (temp1*importo) + " €");
                			}
                			else {
                				saldo -= importo;
                				JOptionPane.showMessageDialog(null,"Hai perso!");
                			}
                		}
                		else if(lostButton.isSelected()) {
                			if(risultato.equals("Sconfitta della squadra in casa")) {
                    			saldo += (temp2 * importo);
                    			JOptionPane.showMessageDialog(null,"Importo accreditato al saldo: " + (temp2*importo) + " €");
                			}
                			else {
                				saldo -= importo;
                				JOptionPane.showMessageDialog(null,"Hai perso!");
                			}
                		}
            		}
					else {
						JOptionPane.showMessageDialog(null,"Saldo insufficiente, pezzente!");
					}
            		updateSaldoInDatabase(conn, saldo);
            		dispose();
            		new Scommesse(id);
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
        
        ritornaButton.addActionListener(new ActionListener() {
        	
        	public void actionPerformed(ActionEvent e) {
        		new Menu(id);
        		dispose();
        	}
        });
        
        panel.setBackground(Color.black);
        panel.add(partitaLabel);
        panel.add(importoLabel);
        panel.add(importoField);
        
        JPanel bottoniPanel = new JPanel(new FlowLayout());
        
		winButton.setPreferredSize(new Dimension(90, 25));
		winButton.setForeground(Color.white);
		winButton.setBackground(Color.black);
		drawButton.setPreferredSize(new Dimension(90, 25));
		drawButton.setForeground(Color.white);
		drawButton.setBackground(Color.black);
		lostButton.setPreferredSize(new Dimension(90, 25));
		lostButton.setForeground(Color.white);
		lostButton.setBackground(Color.black);
		
		vittoriaLabel = new JLabel(Double.toString(temp), JLabel.CENTER);
		pareggioLabel = new JLabel(Double.toString(temp1), JLabel.CENTER);
		sconfittaLabel = new JLabel(Double.toString(temp2), JLabel.CENTER);
		
        vittoriaLabel.setForeground(Color.white);
        pareggioLabel.setForeground(Color.white);
        sconfittaLabel.setForeground(Color.white);
        vittoriaLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        pareggioLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        sconfittaLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        vittoriaLabel.setPreferredSize(new Dimension(90, 25));
        pareggioLabel.setPreferredSize(new Dimension(90, 25));
        sconfittaLabel.setPreferredSize(new Dimension(90, 25));
        
        bottoniPanel.setBackground(Color.black);
        bottoniPanel.add(winButton);
        bottoniPanel.add(drawButton);
        bottoniPanel.add(lostButton);
        bottoniPanel.add(vittoriaLabel);
        bottoniPanel.add(pareggioLabel);
        bottoniPanel.add(sconfittaLabel);
        
        JPanel scommettiPanel = new JPanel(new GridLayout(1,2));
        
		scommettiButton.setPreferredSize(new Dimension(150, 60));
		scommettiButton.setForeground(Color.white);
		scommettiButton.setBackground(Color.black);
		
		ritornaButton.setPreferredSize(new Dimension(150, 60));
		ritornaButton.setForeground(Color.white);
		ritornaButton.setBackground(Color.black);
		
		
        scommettiPanel.setBackground(Color.black);
        scommettiPanel.add(scommettiButton);
        scommettiPanel.add(ritornaButton);
        
        add(panel, BorderLayout.NORTH);       
        add(bottoniPanel, BorderLayout.CENTER);
        add(scommettiPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
    
    //Determinazione risultato
    
    private static String scommetti(String nome) {
        Random random = new Random();
        int risultato = random.nextInt(3); 
        if (risultato == 0) {
        	JOptionPane.showMessageDialog(null, "La squadra in casa ha vinto");
            return "Vittoria della squadra in casa";
        } else if (risultato == 1) {
        	JOptionPane.showMessageDialog(null, "Le squadre hanno pareggiato");
            return "Pareggio";
        } else {
        	JOptionPane.showMessageDialog(null, "La squadra in trasferta ha vinto");
            return "Sconfitta della squadra in casa";
        }
    }
    
    //Visualizza il saldo disponibile da database
    
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
    
    //Aggiorna il saldo disponibile in database dopo la scommessa
    
    private static void updateSaldoInDatabase(Connection conn, double saldo) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE utente SET saldo = " + saldo + " WHERE id = " + id);
        stmt.close();
    }
}
