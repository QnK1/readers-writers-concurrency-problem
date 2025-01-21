package pl.edu.agh.kis.lab;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.Semaphore;

public class Library {
    private final Semaphore resourceSemaphore;
    private final Semaphore infoSemaphore;
    private final int maxReaderCount;

    @Getter
    private final Set<Reader> currentReaders;
    @Getter
    private Writer currentWriter;
    @Getter
    private final Queue<String> currentQueue;

    public Library(int maxReaderCount) {
        this.maxReaderCount = maxReaderCount;
        currentReaders = new HashSet<>();
        currentWriter = null;
        currentQueue = new LinkedList<>();

        resourceSemaphore = new Semaphore(maxReaderCount, true);
        infoSemaphore = new Semaphore(1);
    }

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

    public void finishRead(Reader reader) {
        try {
            infoSemaphore.acquire(1);
            if(!verifyAndUpdateFinishingReader(reader)){
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

    public void finishWrite(Writer writer) {
        try {
            infoSemaphore.acquire(1);
            if(!verifyAndUpdateFinishingWriter(writer)){
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

    public void acquireInfoLock(){
        try {
            infoSemaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WriterThreadInterruptedException(e);
        }
    }

    public void releaseInfoLock(){
        infoSemaphore.release(1);
    }

    private boolean verifyAndUpdateFinishingReader(Reader reader) {
        if(currentReaders.contains(reader)) {
            currentReaders.remove(reader);
            return true;
        } else {
            return false;
        }
    }

    private boolean verifyAndUpdateFinishingWriter(Writer writer) {
        if(currentWriter == writer) {
            currentWriter = null;
            return true;
        } else {
            return false;
        }
    }

}
