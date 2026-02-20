package elisaraeli.U5_W3_D5.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Questo è il DTO per creare la prenotazione
public record PrenotazioneDTO(
        @NotNull(message = "L'id dell'evento è obbligatorio")
        UUID idEvento
) {
}

