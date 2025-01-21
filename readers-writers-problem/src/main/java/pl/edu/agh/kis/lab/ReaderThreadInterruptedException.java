package pl.edu.agh.kis.lab;

/**
 * Represents an unchecked exception thrown when a reader thread is interrupted during execution.
 */
public class ReaderThreadInterruptedException extends RuntimeException {

    /**
     * Constructs a new ReaderThreadInterruptedException with the specified cause.
     *
     * @param e the exception that caused this exception to be thrown
     */
    public ReaderThreadInterruptedException(Exception e) {
        super(e);
    }
}