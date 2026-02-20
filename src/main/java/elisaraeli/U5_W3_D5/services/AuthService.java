package elisaraeli.U5_W3_D5.services;

import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.UnauthorizedException;
import elisaraeli.U5_W3_D5.payloads.LoginDTO;
import elisaraeli.U5_W3_D5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteService utenteService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(UtenteService utenteService, JWTTools jwtTools, PasswordEncoder bcrypt) {

        this.utenteService = utenteService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        // 1. Controllo credenziali
        // 1.1 Controllo se esiste un utente con quell'email
        Utente found = this.utenteService.findByEmail(body.email());

        // 1.2 Se esiste controllo se la sua password (quella nel DB) è uguale a quella nel body

        if (bcrypt.matches(body.password(), found.getPassword())) {
            // 2. Se credenziali OK
            // 2.1 Genero token
            String accessToken = jwtTools.generateToken(found);

            // 2.2 Ritorno token
            return accessToken;

        } else {
            // 3. Se credenziali non ok --> 401 Unauthorized
            throw new UnauthorizedException("Credenziali errate!");
        }


    }
}