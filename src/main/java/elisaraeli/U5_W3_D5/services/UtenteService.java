package elisaraeli.U5_W3_D5.services;

import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.BadRequestException;
import elisaraeli.U5_W3_D5.exceptions.NotFoundException;
import elisaraeli.U5_W3_D5.payloads.UtenteDTO;
import elisaraeli.U5_W3_D5.payloads.UtentePayloadDTO;
import elisaraeli.U5_W3_D5.repositories.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService {
    private final PasswordEncoder bcrypt;
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {

        this.utenteRepository = utenteRepository;
        this.bcrypt = passwordEncoder;
    }

    public UtentePayloadDTO save(UtenteDTO body) {
        // controllo che l'email non sià già presente
        utenteRepository.findByEmail(body.email()).ifPresent(user -> {
            throw new BadRequestException("L'email: " + body.email() + " è già stata utilizzata");
        });
        // controllo che l'username non sia già presente
        utenteRepository.findByUsername(body.username()).ifPresent(user -> {
            throw new BadRequestException("L'username: " + body.username() + " è già stato utilizzato");
        });
        Utente newUtente = new Utente();
        newUtente.setNome(body.nome());
        newUtente.setCognome(body.cognome());
        newUtente.setUsername(body.username());
        newUtente.setEmail(body.email());
        newUtente.setPassword(bcrypt.encode(body.password()));
        newUtente.setRuolo(body.ruolo());

        Utente utenteSalvato = utenteRepository.save(newUtente);

        return new UtentePayloadDTO(
                utenteSalvato.getId(),
                utenteSalvato.getUsername(),
                utenteSalvato.getNome(),
                utenteSalvato.getCognome(),
                utenteSalvato.getEmail(),
                utenteSalvato.getRuolo()
        );

    }

    // cerco un utente per id
    public Utente findById(UUID id) {
        return utenteRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }


    // Cerco un utente per email
    public Utente findByEmail(String email) {
        return this.utenteRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("L'utente con email " + email + " non è stato trovato!"));
    }
}