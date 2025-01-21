package pl.edu.agh.kis.lab;

import lombok.Getter;

import java.util.Random;

@Getter
public class Writer extends Thread{
    private final Library library;
    private final String writerName;
    private final Random random;
    private final int nOfIterations;
    private final LibraryInfoManager libraryInfoManager;
    private final int minTime;
    private final int maxTime;

    public Writer(Library library, String id, int minTime, int maxTime) {
        this.library = library;
        this.writerName = id;
        this.random = new Random();
        this.nOfIterations = 0;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.libraryInfoManager = new LibraryInfoManager(library);
    }

    public Writer(Library library, String id, int minTime, int maxTime, int iterations) {
        this.library = library;
        this.writerName = id;
        this.random = new Random();
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.nOfIterations = Math.max(iterations, 0);
        this.libraryInfoManager = new LibraryInfoManager(library);
    }

    @Override
    public void run() {
        boolean condition = true;
        int i = 0;

        while(condition){
            try {
                Thread.sleep(random.nextInt(maxTime - minTime) + (long) minTime);

                library.acquireInfoLock();
                System.out.println("\n" + writerName + " wants to write");
                System.out.print(libraryInfoManager.getLibraryInfo());
                System.out.println();
                library.releaseInfoLock();

                library.requestWrite(this);

                Thread.sleep(random.nextInt(maxTime - minTime) + (long) minTime);

                library.acquireInfoLock();
                System.out.println("\n" + writerName + " has finished writing");
                System.out.print(libraryInfoManager.getLibraryInfo());
                System.out.println();
                library.releaseInfoLock();

                library.finishWrite(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new WriterThreadInterruptedException(e);
            }

            if(nOfIterations != 0){
                ++i;

                if(i == nOfIterations){
                    condition = false;
                }
            }
        }
    }
}
