package net.codejava;

import java.util.*;
import javax.swing.*;
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

public class Menu extends JFrame {
	
	//Dichiarazione attributi
	
	private static int id;
    private JLabel menuLabel;	
	private JButton casinoButton, sportButton, overviewButton, exitButton;
    
	//Costruttore
	
    public Menu(int id) {
    	
    	this.id = id;
    	menuLabel = new JLabel("MENU'", JLabel.CENTER);		
		
		//Stile grafico del menu
		
		menuLabel.setForeground(Color.white);
		menuLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
		
		//Background dei bottoni
		ImageIcon imageIcon = new ImageIcon("casino.jpg");
		ImageIcon imageIcon1 = new ImageIcon("calcio.jpg");
		ImageIcon imageIcon2 = new ImageIcon("overview.jpg");
		ImageIcon imageIcon3 = new ImageIcon("exit.jpg");
        Image image = imageIcon.getImage().getScaledInstance(340, 100, Image.SCALE_SMOOTH);
        Image image1 = imageIcon1.getImage().getScaledInstance(340, 100, Image.SCALE_SMOOTH);
        Image image2 = imageIcon2.getImage().getScaledInstance(340, 100, Image.SCALE_SMOOTH);
        Image image3 = imageIcon3.getImage().getScaledInstance(360, 100, Image.SCALE_SMOOTH);
        
        imageIcon = new ImageIcon(image);
        imageIcon1 = new ImageIcon(image1);
        imageIcon2 = new ImageIcon(image2);
        imageIcon3 = new ImageIcon(image3);
        
		casinoButton = new JButton("");
		casinoButton.setIcon(imageIcon);
		casinoButton.setText("");
		sportButton = new JButton("");
		sportButton.setIcon(imageIcon1);
		overviewButton = new JButton("");
		overviewButton.setIcon(imageIcon2);
		exitButton = new JButton("");
		exitButton.setIcon(imageIcon3);
		
		Insets insets = new Insets(20, 20, 20, 20);
		

		JPanel panel = new JPanel(new GridLayout(5,1));
		

		panel.setBackground(Color.black);

		setTitle("Menu");
		setSize(320, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel.add(menuLabel);
		panel.add(casinoButton);	
		panel.add(sportButton);
		panel.add(overviewButton);
		panel.add(exitButton);
		
		casinoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				try {
					new Casino(id);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
				dispose();
				
			}
		});
		
		sportButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				new Scommesse(id);		
				dispose();
				
			}
		});
		
		overviewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				new Overview(id);			
				dispose();
			}
		});
		
		exitButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent mod) {
				
				new Login();
				JOptionPane.showMessageDialog(null, "Logged out");
				dispose();
				
			}
		});
		
		add(panel);
		setVisible(true);
    	
    }
}
