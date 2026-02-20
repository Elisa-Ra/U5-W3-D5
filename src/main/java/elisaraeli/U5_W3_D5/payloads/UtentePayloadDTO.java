package elisaraeli.U5_W3_D5.payloads;

import elisaraeli.U5_W3_D5.entities.Ruolo;

import java.util.UUID;

// Questo è il DTO di risposta dell'utente, in cui tolgo le informazioni sensibili
public record UtentePayloadDTO(
        UUID id,
        String username,
        String nome,
        String cognome,
        String email,
        Ruolo ruolo
) {
}
