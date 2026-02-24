package sec;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Thread_Connessioni implements Runnable{
	private Socket s;
	private boolean connesso;
	private PrintWriter pw = null;
    private Scanner sc = null;
    private String username;
	
	//Funzione invio messaggi con prevenzione vuoto o null
	public void ServerSend(String msg){
		if(Server.messaggeri.size()<1){
			return;
		}else if(msg.isBlank() || msg.isEmpty()){
			return;
		}else{
			for(PrintWriter u : Server.messaggeri) {
					u.println(msg);
			}
		}
	}

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
				//pw.println("Accesso eseguito. Benvenuto " + this.username); //print solo a te stesso e
				//non a tutti i membri del server()
				Server.messaggeri.add(pw);
				ServerSend(this.username + "si è connesso.");
				ServerSend("CONNECT");
				connesso = true;
			}
			while(connesso && sc.hasNextLine()) {
				String cv = sc.nextLine().trim();
				String msg = username + " : " + cv;
				
				ServerSend(msg);
			}
			pw.close();
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
	        if (pw != null) Server.messaggeri.remove(pw);
	        ServerSend(username + " si è disconnesso.");
			ServerSend("DISCONNECT");
	        try { s.close(); } catch (IOException e) { }
	    }
	}
}
