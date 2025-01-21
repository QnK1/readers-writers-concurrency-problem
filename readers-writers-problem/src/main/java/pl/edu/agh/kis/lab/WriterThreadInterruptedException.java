package pl.edu.agh.kis.lab;

/**
 * Represents an unchecked exception thrown when a writer thread is interrupted during execution.
 */
public class WriterThreadInterruptedException extends RuntimeException {

    /**
     * Constructs a new WriterThreadInterruptedException with the specified cause.
     *
     * @param e the exception that caused this exception to be thrown
     */
    public WriterThreadInterruptedException(Exception e) {
        super(e);
    }
}