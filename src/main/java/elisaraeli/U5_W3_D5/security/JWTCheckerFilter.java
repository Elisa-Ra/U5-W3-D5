package elisaraeli.U5_W3_D5.security;

import elisaraeli.U5_W3_D5.entities.Utente;
import elisaraeli.U5_W3_D5.exceptions.UnauthorizedException;
import elisaraeli.U5_W3_D5.services.UtenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component

public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UtenteService utenteService;

    @Autowired
    public JWTCheckerFilter(JWTTools jwtTools, UtenteService utenteService) {
        this.jwtTools = jwtTools;
        this.utenteService = utenteService;
    }

    // metodo che viene eseguito a ogni richiesta e che controllerà i token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Controllo se la richiesta ha l'header Authorization nel formato Bearer + token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'Authorization header nel formato corretto");

        // Estraggo il token dall'header
        String accessToken = authHeader.replace("Bearer ", "");

        //Controllo che il token sia valido (controllo la firma e la data di scadenza)
        jwtTools.verifyToken(accessToken);
        // ---------------- AUTORIZZAZIONE -------------

        // Dall'id del token cerco l'utente
        UUID utenteId = jwtTools.extractIdFromToken(accessToken);
        Utente authenticatedUtente = this.utenteService.findById(utenteId);

        // Poi si associa l'utente al Security Context

        // Primo parametro è lo user, secondo null, terzo le authorities
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUtente, null, authenticatedUtente.getAuthorities());
        // Imposto l'utente nel contesto della richiesta.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Se tutto è OK --> andiamo avanti, trasmettiamo la richiesta al prossimo
        filterChain.doFilter(request, response);

    }

    // Tramite l'Override del metodo sottostante posso specificare quando il filtro non debba essere chiamato in causa
    // Ad esempio posso dirgli di non filtrare tutte le richieste dirette al controller "/auth"
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
        // return request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/auth/register");
    }
}