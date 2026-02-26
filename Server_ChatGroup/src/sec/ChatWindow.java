package sec;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatWindow extends JFrame{
	private Scanner sc;


	private int contatore;
	
	public ChatWindow(Socket s, String msg) {
		super("CHAT");
		setSize(400,600);
		setLocationRelativeTo(null);

		
		JPanel panelloPrincipale = new JPanel();
		panelloPrincipale.setLayout(new BorderLayout());
		String testo = "Online: " + Integer.toString(contatore);
		JLabel online = new JLabel(testo);
		JTextArea c = new JTextArea();         // Area dove appaiono i messaggi
		c.setEditable(false);
		JScrollPane scroll = new JScrollPane(c); // Aggiunge le barre di scorrimento
		
		panelloPrincipale.add(online, BorderLayout.NORTH);
		panelloPrincipale.add(scroll, BorderLayout.CENTER);

		JTextField txt = new JTextField(20);
		
		JButton send = new JButton("➤");            // Bottone di invio
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(txt, BorderLayout.CENTER);
		p.add(send, BorderLayout.EAST);
		
		add(p, BorderLayout.SOUTH);
		add(panelloPrincipale, BorderLayout.CENTER);
		
		Send a = new Send(txt, s, msg);               // Ascoltatore bottone per inviare il messaggio
		send.addActionListener(a);
		txt.addActionListener(a);
		
		//Loop Lettura Messaggi
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sc = new Scanner(s.getInputStream());
					
					while(sc.hasNextLine()) {

						String messaggioRicevuto = sc.nextLine();

						if(messaggioRicevuto.equals("CONNECT")) {
							contatore++;
							online.setText("Online: " + contatore);
						} else if(messaggioRicevuto.equals("DISCONNECT")) {
							contatore--;
							online.setText("Online: " + contatore);
						} else {
							c.append(messaggioRicevuto + "\n");
	                    	c.setCaretPosition(c.getDocument().getLength()); //AutoScroll
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}