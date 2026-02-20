package elisaraeli.U5_W3_D5.controllers;

import elisaraeli.U5_W3_D5.exceptions.ValidationException;
import elisaraeli.U5_W3_D5.payloads.LoginDTO;
import elisaraeli.U5_W3_D5.payloads.LoginResponseDTO;
import elisaraeli.U5_W3_D5.payloads.UtenteDTO;
import elisaraeli.U5_W3_D5.payloads.UtentePayloadDTO;
import elisaraeli.U5_W3_D5.services.AuthService;
import elisaraeli.U5_W3_D5.services.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UtenteService utenteService;

    public AuthController(AuthService authService, UtenteService utenteService) {
        this.authService = authService;
        this.utenteService = utenteService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {

        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UtentePayloadDTO createUser(@RequestBody @Validated UtenteDTO payload, BindingResult validationResult) throws IOException {
        // @Validated serve per attivare la validazione, se non lo usiamo è come non farla

        if (validationResult.hasErrors()) {


            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();

            throw new ValidationException(errorsList);
        } else {
            return this.utenteService.save(payload);
        }

    }
}