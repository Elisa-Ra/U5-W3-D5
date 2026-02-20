package elisaraeli.U5_W3_D5.payloads;

import elisaraeli.U5_W3_D5.entities.Ruolo;
import jakarta.validation.constraints.*;

// Questo è il DTO di creazione dell'utente
public record UtenteDTO(
        @NotEmpty(message = "Lo username è obbligatorio!")
        @Size(min = 3, max = 30, message = "Lo username deve essere compreso tra 3 e 30 caratteri.")
        String username,
        @NotEmpty(message = "Il nome è obbligatorio!")
        @Size(min = 3, max = 30, message = "Il nome deve essere compreso tra 3 e 30 caratteri.")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatorio!")
        @Size(min = 3, max = 30, message = "Il cognome deve essere compreso tra 3 e 30 caratteri.")
        String cognome,
        @NotEmpty(message = "L'email è obbligatoria!")
        @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "L'email inserita non è valida.")
        @Email(message = "L'email inserita non è valida.")
        String email,
        @NotBlank(message = "La password è obbligatoria!")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri.")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$", message = "La password deve contenere una lettera maiuscola")
        String password,
        @NotNull(message = "Il ruolo dell'utente è obbligatorio!")
        Ruolo ruolo
) {

}