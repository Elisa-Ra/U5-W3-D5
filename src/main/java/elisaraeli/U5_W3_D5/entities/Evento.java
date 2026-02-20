package elisaraeli.U5_W3_D5.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue
    private UUID id;
    private String titolo;
    private String descrizione;
    private LocalDate data;
    private String luogo;
    private int posti;
    @ManyToOne
    @JoinColumn(name = "id_organizzatore", nullable = false)
    private Utente organizzatore;
}
