package sec;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Thread_Connessioni implements Runnable{
	Socket s;
	boolean connesso;
	PrintWriter pw = null;
    Scanner sc = null;
    String username = "";
	
	public Thread_Connessioni(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		try {
			sc = new Scanner(s.getInputStream());
			pw = new PrintWriter(s.getOutputStream(),true);
						
			if(sc.hasNextLine()) {
				this.username = sc.nextLine();
			}
			
			// controllo crendenziali di accesso
			if(sc.hasNextLine()) {
				String OTP_Generato = sc.nextLine();
				
				sec.Sicurezza verifica = new sec.Sicurezza(username);
				String OTP_Verifica = verifica.getOTP();
				
				// credenziali errate e viene cacciato
				if(!OTP_Generato.equals(OTP_Verifica)) {
					pw.println("Codice non valido");
					s.close();
					
					return;
				}
				
				// connessione e aggiunta al canale di comunicazione
				pw.println("Accesso eseguito. Benvenuto " + this.username);
				Server.messaggeri.add(pw);
				
				connesso = true;
			}
			while(connesso && sc.hasNextLine()) {
				String cv = sc.nextLine().trim();
				String msg = username + " : " + cv;
				
				for(PrintWriter u : Server.messaggeri) {
					u.println(msg);
				}
			}
			pw.close();
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
	        if (pw != null) Server.messaggeri.remove(pw);
	        System.out.println(username + " si è disconnesso.");
	        try { s.close(); } catch (IOException e) { }
	    }
	}
}
