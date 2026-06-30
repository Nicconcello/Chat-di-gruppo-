package sec;

import java.security.MessageDigest;

public class Sicurezza {
	
	String username;
	int chiave;
	
	public Sicurezza(String username) {
		this.username =username;
	}

	public void GeneraChiave() {
		for(int i = 0; i < username.length(); i++) {
			char lettera = username.charAt(i);
			int ValoreAscii = lettera;
			chiave += ValoreAscii * (i + 1);
			}
		System.out.println(chiave);
	}
	
	public String getOTP() {
	    try {
	        long tempo = System.currentTimeMillis() / 60000;
	        String combinazione = chiave + ":" + tempo;

	        // 1. Prepariamo SHA-256
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        byte[] hash = md.digest(combinazione.getBytes());

	        // 2. Prendiamo i primi 4 byte e li trasformiamo in un int positivo
	        // Usiamo l'operatore & 0xFF per gestire i numeri come "unsigned"
	        int risultato = ((hash[0] & 0xFF) << 24) | 
	                        ((hash[1] & 0xFF) << 16) | 
	                        ((hash[2] & 0xFF) << 8)  | 
	                        (hash[3] & 0xFF);

	        // 3. Lo rendiamo positivo (togliamo il segno) e lo limitiamo a 6 cifre
	        int codiceFinale = Math.abs(risultato) % 1000000;

	        // 4. Lo restituiamo come stringa formattata (es: 001234 invece di 1234)
	        return String.format("%06d", codiceFinale);

	    } catch (Exception e) {
	        return "ERRORE";
	    }
	}
}