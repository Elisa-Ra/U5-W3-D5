package elisaraeli.U5_W3_D5.repositories;

import elisaraeli.U5_W3_D5.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {

}
