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

    public String getCurrentInfo(){
        try {
            infoSemaphore.acquire(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Current readers count: ").append(currentReaders.size()).append("\n");
        sb.append("Current readers: ");
        for(Reader reader : getCurrentReaders()){
            sb.append(reader.getReaderName()).append(" ");
        }
        sb.append("\n");
        sb.append("Current writer: ").append(currentWriter == null ? "None" : currentWriter.getWriterName());
        sb.append("\n");
        sb.append("Current queue size: ").append(currentQueue.size()).append("\n");
        sb.append("Current queue: ");
        for(String s : currentQueue){
            sb.append(s).append(" ");
        }

        infoSemaphore.release(1);
        return sb.toString();
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
