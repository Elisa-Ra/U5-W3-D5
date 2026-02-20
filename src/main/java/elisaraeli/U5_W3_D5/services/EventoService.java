package elisaraeli.U5_W3_D5.services;

import elisaraeli.U5_W3_D5.entities.Evento;
import elisaraeli.U5_W3_D5.entities.Ruolo;
import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.BadRequestException;
import elisaraeli.U5_W3_D5.exceptions.NotFoundException;
import elisaraeli.U5_W3_D5.exceptions.UnauthorizedException;
import elisaraeli.U5_W3_D5.payloads.EventoDTO;
import elisaraeli.U5_W3_D5.payloads.EventoPayloadDTO;
import elisaraeli.U5_W3_D5.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;
    // Devo prendere anche l'organizzatore dell'evento dagli utenti
    @Autowired
    private UtenteService utenteService;


    public EventoPayloadDTO save(EventoDTO body, UUID idOrganizzatore) {

        // controllo che l'utente sia un organizzatore e non un utente normale
        Utente organizzatore = utenteService.findById(idOrganizzatore);
        if (organizzatore.getRuolo() != Ruolo.ORGANIZZATORE) {
            throw new BadRequestException("Attenzione! Gli eventi possono essere creati solo dagli organizzatori di eventi.");
        }

        Evento newEvento = new Evento();
        newEvento.setTitolo(body.titolo());
        newEvento.setDescrizione(body.descrizione());
        newEvento.setData(body.data());
        newEvento.setLuogo(body.luogo());
        newEvento.setPosti(body.posti());
        newEvento.setOrganizzatore(organizzatore);

        // salvo l'evento
        Evento eventoSalvato = eventoRepository.save(newEvento);

        // restituisco il payload di risposta
        return new EventoPayloadDTO(
                eventoSalvato.getId(),
                eventoSalvato.getTitolo(),
                eventoSalvato.getDescrizione(),
                eventoSalvato.getData(),
                eventoSalvato.getLuogo(),
                eventoSalvato.getPosti(),
                organizzatore.getId(),
                organizzatore.getNome(),
                organizzatore.getCognome()
        );

    }

    public Page<EventoPayloadDTO> getEvento(int page, int size, String sort) {
        if (size > 100) size = 100;
        if (size < 1) size = 1;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return eventoRepository.findAll(pageable).map(evento -> new EventoPayloadDTO(
                evento.getId(),
                evento.getTitolo(),
                evento.getDescrizione(),
                evento.getData(),
                evento.getLuogo(),
                evento.getPosti(),
                evento.getOrganizzatore().getId(),
                evento.getOrganizzatore().getNome(),
                evento.getOrganizzatore().getCognome()
        ));

    }

    // cerco un evento per id
    public Evento findById(UUID id) {
        return eventoRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // elimino un evento per id
    public void findByIdAndDelete(UUID id, UUID idOrganizzatore) {
        Evento eventoDaEliminare = this.findById(id);

        // Controllo che l'utente stia eliminando un evento creato da lui
        if (!eventoDaEliminare.getOrganizzatore().getId().equals(idOrganizzatore)) {
            throw new UnauthorizedException("Attenzione! Puoi eliminare solo gli eventi di cui sei l'organizzatore.");
        }

        eventoRepository.delete(eventoDaEliminare);
    }

    // modifico un evento per id
    public EventoPayloadDTO findByIdAndUpdate(UUID id, EventoDTO body, UUID idOrganizzatore) {

        Evento eventoDaModificare = this.findById(id);
        // Controllo per vedere se l'utente sta cercando di modificare un evento creato da lui
        if (!eventoDaModificare.getOrganizzatore().getId().equals(idOrganizzatore)) {
            throw new UnauthorizedException("Attenzione! Puoi modificare solo gli eventi di cui sei l'organizzatore.");
        }

        eventoDaModificare.setTitolo(body.titolo());
        eventoDaModificare.setDescrizione(body.descrizione());
        eventoDaModificare.setData(body.data());
        eventoDaModificare.setLuogo(body.luogo());
        eventoDaModificare.setPosti(body.posti());

        Evento eventoModificato = eventoRepository.save(eventoDaModificare);

        return new EventoPayloadDTO(
                eventoModificato.getId(),
                eventoModificato.getTitolo(),
                eventoModificato.getDescrizione(),
                eventoModificato.getData(),
                eventoModificato.getLuogo(),
                eventoModificato.getPosti(),
                eventoModificato.getOrganizzatore().getId(),
                eventoModificato.getOrganizzatore().getNome(),
                eventoModificato.getOrganizzatore().getCognome()
        );

    }

}
