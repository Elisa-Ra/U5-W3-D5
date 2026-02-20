package elisaraeli.U5_W3_D5.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

// Questo è il DTO di creazione dell'evento, con i controlli di validazione
public record EventoDTO(
        @NotEmpty(message = "Il titolo è obbligatorio!")
        @Size(min = 3, max = 30, message = "Il titolo deve essere compreso tra 3 e 30 caratteri.")
        String titolo,
        @NotEmpty(message = "La descrizione dell'evento è obbligatoria!")
        @Size(min = 3, max = 60, message = "La descrizione deve essere compresa tra 3 e 60 caratteri.")
        String descrizione,
        @NotNull(message = "La data dell'evento è obbligatoria!")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDate data,
        @NotEmpty(message = "Il luogo dell'evento è obbligatorio!")
        @Size(min = 3, max = 30, message = "Il nome del luogo deve essere compreso tra 3 e 30 caratteri.")
        String luogo,
        @Max(value = 150, message = "Il numero massimo di posti per l'evento è 300")
        @Min(value = 1, message = "Il numero minimo di posti per l'evento è 1")
        int posti

) {

}
