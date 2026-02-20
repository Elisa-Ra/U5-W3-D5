package elisaraeli.U5_W3_D5.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(UUID id) {
        super("L'elemento con id: " + id + " non è stato trovato!");
    }
}