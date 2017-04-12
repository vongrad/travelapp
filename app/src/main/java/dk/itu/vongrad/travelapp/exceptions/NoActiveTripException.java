package dk.itu.vongrad.travelapp.exceptions;

/**
 * Created by Adam Vongrej on 4/12/17.
 */

public class NoActiveTripException extends Exception {

    public NoActiveTripException(String message) {
        super(message);
    }
}
