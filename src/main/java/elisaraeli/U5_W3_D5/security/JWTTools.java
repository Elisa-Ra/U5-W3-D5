package elisaraeli.U5_W3_D5.security;

import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {
    @Value("${jwt.secret}")
    private String secret;

    // Classe per generare i token
    // Nel payload del token ci va l'ID dell'utente, passiamo l'utente come parametro
    public String generateToken(Utente utente) {
        return Jwts.builder()
                // Data di emissione (IaT - Issued At), va messa in millisecondi
                .issuedAt(new Date(System.currentTimeMillis()))
                // Data di scadenza (Expiration Date) anche questa va messa in millisecondi
                .expiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))
                // Subject cioè a chi appartiene il token. Ci inseriamo l'id dell'utente (MAI DATI SENSIBILI)
                .subject(String.valueOf(utente.getId()))
                // Firmo il token fornendogli un segreto che il server conosce e usa per creare token
                // ma anche per verificarli
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    // Classe per verificare i token (leggerli e validarli)
    public void verifyToken(String token) {

        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
            // Questi metodi ci possono lanciare diverse eccezioni, a seconda della problematica
            // Ci può lanciare una certa eccezione se il token dovesse essere scaduto,
            // un'altra se il token è stato manipolato (firma non valida)
            // un'altra ancora se il token dovesse essere malformato (es. Se manca una parte)
        } catch (Exception ex) {
            throw new UnauthorizedException("Problemi col token! Effettua di nuovo il login!");
        }
    }

    public UUID extractIdFromToken(String token) {
        return UUID.fromString(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject());
    }
}