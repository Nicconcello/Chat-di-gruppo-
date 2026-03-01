package crittografia;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair; // Importi la classe
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore.Direction;

import java.util.HashMap;
import java.util.Map;

public class MyIdentityKeyStore implements IdentityKeyStore{
    private IdentityKeyPair identificatore;      //oggetto specifico di una libreria crittografica
    private int RegistrationId;
    private Map<SignalProtocolAddress, IdentityKey> list;

    public MyIdentityKeyStore(IdentityKeyPair i, int r) {
        this.identificatore = i;
        this.RegistrationId = r;
        list = new HashMap<>();
    }

    public boolean equals(Object o) {
        if(o != null && getClass().equals(o.getClass())) {
            MyIdentityKeyStore b = (MyIdentityKeyStore)o;
            return b.identificatore.equals(identificatore);
        } else return false;
    }

    public int hashCode() {
        return identificatore.hashCode();
    }

    public IdentityKeyPair getIdentityKeyPair() { return identificatore; }

    public int getLocalRegistrationId() { return RegistrationId; }

    public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
        // La mappa restituisce la vecchia chiave se ce n'era già una, altrimenti null
        IdentityKey chiaveEsistente = list.put(address, identityKey);
        
        // Restituiamo true solo se c'era una chiave vecchia ed è DIVERSA da quella nuova
        if (chiaveEsistente != null && !chiaveEsistente.equals(identityKey)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTrustedIdentity(SignalProtocolAddress i, IdentityKey p, Direction direction) {
        IdentityKey chiave = list.get(i);

        if(chiave == null || chiave.equals(p)) {
            return true;
        } else {
            return false;
        }
    }

    public IdentityKey getIdentity(SignalProtocolAddress address) { return list.get(address); }
}
