package elisaraeli.U5_W3_D5.controllers;

import elisaraeli.U5_W3_D5.entities.Evento;
import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.BadRequestException;
import elisaraeli.U5_W3_D5.payloads.EventoDTO;
import elisaraeli.U5_W3_D5.payloads.EventoPayloadDTO;
import elisaraeli.U5_W3_D5.services.EventoService;
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
@RequestMapping("/eventi")
public class EventoController {
    @Autowired
    EventoService eventoService;

    // 1. - POST http://localhost:3001/eventi (+ req.body)
    // L'evento lo può creare solo l'organizzatore
    @PostMapping("")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.CREATED) // <-- 201
    public EventoPayloadDTO saveEvento(@RequestBody @Validated EventoDTO body, BindingResult validation, @AuthenticationPrincipal Utente utente
    ) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return eventoService.save(body, utente.getId());

    }

    // 2. - GET http://localhost:3001/eventi
    @GetMapping("")
    public Page<EventoPayloadDTO> getEvento(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return eventoService.getEvento(page, size, sortBy);
    }

    // 3. - GET http://localhost:3001/eventi/{id}
    @GetMapping("/{eventoId}")
    public Evento findById(@PathVariable UUID eventoId) {
        return eventoService.findById(eventoId);
    }

    // 4. - PUT http://localhost:3001/eventi/{id} (+ req.body)
    // Solo l'organizzatore può modificare un evento
    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public EventoPayloadDTO findAndUpdate(@PathVariable UUID eventoId, @RequestBody @Validated EventoDTO body,
                                          BindingResult validation,
                                          @AuthenticationPrincipal Utente utente
    ) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return eventoService.findByIdAndUpdate(eventoId, body, utente.getId());
    }

    // 5. - DELETE http://localhost:3001/eventi/{id}
    // Solo l'organizzatore può eliminare un evento
    @DeleteMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // <-- 204 NO CONTENT
    public void findAndDelete(@PathVariable UUID eventoId,
                              @AuthenticationPrincipal Utente utente
    ) {
        eventoService.findByIdAndDelete(eventoId, utente.getId());
    }
}
