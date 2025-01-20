package pl.edu.agh.kis.lab;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Library {
    private Set<Reader> currentlyReading;
    private Writer currentlyWriting;
    private final Semaphore resourceSemaphore;
    private final Semaphore currentlyReadingSempahore;
    private final int maxReaderCount;

    public Library(int maxReaderCount) {
        this.maxReaderCount = maxReaderCount;
        currentlyReading = new HashSet<>();
        currentlyWriting = null;

        resourceSemaphore = new Semaphore(5, true);
        currentlyReadingSempahore = new Semaphore(1);
    }

    public void requestRead(Reader reader) {
        try{
            resourceSemaphore.acquire(1);
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void finishRead(Reader reader) {
        resourceSemaphore.release(1);
    }

    public void requestWrite(Writer writer) {
        try{
            resourceSemaphore.acquire(5);
        } catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public void finishWrite(Writer writer) {
        resourceSemaphore.release(5);
    }

    public int getNumberOfReaders() {
        return currentlyReading.size();
    }

    public boolean isWriting(){
        return currentlyWriting != null;
    }

}
