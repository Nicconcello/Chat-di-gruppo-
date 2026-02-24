package sec;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatWindow extends JFrame{
	private Scanner sc;

	public ChatWindow(Socket s, String msg) {
		super("CHAT");
		setSize(400,600);
		setLocationRelativeTo(null);

		//Chat Area
		JTextArea c = new JTextArea();
		c.setEditable(false);
		JScrollPane scroll = new JScrollPane(c);
		
		//Send Bar
		JTextField txt = new JTextField(20);	
		JButton send = new JButton("➤");
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(txt, BorderLayout.CENTER);
		p.add(send, BorderLayout.EAST);
		
		add(p, BorderLayout.SOUTH);
		add(scroll, BorderLayout.CENTER);
		
		Send a = new Send(txt, s, msg);
		send.addActionListener(a);
		
		//Loop Lettura Messaggi
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					sc = new Scanner(s.getInputStream());
					
					while(sc.hasNextLine()) {
						String messaggioRicevuto = sc.nextLine();
						
						c.append(messaggioRicevuto + "\n");
	                    c.setCaretPosition(c.getDocument().getLength()); //AutoScroll
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