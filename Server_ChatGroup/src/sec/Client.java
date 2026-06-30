package sec;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client {

	public static void main(String[] args) {
		try {//local per testare
			Socket s = new Socket("127.0.0.1", 5000);
			
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