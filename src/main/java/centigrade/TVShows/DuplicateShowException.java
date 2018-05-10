package centigrade.TVShows;

public class DuplicateShowException extends Exception {
    public DuplicateShowException() {}

    public DuplicateShowException(String message){
        super(message);
    }
}
