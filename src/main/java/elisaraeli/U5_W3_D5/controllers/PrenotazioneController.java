package elisaraeli.U5_W3_D5.controllers;


import elisaraeli.U5_W3_D5.entities.Prenotazione;
import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.BadRequestException;
import elisaraeli.U5_W3_D5.payloads.PrenotazioneDTO;
import elisaraeli.U5_W3_D5.payloads.PrenotazionePayloadDTO;
import elisaraeli.U5_W3_D5.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioneController {
    @Autowired
    PrenotazioneService prenotazioneService;

    // 1. - POST http://localhost:3001/prenotazioni (+ req.body)
    // La prenotazione la può creare solo un utente normale
    @PostMapping("")
    @PreAuthorize("hasAuthority('UTENTE')")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public PrenotazionePayloadDTO savePrenotazione(@RequestBody @Validated PrenotazioneDTO body, BindingResult validation, @AuthenticationPrincipal Utente utente
    ) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return prenotazioneService.save(body, utente.getId());

    }

    // 2. - GET http://localhost:3001/prenotazioni
    @GetMapping("")
    public Page<PrenotazionePayloadDTO> getPrenotazione(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return prenotazioneService.getPrenotazione(page, size, sortBy);
    }

    // 3. - GET http://localhost:3001/prenotazioni/{id}
    @GetMapping("/{prenotazioneId}")
    public Prenotazione findById(@PathVariable UUID prenotazioneId) {
        return prenotazioneService.findById(prenotazioneId);
    }


    // 5. - DELETE http://localhost:3001/prenotazioni/{id}
    // Solo l'utente che ha fatto la prenotazione può eliminarla
    @DeleteMapping("/{prenotazioneId}")
    @PreAuthorize("hasAuthority('UTENTE')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204 NO CONTENT
    public void findAndDelete(@PathVariable UUID prenotazioneId,
                              @AuthenticationPrincipal Utente utente
    ) {
        prenotazioneService.findByIdAndDelete(prenotazioneId, utente.getId());
    }
}
