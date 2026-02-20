package elisaraeli.U5_W3_D5.services;

import elisaraeli.U5_W3_D5.entities.Evento;
import elisaraeli.U5_W3_D5.entities.Prenotazione;
import elisaraeli.U5_W3_D5.entities.Ruolo;
import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.BadRequestException;
import elisaraeli.U5_W3_D5.exceptions.NotFoundException;
import elisaraeli.U5_W3_D5.exceptions.UnauthorizedException;
import elisaraeli.U5_W3_D5.payloads.PrenotazioneDTO;
import elisaraeli.U5_W3_D5.payloads.PrenotazionePayloadDTO;
import elisaraeli.U5_W3_D5.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    // Prendo l'evento
    @Autowired
    private EventoService eventoService;
    // Devo prendere anche l'organizzatore dell'evento dagli utenti
    @Autowired
    private UtenteService utenteService;

    public PrenotazionePayloadDTO save(PrenotazioneDTO body, UUID idUtente) {


        // controllo che l'utente sia un utente normale e non organizzatore
        // perché solo gli utenti normali possono prenotare un evento (gli organizzatori no)
        Utente utente = utenteService.findById(idUtente);
        if (utente.getRuolo() != Ruolo.UTENTE) {
            throw new BadRequestException("Attenzione! Gli eventi possono essere prenotati solo dagli utenti normali.");
        }

        // prendo l'evento
        Evento evento = eventoService.findById(body.idEvento());

        // faccio un controllo per vedere se ci sono ancora posti all'evento

        if (evento.getPosti() <= 0) {
            throw new BadRequestException("Attenzione! Purtroppo non sono rimasti posti disponibili per l'evento" +
                    "con id: " + body.idEvento() + ".");
        }

        // controllo anche se questo utente ha già prenotato questo evento

        boolean prenotato = prenotazioneRepository.existsByUtenteAndEvento(utente, evento);
        if (prenotato) {
            throw new BadRequestException("Attenzione! Hai già una prenotazione per l'evento con id: "
                    + body.idEvento() + ".");
        }


        // ora proseguo con la prenotazione

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtente(utente);
        prenotazione.setEvento(evento);
        prenotazione.setDataPrenotazione(LocalDate.now());

        // visto che un posto all'evento è stato prenotato, devo diminuire i posti disponibili

        evento.setPosti(evento.getPosti() - 1);

        Prenotazione prenotazioneSalvata = prenotazioneRepository.save(prenotazione);

        // restituisco il payload di risposta

        return new PrenotazionePayloadDTO(
                prenotazioneSalvata.getId(),
                evento.getId(),
                evento.getTitolo(),
                evento.getData(),
                utente.getId(),
                utente.getNome(),
                utente.getCognome(),
                prenotazioneSalvata.getDataPrenotazione()
        );

    }

    public Page<PrenotazionePayloadDTO> getPrenotazione(int page, int size, String sort) {
        if (size > 100) size = 100;
        if (size < 1) size = 1;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return prenotazioneRepository.findAll(pageable).map(prenotazione -> new PrenotazionePayloadDTO(
                prenotazione.getId(),
                prenotazione.getEvento().getId(),
                prenotazione.getEvento().getTitolo(),
                prenotazione.getEvento().getData(),
                prenotazione.getUtente().getId(),
                prenotazione.getUtente().getNome(),
                prenotazione.getUtente().getCognome(),
                prenotazione.getDataPrenotazione()
        ));

    }

    // cerco una prenotazione per id
    public Prenotazione findById(UUID id) {
        return prenotazioneRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // elimino una prenotazione per id
    public void findByIdAndDelete(UUID id, UUID idUtente) {
        Prenotazione prenotazioneDaEliminare = this.findById(id);

        // Controllo che l'utente stia eliminando una sua prenotazione
        if (!prenotazioneDaEliminare.getUtente().getId().equals(idUtente)) {
            throw new UnauthorizedException("Attenzione! Puoi eliminare solo le tue prenotazioni.");
        }

        prenotazioneRepository.delete(prenotazioneDaEliminare);
    }

}
