package elisaraeli.U5_W3_D5.payloads;

import java.time.LocalDate;
import java.util.UUID;

// Questo è il DTO di risposta dell'evento in cui mostro anche i dati (non sensibili) dell'organizzatore dell'evento
public record EventoPayloadDTO(
        UUID id,
        String titolo,
        String descrizione,
        LocalDate data,
        String luogo,
        int posti,
        UUID idOrganizzatore,
        String nomeOrganizzatore,
        String cognomeOrganizzatore
) {
}
