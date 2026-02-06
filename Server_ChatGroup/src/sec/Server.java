package sec;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
	// Lista globale di tutti i "tubi" di uscita per i messaggi
    public static CopyOnWriteArrayList<PrintWriter> messaggeri = new CopyOnWriteArrayList<>();
    
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(5000);
			while(true) {
				System.out.println("Il server attende connessioni....");
				Socket s = ss.accept();
				
				Thread_Connessioni tc = new Thread_Connessioni(s);
				Thread t = new Thread(tc);
				t.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
