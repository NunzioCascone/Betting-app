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

public class Admin extends JFrame {

	private static final String DB_URL = "jdbc:mysql://localhost/betting";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "BlaBlaBla24.";

	public Admin() {

		JPanel panel = new JPanel(new GridLayout(5,1, 20, 25));
		panel.setBackground(Color.BLACK);
		panel.setPreferredSize(new Dimension(320, 480)); // Imposta le dimensioni del pannello

		setTitle("Admin");
		setSize(320, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Crea JLabel personalizzato
		JLabel adminLabel = new JLabel("Administration Panel", JLabel.CENTER);
		adminLabel.setForeground(Color.white);
		adminLabel.setFont(new Font("Verdana", Font.PLAIN, 20));


		// Crea i bottoni personalizzati
		JButton btnVisualizza = new JButton("Visualizza Utenti");
		JButton btnRimuovi = new JButton("Sospendi Account");
		JButton btnModifica = new JButton("Modifica Quotazioni");
		JButton btnEsci = new JButton("Esci");

		// Imposta lo stile dei bottoni
		Font font = new Font("Verdana", Font.BOLD, 20);
		Color foreground = Color.WHITE;
		Color background = Color.BLACK;
		btnVisualizza.setPreferredSize(new Dimension(150, 80));
		btnVisualizza.setFont(font);
		btnVisualizza.setForeground(foreground);
		btnVisualizza.setBackground(background);
		btnRimuovi.setPreferredSize(new Dimension(150, 80));
		btnRimuovi.setFont(font);
		btnRimuovi.setForeground(foreground);
		btnRimuovi.setBackground(background);
		btnModifica.setPreferredSize(new Dimension(150, 80));
		btnModifica.setFont(font);
		btnModifica.setForeground(foreground);
		btnModifica.setBackground(background);
		btnEsci.setPreferredSize(new Dimension(150, 80));
		btnEsci.setFont(font);
		btnEsci.setForeground(foreground);
		btnEsci.setBackground(background);

		// Aggiungi i bottoni al pannello
		panel.add(adminLabel);
		panel.add(btnVisualizza);
		panel.add(btnRimuovi);
		panel.add(btnModifica);
		panel.add(btnEsci);

		add(panel);
		setVisible(true);

		btnModifica.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				JPanel panelModifica = new JPanel(new GridLayout(9, 1));
				
				setTitle("Modifica quote");
				setSize(300, 440);
				setLocationRelativeTo(null);
				setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				panelModifica.setBackground(Color.BLACK);
				
				JLabel modificaLabel = new JLabel("Modifica quotazioni", JLabel.CENTER);
				JLabel squadreLabel = new JLabel("Scegli una partita", JLabel.CENTER);
				JTextField squadreField = new JTextField();
				JLabel quotaV = new JLabel("Quota vittoria", JLabel.CENTER);
				JTextField quotaVField = new JTextField();
				JLabel quotaD = new JLabel("Quota pareggio", JLabel.CENTER);
				JTextField quotaDField = new JTextField();
				JLabel quotaL = new JLabel("Quota sconfitta", JLabel.CENTER);
				JTextField quotaLField = new JTextField();
				
				Font font = new Font("Verdana", Font.BOLD, 16);
				Color foreground = Color.WHITE;
				modificaLabel.setFont(font);
				modificaLabel.setForeground(foreground);
				squadreLabel.setFont(font);
				squadreLabel.setForeground(foreground);
				quotaV.setFont(font);
				quotaV.setForeground(foreground);
				quotaD.setFont(font);
				quotaD.setForeground(foreground);
				quotaL.setFont(font);
				quotaL.setForeground(foreground);
				modificaLabel.setPreferredSize(new Dimension(100, 35));
				squadreLabel.setPreferredSize(new Dimension(100, 35));
				quotaV.setPreferredSize(new Dimension(100, 35));
				quotaD.setPreferredSize(new Dimension(100, 35));
				quotaL.setPreferredSize(new Dimension(100, 35));
				squadreField.setPreferredSize(new Dimension(100, 35));
				quotaVField.setPreferredSize(new Dimension(100, 35));
				quotaDField.setPreferredSize(new Dimension(100, 35));
				quotaLField.setPreferredSize(new Dimension(100, 35));
				
				
				panelModifica.add(modificaLabel);
				panelModifica.add(squadreLabel);
				panelModifica.add(squadreField);
				panelModifica.add(quotaV);
				panelModifica.add(quotaVField);
				panelModifica.add(quotaD);
				panelModifica.add(quotaDField);
				panelModifica.add(quotaL);
				panelModifica.add(quotaLField);
							
				Connection conn = null;
				PreparedStatement stmt1 = null;
				UIManager.put("OptionPane.maximumSize",new Dimension(320,480));
				UIManager.put("OptionPane.background", Color.black);
				UIManager.put("Panel.background", Color.black);
				UIManager.put("OptionPane.errorDialog.titlePane.foreground", Color.white);
				UIManager.put("OptionPane.errorDialog.titlePane.background", Color.black);
				UIManager.put("OptionPane.errorDialog.border.background", Color.black);
				JOptionPane.showMessageDialog(null, panelModifica, "Modifica quote", JOptionPane.PLAIN_MESSAGE);
				
				try {
					conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
					String sql = "UPDATE squadre SET quota_vittoria = ?, quota_pareggio = ?, quota_sconfitta = ? WHERE nome = ?";                    
					
					
					stmt1=conn.prepareStatement(sql);
					stmt1.setDouble(1, Double.parseDouble(quotaVField.getText()));
					stmt1.setDouble(2, Double.parseDouble(quotaDField.getText()));
					stmt1.setDouble(3, Double.parseDouble(quotaLField.getText()));
					stmt1.setString(4, squadreField.getText());
					
					stmt1.executeUpdate();
				}
				catch(SQLException ex) {
					ex.printStackTrace();
				}finally {
					try {
						stmt1.close();
						conn.close();
					}catch(SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		
		btnRimuovi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection conn = null;
				PreparedStatement stmt = null;
				PreparedStatement stmt1 = null;

				String sql = "DELETE FROM utente WHERE nome = ?"; 
				String sql1 = "DELETE FROM accesso WHERE nome = ?";

				String dep = "Nome utente";				
				String input = JOptionPane.showInputDialog(null, dep, "Sospendi account", JOptionPane.PLAIN_MESSAGE);                         				
				String nomeUtente = input;

				try {
					conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
					
														
							stmt = conn.prepareStatement(sql);
							stmt.setString(1, nomeUtente);
							
							stmt1 = conn.prepareStatement(sql1);
							stmt1.setString(1, nomeUtente);

							// Esegue la query
							stmt.execute();
							stmt1.execute();
							
							JOptionPane.showMessageDialog(null, "Account rimosso");
					
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				} 
				finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
						if (stmt1 != null) {
							stmt1.close();
						}
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

		btnVisualizza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Visualizza();
			}
		});

		btnEsci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Login();
				dispose();	    		
			}
		});
	}
}
