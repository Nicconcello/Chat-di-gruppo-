package sec;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {

	public static void main(String[] args) {
		try {
			Socket s = new Socket("127.0.0.1", 5000); 
			//cambiando porta posso creare una "Stanza" separata da quella principale.
			//I socket su Ip:PortA e su Ip:PortB non si incontrano mai e non possono parlare tra loro
			
			Login lg = new Login(s);
			
		} catch (ConnectException e) {

			Eccezione serverChiuso = new Eccezione("Il server non è attivo");

			JOptionPane.showMessageDialog(
				null,
				serverChiuso.getMessage(),
				"Errore di connessione",
				JOptionPane.ERROR_MESSAGE
			);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}