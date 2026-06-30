package sec;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
		}else if(msg.isBlank()){
			return;
		}else{
			salvaInArchivio(msg);
			for(PrintWriter u : Server.messaggeri) {
					u.println(msg);
			}
		}
	}

	//Gettone per utilizzo dell'archivio
	private static final Object lockArchivio = new Object();

	//Funzione scrittura nell'archivio
	public void salvaInArchivio(String msg) {
		if(msg != null && !msg.equals("CONNECT") && !msg.equals("DISCONNECT")) {
			Path percorso = Path.of("ARCHIVIO.txt");

			// Aggiungiamo un "a capo" dopo il messaggio
            String testoDaSalvare = msg + System.lineSeparator();

			synchronized(lockArchivio) {
				try {
					Files.writeString(percorso, testoDaSalvare,StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	//Funzione per scaricare l'archivio ogni volta che entri nella chat
	public void caricaArchivio(PrintWriter pwPersonale) {
		Path percorso = Path.of("ARCHIVIO.txt");

		if(java.nio.file.Files.exists(percorso)) {

			synchronized(lockArchivio) {
				try {
					java.util.List<String> cronologia = java.nio.file.Files.readAllLines(percorso);

					for(String riga : cronologia) {
						pwPersonale.println(riga);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
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
			
			if (sc.hasNextLine()) {
				String messaggio = sc.nextLine();
				if(!messaggio.equals("PARTI")) {
					return;
				}
			}
		
			
			//Aggiungere Wait, sbloccato DOPO aver premuto il tasto CONNETTI dalla finestra login
			


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
				

				pw.println("Accesso eseguito");

				caricaArchivio(pw);
				
				// connessione e aggiunta al canale di comunicazione
				//pw.println("Accesso eseguito. Benvenuto " + this.username); //print solo a te stesso e
				//non a tutti i membri del server()
				Server.messaggeri.add(pw);
				ServerSend("CONNECT");

				ServerSend(this.username + " si è connesso.");
				
				connesso = true;
			}
			while(connesso && sc.hasNextLine()) {
				String cv = sc.nextLine().trim();
				String msg = username + " : " + cv;
				
				ServerSend(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	        if (pw != null) Server.messaggeri.remove(pw);
	        ServerSend(username + " si è disconnesso.");
			ServerSend("DISCONNECT");
	        try { s.close(); } catch (IOException e) { }
	    }
	}
}
