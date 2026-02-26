package sec;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GeneratoreOTP {

	public static void main(String[] args) {
		JFrame finestra = new JFrame("OTP");
		finestra.setSize(300,200);
		finestra.setLocationRelativeTo(null);
		finestra.setLayout(new GridLayout(3,1));
		
		JTextField name = new JTextField(20);
		JLabel lname = new JLabel("USERNAME");
		JPanel p = new JPanel();
		p.add(lname, BorderLayout.NORTH);
		p.add(name, BorderLayout.SOUTH);
		finestra.add(p);
		
		JTextField cod = new JTextField(20);
		cod.setEditable(false);
		JLabel lcod = new JLabel("OTP");
		JPanel p2 = new JPanel();
		p2.add(lcod, BorderLayout.NORTH);
		p2.add(cod, BorderLayout.SOUTH);
		finestra.add(p2);
		
		JButton genera = new JButton("GENERA");
		finestra.add(genera);
		
		Premi click = new Premi(name, cod);
		genera.addActionListener(click);
		
		finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		finestra.setVisible(true);
	}
}