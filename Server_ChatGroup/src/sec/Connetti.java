package sec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Connetti implements ActionListener{
	JTextField un;
	JTextField psw;
	Socket s;
	Scanner sc;
	PrintWriter pw;
	JFrame finestra;
	
	public Connetti(JTextField un, JTextField psw, Socket s, JFrame finestra) {
		this.un = un;
		this.psw = psw;
		this.s = s;
		this.finestra = finestra;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			sc = new Scanner(s.getInputStream());
			pw = new PrintWriter(s.getOutputStream(), true);
			
			if(!un.getText().isEmpty() && !psw.getText().isEmpty()) {
				pw.println(un.getText());
				pw.println(psw.getText());
				
				// Il server ci manderà una riga di risposta
                if(sc.hasNextLine()) {
                    String risposta = sc.nextLine();
                    System.out.println("Il server dice: " + risposta);
                    
                    if (risposta.contains("Accesso eseguito")) {
                        // QUI: Chiuderai il login e aprirai la ChatWindow
                        System.out.println("Entriamo!");
                        
                        finestra.dispose();
                        
                        ChatWindow cw = new ChatWindow(s, un.getText());
                    } else {
                        System.out.println("Accesso negato: " + risposta);
                        // Magari qui potresti svuotare il campo password per riprovare
                        un.setText("");
                        psw.setText("");
                    }
                }
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
