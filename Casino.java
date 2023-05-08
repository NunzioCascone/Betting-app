package net.codejava;

import java.util.*;
import java.util.Timer;
import javax.swing.*;
import net.codejava.Menu;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Casino extends JFrame {

	private static int id;
	private int risultato;
	private static final String DB_URL = "jdbc:mysql://localhost:3306/betting";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "BlaBlaBla24.";
	private JLabel importoLabel, numeroLabel, immagineLabel;
	private JToggleButton evenButton, oddButton, blackButton, redButton;
	private JButton scommettiButton, ritornaButton;
	private JTextField importoField, numeroField;


	public Casino(int id) throws MalformedURLException {

		this.id = id;

		JPanel panel = new JPanel(new GridLayout(1,1));

		setTitle("Roulette");
		setSize(320,480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBackground(Color.BLACK);

		Icon icon = new ImageIcon("giphy3.gif");
		immagineLabel = new JLabel(icon);
		immagineLabel.setBackground(Color.black);

		panel.add(immagineLabel);

		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(6, 2, 10, 10)); // imposta un layout a griglia con 6 righe, 2 colonne e spaziatura tra i componenti di 10 pixel
		panel1.setBackground(Color.black);

		Font font = new Font("Verdana", Font.PLAIN, 20);

		redButton = new JToggleButton("RED");
		redButton.setFont(font);
		blackButton = new JToggleButton("BLACK");
		blackButton.setFont(font);
		oddButton = new JToggleButton("ODD");
		oddButton.setFont(font);
		evenButton = new JToggleButton("EVEN");
		evenButton.setFont(font);
		numeroField = new JTextField();
		numeroField.setFont(font);
		numeroField.setText("0");
		numeroLabel = new JLabel("Numero:");
		numeroLabel.setForeground(Color.WHITE);
		numeroLabel.setFont(font);
		importoField = new JTextField();
		importoField.setFont(font);
		importoLabel = new JLabel("Importo:");
		importoLabel.setForeground(Color.WHITE);
		importoLabel.setFont(font);

		evenButton.setPreferredSize(new Dimension(90, 25));
		evenButton.setForeground(Color.white);
		evenButton.setBackground(Color.black);
		oddButton.setPreferredSize(new Dimension(90, 25));
		oddButton.setForeground(Color.white);
		oddButton.setBackground(Color.black);
		blackButton.setPreferredSize(new Dimension(90, 25));
		blackButton.setForeground(Color.white);
		blackButton.setBackground(Color.black);
		redButton.setPreferredSize(new Dimension(90, 25));
		redButton.setForeground(Color.black);
		redButton.setBackground(Color.red);

		panel1.add(importoLabel);
		panel1.add(importoField);
		panel1.add(numeroLabel);
		panel1.add(numeroField);
		panel1.add(oddButton);
		panel1.add(evenButton);
		panel1.add(redButton);
		panel1.add(blackButton);

		JPanel panel2 = new JPanel(new GridLayout(1,2));

		panel2.setBackground(Color.black);

		scommettiButton = new JButton("Scommetti");
		scommettiButton.setFont(font);
		scommettiButton.setForeground(Color.white);
		scommettiButton.setBackground(Color.black);

		ritornaButton = new JButton("Menu'");
		ritornaButton.setFont(font);
		ritornaButton.setForeground(Color.white);
		ritornaButton.setBackground(Color.black);

		panel2.setBackground(Color.black);
		panel2.add(scommettiButton);
		panel2.add(ritornaButton);

		add(panel, BorderLayout.NORTH);
		add(panel1, BorderLayout.CENTER);
		add(panel2, BorderLayout.SOUTH);
		setVisible(true);   	

		scommettiButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Connection conn = null;
				double importo = Double.parseDouble(importoField.getText());
				try {
					conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
					int numero = Integer.parseInt(numeroField.getText());           	
					String risultato = scommetti(numero);
					
					double saldo = getSaldoFromDatabase(conn);
					
					if(saldo > 0 && importo <= saldo) {
						if(oddButton.isSelected() || redButton.isSelected()) {
							if(risultato.equals("Vincente dispari")) {
								saldo += (importo*2);
								JOptionPane.showMessageDialog(null,"Importo accreditato al saldo: " + (importo*2) + " €");
							}
							else {
								saldo -= importo;
								JOptionPane.showMessageDialog(null,"Hai perso!");
							}
						}
						else if(evenButton.isSelected() || blackButton.isSelected()) {
							if(risultato.equals("Vincente pari")) {
								saldo += (importo*2);
								JOptionPane.showMessageDialog(null,"Importo accreditato al saldo: " + (importo*2) + " €");
							}
							else {
								saldo -= importo;
								JOptionPane.showMessageDialog(null,"Hai perso!");
							}						
						}
						else if(numero == 0) {
							if(risultato.equals("Vincente 0")) {
								saldo += (importo*100);
								JOptionPane.showMessageDialog(null,"Importo accreditato al saldo: " + (importo*100) + " €");
							}
							else {
								saldo -= importo;
								JOptionPane.showMessageDialog(null,"Hai perso!");
							}
						}
						else if (risultato.equals("Vincente scelta")) {
							saldo += (importo*10);
							JOptionPane.showMessageDialog(null,"Importo accreditato al saldo: " + (importo*10) + " €");
						}
						else {
							saldo -= importo;
							JOptionPane.showMessageDialog(null,"Hai perso!");
						}
					}
					else {
						JOptionPane.showMessageDialog(null,"Saldo insufficiente, pezzente!");
					}
					updateSaldoInDatabase(conn, saldo);
					dispose();
					new Casino(id);
				}
				catch (SQLException ex) {
					ex.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
	}

	private static String scommetti(int scelta) {
		Random random = new Random();
		int risultato = random.nextInt(36);

		boolean rosso = false;
		boolean nero = false;

		if (risultato == scelta && scelta == 0) {
			JOptionPane.showMessageDialog(null, "Il numero vincente e' 0");
			return "Vincente 0";
		} 
		else if (risultato == scelta) {
			JOptionPane.showMessageDialog(null, "Il numero vincente e' " + scelta);
			return "Vincente scelta";
		} 
		else if (risultato %2 == 0) {
			JOptionPane.showMessageDialog(null, "Il numero vincente e' " + risultato);
			rosso = true;
			return "Vincente pari";
		}
		else if (risultato %2 != 0) {
			JOptionPane.showMessageDialog(null, "Il numero vincente e' " + risultato);
			nero = true;
			return "Vincente dispari";
		}
		else {
			JOptionPane.showMessageDialog(null, "Il numero vincente e' " + risultato);
			return "Perdita";
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
