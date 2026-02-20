package elisaraeli.U5_W3_D5.repositories;

import elisaraeli.U5_W3_D5.entities.Evento;
import elisaraeli.U5_W3_D5.entities.Prenotazione;
import elisaraeli.U5_W3_D5.entities.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, UUID> {

    // per cercare la lista delle prenotazioni di un determinato utente
    boolean existsByUtenteAndEvento(Utente utente, Evento evento);

    List<Prenotazione> findByUtente(Utente utente);

}
