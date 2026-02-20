package elisaraeli.U5_W3_D5.payloads;

import java.time.LocalDate;
import java.util.UUID;

// Questo è il DTO di risposta della prenotazione
public record PrenotazionePayloadDTO(
        UUID id,
        UUID idEvento,
        String titoloEvento,
        LocalDate dataEvento,
        UUID idUtente,
        String nomeUtente,
        String cognomeUtente,
        LocalDate dataPrenotazione

) {
}
