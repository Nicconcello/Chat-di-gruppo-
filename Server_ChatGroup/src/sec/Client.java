package sec;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
		try {
			Socket s = new Socket("127.0.0.1", 5000); 
			//cambiando porta posso creare una "Stanza" separata da quella principale.
			//I socket su Ip:PortA e su Ip:PortB non si incontrano mai e non possono parlare tra loro
			Login lg = new Login(s);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

