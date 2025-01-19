package pl.edu.agh.kis.lab;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Library {
    private Set<Reader> currentlyReading;
    private Writer currentlyWriting;
    private Semaphore mutex;
    private Semaphore wrt;
    private final int maxReaderCount;
    private final int maxWriterCount;

    public Library(int maxReaderCount, int maxWriterCount) {
        this.maxReaderCount = maxReaderCount;
        this.maxWriterCount = maxWriterCount;
        currentlyReading = new HashSet<>();
        currentlyWriting = null;
    }

    public void requestRead(Reader reader) {

    }

    public void finishRead(Reader reader) {

    }

    public void requestWrite(Writer writer) {

    }

    public void finishWrite(Writer writer) {

    }

    private int getReaderCount(){
        return currentlyReading.size();
    }

}
