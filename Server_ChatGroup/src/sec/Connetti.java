package sec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


public class Connetti implements ActionListener{
	private JTextField un;
	private JTextField psw;
	private Socket s;
	private Scanner sc;
	private PrintWriter pw;
	private JFrame finestra;
	
	public Connetti(JTextField un, JTextField psw, Socket s, JFrame finestra) {
		this.un = un;
		this.psw = psw;
		this.s = s;
		this.finestra = finestra;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			//Inizializzazione Connessione
			sc = new Scanner(s.getInputStream());
			pw = new PrintWriter(s.getOutputStream(), true);
			
			if(!un.getText().isBlank() && !psw.getText().isBlank()) {
				pw.println(un.getText());
				pw.println(psw.getText()); 
				//Correggere da cifrare per evitare che chiunque possa recuperarla leggendo le connessioni dalla console del server
				
				// Il server ci manderà una riga di risposta
                if(sc.hasNextLine()) {
                    String risposta = sc.nextLine();
                    System.out.println("Il server dice: " + risposta);
                    
					//Chiusure Login e Apertura Chat Window
                    if (risposta.contains("Accesso eseguito")) {
                        System.out.println("Entriamo!");
                        
                        finestra.dispose();
                        
                        ChatWindow cw = new ChatWindow(s, un.getText());
                    } else { //Credenziali invalide
                        System.out.println("Accesso negato: " + risposta);

                        JOptionPane.showMessageDialog(null, "Password o Username non validi", "Login Error", JOptionPane.ERROR_MESSAGE); //PopUp error
						
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
