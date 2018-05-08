package centigrade.movies;

public class DuplicateMovieException extends Exception {
    public DuplicateMovieException() {}

    public DuplicateMovieException(String message){
        super(message);
    }
}
