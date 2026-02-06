package sec;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame{
	
	public Login (Socket s) {
		super("LOG IN");
		setSize(400,200);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3,1));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		JTextField un = new JTextField(20);
		JLabel lun = new JLabel("USERNAME");
		p1.add(lun, BorderLayout.NORTH);
		p1.add(un, BorderLayout.SOUTH);
		
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		JTextField psw = new JTextField(20);
		JLabel lpsw = new JLabel("PASSWORD");
		p2.add(lpsw, BorderLayout.NORTH);
		p2.add(psw, BorderLayout.SOUTH);
		
		JButton connect = new JButton("CONNETTI");
		
		Connetti cn = new Connetti(un, psw, s, this);
		connect.addActionListener(cn);
		
		add(p1);
		add(p2);
		add(connect);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}