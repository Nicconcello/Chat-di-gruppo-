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
            String testoDaSalvare = msg + System.lineSeparator();

			synchronized(lockArchivio) {
				try {
					Files.writeString(percorso, testoDaSalvare,StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				} catch (IOException e) {
					System.out.println("[SERVER] Errore salvataggio archivio: " + e.getMessage());
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
					System.out.println("[SERVER] Errore caricamento archivio: " + e.getMessage());
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
			System.out.println("\n[SERVER] Nuovo client in fase di connessione. Apro i flussi dati...");
			sc = new Scanner(s.getInputStream());
			pw = new PrintWriter(s.getOutputStream(),true);	
			
			System.out.println("[SERVER] In attesa del comando 'PARTI' dal client...");
			if (sc.hasNextLine()) {
				String messaggio = sc.nextLine();
				System.out.println("[SERVER] Ricevuto: " + messaggio);
				if(!messaggio.equals("PARTI")) {
					System.out.println("[SERVER] Comando non riconosciuto. Interrompo il processo.");
					return;
				}
			}

			System.out.println("[SERVER] In attesa di ricevere l'username...");
			if(sc.hasNextLine()) {
				this.username = sc.nextLine();
				System.out.println("[SERVER] Username catturato: " + username);
			}
			
			System.out.println("[SERVER] In attesa di ricevere l'OTP...");
			if(sc.hasNextLine()) {
				String OTP_Generato = sc.nextLine();
				System.out.println("[SERVER] OTP ricevuto dal Client: " + OTP_Generato);
				
				sec.Sicurezza verifica = new sec.Sicurezza(username);
				String OTP_Verifica = verifica.getOTP();
				System.out.println("[SERVER] OTP calcolato internamente: " + OTP_Verifica);
				
				if(!OTP_Generato.equals(OTP_Verifica)) {
					System.out.println("[SERVER] ❌ ERRORE: I due codici OTP NON combaciano. Rifiuto l'accesso.");
					pw.println("Codice non valido");
					s.close();
					return;
				}
				
				System.out.println("[SERVER] ✅ SUCESSO: OTP validato. Invio 'Accesso eseguito' al client.");
				pw.println("Accesso eseguito");

				caricaArchivio(pw);
				
				Server.messaggeri.add(pw);
				ServerSend("CONNECT");
				ServerSend(this.username + " si è connesso.");
				connesso = true;
			}
			
			System.out.println("[SERVER] In ascolto dei messaggi chat da " + username + "...");
			while(connesso && sc.hasNextLine()) {
				String cv = sc.nextLine().trim();
				String msg = username + " : " + cv;
				ServerSend(msg);
			}
			
		} catch (Exception e) {
			System.out.println("[SERVER] ⚠️ ECCEZIONE GENERATA DURANTE L'ESECUZIONE: ");
			e.printStackTrace();
		} finally {
			System.out.println("[SERVER] Disconnessione e chiusura risorse per " + username);
	        if (pw != null) Server.messaggeri.remove(pw);
	        if (username != null) ServerSend(username + " si è disconnesso.");
			ServerSend("DISCONNECT");
	        try { s.close(); } catch (IOException e) { }
	    }
	}
}
