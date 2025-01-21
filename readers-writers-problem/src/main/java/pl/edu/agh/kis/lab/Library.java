package pl.edu.agh.kis.lab;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Represents a library system that manages concurrent access to its resources by readers and writers.
 * <p>
 * Readers can read concurrently up to a specified maximum number, while writers require exclusive access.
 * The library uses semaphores to manage access and maintain synchronization.
 */
public class Library {
    private final Semaphore resourceSemaphore;
    private final Semaphore infoSemaphore;
    private final int maxReaderCount;

    /**
     * Set of readers currently reading from the library.
     */
    @Getter
    private final Set<Reader> currentReaders;

    /**
     * The writer currently writing to the library. Null if no writer is active.
     */
    @Getter
    private Writer currentWriter;

    /**
     * Queue of readers and writers waiting to access the library resources.
     */
    @Getter
    private final Queue<String> currentQueue;

    /**
     * Constructs a Library instance.
     *
     * @param maxReaderCount the maximum number of readers allowed to read concurrently
     */
    public Library(int maxReaderCount) {
        this.maxReaderCount = maxReaderCount;
        currentReaders = new HashSet<>();
        currentWriter = null;
        currentQueue = new LinkedList<>();

        resourceSemaphore = new Semaphore(maxReaderCount, true);
        infoSemaphore = new Semaphore(1);
    }

    /**
     * Allows a reader to request access to read from the library.
     *
     * @param reader the reader requesting access
     */
    public void requestRead(Reader reader) {
        try {
            infoSemaphore.acquire(1);
            if (currentReaders.contains(reader)) {
                infoSemaphore.release(1);
                return;
            }
            currentQueue.add(reader.getReaderName());
            infoSemaphore.release(1);

            resourceSemaphore.acquire(1);

            infoSemaphore.acquire(1);
            currentQueue.remove();
            currentReaders.add(reader);
            infoSemaphore.release(1);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ReaderThreadInterruptedException(e);
        }
    }

    /**
     * Allows a reader to finish reading and release their access.
     *
     * @param reader the reader finishing their reading session
     */
    public void finishRead(Reader reader) {
        try {
            infoSemaphore.acquire(1);
            if (!verifyAndUpdateFinishingReader(reader)) {
                infoSemaphore.release(1);
                return;
            }
            infoSemaphore.release(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ReaderThreadInterruptedException(e);
        }

        resourceSemaphore.release(1);
    }

    /**
     * Allows a writer to request exclusive access to write to the library.
     *
     * @param writer the writer requesting access
     */
    public void requestWrite(Writer writer) {
        try {
            infoSemaphore.acquire(1);
            if (currentWriter != null) {
                infoSemaphore.release(1);
                return;
            }
            currentQueue.add(writer.getWriterName());
            infoSemaphore.release(1);

            resourceSemaphore.acquire(maxReaderCount);

            infoSemaphore.acquire(1);
            currentQueue.remove();
            currentWriter = writer;
            infoSemaphore.release(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WriterThreadInterruptedException(e);
        }

    }

    /**
     * Allows a writer to finish writing and release their exclusive access.
     *
     * @param writer the writer finishing their writing session
     */
    public void finishWrite(Writer writer) {
        try {
            infoSemaphore.acquire(1);
            if (!verifyAndUpdateFinishingWriter(writer)) {
                infoSemaphore.release(1);
                return;
            }
            infoSemaphore.release(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WriterThreadInterruptedException(e);
        }

        resourceSemaphore.release(maxReaderCount);
    }

    /**
     * Acquires the lock for accessing library information.
     */
    public void acquireInfoLock() {
        try {
            infoSemaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WriterThreadInterruptedException(e);
        }
    }

    /**
     * Releases the lock for accessing library information.
     */
    public void releaseInfoLock() {
        infoSemaphore.release(1);
    }

    /**
     * Verifies if a reader is valid and removes them from the list of current readers.
     *
     * @param reader the reader to verify and update
     * @return true if the reader was valid and removed, false otherwise
     */
    private boolean verifyAndUpdateFinishingReader(Reader reader) {
        if (currentReaders.contains(reader)) {
            currentReaders.remove(reader);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies if a writer is valid and removes them as the current writer.
     *
     * @param writer the writer to verify and update
     * @return true if the writer was valid and removed, false otherwise
     */
    private boolean verifyAndUpdateFinishingWriter(Writer writer) {
        if (currentWriter == writer) {
            currentWriter = null;
            return true;
        } else {
            return false;
        }
    }
}
