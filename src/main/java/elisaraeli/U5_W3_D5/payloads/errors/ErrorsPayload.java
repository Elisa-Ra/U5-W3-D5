package elisaraeli.U5_W3_D5.payloads.errors;

import java.util.Date;

public record ErrorsPayload(
        String message,
        Date timestamp) {
    public ErrorsPayload(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}